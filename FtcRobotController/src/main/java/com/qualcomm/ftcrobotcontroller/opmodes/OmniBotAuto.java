package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by akhil on 8/29/2015.
 */
public class OmniBotAuto extends OpMode {

    DcMotor motor1 ;
    DcMotor motor2 ;
    DcMotor motor3 ;
    DcMotor motor4 ;

    int scaleDegree = 1 ;

    public OmniBotAuto () {

    }

    @Override
    public void start () {

        motor1 = hardwareMap.dcMotor.get("motor_1") ;
        motor2 = hardwareMap.dcMotor.get("motor_2") ;
        motor3 = hardwareMap.dcMotor.get("motor_4") ;
        motor4 = hardwareMap.dcMotor.get("motor_3") ;

        motor1.setDirection(DcMotor.Direction.REVERSE) ;
        motor4.setDirection(DcMotor.Direction.REVERSE) ;


    }
    @Override
    public void loop() {

        motor1.setPower(0.15);
        motor2.setPower(0.15);

    telemetry.addData("Text", "*** Robot Data***");
    telemetry.addData("time", "elapsed time: " + Double.toString(this.time));
    telemetry.addData("left tgt pwr",  "left  pwr: " + Double.toString(motor1.getPower()));
    telemetry.addData("right tgt pwr", "right pwr: " + Double.toString(motor2.getPower()));


    }
    @Override
    public void stop() {

    }
}
