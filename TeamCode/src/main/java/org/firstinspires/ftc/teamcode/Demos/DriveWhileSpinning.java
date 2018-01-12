package org.firstinspires.ftc.teamcode.Demos;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.MainTeleOp;
import org.firstinspires.ftc.teamcode.NullbotHardware;

/**
 * Created by guberti on 12/8/2017.
 */
@TeleOp(name="DEMO Drive while spinning", group="Demo")
public class DriveWhileSpinning extends MainTeleOp {
    NullbotHardware robot = new NullbotHardware();

    @Override
    public void runOpMode() {
        robot.init(hardwareMap, this, gamepad1, gamepad2);

        // Enables hardware-controlled PID wheel speeds
        robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();

        while (opModeIsActive()) {

            // Use the bumpers to compute a turn speed
            double turnSpeed = 0;
            if (gamepad1.left_bumper && !gamepad1.right_bumper) {
                turnSpeed = 0.3;
            } else if (gamepad1.right_bumper && !gamepad1.left_bumper) {
                turnSpeed = -0.3;
            }

            double[] driveSpeeds;

            if (getLeftStickDist(gamepad1) > 0.15) {

                double controllerAngle =
                        Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) + Math.PI / 2;
                controllerAngle = robot.normAngle(controllerAngle);

                robot.updateReadings(); // Tells the robot to read from the IMU
                double robotAngle = robot.normAngle(controllerAngle + robot.getGyroHeading());

                driveSpeeds = robot.getDrivePowersFromAngle(robotAngle);

            } else {
                driveSpeeds = new double[]{0, 0, 0, 0};
            }

            for (int i = 0; i < 4; i++) {
                double fTS;

                if (i % 2 == 0) {
                    fTS = turnSpeed;
                } else {
                    fTS = -turnSpeed;
                }

                robot.motorArr[i].setPower(driveSpeeds[i] * 0.7 + fTS);
            }
        }
    }
}
