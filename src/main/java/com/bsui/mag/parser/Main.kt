package com.bsui.mag.parser

import com.bsui.mag.parser.tree.TreeFabric.Companion.hasCycles
import org.xml.sax.SAXException
import java.awt.Color
import java.awt.Toolkit
import java.io.IOException
import javax.swing.JEditorPane
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.event.HyperlinkEvent
import javax.xml.parsers.ParserConfigurationException
import java.awt.Color.white
import javax.swing.UIManager
import java.awt.Dimension





object Main {
    @Throws(ParserConfigurationException::class, SAXException::class, IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val options = arrayOf("Check for cycles", "Browse Pages")
        UIManager.put("OptionPane.background", Color.YELLOW)
        UIManager.put("OptionPane.minimumSize", Dimension(500, 50))
        UIManager.put("Button.background", Color.GREEN);
        UIManager.put("Panel.background", Color.YELLOW)
        val result = JOptionPane.showOptionDialog(
                null,
                "Please, choose option:",
                "Viewer",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        )
        if (result == JOptionPane.YES_OPTION) {
            val message: String
            message = if (hasCycles()) {
                "Сycles are available"
            } else {
                "No cycles"
            }
            JOptionPane.showConfirmDialog(null, message)
        } else if (result == JOptionPane.NO_OPTION) {
            viewHtml()
        }
    }

    @Throws(IOException::class, SAXException::class, ParserConfigurationException::class)
    private fun viewHtml() {
        val jFrame = JFrame()
        val ed1 = JEditorPane("text/html", HtmlHandler().getHtml("index"))
        jFrame.add(ed1)
        ed1.addHyperlinkListener { e: HyperlinkEvent ->
            if (HyperlinkEvent.EventType.ACTIVATED == e.eventType) {
                try {
                    ed1.text = HtmlHandler().getHtml(e.description)
                } catch (ex: ParserConfigurationException) {
                    throw RuntimeException(ex)
                } catch (ex: SAXException) {
                    throw RuntimeException(ex)
                } catch (ex: IOException) {
                    throw RuntimeException(ex)
                }
                ed1.updateUI()
            }
        }
        jFrame.isVisible = true
        ed1.isEditable = false
        jFrame.size = Toolkit.getDefaultToolkit().screenSize
        jFrame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    }
}