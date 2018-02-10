package org.firstinspires.ftc.teamcode.AutonomousEndpoints.OldAuto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Alliance;
import org.firstinspires.ftc.teamcode.CompleteOldAutonomous;
import org.firstinspires.ftc.teamcode.StartingPosition;

/**
 * Created by guberti on 12/2/2017.
 */
@Autonomous(name="Old BACK RED autonomous", group="C_OldAuto")
public class OldRedBack extends CompleteOldAutonomous {

    @Override
    public void runOpMode() {
        robot.color = Alliance.RED;
        robot.startingPad = StartingPosition.BACK;

        super.runOpMode();
    }
}