package org.firstinspires.ftc.team9853;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.chathamrobotics.ftcutils.TeleOpMode;

import java.util.Map;

/**
 * teleop opmode
 */
@TeleOp(name = "Driving", group = "General")

public class TeleMode extends TeleOpMode {
    /*
     * Called continuously while opmode is active
     */
    @Override
    public void loop() {
        // Drive
        if(gamepad1.dpad_up){driver.offsetAngle = driver.FRONT_OFFSET;}
        if(gamepad1.dpad_left){driver.offsetAngle = driver.LEFT_OFFSET;}
        if(gamepad1.dpad_down){driver.offsetAngle = driver.BACK_OFFSET;}
        if(gamepad1.dpad_right){driver.offsetAngle = driver.RIGHT_OFFSET;}

        driver.drive(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x, true);

        debug();
    }
}
