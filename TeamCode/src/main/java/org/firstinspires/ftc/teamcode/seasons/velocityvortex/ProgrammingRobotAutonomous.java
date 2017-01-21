package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by ftc6347 on 12/30/16.
 */
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
            telemetry.addData("init loop", isStarted());
            telemetry.addData(">", "Integrated Z value = %d", robot.getGyroSensor().getIntegratedZValue());
            telemetry.addData(">", "Gyro error from 90 degrees = %f", getGyroError(90));
            telemetry.update();
            idle();
        }

        robot.getFrontLeft().setPower(0.5);
        robot.getFrontRight().setPower(0.5);
        robot.getBackLeft().setPower(0.5);
        robot.getBackRight().setPower(0.5);

        // turn 90 degrees
        gyroPivot(0.5, 90);

        while(opModeIsActive()) {
            telemetry.addData("gyro error", getGyroError(90));
            telemetry.update();
        }
    }

    protected void proportionalLineFollow(double speed) {

        // this method should only be used in a loop

        // TODO: find actual LIGHT_SENSOR_PERFECT_VALUE value

        double correction = ProgrammingRobotHardware.LIGHT_SENSOR_PERFECT_VALUE
                - robot.getFrontLightSensor().getRawLightDetected();

        // correction positively affects one side and negatively affects other
        robot.getFrontLeft().setPower(speed - correction);
        robot.getBackLeft().setPower(speed - correction);
        robot.getFrontRight().setPower(speed + correction);
        robot.getBackLeft().setPower(speed + correction);
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
