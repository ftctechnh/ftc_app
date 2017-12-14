package org.firstinspires.ftc.teamcode.TestCode.Vuforia;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.UtilVuforia;


/**
 * Tests the Vuforia wrapper class
 */
@TeleOp(name = "Vuforia Wrapper" , group = "Prototypes")
@Disabled
@SuppressWarnings("unused")
public class OpVuforiaWrapperTest extends LinearOpMode
{
    private UtilVuforia _vuforia = new UtilVuforia();


    @Override
    public void runOpMode() throws InterruptedException
    {
        _vuforia.init(hardwareMap , VuforiaLocalizer.CameraDirection.BACK);

        _vuforia.activate();

        waitForStart();

        while(opModeIsActive())
        {
            _vuforia.readMarker();

            telemetry.addData("Vu Mark" , _vuforia.currentMarker());
            telemetry.addData("Trans X" , _vuforia.xTrans());
            telemetry.addData("Trans Y" , _vuforia.yTrans());
            telemetry.addData("Trans Z" , _vuforia.zTrans());
            telemetry.addData("Rot x" , _vuforia.xAngle());
            telemetry.addData("Rot y" , _vuforia.yAngle());
            telemetry.addData("Rot z" , _vuforia.zAngle());
            telemetry.update();
        }
    }
}
