package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;

/**
 * Created by Eric on 11/9/2017.
 */

@Autonomous

public class RedNorthRight extends LinearOpMode {
    PengwinArm pengwinArm;
    JeffThePengwin jeffThePengwin;
    //
    static final double Pi = 3.141592653589793238462643383279502;
    //
    @Override
    public void runOpMode() throws InterruptedException {
        //
        startify();
        //
        waitForStartify();
        //
        //woo hoo
        //
        //Insert Code Here
        telemetry.addData("Pi", Pi);
        telemetry.update();
    }
    //
    //
    //
    private void forward(double number){
        //Insert math Here
        //Radius is 2"
    }
    //
    private void waitForStartify(){
        waitForStart();
    }
    //
    private void startify(){
        jeffThePengwin.leftBackMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        jeffThePengwin.leftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        jeffThePengwin.rightBackMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        jeffThePengwin.rightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //
        jeffThePengwin.leftBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        jeffThePengwin.leftFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        jeffThePengwin.rightBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        jeffThePengwin.rightFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}