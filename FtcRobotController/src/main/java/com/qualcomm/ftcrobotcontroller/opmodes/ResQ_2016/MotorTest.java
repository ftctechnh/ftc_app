package com.qualcomm.ftcrobotcontroller.opmodes.ResQ_2016;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Peter on 11/1/2015.
 */
public class MotorTest extends OpMode
{
    DcMotor rightMotor;
    DcMotor leftMotor;

        public void init()
        {
            rightMotor = hardwareMap.dcMotor.get("rightMotor");
            leftMotor = hardwareMap.dcMotor.get("leftMotor");
            leftMotor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
            rightMotor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        }
        public void loop()
        {
            telemetry.addData("leftMotor", leftMotor.getCurrentPosition());
            telemetry.addData("rightMotor", rightMotor.getCurrentPosition());
        }


}
