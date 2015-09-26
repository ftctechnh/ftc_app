package org.swerverobotics.library.examples;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;

/**
 * An example of a synchronous opmode that implements a simple drive-a-bot. 
 */
@TeleOp(name="Simple TeleOp", group="Swerve Examples")
@Disabled
public class SynchTeleOp extends SynchronousOpMode
    {
    // All hardware variables can only be initialized inside the main() function,
    // not here at their member variable declarations.
    DcMotor motorLeft = null;
    DcMotor motorRight = null;

    @Override protected void main() throws InterruptedException
        {
        // Initialize our hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names you assigned during the robot configuration
        // step you did in the FTC Robot Controller app on the phone.
        this.motorLeft = this.hardwareMap.dcMotor.get("motorLeft");
        this.motorRight = this.hardwareMap.dcMotor.get("motorRight");

        // Configure the knobs of the hardware according to how you've wired your
        // robot. Here, we assume that there are no encoders connected to the motors,
        // so we inform the motor objects of that fact.
        this.motorLeft.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        this.motorRight.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);

        // One of the two motors (here, the left) should be set to reversed direction
        // so that it can take the same power level values as the other motor.
        this.motorLeft.setDirection(DcMotor.Direction.REVERSE);

        // Configure the dashboard however we want it
        this.configureDashboard();
        
        // Wait until we've been given the ok to go
        this.waitForStart();
        
        // Enter a loop processing all the input we receive
        while (this.opModeIsActive())
            {
            if (this.updateGamepads())
                {
                // There is (likely) new gamepad input available.
                // Do something with that! Here, we just drive.
                this.doManualDrivingControl(this.gamepad1);
                }

            // Emit telemetry with the freshest possible values
            this.telemetry.update();

            // Let the rest of the system run until there's a stimulus from the robot controller runtime.
            this.idle();
            }
        }

    /**
     * Implement a simple two-motor driving logic using the left and right
     * right joysticks on the indicated game pad.
     */
    void doManualDrivingControl(Gamepad pad) throws InterruptedException
        {
        // Remember that the gamepad sticks range from -1 to +1, and that the motor
        // power levels range over the same amount
        float ctlPower    =  pad.left_stick_y;
        float ctlSteering =  pad.right_stick_x;

        // We're going to assume that the deadzone processing has been taken care of for us
        // already by the underlying system (that appears to be the intent). Were that not
        // the case, then we would here process ctlPower and ctlSteering to be exactly zero
        // within the deadzone.

        // Map the power and steering to have more oomph at low values (optional)
        ctlPower = this.xformDrivingPowerLevels(ctlPower);
        ctlSteering = this.xformDrivingPowerLevels(ctlSteering);

        // Dampen power to avoid clipping so we can still effectively steer even
        // under heavy throttle.
        //
        // We want
        //      -1 <= ctlPower - ctlSteering <= 1
        //      -1 <= ctlPower + ctlSteering <= 1
        // i.e
        //      ctlSteering -1 <= ctlPower <=  ctlSteering + 1
        //     -ctlSteering -1 <= ctlPower <= -ctlSteering + 1
        ctlPower = Range.clip(ctlPower,  ctlSteering -1,  ctlSteering +1);
        ctlPower = Range.clip(ctlPower, -ctlSteering -1, -ctlSteering +1);

        // Figure out how much power to send to each motor. Be sure
        // not to ask for too much, or the motor will throw an exception.
        float powerLeft  = Range.clip(ctlPower - ctlSteering, -1f, 1f);
        float powerRight = Range.clip(ctlPower + ctlSteering, -1f, 1f);

        // Tell the motors
        this.motorLeft.setPower(powerLeft);
        this.motorRight.setPower(powerRight);
        }

    float xformDrivingPowerLevels(float level)
    // A useful thing to do in some robots is to map the power levels so that
    // low power levels have more power than they otherwise would. This sometimes
    // help give better driveability.
        {
        // We use a log function here as a simple way to transform the levels.
        // You might want to try something different: perhaps construct a
        // manually specified function using a table of values over which
        // you interpolate.
        float zeroToOne = Math.abs(level);
        float oneToTen  = zeroToOne * 9 + 1;
        return (float)(Math.log10(oneToTen) * Math.signum(level));
        }
    
    void configureDashboard()
        {
        // Configure the dashboard. Here, it will have one line, which will contain three items
        this.telemetry.addLine
            (
            this.telemetry.item("left:", new IFunc<Object>()
                {
                @Override public Object value()
                    {
                    return format(motorLeft.getPower());
                    }
                }),
            this.telemetry.item("right: ", new IFunc<Object>()
                {
                @Override public Object value()
                    {
                    return format(motorLeft.getPower());
                    }
                }),
            this.telemetry.item("mode: ", new IFunc<Object>()
                {
                @Override public Object value()
                    {
                    return motorLeft.getChannelMode();
                    }
                })
            );
        }

    // Handy functions for formatting data for the dashboard
    String format(double d)
        {
        return String.format("%.1f", d);
        }
    }
