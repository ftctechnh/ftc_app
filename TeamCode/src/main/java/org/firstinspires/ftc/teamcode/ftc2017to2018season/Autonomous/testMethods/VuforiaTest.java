package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.testMethods;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.Autonomous_General;

/**
 * Created by Inspiration Team on 12/29/2017.
 */
@Autonomous(name = "VuforiaTest")
public class VuforiaTest extends Autonomous_General {

    public void runOpMode(){
        vuforiaInit(true, true);


        waitForStart();
        toggleLight(false);
        startTracking();
        while(!vuMarkFound() && opModeIsActive()){

        }
        stopTracking();
        telemetry.addData("Vumark" , vuMark);
        telemetry.update();
    }
}
