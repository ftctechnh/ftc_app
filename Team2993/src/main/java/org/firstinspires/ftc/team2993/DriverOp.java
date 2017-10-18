package org.firstinspires.ftc.team2993;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.team2993.structural.RobotHardware;



@TeleOp(name = "TeleOp", group="Regular")
public class DriverOp extends OpMode
{
    private RobotHardware robot;

    private final double threshhold = .05d;
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

        if      (gamepad1.left_trigger > .5f)
            robot.armServo.setPosition(robot.SERVO_CLOSED);
        else if (gamepad1.left_bumper == true)
            robot.armServo.setPosition(robot.SERVO_OPEN);

        if      (gamepad1.right_trigger > .5f)
            robot.SetArm(speed);
        else if (gamepad1.right_bumper == true)
            robot.SetArm(-speed);
    }

    public void telemetry(String msg)
    {
        telemetry.addData("%s", msg);
    }


}
