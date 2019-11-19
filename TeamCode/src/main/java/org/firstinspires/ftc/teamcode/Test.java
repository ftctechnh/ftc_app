package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

//This is a test autonomous to see if I can extend AutonomousFirst to avoid rewriting code.
@Autonomous(name = "Test", group = "Autonomous")
public class Test extends AutonomousFirst {
    @Override
    public void runOpMode() throws InterruptedException {
        runSetup();
        turnDegrees(1, 360);
        driveEncoder(1, 10, 10);

    }
}
