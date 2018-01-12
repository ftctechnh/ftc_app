package org.firstinspires.ftc.teamcode.ftc2017to2018season.Test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.Autonomous_General;

/**
 * Created by Inspiration Team on 1/7/2018.
 */

@Autonomous(name = "rangeTest")
@Disabled
public class rangeTest extends Autonomous_General {

    public void runOpMode(){
        initiate();

        waitForStart();

        simpleRangeDistance(50,0.5,4);
    }
}
