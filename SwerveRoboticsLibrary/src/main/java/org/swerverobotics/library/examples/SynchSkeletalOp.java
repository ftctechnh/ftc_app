package org.swerverobotics.library.examples;

import org.swerverobotics.library.SynchronousOpMode;
import org.swerverobotics.library.interfaces.*;

/**
 * This is a very simple placeholder. It doesn't actually *do* anything. Rather, it merely 
 * shows you the basic skeletal structure of what a SynchronousOpMode should look like. 
 */
@TeleOp(name="Skeletal", group="Swerve Examples")
@Disabled
public class SynchSkeletalOp extends SynchronousOpMode
    {
    @Override protected void main() throws InterruptedException
        {
        // Initialize your program state, your robot, and the telemetry dashboard (if you want it).
        //    (not shown)
        
        // Wait for the game to begin
        this.waitForStart();
        
        // Loop until the game is finished
        while (this.opModeIsActive())
            {
            if (this.updateGamepads())
                {
                // Do something with it!
                }
            
            // Emit the latest telemetry and wait, letting other things run
            this.telemetry.update();
            this.idle();
            }
        }
    }
