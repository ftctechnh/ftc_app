package org.firstinspires.ftc.team2993;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.team2993.structural.RobotHardware;
import org.firstinspires.ftc.team2993.structural.Sensors;



@TeleOp(name = "TeleOp", group="Regular")
public class DriverOp extends OpMode
{
    private RobotHardware robot;

    private final double threshhold = 0.2;
    private final double speed = .5d;

    @Override
    public void init()
    {
        robot = new RobotHardware(hardwareMap);
        robot.init();
    }

    @Override
    public void loop()
    {
        driverOne();
    }

    @Override
    public void stop()
    {
        robot.SetDrive(0,0);
    }

    public void driverOne()
    {
        double leftStick = gamepad1.left_stick_y;
        double rightStick = gamepad1.right_stick_y;
        leftStick = (Math.abs(leftStick) > threshhold ? leftStick : 0);
        rightStick = (Math.abs(rightStick) > threshhold ? rightStick : 0);

        robot.driveLeft(leftStick);
        robot.driveRight(rightStick);

        if (gamepad1.b)
            robot.SetArm(.25);
        else if (gamepad1.x)
            robot.SetArm(-.25);
        else
            robot.SetArm(0);

        if (gamepad1.a || gamepad1.left_bumper)
            robot.claw.setPosition(0);
        else if (gamepad1.y || gamepad1.right_bumper)
            robot.claw.setPosition(1);

        if (gamepad1.dpad_down)
            robot.sideArm.setPosition(-1);
        else if (gamepad1.dpad_up)
            robot.sideArm.setPosition(1);
    }

    public void telemetry(double msg)
    {
        telemetry.addData("%s", msg);
    }

    public void telemetry(String msg)
    {
        telemetry.addData("%s", msg);
    }
}
