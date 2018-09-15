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
    private float rValue, lValue;
    private boolean toggleSpeedMode = false;
    private boolean hasSpeedModeBeenActivated = false;


    public void init()
    {
        tank = new ParadeBot(hardwareMap);
        gamepad1.setJoystickDeadzone(.4f);
        gamepad2.setJoystickDeadzone(.4f);
    }

    public void start()
    {

    }

    public void loop()
    {

        if (toggleSpeedMode)
        {
            telemetry.addData("Speed mode = ON", null);
            rValue = gamepad1.right_stick_y;
            lValue = gamepad1.left_stick_y;
        }
        else
        {
            telemetry.addData("speed mode = OFF", null);
            rValue = gamepad1.right_stick_y * .5f;
            lValue = gamepad1.left_stick_y * .5f;
        }

        telemetry.addData("Radian Velocity Left", tank.getDriveLeftOne().getVelocity(AngleUnit.RADIANS));
        telemetry.addData("radian v right", tank.getDriveRightOne().getVelocity(AngleUnit.RADIANS));
        telemetry.addData("LJoyStick= ", gamepad1.left_stick_y);
        telemetry.addData("RJoyStick= ", gamepad1.right_stick_y);
        telemetry.addData("REncoders= ", tank.getRightEncoderPos());
        telemetry.addData("LEncoders= ", tank.getLeftEncoderPos());
        telemetry.addData("Distance from Front Left (in)= ", tank.getDistFromFrontLeft_In());
        telemetry.addData("Distance from Front Right (in)= ", tank.getDistFromFrontRight_In());
        telemetry.update();
        tank.driveMotors(lValue, rValue);

        if (gamepad1.a)
        {
            if (!hasSpeedModeBeenActivated)
            {
                toggleSpeedMode = !toggleSpeedMode;
                hasSpeedModeBeenActivated = true;
            }
        }
        else
        {
            hasSpeedModeBeenActivated = false;
        }
    }

    public void stop()
    {
        tank.stopAllMotors();
    }
}
