package org.firstinspires.ftc.teamcode.Year_2018_19.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name="ModernRoboticsTeleOp", group="TeleOpMode")
//@Disabled

public class ModernRoboticsTeleOp extends OpMode {

    public DcMotor leftDrive;
    public DcMotor rightDrive;

    @Override
    public void init()
    {
        leftDrive = hardwareMap.get(DcMotor.class, "leftDrive");
        rightDrive =  hardwareMap.get(DcMotor.class, "rightDrive");
        telemetry.addData("Status", "Robot has initialized!");
    }

    @Override
    public void start()
    {
        telemetry.addData("Status", "Robot has started running!");
    }

    @Override
    public void loop()
    {
        leftDrive.setPower(-gamepad1.left_stick_y);
        rightDrive.setPower(-gamepad1.right_stick_y);
    }

    @Override
    public void stop() {
        telemetry.addData("Status", "Robot has stopped!");
    }
}
