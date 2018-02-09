package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

import java.util.HashMap;

/**
 * Created by guberti on 12/2/2017.
 */
@Autonomous(name="Ultrasonic BACK RED autonomous", group="Autonomous")
public class CompRedBackFull extends NavigatorAutonomous {

    @Override
    public void runOpMode() {
        robot.color = Alliance.RED;
        robot.startingPad = StartingPosition.BACK;

        COLUMN_DISTANCES = new HashMap<>();
        COLUMN_DISTANCES.put(RelicRecoveryVuMark.LEFT, new Double[] {46.9, 25.0});
        COLUMN_DISTANCES.put(RelicRecoveryVuMark.CENTER, new Double[] {54.7, 33.0});
        COLUMN_DISTANCES.put(RelicRecoveryVuMark.RIGHT, new Double[] {62.2, 39.0});

        super.runOpMode();

    }
}