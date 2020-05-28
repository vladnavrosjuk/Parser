package com.bsui.mag.parser.tree

import com.bsui.mag.parser.HtmlHandler
import org.xml.sax.SAXException
import java.io.File
import java.io.IOException
import java.util.*
import java.util.function.Predicate
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException

class TreeFabric {
    private val nodeMap: MutableMap<String, Node?> = HashMap()
    @get:Throws(ParserConfigurationException::class, IOException::class, SAXException::class)
    val rootNode: Node
        get() {
            val rootNode = Node("index")
            var nodeMapSize = 0
            var nodeList: List<Node?> = listOf(rootNode)
            while (true) {
                for (node in nodeList) {
                    if (!nodeMap.containsKey(node!!.name)) {
                        nodeMap[node.name] = node
                        nodeList = createNodeListFromPageContent(node.name)
                        node.addAll(nodeList)
                    }
                }
                if (nodeMap.size == nodeMapSize) {
                    break
                }
                nodeMapSize = nodeMap.size
            }
            nodeMap.clear()
            return rootNode
        }

    @Throws(ParserConfigurationException::class, IOException::class, SAXException::class)
    private fun createNodeListFromPageContent(pageName: String): List<Node?> {
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val document = builder.parse(File(HtmlHandler.PATH_TO_RESOURSES + pageName + ".xml"))
        val refElements = document.documentElement.getElementsByTagName("ref")
        val nodeList: MutableList<Node?> = ArrayList()
        for (i in 0 until refElements.length) {
            val ref = refElements.item(i).textContent
            var node: Node?
            node = if (nodeMap.containsKey(ref)) {
                nodeMap[ref]
            } else {
                Node(ref)
            }
            nodeList.add(node)
        }
        return nodeList
    }

    companion object {
        @JvmStatic
        @Throws(IOException::class, SAXException::class, ParserConfigurationException::class)
        fun hasCycles(): Boolean {
            val graphCreator = TreeFabric()
            val root = graphCreator.rootNode
            val hasCycle = BooleanArray(1)
            val nodeHashSet: MutableSet<Node> = HashSet()
            root.dfs(object: Predicate<Node>{
                override fun test(t: Node): Boolean {
                    hasCycle[0] = !nodeHashSet.add(t)
                    return !hasCycle[0]
                }
            })
            //root.dfs { node: Node -> !!nodeHashSet.add(node).also { hasCycle[0] = node } }
            return hasCycle[0]
        }
    }
}