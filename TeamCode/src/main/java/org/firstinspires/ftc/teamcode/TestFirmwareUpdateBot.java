package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Peter on 9/15/2016.
 */
public class TestFirmwareUpdateBot
{
    public HardwareMap hardwareMap = null;
    public DcMotor  motorOne = null;
    public DcMotor motorTwo = null;

    public TestFirmwareUpdateBot()
    {

    }

    public void init(HardwareMap hwMap)
    {
        hardwareMap = hwMap;

        motorOne = hardwareMap.dcMotor.get("motorOne");
        motorTwo = hardwareMap.dcMotor.get("motorTwo");

        motorOne.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorTwo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        motorOne.setPower(0);
        motorTwo.setPower(0);

        motorOne.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorTwo.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
}
