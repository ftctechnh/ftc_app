package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorController.RunMode;

/**
 * Created by Peter on 10/25/2015.
 */
public class TestAutoBot extends OpMode
{
    DcMotor leftMotor;
    DcMotor rightMotor;


            public void init()
            {
                leftMotor = hardwareMap.dcMotor.get("leftMotor");
                rightMotor = hardwareMap.dcMotor.get("rightMotor");
                rightMotor.setDirection(DcMotor.Direction.REVERSE);
                rightMotor.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
                leftMotor.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);

            }

            public void start()
            {

                leftMotor.setTargetPosition(leftMotor.getCurrentPosition() + 1000);
                rightMotor.setTargetPosition(rightMotor.getCurrentPosition() + 1000);
                rightMotor.setPower(1.0f);
                leftMotor.setPower(1.0f);
                telemetry.addData("leftMotor", leftMotor.getCurrentPosition());
                telemetry.addData("rightMotor", rightMotor.getCurrentPosition());
                telemetry.addData("leftMotor", leftMotor.getChannelMode());
                telemetry.addData("rightMotor", rightMotor.getChannelMode());


            }
            public void loop() {}

            public void stop()
            {
                leftMotor.setPower(0.0);
                rightMotor.setPower(0.0);
            }
}
