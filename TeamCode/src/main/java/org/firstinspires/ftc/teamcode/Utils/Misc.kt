package org.firstinspires.ftc.teamcode.Utils


public fun wait(mill: Int) {
    try {
        Thread.sleep(mill.toLong())
    } catch (e: Exception) {
        Thread.currentThread().interrupt()
    }
}