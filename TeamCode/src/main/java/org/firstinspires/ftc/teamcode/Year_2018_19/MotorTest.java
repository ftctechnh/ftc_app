package org.firstinspires.ftc.teamcode.Year_2018_19;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "DartTeleOp", group = "TeleOpMode")
//@Disabled

public class MotorTest extends OpMode
{
    private DcMotor leftDrive;

    @Override
    public void start() {
        telemetry.addData("Status", "Robot has started running!");
    }
    @Override
    public void init()
    {
        leftDrive = hardwareMap.get(DcMotor.class, "left_drive");
        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        telemetry.addData("Status", "Robot has successfully initialized!");
    }
    @Override
    public void loop()
    {
        float value_l = -gamepad1.left_stick_y;
        float value_r = -gamepad1.right_stick_y;

        leftDrive.setPower(value_l/3);
    }
}