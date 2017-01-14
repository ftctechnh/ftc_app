package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by ftc6347 on 12/30/16.
 */
@Disabled
@Autonomous(name = "Programming Robot Autonomous", group = "autonomous")
public class ProgrammingRobotAutonomous extends LinearOpMode {

    private ProgrammingRobotHardware robot;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new ProgrammingRobotHardware(hardwareMap, telemetry);

        // make sure the gyro is calibrated before continuing
        while (!isStopRequested() && robot.getGyroSensor().isCalibrating()) {
            sleep(50);
            telemetry.addData(">", "Calibrating Gyro");
            telemetry.update();
            idle();
        }

        telemetry.addData(">", "Gyro Calibrated");
        telemetry.update();

        // Wait for the game to start (Display Gyro value), and reset gyro before we move..
        while (!isStarted()) {
            telemetry.addData(">", "Integrated Z value = %d", robot.getGyroSensor().getIntegratedZValue());
            telemetry.update();
            idle();
        }

        while(opModeIsActive()) {
            telemetry.addData("steer", getSteer(getGyroError(90)));
            telemetry.addData("error", getGyroError(90));
            telemetry.update();
        }

//        gyroDrive(90, 8, 0.2);
//        encoderDrive(0.2, 4);
    }

    public void gyroDrive(int angle, int distance, double speed) {
        int moveCounts = (int)(distance * ProgrammingRobotHardware.COUNTS_PER_INCH);

        int leftSideTarget = robot.getFrontLeft().getCurrentPosition() + moveCounts;
        int rightSideTarget = robot.getFrontRight().getCurrentPosition() + moveCounts;

        // set the target position for the left drive motors
        robot.getFrontLeft().setTargetPosition(leftSideTarget);
        robot.getBackLeft().setTargetPosition(leftSideTarget);

        // set the target position for the right drive motors
        robot.getFrontRight().setTargetPosition(-rightSideTarget);
        robot.getBackRight().setTargetPosition(-rightSideTarget);

        // set all motors to be RUN_TO_POSITION
        robot.getFrontLeft().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.getFrontRight().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.getBackLeft().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.getBackRight().setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // set the initial speed for the left drive motors
        robot.getFrontLeft().setPower(speed);
        robot.getBackLeft().setPower(speed);

        // set the initial speed for the right drive motors
        robot.getFrontRight().setPower(speed);
        robot.getBackRight().setPower(speed);

        while(opModeIsActive() && robot.areDriveMotorsBusy()) {
            double steer = getSteer(getGyroError(angle));
            double leftPower;
            double rightPower;

//            telemetry.addData("steer", steer);
//            telemetry.update();

            if(distance < 0) {
                steer *= -1;
            }

            // set target to negative if necessary
            if(getGyroError(angle) < 0) {
                int newTarget = -robot.getFrontRight().getTargetPosition();
                robot.getFrontRight().setTargetPosition(newTarget);
                robot.getBackRight().setTargetPosition(newTarget);
            }

            leftPower = speed + steer;
            rightPower = speed + steer;

            // normalize speeds if any one exceeds +/- 1.0;
            double max = Math.max(Math.abs(leftPower), Math.abs(rightPower));
            if (max > 1.0) {
                leftPower /= max;
                rightPower /= max;
            }

            // set the corrected speed for the left drive motors
            robot.getFrontLeft().setPower(leftPower);
            robot.getBackLeft().setPower(leftPower);

            // set the corrected speed for the right drive motors
            robot.getFrontRight().setPower(rightPower);
            robot.getBackRight().setPower(rightPower);

            telemetry.addData("left speed", robot.getFrontLeft().getPower());
            telemetry.addData("right speed", robot.getFrontRight().getPower());
            telemetry.update();
        }

        robot.stopDriveMotors();

        // set all motors to be RUN_USING_ENCODER
        robot.getFrontLeft().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.getFrontRight().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.getBackLeft().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.getBackRight().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void encoderDrive(double speed, double inches) {
        int target = (int)(inches * ProgrammingRobotHardware.COUNTS_PER_INCH);

        // set the target position for each motor
        robot.getFrontLeft().setTargetPosition(target);
        robot.getFrontRight().setTargetPosition(-target);
        robot.getBackRight().setTargetPosition(-target);
        robot.getBackLeft().setTargetPosition(target);

        // set RUN_TO_POSITION for each motor
        robot.getFrontLeft().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.getFrontRight().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.getBackRight().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.getBackLeft().setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // set the power for the left drive motors
        robot.getFrontLeft().setPower(speed);
        robot.getBackLeft().setPower(speed);

        // set the power for the right drive motors
        robot.getFrontRight().setPower(speed);
        robot.getBackRight().setPower(speed);

        while(opModeIsActive() && robot.areDriveMotorsBusy()) {
            idle();
        }

        robot.stopDriveMotors();

        // set RUN_TO_POSITION for each motor
        robot.getFrontLeft().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.getFrontRight().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.getBackRight().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.getBackLeft().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public double getSteer(double error) {
        return Range.clip(Math.abs(error)
                * ProgrammingRobotHardware.P_DRIVE_COEFF, 0, 1);
    }

    public double getGyroError(double targetAngle) {
        double robotError = targetAngle - robot.getGyroSensor().getIntegratedZValue();
        while (robotError > 180 && opModeIsActive())  robotError -= 360;
        while (robotError <= -180 && opModeIsActive()) robotError += 360;
        return robotError;
    }
}
