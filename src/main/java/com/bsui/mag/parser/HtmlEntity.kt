package com.bsui.mag.parser

import java.util.*
import java.util.stream.Collectors

class HtmlEntity(val name: String?) {
    private val attributes: MutableMap<String?, String?> = HashMap()
    private var source: String? = ""
    private val styleMap: MutableMap<String?, String?> = HashMap()
    val htmlEntities: MutableList<HtmlEntity?> = ArrayList()

    fun putAttr(key: String?, value: String?) {
        attributes[key] = value
    }

    fun putStyleEntry(key: String?, value: String?) {
        styleMap[key] = value
    }

    fun setSource(source: String?) {
        this.source = source
    }

    override fun toString(): String {
        val attrs = attributes.entries.stream()
                .map { entry: Map.Entry<String?, String?> -> entry.key.toString() + "=\"" + entry.value + "\"" }
                .collect(Collectors.joining(" ", "", ""))
        val style = styleMap.entries.stream()
                .map { entry: Map.Entry<String?, String?> -> entry.key.toString() + ":" + entry.value }
                .collect(Collectors.joining(";", "style=\"", "\""))
        if (source == "") {
            source = htmlEntities.stream().map { obj: HtmlEntity? -> obj.toString() }.collect(Collectors.joining("", "", ""))
        }
        return "<$name $attrs $style>$source</$name>"
    }

}