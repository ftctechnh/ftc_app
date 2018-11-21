package org.firstinspires.ftc.teamcode.Utils

import com.fasterxml.jackson.module.kotlin.*
import org.firstinspires.ftc.teamcode.Models.PIDConstants
import java.io.File
import java.io.InputStream

fun getPIDConstantsFromFile(path:String):PIDConstants{
    val mapper = jacksonObjectMapper()
    val jsonString = readFile(path)
    val constants: PIDConstants = mapper.readValue(jsonString)
    return constants
}

fun readFile (path:String):String {
    val inputStream: InputStream = File(path).inputStream()
    val inputString = inputStream.bufferedReader().use { it.readText() }
    return inputString
}