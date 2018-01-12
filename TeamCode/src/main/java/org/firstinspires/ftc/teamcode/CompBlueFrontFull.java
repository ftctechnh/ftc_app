package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by guberti on 12/2/2017.
 */
@Autonomous(name="Complete FRONT BLUE autonomous", group="Autonomous")
public class CompBlueFrontFull extends CompleteAutonomous {

    @Override
    public void runOpMode() {
        robot.color = Alliance.BLUE;
        robot.startingPad = StartingPosition.FRONT;

        super.runOpMode();

    }
}
