package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import java.lang.*;

//the one that controls like a racing game

/**
 * Created by BeehiveRobotics-3648 on 7/28/2017.
 */

@TeleOp(name = "TeleOpVG", group = "linear OpMode")
@Disabled
public class TeleOpVG2 extends OpMode {
    iDrive Drive = new OpenLoop();

    @Override
    public void init() {
        Drive.Init(hardwareMap.dcMotor.get("m1"), hardwareMap.dcMotor.get("m2"), hardwareMap.dcMotor.get("m3") , hardwareMap.dcMotor.get("m4"));
    }

    @Override
    public void loop() {
        float speed = 0;
        float steering = 0;
        float middle = 0;
        if (gamepad1.right_trigger > gamepad1.left_trigger) {
            speed = gamepad1.right_trigger;
            steering = gamepad1.left_stick_x * speed;
        }
        else {
            speed = -gamepad1.left_trigger;
            steering = -gamepad1.left_stick_x * speed;
        }
        Drive.Drive(steering, speed, 0);
    }

    public void reverseMotor(DcMotor motor) {
        motor.setDirection(DcMotor.Direction.REVERSE);
    }
}
// TODO: Test the OpenLoop class and iDrive Interface implemented in this teleop