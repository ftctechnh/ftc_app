package org.firstinspires.ftc.teamcode.ftc2017to2018season.Test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.Autonomous_General;

/**
 * Created by Inspiration Team on 1/7/2018.
 */
@Autonomous(name = "revGyro")
public class revGyro extends Autonomous_General {


    public void runOpMode(){
        initiate(false);

        composeTelemetry();

        waitForStart();


        while(opModeIsActive()){
            telemetry.addData("current header", angles.firstAngle);
            telemetry.update();
        }
    }
}
