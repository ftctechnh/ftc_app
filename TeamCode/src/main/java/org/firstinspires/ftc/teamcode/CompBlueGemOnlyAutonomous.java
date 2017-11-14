package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by guberti on 11/4/2017.
 */

@Autonomous(name="Knock off gem (FOR BLUE TEAM)", group="Autonomous")
public class CompBlueGemOnlyAutonomous extends NullbotGemOnlyAutonomous {

    @Override
    public void runOpMode() {
        robot.color = Alliance.BLUE;
        directionMultiplier = 1;

        super.runOpMode();

    }
}
