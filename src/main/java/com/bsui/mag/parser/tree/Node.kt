package com.bsui.mag.parser.tree

import java.util.*
import java.util.function.Consumer
import java.util.function.Predicate

class Node(var name: String) {
    private var nodeList: MutableList<Node> = ArrayList()
    fun dfs(nodeConsumer: Predicate<Node>) {
        if (nodeConsumer.test(this)) {
            nodeList.forEach(Consumer { node: Node -> node.dfs(nodeConsumer) })
        }
    }

    fun addAll(c: Collection<Node?>?) {
        nodeList.addAll(c as Collection<Node>)
    }

    fun getNodeList(): List<Node> {
        return nodeList
    }

    fun setNodeList(nodeList: MutableList<Node>) {
        this.nodeList = nodeList
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val node = o as Node
        return name == node.name
    }

    override fun hashCode(): Int {
        return Objects.hash(name)
    }

}