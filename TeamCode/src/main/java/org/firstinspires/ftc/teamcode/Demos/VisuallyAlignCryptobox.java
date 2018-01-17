package org.firstinspires.ftc.teamcode.Demos;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.detectors.CryptoboxDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.NullbotHardware;

/**
 * Created by guberti on 1/9/2018.
 */

@Autonomous(name="DEMO See cryptobox", group="Demo")
@Disabled
public class VisuallyAlignCryptobox extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private CryptoboxDetector cryptoboxDetector = null;
    private NullbotHardware robot = new NullbotHardware();

    @Override
    public void runOpMode() {
        robot.init(hardwareMap, this, gamepad1, gamepad2);
        cryptoboxDetector = new CryptoboxDetector();
        cryptoboxDetector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        cryptoboxDetector.rotateMat = true;
        cryptoboxDetector.enable();

        waitForStart();
        runtime.reset();

        while(opModeIsActive()) {
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("isCryptoBoxDetected", cryptoboxDetector.isCryptoBoxDetected());
            telemetry.addData("isColumnDetected ",  cryptoboxDetector.isColumnDetected());

            telemetry.addData("Center crypto box ",  cryptoboxDetector.getCryptoBoxCenterPosition());
            telemetry.update();
        }
    }
}
