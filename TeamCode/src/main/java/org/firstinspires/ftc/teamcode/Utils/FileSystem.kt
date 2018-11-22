package org.firstinspires.ftc.teamcode.Utils

import android.os.Environment
import com.fasterxml.jackson.module.kotlin.*
import org.firstinspires.ftc.teamcode.Models.PIDConstants
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import java.lang.Exception

fun getPIDConstantsFromFile(path:String):PIDConstants{
    val mapper = jacksonObjectMapper()
    val jsonString = readFile(path)
    val constants: PIDConstants = mapper.readValue(jsonString)
    return constants
}

fun writeFile(path:String,content:String,overWrite:Boolean=false){
    val l:Logger = Logger("FILEREADER",path)
    val fullPath = Environment.getExternalStorageDirectory().path + "/Intersect/" + path
    l.log("Writing to $fullPath")
    val file:File = File(fullPath)

    if(overWrite){
        file.writeText(content)

    }else{
        file.appendText(content)
    }
    l.log("Wrote $content")
}

fun clearFile(path:String){
    val l:Logger = Logger("CLEAR FILe",path)
    l.log("Clearing file $path")
    writeFile(path=path,content="",overWrite = true)
    l.log("Cleared file $path")

}

fun readFile (path:String):String {
    val l:Logger = Logger("FILEREADER",path)
    val fullPath = Environment.getExternalStorageDirectory().path + "/Intersect/" + path
    l.log("Reading from path: $fullPath")
    val inputStream: InputStream = File(fullPath).inputStream()
    val fileString = inputStream.bufferedReader().use { it.readText()}
    l.log("File read:\n $fileString")
    return fileString
}