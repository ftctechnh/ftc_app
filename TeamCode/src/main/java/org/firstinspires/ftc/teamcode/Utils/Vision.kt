package org.firstinspires.ftc.teamcode.Utils


import com.qualcomm.robotcore.eventloop.opmode.OpMode

import org.firstinspires.ftc.robotcore.external.ClassFactory
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection
import org.firstinspires.ftc.robotcore.external.tfod.Recognition
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector

class Vision(internal var opMode: OpMode) {
    private val l = Logger("VISION")
    private var vuforia: VuforiaLocalizer? = null
    private var tfod: TFObjectDetector? = null
    fun startVision() {
        initVuforia()
        initTfod()
        tfod!!.activate()
    }

    fun shutDown() {
        tfod!!.shutdown()
    }

    fun detectRobust(numPolls: Int,delay:Int=10): Int {
        var d: Int
        for (i in 0 until numPolls) {
            d = detect()
            l.logData("Poll #", i + 1)
            l.logData("Val", d)
            if (d != -1) {
                return d
            }
            wait(delay)
        }
        l.log("didnt find in max tries, returning -1")
        return -1
    }

    fun detect(): Int {
        l.log("detect() called")
        val updatedRecognitions = tfod!!.updatedRecognitions
        if (updatedRecognitions != null) {
            var goldMineralX = -1
            for (recognition in updatedRecognitions) {
                if (recognition.label == LABEL_GOLD_MINERAL) {
                    goldMineralX = recognition.left.toInt()
                    l.logData("GoldX", goldMineralX)
                    l.logData("Confidence", recognition.confidence.toDouble())
                    break
                }
            }


            return goldMineralX
        } else {
            l.logData("GoldX", -1)
            return -1
        }
    }

    /**
     * Initialize the Vuforia localization engine.
     */
    private fun initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        val parameters = VuforiaLocalizer.Parameters()

        parameters.vuforiaLicenseKey = VUFORIA_KEY
        parameters.cameraDirection = CameraDirection.BACK

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters)
        l.log("initialized vuforia")
        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    /**
     * Initialize the Tensor Flow Object Detection engine.
     */
    private fun initTfod() {
        val tfodMonitorViewId = opMode.hardwareMap.appContext.resources.getIdentifier(
                "tfodMonitorViewId", "id", opMode.hardwareMap.appContext.packageName)
        val tfodParameters = TFObjectDetector.Parameters(tfodMonitorViewId)
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia)
        tfod!!.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL)
        l.log("initialized tfod")

    }

    companion object {
        private val TFOD_MODEL_ASSET = "RoverRuckus.tflite"
        private val LABEL_GOLD_MINERAL = "Gold Mineral"
        private val LABEL_SILVER_MINERAL = "Silver Mineral"
        private val VUFORIA_KEY = "AQgn1d//////AAAAGS+F+GWwAEbtqn64lm+fvolRqft5tIJLGdUCsB51qVZHMP3UU8cTCBMKvjCBUTxHfkooO1dljaRLNzaDMMTbWw978Agd7qMrUQF/I4dsE+oVUhLVTHxHPl4r8T4LJ1+B5KHvXQyTr7S3bTU1xy/id/uACCppztVO6mH6Aj0FwY/v3lDYnL9sQNVi2DNXNrnQmmshyJC74C4Se8a6A/II7vcaQ00Ot3PlSB9LjH6K28EQ3oiLnc6tKTGjbU+uTBdoix2KUDL7xVa8c6biG2lcuu7j6dRrw/uvUrh7RpWcmvQDdoshtLlXLsvacLwr5NzMX+4quVkydj/3KRrixOKnepk0ZSPiSlt+J+ThynHcgevu"
    }
}