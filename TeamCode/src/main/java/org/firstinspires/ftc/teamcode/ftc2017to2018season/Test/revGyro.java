package org.firstinspires.ftc.teamcode.ftc2017to2018season.Test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.Autonomous_General_George;

/**
 * Created by Inspiration Team on 1/7/2018.
 */
@Autonomous(name = "revGyro")
@Disabled
public class revGyro extends Autonomous_General_George {


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
