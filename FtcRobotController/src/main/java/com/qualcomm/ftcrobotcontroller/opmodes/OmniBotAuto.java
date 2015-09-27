package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by akhil on 8/29/2015.
 */
public class OmniBotAuto extends OpMode {


    @Override
    public void init() {

    }
    DcMotor motor1 ;
    DcMotor motor2 ;
    DcMotor motor3 ;
    DcMotor motor4 ;


    public OmniBotAuto () {

    }
    ElapsedTime elapsedTime = new ElapsedTime();
    @Override
    public void start () {

        if (gamepad1.left_bumper) {
            motor1 = hardwareMap.dcMotor.get("motor_1");
            motor2 = hardwareMap.dcMotor.get("motor_2");
            motor3 = hardwareMap.dcMotor.get("motor_3");
            motor4 = hardwareMap.dcMotor.get("motor_4");

            motor1.setDirection(DcMotor.Direction.REVERSE);
            motor4.setDirection(DcMotor.Direction.REVERSE);

            elapsedTime.reset();
            elapsedTime.startTime();
        }
    }
    @Override
    public void loop() {
        double etime;
        etime = elapsedTime.time();
        if (etime < 5) {
            motor1.setPower(0.15);
            motor2.setPower(0.15);
            motor3.setPower(0.15);
            motor4.setPower(0.15);
        }
        else  {
            motor1.setPower(0);
            motor2.setPower(0);
            motor3.setPower(0);
            motor4.setPower(0);


        }
        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("time", "elapsed time: " + Double.toString(this.time));
        telemetry.addData("left tgt pwr", "left  pwr: " + Double.toString(motor1.getPower()));
        telemetry.addData("right tgt pwr", "right pwr: " + Double.toString(motor2.getPower()));



    }
    @Override
    public void stop() {

    }
}
