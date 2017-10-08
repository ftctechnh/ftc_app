package org.firstinspires.ftc.teamcode.TestCode.Vuforia;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.Core.Utility.UtilVuforia;


/**
 * Tests the Vuforia wrapper class
 */
@TeleOp(name = "Vuforia Wrapper" , group = "Prototypes")
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
            telemetry.addData("Vu Mark" , _vuforia.currentMarker());
            telemetry.update();
        }
    }
}
