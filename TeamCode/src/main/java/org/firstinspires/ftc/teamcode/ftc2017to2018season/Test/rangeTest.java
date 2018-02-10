package org.firstinspires.ftc.teamcode.ftc2017to2018season.Test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.Autonomous_General_George;

/**
 * Created by Inspiration Team on 1/7/2018.
 */

@Autonomous(name = "rangeTest")
@Disabled
public class rangeTest extends Autonomous_General_George {

    public void runOpMode(){
        initiate(true);

        waitForStart();

        //simpleRangeDistance(50,0.5,4);
    }
}
