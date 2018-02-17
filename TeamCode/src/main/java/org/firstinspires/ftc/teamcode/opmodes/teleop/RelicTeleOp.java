package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.math.vector.Vec3;
import org.firstinspires.ftc.teamcode.opmodes.base.RelicBase;

import static org.firstinspires.ftc.teamcode.robot.peripherals.locomotion.Drivetrain.DriveMode.STATIC;

/**
 * Created by Derek on 2/9/2018.
 *
 * TeleOp for the RelicRecovery Game
 */

@TeleOp(name="RelicTeleOp",group = "Relic")
public class RelicTeleOp extends RelicBase {

    @Override
    public void init() {
        super.init();
        super.drivetrain.setDriveMode(STATIC);
    }

    @Override
    public void loop() {
        //Controls for the gripper
        if (gamepad2.dpad_right || gamepad2.dpad_left) {
            gripper.setPosition(gamepad2.dpad_right ? gripper.getPosition() + clawIncrement : gripper.getPosition() - clawIncrement);
        }

        check:
        {
            if (wrapper2.X.isPressed()) {
                gripper.setPosition(gripper.getClampPositions().OPEN);
                break check;
            }

            if (wrapper2.Y.isPressed()) {
                gripper.setPosition(gripper.getClampPositions().CENTER);
                break check;
            }

            if (wrapper2.B.isPressed()) {
                gripper.setPosition(gripper.getClampPositions().CLOSED);
            }
        }

        //controls the main arm
        double armPower;
        if (gamepad2.left_stick_y >= 0) {
            armPower = gamepad2.right_stick_y * boomUpFactor;
        } else {
            armPower = gamepad2.right_stick_y * boomDownFactor;
        }

        arm.setPower(armPower);
        Vec3 movement;

        if (gamepad1.right_trigger >= 0.6) {
            movement = new Vec3(
                    - gamepad1.left_stick_x, //Strafe Movement
                    gamepad1.left_stick_y,   //Forward Movement
                    gamepad1.right_stick_x   //Angle of rotation
            );
        } else {
            movement = new Vec3(
                    - gamepad1.left_stick_x * strafeFactor,  //Strafe Movement
                    gamepad1.left_stick_y * straightFactor,  //Forward Movement
                    gamepad1.right_stick_x * turnFactor      //Angle of rotation
            );
        }


        drivetrain.setIntegrator(movement);
        super.loop();
    }

}
