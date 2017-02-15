package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.HolonomicRobot;

/**
 * Created by 292486 on 2/9/2017.
 */

public class HolonomicTankStrafe extends OpMode {

    HolonomicRobot robot = new HolonomicRobot();

    private double l, r, triggerL, triggerR;

    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {
        l = -gamepad1.left_stick_y; //Left vertical joystick value is reversed
        r = gamepad1.right_stick_y;
        triggerL = gamepad1.left_trigger;
        triggerR = gamepad1.right_trigger;

        robot.tankSmooth(-gamepad1.left_stick_y, gamepad1.right_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

        if(gamepad1.x)
        {
            triggerR /= 2.0;
            triggerL /= 2.0;
            l /= 2.0;
            r /= 2.0;
        }

        if(gamepad1.right_bumper)
        {
            robot.shooterRed.setPower(0.75);
            robot.shooterBlue.setPower(0.75);
        } else if(gamepad1.left_bumper) {
            robot.shooterRed.setPower(-.5);
            robot.shooterBlue.setPower(-.5);
        } else {
            robot.shooterRed.setPower(0);
            robot.shooterBlue.setPower(0);
        }

        if(gamepad2.right_bumper)
        {
            robot.intake.setPower(.75);
        } else if(gamepad2.left_bumper) {
            robot.intake.setPower(-0.75);
        } else {
            robot.intake.setPower(0);
        }
    }
}
