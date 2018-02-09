package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

import java.util.HashMap;

/**
 * Created by guberti on 12/2/2017.
 */
@Autonomous(name="Ultrasonic FRONT BLUE autonomous", group="Autonomous")
public class CompBlueFrontFull extends NavigatorAutonomous {

    @Override
    public void runOpMode() {
        robot.color = Alliance.BLUE;
        robot.startingPad = StartingPosition.FRONT;

        COLUMN_DISTANCES = new HashMap<>();
        COLUMN_DISTANCES.put(RelicRecoveryVuMark.LEFT, new Double[] {23.3, 2.0});
        COLUMN_DISTANCES.put(RelicRecoveryVuMark.CENTER, new Double[] {30.4, 8.0});
        COLUMN_DISTANCES.put(RelicRecoveryVuMark.RIGHT, new Double[] {38.1, 14.0});

        super.runOpMode();


    }
}
