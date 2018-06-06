package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * Created by Jeremy on 7/30/2017.
 */
@TeleOp(name = "ParadeBot", group = "Test")
public class ParadeBotTele extends OpMode
{
    private ParadeBot tank;

    public void init()
    {
        tank = new ParadeBot(hardwareMap);
        gamepad1.setJoystickDeadzone(.5f);
        gamepad2.setJoystickDeadzone(.5f);
    }

    public void start()
    {

    }

    public void loop()
    {
        telemetry.addData("Radian Velocity Left", tank.getDriveLeftOne().getVelocity(AngleUnit.RADIANS));
        telemetry.addData("radian v right", tank.getDriveRightOne().getVelocity(AngleUnit.RADIANS));
        telemetry.addData("LJoyStick= ", gamepad1.left_stick_y);
        telemetry.addData("RJoyStick= ", gamepad1.right_stick_y);
        telemetry.addData("REncoders= ", tank.getRightEncoderPos());
        telemetry.addData("LEncoders= ", tank.getLeftEncoderPos());
        if(gamepad1.y)
        {
            tank.driveMotors(1, 1);
        }
        else if(gamepad1.x)
        {
            tank.driveMotors(-1,-1);
        }
        else
        tank.driveMotors(-gamepad1.right_stick_y, -gamepad1.left_stick_y);
    }

    public void stop()
    {
        tank.stopAllMotors();
    }
}
