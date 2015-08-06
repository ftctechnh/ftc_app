import org.swerverobotics.library.SynchronousOpMode;

/**
 * This is a very simple placeholder to get you going. It doesn't actually do
 * ANYTHING, it merely shows you the basic structure of what a SynchronousOpMode 
 * looks like. More interesting examples can be found in org.swerverobotics.library.examples.
 * 
 * (TODO: mention XML file)
 * (TODO: mention that this doesn't have to be in this source tree)
 */
public class YourTeamsFirstOpMode extends SynchronousOpMode
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
            if (this.newGamePadInputAvailable())
                {
                // Do something with it!
                }
            
            // Emit the latest telemetry and wait, letting other things run
            this.telemetry.update();
            this.idle();
            }
        }
    }
