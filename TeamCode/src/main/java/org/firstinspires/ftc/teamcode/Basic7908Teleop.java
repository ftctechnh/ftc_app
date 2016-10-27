package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

//@Disabled
@TeleOp(name="Drive", group="7098")
public class Basic7908Teleop extends OpMode
{
    private Hardware7908Robot robot;

    @Override
    public void init()
    {
        robot = new Hardware7908Robot(hardwareMap);
    }

    @Override
    public void init_loop()
    {
    }

    @Override
    public void start()
    {
        robot.winMatch();
    }

    @Override
    public void loop()
    {
        robot.drive(-gamepad1.left_stick_y, -gamepad2.right_stick_y);
    }

    @Override
    public void stop()
    {
    }
}
