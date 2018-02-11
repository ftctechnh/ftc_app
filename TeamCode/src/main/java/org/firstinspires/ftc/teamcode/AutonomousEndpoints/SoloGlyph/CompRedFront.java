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
@Autonomous(name="Solo-glyph FRONT RED autonomous", group="B_SingleGlyph")
public class CompRedFront extends NavigatorAutonomous {

    @Override
    public void runOpMode() {
        robot.color = Alliance.RED;
        robot.startingPad = StartingPosition.FRONT;

        COLUMN_DISTANCES = new HashMap<>();
        COLUMN_DISTANCES.put(RelicRecoveryVuMark.LEFT, new Double[] {22.6, 2.0});
        COLUMN_DISTANCES.put(RelicRecoveryVuMark.CENTER, new Double[] {30.4, 8.0});
        COLUMN_DISTANCES.put(RelicRecoveryVuMark.RIGHT, new Double[] {37.65, 14.0});

        super.runOpMode();

    }
}
