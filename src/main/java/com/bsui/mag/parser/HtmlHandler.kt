package com.bsui.mag.parser

import com.bsui.mag.parser.scheme.SchemeEntity
import com.google.gson.Gson
import org.xml.sax.SAXException
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import javax.xml.parsers.ParserConfigurationException
import javax.xml.parsers.SAXParserFactory

class HtmlHandler {
    private val textParser: TextParser
    @Throws(ParserConfigurationException::class, SAXException::class, IOException::class)
    fun getHtml(name: String): String? {
        val factory = SAXParserFactory.newInstance()
        val parser = factory.newSAXParser()
        parser.parse(File("$PATH_TO_RESOURSES$name.xml"), textParser)
        return textParser.html
    }

    companion object {
        var PATH_TO_RESOURSES = "C:\\Users\\Vlad\\IdeaProjects\\parser\\src\\main\\resources\\"
    }

    init {
        val gson = Gson()
        val schemeEntities = gson.fromJson(InputStreamReader(FileInputStream(PATH_TO_RESOURSES + "scheme.json"), StandardCharsets.UTF_8), Array<SchemeEntity>::class.java)
        textParser = TextParser(schemeEntities)
    }
}