package org.overlake.ftc.team_7330.Autonomous;

import org.swerverobotics.library.interfaces.Autonomous;
import org.overlake.ftc.team_7330.Autonomous.AutonomousOpMode;
/**
 * Created by Nikhil on 12/10/2015.
 */

@Autonomous
public class RampBlue extends AutonomousOpMode
{
    @Override protected void main() throws InterruptedException
    {
        waitForStart();
        initializeAllDevices();
        driveWithEncoders(1.0, .7);
        turn(45, .7);
        driveWithEncoders(3.0,.7);
        driveWithEncoders(-1.0,-.7);
        turn(55,.7);
        driveWithEncoders(3,1.0);
    }



}
