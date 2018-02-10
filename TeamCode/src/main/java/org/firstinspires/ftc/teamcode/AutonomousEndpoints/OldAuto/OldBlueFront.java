package org.firstinspires.ftc.teamcode.AutonomousEndpoints.OldAuto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Alliance;
import org.firstinspires.ftc.teamcode.CompleteOldAutonomous;
import org.firstinspires.ftc.teamcode.StartingPosition;

/**
 * Created by guberti on 12/2/2017.
 */
@Autonomous(name="Old FRONT BLUE autonomous", group="C_OldAuto")
public class OldBlueFront extends CompleteOldAutonomous {

    @Override
    public void runOpMode() {
        robot.color = Alliance.BLUE;
        robot.startingPad = StartingPosition.FRONT;

        super.runOpMode();
    }
}