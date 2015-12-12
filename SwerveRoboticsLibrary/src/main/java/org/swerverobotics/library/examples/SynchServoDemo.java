package org.swerverobotics.library.examples;

import com.qualcomm.robotcore.hardware.Servo;

import org.swerverobotics.library.SynchronousOpMode;
import org.swerverobotics.library.interfaces.Disabled;
import org.swerverobotics.library.interfaces.TeleOp;

/**
 * This is an example showing the use of servos.
 * It assumes that you have 3 servos connected to a Core Servo Controller
 * and that the servos are named "servo1", "servo2", and "servo3" in your robot configuration file.
 */
@TeleOp(name="Servo demo", group="Swerve Examples")
@Disabled
public class SynchServoDemo extends SynchronousOpMode
{
    Servo s1 = null;
    Servo s2 = null;
    Servo s3 = null;

    @Override protected void main() throws InterruptedException
    {
        // Initialize your program state, your robot, and the telemetry dashboard (if you want it).
        s1 = this.hardwareMap.servo.get("servo1");
        s2 = this.hardwareMap.servo.get("servo2");
        s3 = this.hardwareMap.servo.get("servo3");

        double position = 0;
        
        // Wait for the game to begin
        this.waitForStart();
        
        // Loop until the game is finished
        while (this.opModeIsActive())
        {
            if (this.updateGamepads())
            {
                // Do something with it!
                if (this.gamepad1.a)
                {
                    position = position + 0.1;
                    if (position > 1) position = 1;
                }
                if (this.gamepad1.b)
                {
                    position = position - 0.1;
                    if (position < 0) position = 0;
                }
                if (this.gamepad1.x)
                {
                    position = 0;
                }
                if (this.gamepad1.y)
                {
                    position = 1;
                }
            }

            s1.setPosition(position);
            s2.setPosition(position);
            s3.setPosition(position);

            // Emit the latest telemetry and wait, letting other things run
            this.telemetry.addData("controls", "a:+  b:-  x:0  y:1");
            this.telemetry.addData("position",  position);
            this.telemetry.update();
            this.idle();
        }
    }
}
