package org.firstinspires.ftc.teamcode.AutonomousEndpoints.SoloGlyph;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.Alliance;
import org.firstinspires.ftc.teamcode.NavigatorAutonomous;
import org.firstinspires.ftc.teamcode.StartingPosition;

import java.util.HashMap;

/**
 * Created by guberti on 12/2/2017.
 */
@Autonomous(name="Solo-glyph FRONT BLUE autonomous", group="B_SingleGlyph")
public class CompBlueFront extends NavigatorAutonomous {

    @Override
    public void runOpMode() {
        robot.color = Alliance.BLUE;
        robot.startingPad = StartingPosition.FRONT;

        COLUMN_DISTANCES = new HashMap<>();
        COLUMN_DISTANCES.put(RelicRecoveryVuMark.LEFT, new Double[] {23.5, 2.0});
        COLUMN_DISTANCES.put(RelicRecoveryVuMark.CENTER, new Double[] {30.6, 8.0});
        COLUMN_DISTANCES.put(RelicRecoveryVuMark.RIGHT, new Double[] {38.8, 14.0});

        super.runOpMode();


    }
}
