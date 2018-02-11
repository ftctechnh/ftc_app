package org.firstinspires.ftc.teamcode.Deprecated;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.Alliance;
import org.firstinspires.ftc.teamcode.NullbotGemOnlyAutonomous;

/**
 * Created by guberti on 11/4/2017.
 */
@Disabled
@Autonomous(name="Knock off gem (FOR RED TEAM)", group="Autonomous")
public class CompRedGemOnlyAutonomous extends NullbotGemOnlyAutonomous {

    @Override
    public void runOpMode() {
        robot.color = Alliance.RED;
        directionMultiplier = 1;

        super.runOpMode();

    }
}
