package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "BasicTele")
public class BasicBotTele extends OpMode
{
    BasicBot basicBot;

    @Override
    public void init()
    {
        basicBot = new BasicBot(hardwareMap);
        gamepad1.setJoystickDeadzone(.1f);
    }

    @Override
    public void loop()
    {
        telemetry.addData("LJoyStick= ", -gamepad1.left_stick_y);
        telemetry.addData("RJoyStick= ", -gamepad1.right_stick_y);
        telemetry.addData("Right Encod Ct: ", basicBot.getDriveRightOne().getCurrentPosition());
        telemetry.addData("Left Encod Ct: ", basicBot.getDriveLeftOne().getCurrentPosition());
        telemetry.update();
        basicBot.driveMotors(-gamepad1.left_stick_y, -gamepad1.right_stick_y);
    }

    @Override
    public void stop()
    {
        basicBot.stopDriveMotors();
        super.stop();
    }
}
