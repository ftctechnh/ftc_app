package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;
import org.firstinspires.ftc.teamcode.Robot.FlagBotHardware;
import org.firstinspires.ftc.teamcode.Robot.Team2901Robot;

/**
 * Created by gallagherb20503 on 9/30/2017.
 */

@TeleOp(name="SimpleDrive", group="TeleOp")
public class Simple_Drive_2901 extends OpMode     {

    Team2901Robot robot       = new Team2901Robot();

    @Override
    public void init () {

        robot.init(hardwareMap);
    }


    @Override
    public void loop() {
        double left;
        double right;

        // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)
        left = -gamepad1.left_stick_y;
        right = -gamepad1.right_stick_y;
        robot.leftMotor.setPower(left);
        robot.rightMotor.setPower(right);
    }
}