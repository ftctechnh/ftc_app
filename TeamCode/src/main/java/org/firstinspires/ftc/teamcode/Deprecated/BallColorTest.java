package org.firstinspires.ftc.teamcode.Deprecated;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by rommac100 on 9/13/17.
 */
@Autonomous(name="BallDetectionTesting", group="Concept")
public class BallColorTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {


        waitForStart();
        /*while(opModeIsActive()) {
            FrameGrabber frameGrabber = FtcRobotControllerActivity.frameGrabber;
            frameGrabber.grabSingleFrame();
            while (!frameGrabber.isResultReady()&&opModeIsActive()) {
                Thread.sleep(5);
            }

            //Get the result
            ImageProcessorResult imageProcessorResult = frameGrabber.getResult();
            BallColorResult result = (BallColorResult) imageProcessorResult.getResult();

            BallColorResult.BallColor ballColourLeft = result.getLeftColor();
            BallColorResult.BallColor ballColourRight = result.getRightColor();

            telemetry.addData("Result", result); //Display it on telemetry
            telemetry.addData("LeftBall", ballColourLeft);
            telemetry.addData("rightBall", ballColourRight);
            telemetry.update();
            //wait before quitting (quitting clears telemetry)
            Thread.sleep(1000);
        }*/
    }
}