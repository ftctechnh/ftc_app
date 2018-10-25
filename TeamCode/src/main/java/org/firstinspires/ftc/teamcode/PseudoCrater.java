package org.firstinspires.ftc.teamcode;
import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.disnodeteam.dogecv.detectors.roverrukus.SamplingOrderDetector;
import java.util.*;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Simon on 10/21/18.
 */
@Autonomous
public class PseudoCrater extends LinearOpMode {

    ErnieHardware robot = new ErnieHardware();

    private GoldAlignDetector detector;

    static final double TICKS_PER_MOTOR_REV = 1120; // Will change depending on encoder
    static final double WHEEL_DIAMETER_INCHES = 4.0; // Will change depending on wheel
    static final double TICKS_PER_INCH = (TICKS_PER_MOTOR_REV) / (WHEEL_DIAMETER_INCHES * Math.PI);



    @Override
    public void runOpMode() {

        robot.init(hardwareMap);
        detector = new GoldAlignDetector();
        detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        detector.useDefaults();

        // Optional Tuning
        detector.alignSize = 100; // How wide (in pixels) is the range in which the gold object will be aligned. (Represented by green bars in the preview)
        detector.alignPosOffset = 0; // How far from center frame to offset this alignment zone.
        detector.downscale = 0.4; // How much to downscale the input frames

        detector.areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA; // Can also be PERFECT_AREA
        //detector.perfectAreaScorer.perfectArea = 10000; // if using PERFECT_AREA scoring
        detector.maxAreaScorer.weight = 0.005;

        detector.ratioScorer.weight = 5;
        detector.ratioScorer.perfectRatio = 1.0;

        detector.enable();

        waitForStart();

        Unlatch();

        while (!detector.getAligned()) {
            if (detector.alignPosOffset < 0) {
                robot.leftDrive.setPower(-1);
                robot.leftDrive.setPower(1);
            } else if (detector.alignPosOffset > 0) {
                robot.leftDrive.setPower(1);
                robot.leftDrive.setPower(-1);
            }
        }

        drive(12, 1);


    }


    public void Unlatch(){
        // Extend latching arm to go down
        // Detach with servo
    }

    public void drive(double INCHES, double POWER){
        int TICKS;
        TICKS = (int)(INCHES * TICKS_PER_INCH); //Might need to set INCHES to negative since that's what it was with last year's drivetrain

        robot.leftDrive.setTargetPosition(robot.leftDrive.getCurrentPosition() + TICKS);
        robot.rightDrive.setTargetPosition(robot.leftDrive.getCurrentPosition() + TICKS);

        robot.leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.leftDrive.setPower(POWER);
        robot.rightDrive.setPower(POWER);

        while(robot.leftDrive.isBusy() && robot.rightDrive.isBusy()){

        }

        robot.leftDrive.setPower(0);
        robot.rightDrive.setPower(0);

        robot.leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

}
