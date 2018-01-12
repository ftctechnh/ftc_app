package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by guberti on 11/4/2017.
 */

@Autonomous(name="Knock off gem (FOR RED TEAM)", group="Autonomous")
public class CompRedGemOnlyAutonomous extends NullbotGemOnlyAutonomous {

    @Override
    public void runOpMode() {
        robot.color = Alliance.RED;
        directionMultiplier = 1;

        super.runOpMode();

    }
}
