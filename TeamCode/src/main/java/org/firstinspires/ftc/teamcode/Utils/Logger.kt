package org.firstinspires.ftc.teamcode.Utils
public class Logger(vararg var tags: String){

    fun log(vararg text: String){
        var toPrint = ""
        tags.forEach { tag -> toPrint += "[${tag.toUpperCase()}] "; }
        text.forEach { t -> toPrint += "${t} "; }
        println(toPrint)
    }

    fun logData(name: String, value: Double){
        log("${name}: ${value}")
    }
    fun logData(name: String, value: Int){
        log("${name}: ${value}")
    }
    fun logData(name: String, value: Boolean){
        log("${name}: ${value}")
    }
    fun logData(name: String, value: Long){
        log("${name}: ${value}")
    }
}

fun main(){
==}
