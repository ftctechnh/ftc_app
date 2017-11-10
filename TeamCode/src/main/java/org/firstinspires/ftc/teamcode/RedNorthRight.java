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
    }
    //
    private void waitForStartify(){
        waitForStart();
    }
}