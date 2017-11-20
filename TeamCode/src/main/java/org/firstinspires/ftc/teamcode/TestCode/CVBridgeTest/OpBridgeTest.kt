@file:Suppress("PackageDirectoryMismatch")
package org.directcurrent.test.cvbridgetest


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.directcurrent.opencv.CVBridge


@TeleOp(name = "CVBridge Test" , group = "Prototypes")
@Suppress("unused", "PropertyName")
class OpBridgeTest: LinearOpMode()
{
    val _base = Base()

    override fun runOpMode()
    {
        waitForStart()

        while(opModeIsActive())
        {
            try
            {
                telemetry.addLine("OpenCV Red Jewel")

                for (i in CVBridge.redJewelPoints)
                {
                    telemetry.addLine("(" + i.x() + " , " + i.y() + ")")
                    telemetry.addLine()
                }


                telemetry.addLine("OpenCV Blue Jewel")

                for(i in CVBridge.blueJewelPoints)
                {
                    telemetry.addLine("(" + i.x() + " , " + i.y() + ")")
                    telemetry.addLine()
                }


                if(CVBridge.redJewelPoints.size != 1 && CVBridge.blueJewelPoints.size != 1)
                {
                    telemetry.addData("Decision" , "Uncertain")
                }
                else if(CVBridge.redJewelPoints[0].y() > CVBridge.blueJewelPoints[0].y())
                {
                    telemetry.addData("Decision" , "Red on left")
                }
                else
                {
                    telemetry.addData("Decision" , "Blue on left")
                }
            }
            catch(e: Exception)
            {
                e.printStackTrace()
            }

            telemetry.update()
        }
    }
}