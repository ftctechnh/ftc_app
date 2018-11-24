package org.firstinspires.ftc.teamcode.TestPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "DriveTest")
@Disabled
public class DriveTest extends OpMode
{
    DcMotorImplEx rightMotor, leftMotor;


    @Override
    public void init()
    {
        rightMotor = hardwareMap.get(DcMotorImplEx.class, "rightDriveOne");
        leftMotor = hardwareMap.get(DcMotorImplEx.class,"leftDriveOne");
        rightMotor.setDirection(DcMotorImplEx.Direction.REVERSE);

        gamepad1.setJoystickDeadzone(.2f);
    }

    @Override
    public void loop()
    {
        rightMotor.setPower(gamepad1.right_stick_y);
        leftMotor.setPower(gamepad1.left_stick_y);
    }

    public void stop()
    {
        rightMotor.setPower(0);
        leftMotor.setPower(0);
        super.stop();
    }
}
