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

        robot.getGyroSensor().calibrate();

        // make sure the gyro is calibrated before continuing
        while (!isStopRequested() && robot.getGyroSensor().isCalibrating()) {
            sleep(50);
            telemetry.addData(">", "Calibrating Gyro");
            telemetry.update();
            idle();
        }

        telemetry.addData(">", "Gyro Calibrated");
        telemetry.update();

        while(!isStarted()) {
            telemetry.addData(">", "Integrated Z value = %d", robot.getGyroSensor().getIntegratedZValue());
            telemetry.update();
            idle();
        }

        while(opModeIsActive()) {
            telemetry.addData("light sensor", robot.getFrontLightSensor().getRawLightDetected());
            telemetry.update();

            proportionalLineFollow(0.1);

        }


    }

    protected void proportionalLineFollow(double speed) {
        double error = ProgrammingRobotHardware.LIGHT_SENSOR_PERFECT_VALUE
                - robot.getFrontLightSensor().getRawLightDetected();

        double correction =
                Range.clip(error * ProgrammingRobotHardware.P_LIGHT_FOLLOW_COEFF, -1, 1);

        double leftPower;
        double rightPower;

        telemetry.addData("correction", correction);

        // if the ODS is on the middle of the line
        if(error < 0) {
            rightPower = speed - correction;
            leftPower = speed;
        } else {
            leftPower = speed + correction;
            rightPower = speed;
        }

        // set power for left side
        robot.getFrontLeft().setPower(leftPower);
        robot.getBackLeft().setPower(leftPower);

        // set power for right side
        robot.getFrontRight().setPower(-rightPower);
        robot.getBackRight().setPower(-rightPower);
    }

    protected void gyroPivot(double speed, double angle) {

        double steer;
        double threshold = getGyroError(angle) > 0 ? ProgrammingRobotHardware.GYRO_ERROR_THRESHOLD
                : -ProgrammingRobotHardware.GYRO_ERROR_THRESHOLD;

        while(opModeIsActive() && Math.abs(getGyroError(angle)) > threshold) {

            steer = Range.clip(getGyroError(angle)
                    * ProgrammingRobotHardware.P_GYRO_TURN_COEFF , -1, 1);

            double proportionalSpeed = speed * steer;

            robot.getFrontLeft().setPower(proportionalSpeed);
            robot.getFrontRight().setPower(proportionalSpeed);

            robot.getBackLeft().setPower(proportionalSpeed);
            robot.getBackRight().setPower(proportionalSpeed);
        }

        // when we're on target, stop the robot
        robot.stopRobot();
    }

    private double getGyroError(double targetAngle) {
        double error = targetAngle - robot.getGyroSensor().getIntegratedZValue();

        // keep the error on a range of -179 to 180
        while(opModeIsActive() && error > 180)  error -= 360;
        while(opModeIsActive() && error <= -180) error += 360;

        return error;
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
}
