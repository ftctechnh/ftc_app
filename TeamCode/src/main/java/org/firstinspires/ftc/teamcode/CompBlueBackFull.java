package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

import java.util.HashMap;

/**
 * Created by guberti on 12/2/2017.
 */
@Autonomous(name="Ultrasonic BACK BLUE autonomous", group="Autonomous")
public class CompBlueBackFull extends NavigatorAutonomous {

    @Override
    public void runOpMode() {
        robot.color = Alliance.BLUE;
        robot.startingPad = StartingPosition.BACK;

        COLUMN_DISTANCES = new HashMap<>();
        COLUMN_DISTANCES.put(RelicRecoveryVuMark.LEFT, new Double[] {46.9, 27.0});
        COLUMN_DISTANCES.put(RelicRecoveryVuMark.CENTER, new Double[] {54.2, 35.0});
        COLUMN_DISTANCES.put(RelicRecoveryVuMark.RIGHT, new Double[] {62.4, 41.0});

        super.runOpMode();

    }
}