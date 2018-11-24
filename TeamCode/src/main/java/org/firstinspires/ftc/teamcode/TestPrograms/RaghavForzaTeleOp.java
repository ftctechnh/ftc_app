package org.firstinspires.ftc.teamcode.TestPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "RaghavForzaTeleOp")
@Disabled
public class RaghavForzaTeleOp extends OpMode
{
    DcMotorImplEx rightMotor, leftMotor;


    @Override
    public void init()
    {
        rightMotor = hardwareMap.get(DcMotorImplEx.class, "rightDriveOne");
        leftMotor = hardwareMap.get(DcMotorImplEx.class,"leftDriveOne");
        rightMotor.setDirection(DcMotorImplEx.Direction.REVERSE);
    }





    @Override
    public void loop()
    {
        if(gamepad1.right_trigger < .3f)
        {
            rightMotor.setPower(0);
            leftMotor.setPower(0);
        }
        if (gamepad1.right_trigger > .3f)
        {
            rightMotor.setPower(gamepad1.right_trigger);
            leftMotor.setPower(gamepad1.left_trigger);
        }
        if (gamepad1.left_trigger < .3f)
        {
            rightMotor.setPower(0);
            leftMotor.setPower(0);
        }
        if (gamepad1.left_trigger > .3f)
        {
            rightMotor.setPower(-gamepad1.right_trigger);
            leftMotor.setPower(-gamepad1.left_trigger);
        }

    }


    public void stop()
    {
        rightMotor.setPower(0);
        leftMotor.setPower(0);
        super.stop();
    }

}
