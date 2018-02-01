package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by guberti on 12/2/2017.
 */
@Autonomous(name="Ultrasonic BACK BLUE autonomous", group="Autonomous")
public class CompBlueBackFull extends NavigatorAutonomous {

    @Override
    public void runOpMode() {
        robot.color = Alliance.BLUE;
        robot.startingPad = StartingPosition.BACK;

        super.runOpMode();

    }
}