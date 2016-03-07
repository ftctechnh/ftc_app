package org.swerverobotics.library.examples;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.*;

import org.swerverobotics.library.ClassFactory;
import org.swerverobotics.library.interfaces.*;

/**
 * This LinearOpMode illustrates a very simple use of the Swerve color sensor implementation.
 * The opmode expects either a HiTechnic or a Modern Robotics color sensor named "colorSensor".
 */
@TeleOp(name="Color Demo (linear)", group="Swerve Examples")
@Disabled
public class LinearColorDemo extends LinearOpMode
    {
    ColorSensor color;
    boolean     ledIsOn;

    @Override
    public void runOpMode() throws InterruptedException
        {
        this.color   = ClassFactory.createSwerveColorSensor(this, this.hardwareMap.colorSensor.get("colorSensor"));
        this.ledIsOn = true;
        this.color.enableLed(ledIsOn);

        waitForStart();

        while (opModeIsActive())
            {
            // Press 'x' to toggle the LED
            if (gamepad1.x)
                {
                this.ledIsOn = !this.ledIsOn;
                this.color.enableLed(this.ledIsOn);
                while (gamepad1.x)
                    {
                    // wait for button release
                    }
                }

            telemetry.addData("red",   this.color.red());
            telemetry.addData("green", this.color.green());
            telemetry.addData("blue",  this.color.blue());
            telemetry.addData("alpha", this.color.alpha());
            }

        }
    }
