package org.firstinspires.ftc.teamcode.vision

import com.qualcomm.robotcore.hardware.HardwareMap
import com.vuforia.CameraDevice
import org.firstinspires.ftc.robotcore.external.ClassFactory
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer

/**
 * Created by David Lukens on 10/31/2018.
 */

class MasterVision(private val parameters: VuforiaLocalizer.Parameters, val hMap: HardwareMap, val useFlash:Boolean, val tfLiteAlgorithm: TFLiteAlgorithm) : Thread() {
    var vuforiaLocalizer: VuforiaLocalizer? = null
    val tfLite = TFLite(this)

    enum class TFLiteAlgorithm{
        INFER_LEFT,
        INFER_RIGHT,
        INFER_NONE
    }


    fun init() {
        if (vuforiaLocalizer == null)
            vuforiaLocalizer = ClassFactory.getInstance().createVuforia(parameters)
        tfLite.init()
    }

    fun enable() {
        init()
        tfLite.enable()
        CameraDevice.getInstance().setFlashTorchMode(useFlash)
    }

    fun disable() {
        tfLite.disable()
        CameraDevice.getInstance().setFlashTorchMode(false)
    }

    fun shutdown() {
        disable()
        tfLite.shutdown()
    }

    override fun run() {
        try {
            while (true) {
                tfLite.updateSampleOrder()
            }
        } catch (ex: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }

    init {
        start()
    }
}