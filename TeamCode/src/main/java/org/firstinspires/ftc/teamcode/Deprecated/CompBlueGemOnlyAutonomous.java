package org.firstinspires.ftc.teamcode.Deprecated;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.Alliance;
import org.firstinspires.ftc.teamcode.NullbotGemOnlyAutonomous;

/**
 * Created by guberti on 11/4/2017.
 */
@Disabled
@Autonomous(name="Knock off gem (FOR BLUE TEAM)", group="Autonomous")
public class CompBlueGemOnlyAutonomous extends NullbotGemOnlyAutonomous {

    @Override
    public void runOpMode() {
        robot.color = Alliance.BLUE;
        directionMultiplier = 1;

        super.runOpMode();

    }
}
