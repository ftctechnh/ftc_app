package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * Created by Jeremy on 7/30/2017.
 */
@TeleOp(name = "TankTestTele", group = "Test")
public class TestTeleOp extends OpMode
{
    private TankBase tank;

    public void init()
    {
        tank = new TankBase(hardwareMap);
        gamepad1.setJoystickDeadzone(.1f);
        gamepad2.setJoystickDeadzone(.1f);
    }

    public void start()
    {

    }

    public void loop()
    {
        telemetry.addData("Expected velcoity ", 3 * Math.PI);
        telemetry.addData("Radian Velocity Left", tank.getDriveLeftOne().getVelocity(AngleUnit.RADIANS));
        telemetry.addData("radian v right", tank.getDriveRightOne().getVelocity(AngleUnit.RADIANS));
        telemetry.addData("LJoyStick= ", gamepad1.left_stick_y);
        telemetry.addData("RJoyStick= ", gamepad1.right_stick_y);
        telemetry.addData("REncoders= ", tank.getRightEncoderPos());
        telemetry.addData("LEncoders= ", tank.getLeftEncoderPos());
        tank.driveMotors(gamepad1.left_stick_y, -gamepad1.right_stick_y);
    }

    public void stop()
    {
        tank.stopAllMotors();
    }
}
