package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

//@Disabled
@Autonomous(name="Basic7908Autonomous", group="7098")
public class Basic7908Autonomous extends OpMode
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
    }

    @Override
    public void loop()
    {
        telemetry.addData("-gamepad1.left_stick_y", -gamepad1.left_stick_y);
        telemetry.addData("-gamepad1.right_stick_y", -gamepad1.right_stick_y);
        robot.drive(-gamepad1.left_stick_y, -gamepad1.right_stick_y);
    }

    @Override
    public void stop()
    {
    }
}
