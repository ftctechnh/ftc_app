package com.acmerobotics.roadrunner.plugin

import com.acmerobotics.roadrunner.gui.MainPanel
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.io.File
import java.nio.file.Files
import javax.swing.*


class PathDesignerPanel(private val project: Project) : JPanel() {
    private val moduleManager = ModuleManager.getInstance(project)

    private val moduleComboBox = JComboBox<String>()
    private val trajectoryComboBox = JComboBox<String>()

    private val trajectoryNameField = JTextField()

    private val mainPanel = MainPanel()

    private var currentModule: String? = null
    private var currentTrajectory: String? = null

    private val comboBoxModule: String?
        get() = moduleComboBox.selectedItem as String?

    private val comboBoxTrajectory: String?
        get() = trajectoryComboBox.selectedItem as String?

    private var dirty: Boolean = false

    private val saveButton = JButton("Save")

    init {
        val groupLayout = GroupLayout(this)
        layout = groupLayout

        val modules = moduleManager.modules
                .filter { it.moduleFilePath.startsWith(project.basePath!!) }
                .filter { it.name != project.name }
                .map { it.name }
                .sorted()
                .toMutableList()

        val moduleLabel = JLabel("Module", SwingConstants.RIGHT)
        moduleComboBox.model = DefaultComboBoxModel(modules.toTypedArray())
        moduleComboBox.addActionListener {
            if (currentModule != comboBoxModule && (!dirty || savePrompt())) {
                updateModuleSelection()
            }
        }
        updateModuleSelection()
        val phantomLabel = JLabel()

        val trajectoryLabel = JLabel("Trajectory", SwingConstants.RIGHT)
        trajectoryComboBox.addActionListener {
            if (currentTrajectory != comboBoxTrajectory && (!dirty || savePrompt())) {
                updateTrajectorySelection()
            }
        }
        updateTrajectorySelection()

        val addButton = JButton("Add")
        addButton.addActionListener {
            if (!dirty || savePrompt()) {
                addTrajectory()
            }
        }

        trajectoryNameField.addKeyListener(object : KeyListener {
            override fun keyTyped(e: KeyEvent?) {
                // do nothing
            }

            override fun keyPressed(e: KeyEvent?) {
                // do nothing
            }

            override fun keyReleased(e: KeyEvent?) {
                markDirty()
            }
        })

        saveButton.addActionListener {
            saveCurrentTrajectory()
        }

        val deleteButton = JButton("Delete")
        deleteButton.addActionListener {
            deleteCurrentTrajectory()
        }

        mainPanel.onTrajectoryUpdateListener = { markDirty() }

        groupLayout.autoCreateGaps = true
        groupLayout.autoCreateContainerGaps = true

        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(moduleLabel)
                        .addComponent(moduleComboBox)
                        .addComponent(phantomLabel))
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(trajectoryLabel)
                        .addComponent(trajectoryComboBox)
                        .addComponent(addButton))
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(trajectoryNameField)
                        .addComponent(saveButton)
                        .addComponent(deleteButton))
                .addComponent(mainPanel))

        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addGroup(groupLayout.createSequentialGroup()
                        .addComponent(moduleLabel)
                        .addComponent(moduleComboBox)
                        .addComponent(phantomLabel))
                .addGroup(groupLayout.createSequentialGroup()
                        .addComponent(trajectoryLabel)
                        .addComponent(trajectoryComboBox)
                        .addComponent(addButton))
                .addGroup(groupLayout.createSequentialGroup()
                        .addComponent(trajectoryNameField)
                        .addComponent(saveButton)
                        .addComponent(deleteButton))
                .addComponent(mainPanel))

        groupLayout.linkSize(moduleLabel, trajectoryLabel)
        groupLayout.linkSize(moduleComboBox, trajectoryComboBox, trajectoryNameField)
        groupLayout.linkSize(phantomLabel, addButton)
    }

    private fun saveCurrentTrajectory() {
        val comboBoxModule = comboBoxModule ?: return
        val comboBoxTrajectory = comboBoxTrajectory ?: return
        val dir = getTrajectoryAssetsDir(comboBoxModule) ?: return
        if (trajectoryNameField.text.isEmpty() || trajectoryNameField.text == comboBoxTrajectory) {
            Files.createDirectories(dir.toPath())
            mainPanel.save(File(dir, "$comboBoxTrajectory.yaml"))
        } else {
            val newTrajectory = trajectoryNameField.text
            Files.createDirectories(dir.toPath())
            val oldFile = File(dir, "$comboBoxTrajectory.yaml")
            val newFile = File(dir, "$newTrajectory.yaml")
            oldFile.delete()
            mainPanel.save(newFile)

            val trajectories = listTrajectoryAssets(comboBoxModule)
                    .map { if (it == comboBoxTrajectory) newTrajectory else it }
            trajectoryComboBox.model = DefaultComboBoxModel(trajectories.toTypedArray())
            trajectoryComboBox.selectedIndex = trajectories.indexOf(newTrajectory)
        }
        markClean()
        updateTrajectorySelection()
    }

    private fun nextUntitledName(): String {
        val comboBoxModule = comboBoxModule ?: return "untitled"
        val trajectories = listTrajectoryAssets(comboBoxModule).toMutableList()
        val prefix = "untitled"
        var name = prefix
        var i = 1
        while (true) {
            if (name !in trajectories) {
                return name
            }
            i++
            name = "$prefix$i"
        }
    }

    private fun addTrajectory(nameArg: String? = null) {
        val comboBoxModule = comboBoxModule ?: return
        mainPanel.clearTrajectory()
        val trajectories = listTrajectoryAssets(comboBoxModule).toMutableList()
        val name = nameArg ?: nextUntitledName()
        trajectories.add(name)
        trajectories.sort()

        val dir = getTrajectoryAssetsDir(comboBoxModule) ?: return
        Files.createDirectories(dir.toPath())
        mainPanel.save(File(dir, "$name.yaml"))

        markClean()

        trajectoryComboBox.model = DefaultComboBoxModel(trajectories.toTypedArray())
        trajectoryComboBox.selectedIndex = trajectories.indexOf(name)
        updateTrajectorySelection()
    }

    private fun deleteCurrentTrajectory() {
        val comboBoxModule = comboBoxModule ?: return
        val comboBoxTrajectory = comboBoxTrajectory ?: return
        val trajectoryFile = File(getTrajectoryAssetsDir(comboBoxModule), "$comboBoxTrajectory.yaml")
        trajectoryFile.delete()
        val trajectories = listTrajectoryAssets(comboBoxModule).filter { it != comboBoxTrajectory }
        trajectoryComboBox.model = DefaultComboBoxModel(trajectories.toTypedArray())
        updateTrajectorySelection()
    }

    private fun updateModuleSelection() {
        if (currentModule == comboBoxModule) return
        val comboBoxModule = comboBoxModule ?: return
        val trajectories = listTrajectoryAssets(comboBoxModule)
        trajectoryComboBox.model = DefaultComboBoxModel(trajectories.toTypedArray())
        updateTrajectorySelection()
        currentModule = comboBoxModule
    }

    private fun updateTrajectorySelection() {
        if (currentTrajectory == comboBoxTrajectory) return
        val comboBoxModule = comboBoxModule ?: return
        val comboBoxTrajectory = comboBoxTrajectory
        if (comboBoxTrajectory == null) {
            mainPanel.clearTrajectory()
            trajectoryNameField.text = ""
        } else {
            mainPanel.load(File(getTrajectoryAssetsDir(comboBoxModule), "$comboBoxTrajectory.yaml"))
            trajectoryNameField.text = comboBoxTrajectory
        }
        markClean()
        currentTrajectory = comboBoxTrajectory
    }

    private fun listTrajectoryAssets(moduleString: String): List<String> {
        val trajectoryAssetsDir = getTrajectoryAssetsDir(moduleString) ?: return listOf()
        if (trajectoryAssetsDir.exists()) {
            return trajectoryAssetsDir
                    .listFiles { _, name -> name.endsWith(".yaml") }
                    .map { it.nameWithoutExtension }
                    .sorted()
        }
        return listOf()
    }

    private fun getTrajectoryAssetsDir(moduleString: String): File? {
        val module = moduleManager.modules.firstOrNull { it.name == moduleString } ?: return null
        val moduleFile = File(module.moduleFilePath)
        return File(moduleFile.parent, "src/main/assets/trajectory")
    }

    private fun markDirty() {
        dirty = true
        saveButton.isEnabled = true
    }

    private fun markClean() {
        dirty = false
        saveButton.isEnabled = false
    }

    private fun savePrompt(): Boolean {
        val response = JOptionPane.showConfirmDialog(
                this,
                "Save the current trajectory?",
                "Unsaved Changes",
                JOptionPane.YES_NO_OPTION)
        return when (response) {
            JOptionPane.YES_OPTION -> {
                saveCurrentTrajectory()
                true
            }
            else -> false
        }
    }
}