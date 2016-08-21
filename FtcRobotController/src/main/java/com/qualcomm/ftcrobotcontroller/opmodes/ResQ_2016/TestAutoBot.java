package com.qualcomm.ftcrobotcontroller.opmodes.ResQ_2016;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;



/**
 * Created by Peter on 10/25/2015.
 */
public class TestAutoBot extends OpMode
{

        DcMotorController dcMotorController;
        DcMotor leftMotor;
        DcMotor rightMotor;
        static int encoderTarget;
                public void init()
                {
                    dcMotorController = hardwareMap.dcMotorController.get("Motor Controller 1");
                    telemetry.addData("start init", 0);
                    leftMotor = hardwareMap.dcMotor.get("leftMotor");
                    rightMotor = hardwareMap.dcMotor.get("rightMotor");
                    telemetry.addData("added motors", 0);
                    leftMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
                    rightMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
                    telemetry.addData("set modes", 0);
                    rightMotor.setDirection(DcMotor.Direction.REVERSE);
                    telemetry.addData("finished!", 0);
                }


                public void loop()
                {
                    //telemetry.addData("startMotor", 0);
                    encoderTarget = (int)(111.11111111*12+leftMotor.getCurrentPosition());
                    //leftMotor.setPower(1.0f);
                    //rightMotor.setPower(1.0f);
                    //while(leftMotor.getCurrentPosition()<encoderTarget)
                    //{
                        telemetry.addData("leftMotor", leftMotor.getCurrentPosition());
                        telemetry.addData("target", encoderTarget);
                    //}
                    //leftMotor.setPower(0);
                    //rightMotor.setPower(0);*/
                    //telemetry.addData("stopped", 0);
                    leftMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
                    rightMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
                }
                public void stop(){}


}
