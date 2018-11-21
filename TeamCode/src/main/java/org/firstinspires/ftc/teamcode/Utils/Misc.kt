package org.firstinspires.ftc.teamcode.Utils

import java.io.File
import java.io.InputStream


public fun wait(mill: Int) {
    try {
        Thread.sleep(mill.toLong())
    } catch (e: Exception) {
        Thread.currentThread().interrupt()
    }
}
