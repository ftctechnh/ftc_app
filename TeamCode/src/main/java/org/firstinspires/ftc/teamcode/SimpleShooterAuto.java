package org.firstinspires.ftc.teamcode;

import android.graphics.Bitmap;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcontroller.internal.CameraProcessor;

import static org.firstinspires.ftc.teamcode.EeyoreHardware.COUNTS_PER_INCH;

@Autonomous(name="Simple Shooter", group="Iterative Opmode")  // @Autonomous(...) is the other common choice
public class SimpleShooterAuto extends CameraProcessor {
    EeyoreHardware robot = new EeyoreHardware();

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);

        telemetry.addData("Status:", "Initializing");
        telemetry.update();

        // Calibrate gyro
        robot.gyro.calibrate();

        while(robot.gyro.isCalibrating()) {
            Thread.sleep(50);
            idle();
        }

        // Initiate camera
        setCameraDownsampling(9);
        startCamera();

        telemetry.addData("Status:", "Initialized (waiting for start)");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        telemetry.addData("Status:", "Moving...");
        telemetry.update();

        // Move off of the wall
        moveStraight(26);

        // At this point, we can try to score the pre-loaded balls
        shootShooter(1);
        Thread.sleep(800);
        shootShooter(0);
        Thread.sleep(1000);
        shootShooter(1);
        Thread.sleep(1000);
        shootShooter(0);

        // Move forward to knock off the yoga ball
        moveStraight(20);
        gyroTurn(45);

        telemetry.addData("Status:", "Shutting down...");
        telemetry.update();

        stopCamera();

        // Run until the end of the match (driver presses STOP)
        while(opModeIsActive()) {
            telemetry.addData("Status:", "Idling...");
            telemetry.update();
            idle();
        }
    }

    public void shootShooter(int power) {
        robot.shooter1.setPower(power);
        robot.shooter2.setPower(power);
    }

    public void moveStraight(float inches) throws InterruptedException {
        robot.l1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.r1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();

        robot.l1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.r1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        idle();

        int targetLeft = robot.l1.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);
        int targetRight = robot.r1.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);

        robot.l1.setPower(0.25);
        robot.l2.setPower(0.25);
        robot.r1.setPower(0.25);
        robot.r2.setPower(0.25);

        while((robot.l1.getCurrentPosition() < targetLeft) && (robot.r1.getCurrentPosition() < targetRight)) {
            telemetry.addData("Target", "left=" + targetLeft + " " + "right=" + targetRight);
            telemetry.addData("Position", "left=" + robot.l1.getCurrentPosition() + " " + "right=" + robot.r1.getCurrentPosition());
            telemetry.addData("Power", "left=" + robot.l1.getPower() + " " + "right=" + robot.r1.getPower());
            telemetry.update();

            idle();
        }

        robot.l1.setPower(0);
        robot.l2.setPower(0);
        robot.r1.setPower(0);
        robot.r2.setPower(0);
    }

    public void gyroTurn(int degree) throws InterruptedException
    {
        int currentDirection = robot.gyro.getHeading();
        double turnMultiplier = 0.05;

        robot.l1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.r2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        idle();

        // First, check to see if we are pointing in the correct direction
        while(Math.abs(degree - currentDirection) > 5) //If we are more than 5 degrees off target, make corrections before moving
        {
            currentDirection = robot.gyro.getHeading();

            int error = degree - currentDirection;
            double speedAdjustment = turnMultiplier * error;

            double leftPower = 0.5 * Range.clip(speedAdjustment, -1, 1);
            double rightPower = 0.5 * Range.clip(-speedAdjustment, -1, 1);

            // Finally, assign these values to the motors

            robot.r1.setPower(rightPower);
            robot.r2.setPower(rightPower);
            robot.l1.setPower(leftPower);
            robot.l2.setPower(leftPower);
            idle();
        }

        robot.r1.setPower(0);
        robot.r2.setPower(0);
        robot.l1.setPower(0);
        robot.l2.setPower(0);
    }
}
