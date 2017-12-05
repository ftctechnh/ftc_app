package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by guberti on 12/2/2017.
 */
@Autonomous(name="Complete FRONT RED autonomous", group="Autonomous")
public class CompRedFrontFull extends CompleteAutonomous {

    @Override
    public void runOpMode() {
        robot.color = Alliance.RED;

        super.runOpMode();

    }
}
