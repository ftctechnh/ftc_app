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
        driveWithEncoders(2.8,1.0);
        turn(45,.9);
        driveWithEncoders(3.2,.4);
    }



}
