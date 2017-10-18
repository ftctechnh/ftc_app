package org.firstinspires.ftc.team2993;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.team2993.structural.RobotHardware;



@TeleOp(name = "TeleOp", group="Regular")
public class DriverOp extends OpMode
{
    RobotHardware robot;

    @Override
    public void init()
    {
        robot = new RobotHardware(hardwareMap);
        robot.init();
    }

    @Override
    public void loop()
    {

    }

    @Override
    public void stop()
    {

    }

    public void driverOne()
    {
        double leftStick = gamepad1.left_stick_y;
        double rightStick = gamepad1.right_stick_y;
        leftStick = (Math.abs(leftStick) > 0.05 ? leftStick : 0);
        rightStick = (Math.abs(rightStick) > 0.05 ? rightStick : 0);

        robot.driveLeft(leftStick);
        robot.driveRight(rightStick);
    }

    public void driverTwo()
    {
        double power = (gamepad2.dpad_up ? 0.5 : (gamepad2.dpad_down ? -0.5 : 0));
        robot.armL.setPower(power);
        robot.armR.setPower(power);

        if(gamepad2.a) robot.armServo.setPosition(robot.SERVO_CLOSED);
        else if(gamepad2.b) robot.armServo.setPosition(robot.SERVO_OPEN);
    }

    public void telemetry(){

    }


}
