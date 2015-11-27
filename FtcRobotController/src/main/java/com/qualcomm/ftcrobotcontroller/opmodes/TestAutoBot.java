package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
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
                leftMotor.setMode(RunMode.RUN_USING_ENCODERS);
                rightMotor.setMode(RunMode.RUN_USING_ENCODERS);
            }
            public void loop()
            {
                telemetry.addData("leftMotor", leftMotor.getCurrentPosition());
                int encoderTarget = ((int)(111.1111111*12+leftMotor.getCurrentPosition()));
                leftMotor.setPower(1.0);
                rightMotor.setPower(1.0);
                while(leftMotor.getCurrentPosition() < encoderTarget) {}
                leftMotor.setPower(0);
                rightMotor.setPower(0);
                try
                {
                    wait(10000);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            public void stop()
            {
                leftMotor.setPower(0.0f);
                rightMotor.setPower(0.0f);
            }

}
