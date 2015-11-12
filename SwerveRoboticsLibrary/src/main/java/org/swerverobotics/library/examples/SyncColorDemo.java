package org.swerverobotics.library.examples;

import com.qualcomm.robotcore.hardware.*;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;

/**
 * This SynchronousOpMode illustrates a very simple use of the Swerve color
 * sensor implementation, which is used automatically. The opmode expects either a HiTechnic
 * or a Modern Robotics color sensor named "colorSensor".
 */
@TeleOp(name="Color Demo (sync)", group="Swerve Examples")
@Disabled
public class SyncColorDemo extends SynchronousOpMode
    {
    ColorSensor color;
    boolean     ledIsOn;

    @Override
    protected void main() throws InterruptedException
        {
        this.color = this.hardwareMap.colorSensor.get("colorSensor");
        this.ledIsOn = true;
        this.color.enableLed(ledIsOn);

        waitForStart();

        while (opModeIsActive())
            {
            if (updateGamepads())
                {
                // Press 'x' to toggle the LED
                if (gamepad1.x)
                    {
                    this.ledIsOn = !this.ledIsOn;
                    this.color.enableLed(this.ledIsOn);
                    }
                }

            telemetry.addData("red",   this.color.red());
            telemetry.addData("green", this.color.green());
            telemetry.addData("blue",  this.color.blue());
            telemetry.addData("alpha", this.color.alpha());
            telemetry.update();
            this.idle();
            }
        }
    }
