package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by student on 11/22/17.
 */

@Autonomous(name="Blue Jewel Test ", group ="Jewel")
public class AutoTestRed {

    LinearOpMode om;
    AutoRobot ar = new AutoRobot();

    public void runOpMode() {
        ar.init(om);
        ar.jewel(false);
    }
}
