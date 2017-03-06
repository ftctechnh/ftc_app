package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by ftc6347 on 2/26/17.
 */
@Autonomous(name = "Stop Test", group = "tests")
public class ProportionalStopTest extends LinearOpModeBase {
    @Override
    public void runOpMode() throws InterruptedException {
        initializeHardware();

        waitForStart();

        stopOnLine(0.05, false);
    }
}
