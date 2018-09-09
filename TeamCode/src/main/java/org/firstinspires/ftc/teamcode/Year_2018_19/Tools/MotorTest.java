package org.firstinspires.ftc.teamcode.Year_2018_19.Tools;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "MotorTest", group = "TestMode")
//@Disabled

public class MotorTest extends OpMode
{
    private DcMotor motor;

    public void init()
    {
        motor = hardwareMap.get(DcMotor.class, "motor");
        telemetry.addData("Status", "Motor has successfully initialized!");
    }

    public void loop()
    {
        motor.setPower(-gamepad1.left_stick_y);
        telemetry.addData("Motor", motor.getPower());
        telemetry.update();
    }

    public void stop()
    {
        motor.setPower(0);
    }
}