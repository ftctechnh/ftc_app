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
    NullbotHardware robot;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap, this, gamepad1, gamepad2);
        robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();

        while (opModeIsActive()) {

            double turnDegree = gamepad1.right_stick_y * 0.15 + 0.35;

            double moveSpeed = gamepad1.left_stick_x * 0.5;

            double[] driveSpeeds = new double[]{0, 0, 0, 0};

            if (getLeftStickDist(gamepad1) > 0.15) {
                for (int i = 0; i < 4; i++) {
                    driveSpeeds[i] = moveSpeed;

                    if (i == 1 || i == 2) {
                        driveSpeeds[i] *= -1;
                    }

                    if (i % 2 == 0) {
                        driveSpeeds[i] += turnDegree;
                    } else {
                        driveSpeeds[i] -= turnDegree;
                    }

                }
            }
            robot.setMotorSpeeds(driveSpeeds);
        }
    }
}
