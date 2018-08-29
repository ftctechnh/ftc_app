package org.firstinspires.ftc.teamcode.DriveSystems.Swerve;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Hardware.SwerveHardware;
import org.firstinspires.ftc.teamcode.Utilities.Control.RampingController;

public class TeleOpSwerve extends LinearOpMode {

    SwerveHardware robot = new SwerveHardware(this);
    RampingController rampController;
    int accelTime = 250;
    Module[] modules = new Module[4];

    final double turnVolatility = 2; // Higher number makes turning more jerklike, but faster

    final double moveMotorThreshold = 0;
    final double triggerThreshold = 0.10;
    final double minSlowModePower = 0.45;
    final int headingLockMS = 1000;

    double initialHeading;
    double desiredHeading;
    boolean slowMode;


    @Override
    public void runOpMode() {
        robot.init();

        for (int i = 0; i < robot.motorArr.length; i++) {
            modules[i] = new Module(robot.motorArr[i], robot.servoArr[i],
                    robot.encoderArr[i], robot.maxVoltages[i], robot.zeroVoltages[i]);
        }

        initialHeading = robot.getGyroHeading() + Math.PI;
        desiredHeading = initialHeading;
        slowMode = false;

        waitForStart();


        while (opModeIsActive()) {
            robot.updateReadings();

            // Determine desired heading
            // This is ugly, but I can't think of anything better
            double ctrlFrac = -1;
            if      (gamepad1.dpad_up    && gamepad1.dpad_left) {ctrlFrac = 1/8;}
            else if (gamepad1.dpad_left  && gamepad1.dpad_down) {ctrlFrac = 3/8;}
            else if (gamepad1.dpad_down && gamepad1.dpad_right) {ctrlFrac = 5/8;}
            else if (gamepad1.dpad_right && gamepad1.dpad_up  ) {ctrlFrac = 7/8;}
            else if (gamepad1.dpad_up)    {ctrlFrac = 0;}
            else if (gamepad1.dpad_left)  {ctrlFrac = 1/4;}
            else if (gamepad1.dpad_down)  {ctrlFrac = 1/2;}
            else if (gamepad1.dpad_right) {ctrlFrac = 3/4;}

            if (ctrlFrac >= 0) {
                desiredHeading = robot.normAngle(initialHeading + Math.PI * 2 * ctrlFrac);
            }

            double heading = robot.getGyroHeading();
            double turnSpeed = robot.normAngle(desiredHeading - heading)/Math.PI;

            Vector vector = new Vector(gamepad1.left_stick_y, gamepad1.left_stick_x, turnSpeed);

            for (int i = 0; i < robot.motorArr.length; i++) {
                modules[i].singleInstance(vector.getAngles()[i]);
                modules[i].runMotor(vector.getMotors()[i]);
            }
        }
    }
}
