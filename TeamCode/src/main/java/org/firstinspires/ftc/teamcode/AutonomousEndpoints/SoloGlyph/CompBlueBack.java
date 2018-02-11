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
@Autonomous(name="Solo-glyph BACK BLUE autonomous", group="B_SingleGlyph")
public class CompBlueBack extends NavigatorAutonomous {

    @Override
    public void runOpMode() {
        robot.color = Alliance.BLUE;
        robot.startingPad = StartingPosition.BACK;

        COLUMN_DISTANCES = new HashMap<>();
        COLUMN_DISTANCES.put(RelicRecoveryVuMark.LEFT, new Double[] {46.9, 27.0});
        COLUMN_DISTANCES.put(RelicRecoveryVuMark.CENTER, new Double[] {55.1, 34.0});
        COLUMN_DISTANCES.put(RelicRecoveryVuMark.RIGHT, new Double[] {62.4, 41.0});

        super.runOpMode();

    }
}