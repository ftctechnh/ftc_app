package org.firstinspires.ftc.teamcode.Demos;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.MainTeleOp;
import org.firstinspires.ftc.teamcode.NullbotHardware;

/**
 * Created by guberti on 12/8/2017.
 */
@TeleOp(name="DEMO Slew drive", group="Demo")
public class SlewDrive extends MainTeleOp {
    NullbotHardware robot = new NullbotHardware();

    @Override
    public void runOpMode() {
        robot.init(hardwareMap, this, gamepad1, gamepad2);
        robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();

        while (opModeIsActive()) {


            // Decide on a speed to turn, which by default is 35% of maximum speed capacity,
            // but can be made faster or slower depending on the position of the right stick
            double turnDegree = gamepad1.right_stick_y * 0.15 + 0.35;

            // Decide on a speed with which to move, depending on the position of our left joystick
            // Since we could be turning with up to 50% of our robot's movement capacity, we need
            // to ensure that we never use more than 50% for movement either
            double moveSpeed = gamepad1.left_stick_x * 0.5;

            double[] driveSpeeds = new double[]{0, 0, 0, 0};

            if (getLeftStickDist(gamepad1) > 0.15) { // If we should move at all

                for (int i = 0; i < 4; i++) {
                    // By default, the robot should move at the desired movement speed
                    driveSpeeds[i] = moveSpeed;

                    // We need to negate motors 1 and 2 in order to move sideways
                    if (i == 1 || i == 2) {
                        driveSpeeds[i] *= -1;
                    }

                    // Add the turn speed value to the left motors (with even motor numbers),
                    // and subtract it from the right motors (with odd motor numbers)
                    if (i % 2 == 0) {
                        driveSpeeds[i] += turnDegree;
                    } else {
                        driveSpeeds[i] -= turnDegree;
                    }

                }
            }

            // Apply these speeds to the robot
            robot.setMotorSpeeds(driveSpeeds);
        }
    }
}
