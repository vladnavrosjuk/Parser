package com.bsui.mag.parser

import com.bsui.mag.parser.scheme.SchemeEntity
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler
import java.util.*
import java.util.function.Consumer
import java.util.function.Predicate

class TextParser(schemeEntities: Array<SchemeEntity>) : DefaultHandler() {
    private val schemeEntities: List<SchemeEntity>
    private val tagStack = Stack<HtmlEntity>()
    private var bodyHtmlEntity: HtmlEntity? = null
    private var currentValue: String? = null
    private var stringConsumer: Consumer<String?>? = null
    var html: String? = null
        private set

    override fun characters(ch: CharArray, start: Int, length: Int) {
        currentValue = String(ch).substring(start, start + length)
    }

    override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
        schemeEntities.stream().filter(findTagByNamePredicate(qName)).findFirst().ifPresent(schemeEntityConsumer())
    }

    override fun endElement(uri: String, localName: String, qName: String) {
        if (stringConsumer != null) {
            stringConsumer!!.accept(currentValue)
            stringConsumer = null
            currentValue = null
        }
        schemeEntities.stream().filter(findTagByNamePredicate(qName)).filter { schemeEntity: SchemeEntity -> tagStack.peek().name == schemeEntity.parseAs?.name }
                .findFirst()
                .ifPresent { schemeEntity: SchemeEntity? -> tagStack.pop() }
    }

    override fun endDocument() {
        println(bodyHtmlEntity.toString())
        html = bodyHtmlEntity.toString()
    }

    private fun findTagByNamePredicate(tagName: String): Predicate<SchemeEntity> {
        return Predicate { schemeEntity: SchemeEntity -> schemeEntity.type == TAG && schemeEntity.name == tagName }
    }

    private fun schemeEntityConsumer(): Consumer<SchemeEntity> {
        return Consumer { schemeEntity: SchemeEntity ->
            val parseAs = schemeEntity.parseAs
            if (parseAs != null) {
                when (parseAs.type) {
                    TAG -> {
                        val newTag = HtmlEntity(parseAs.name)
                        if (bodyHtmlEntity == null) {
                            bodyHtmlEntity = newTag
                        } else {
                            tagStack.peek().htmlEntities.add(newTag)
                        }
                        tagStack.push(newTag)
                    }
                    SOURCE -> stringConsumer = Consumer { s: String? -> tagStack.peek().setSource(s) }
                    STYLE -> if (parseAs.value != null) {
                        tagStack.peek().putStyleEntry(parseAs.name, parseAs.value)
                    } else {
                        stringConsumer = Consumer { s: String? -> tagStack.peek().putStyleEntry(parseAs.name, s) }
                    }
                    ATTR -> if (parseAs.value != null) {
                        tagStack.peek().putAttr(parseAs.name, parseAs.value)
                    } else {
                        stringConsumer = Consumer { s: String? -> tagStack.peek().putAttr(parseAs.name, s) }
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "tag"
        private const val ATTR = "attr"
        private const val STYLE = "style"
        private const val SOURCE = "source"
    }

    init {
        this.schemeEntities = Arrays.asList(*schemeEntities)
    }
}