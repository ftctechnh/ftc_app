package org.overlake.ftc.team_7330.Autonomous;

import org.swerverobotics.library.interfaces.Autonomous;
import org.overlake.ftc.team_7330.Autonomous.AutonomousOpMode;
/**
 * Created by Nikhil on 12/13/2015.
 */

@Autonomous
public class RampLeft extends AutonomousOpMode {
        @Override protected void main() throws InterruptedException {
            waitForStart();
            initializeAllDevices();
            driveWithEncoders(1.75, .5);
            turn(-91, .8);
            driveWithEncoders(3, 1.0);
        }

}