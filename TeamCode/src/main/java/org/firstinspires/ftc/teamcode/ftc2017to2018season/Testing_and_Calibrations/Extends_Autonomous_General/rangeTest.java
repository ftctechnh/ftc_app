package org.firstinspires.ftc.teamcode.ftc2017to2018season.Testing_and_Calibrations.Extends_Autonomous_General;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.Autonomous_General_George_;

/**
 * Created by Inspiration Team on 1/7/2018.
 */

@Autonomous(name = "rangeTest")
@Disabled
public class rangeTest extends Autonomous_General_George_ {

    public void runOpMode(){
        initiate(false);

        waitForStart();

        while(opModeIsActive()) {
            telemetry.addData("", wallAlignBack.getDistance(DistanceUnit.CM));
            telemetry.update();
        }

        //simpleRangeDistance(50,0.5,4);
    }
}
