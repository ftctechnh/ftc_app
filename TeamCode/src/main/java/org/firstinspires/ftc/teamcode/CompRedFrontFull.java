package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

import java.util.HashMap;

/**
 * Created by guberti on 12/2/2017.
 */
@Autonomous(name="Ultrasonic FRONT RED autonomous", group="Autonomous")
public class CompRedFrontFull extends NavigatorAutonomous {

    @Override
    public void runOpMode() {
        robot.color = Alliance.RED;
        robot.startingPad = StartingPosition.FRONT;

        COLUMN_DISTANCES = new HashMap<>();
        COLUMN_DISTANCES.put(RelicRecoveryVuMark.LEFT, new Double[] {22.6, 2.0});
        COLUMN_DISTANCES.put(RelicRecoveryVuMark.CENTER, new Double[] {30.6, 8.0});
        COLUMN_DISTANCES.put(RelicRecoveryVuMark.RIGHT, new Double[] {37.65, 14.0});

        super.runOpMode();

    }
}
