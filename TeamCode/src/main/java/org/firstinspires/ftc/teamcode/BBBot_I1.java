package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.GyroSensor;

//Baby Balancing Robot Iteration 1(BBBot I1) created by Eric on 8/29/2017.

@TeleOp (name="BBBot_I1",group="BBBot" )
@Disabled
public class BBBot_I1 extends LinearOpMode{

    GyroSensor g;

    int c;

    @Override
    public void runOpMode() throws InterruptedException {

        g = hardwareMap.gyroSensor.get("sensor_gyro");

        waitForStart();

        while (opModeIsActive()){
            //
            if (g.getRotationFraction() <= 3){
                //
                //
            }else{
                //
                //
            }

            //
            //
        }



    }
}
