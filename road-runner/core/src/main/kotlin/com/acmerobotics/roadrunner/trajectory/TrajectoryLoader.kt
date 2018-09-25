package com.acmerobotics.roadrunner.trajectory

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.File

/**
 * Class containing methods for saving and loading [TrajectoryConfig] instances to YAML files.
 */
object TrajectoryLoader {
    private val MAPPER = ObjectMapper(YAMLFactory()).registerKotlinModule()

    /**
     * Saves [trajectoryConfig] to [file].
     */
    @JvmStatic
    fun saveConfig(trajectoryConfig: TrajectoryConfig, file: File) {
        MAPPER.writerWithDefaultPrettyPrinter().writeValue(file, trajectoryConfig)
    }

    /**
     * Loads a [TrajectoryConfig] from [file].
     */
    @JvmStatic
    fun loadConfig(file: File) = MAPPER.readValue(file, TrajectoryConfig::class.java)

    /**
     * Loads a [Trajectory] from [file].
     */
    @JvmStatic
    fun load(file: File) = loadConfig(file).toTrajectory()
}