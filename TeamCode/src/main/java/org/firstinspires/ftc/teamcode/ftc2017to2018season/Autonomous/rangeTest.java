package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Inspiration Team on 1/7/2018.
 */

@Autonomous(name = "rangeTest")
public class rangeTest extends Autonomous_General{

    public void runOpMode(){
        initiate();

        waitForStart();

        simpleRangeDistance(50,0.5,4);
    }
}
