package com.acmerobotics.roadrunner.gui

import java.awt.Dimension
import java.awt.FileDialog
import java.awt.Toolkit
import java.awt.event.KeyEvent
import java.io.File
import javax.swing.*

private val COMMAND_MASK = Toolkit.getDefaultToolkit().menuShortcutKeyMask

class MainFrame : JFrame() {

    init {
        title = "Spline Designer"
        size = Dimension(600, 800)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        isResizable = false

        val mainPanel = MainPanel()

        contentPane = JPanel()
        contentPane.add(mainPanel)

        val fileMenu = JMenu("File")
        val openMenuItem = JMenuItem("Open")
        openMenuItem.addActionListener {
            val fileDialog = FileDialog(this, "Choose a file", FileDialog.LOAD)
            fileDialog.setFilenameFilter { _, name -> name.endsWith(".yaml") }
            fileDialog.isVisible = true

            if (fileDialog.file != null) {
                val filename = fileDialog.directory + fileDialog.file
                mainPanel.load(File(filename))
            }
        }
        openMenuItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_O, COMMAND_MASK)
        val saveMenuItem = JMenuItem("Save")
        saveMenuItem.addActionListener {
            val fileDialog = FileDialog(this, "Choose a file", FileDialog.SAVE)
            fileDialog.setFilenameFilter { _, name -> name.endsWith(".yaml") }
            fileDialog.isVisible = true

            if (fileDialog.file != null) {
                val filename = fileDialog.directory + if (fileDialog.file.contains("\\.")) {
                    fileDialog.file.split("\\.").dropLast(1).joinToString(".") + ".yaml"
                } else {
                    fileDialog.file + ".yaml"
                }

                mainPanel.save(File(filename))
            }
        }
        saveMenuItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_S, COMMAND_MASK)
        fileMenu.add(openMenuItem)
        fileMenu.add(saveMenuItem)

        val menuBar = JMenuBar()
        menuBar.add(fileMenu)

        jMenuBar = menuBar
    }
}