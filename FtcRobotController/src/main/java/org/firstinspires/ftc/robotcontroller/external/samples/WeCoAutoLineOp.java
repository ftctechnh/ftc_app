package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Connor on 3/15/2016.
 */
public class WeCoAutoLineOp extends OpMode {

    DcMotor motorRight;
    DcMotor motorLeft;

    static final double reset = 0;
    static final double normalSpeed = 0.5;
    static final double wheelDiam = 3.96;
    static final double distanceFinal = 120;
    static final double distanceperRev = Math.PI * wheelDiam;
    static final double ticksperRev = 1440;


    @Override
    public void init(){
        motorRight.setPower(reset);
        motorLeft.setPower(reset);
    }


    @Override
    public void start(){
        motorRight = hardwareMap.dcMotor.get("motorRight") ;
        motorLeft = hardwareMap.dcMotor.get("motorLeft") ;

        motorRight.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop(){
        double currentEncoderPos = motorLeft.getCurrentPosition();
        if (currentEncoderPos <= distanceFinal / distanceperRev * ticksperRev){
            DbgLog.msg("====Moving===");
            motorRight.setPower(normalSpeed);
            motorLeft.setPower(normalSpeed);

        }
        else {
            motorRight.setPower(reset);
            motorLeft.setPower(reset);
            DbgLog.msg("====Stopped===");
        }

        telemetry.addData ("0 current position", "Current Position is" + String.format("%.2f", currentEncoderPos));
        telemetry.addData ("1 Motor 1", "Motor Right power is " + String.format("%.2f", normalSpeed));
        telemetry.addData ("1 Motor 1", "Motor Left power is " + String.format("%.2f", normalSpeed));
    }






}
