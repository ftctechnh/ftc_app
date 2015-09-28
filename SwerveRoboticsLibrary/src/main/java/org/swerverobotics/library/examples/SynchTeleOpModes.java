package org.swerverobotics.library.examples;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;

/**
 * An example of a synchronous opmode that's a little more complex than
 * SynchTeleOp, in that it supports multiple different drive modes that are switched
 * between using the A, B, and Y gamepad buttons.
 * 
 * TODO: Perhaps consolidate the two examples 
 */
@TeleOp(name="Fancy Tele Op", group="Swerve Examples")
@Disabled
public class SynchTeleOpModes extends SynchronousOpMode
    {
    enum DRIVEMODE { TANK, ARCADE, LEFT_STICK };
    
    // All hardware variables can only be initialized inside the main() function,
    // not here at their member variable declarations.
    DcMotor motorLeft  = null;
    DcMotor motorRight = null;

    DRIVEMODE driveMode      = DRIVEMODE.TANK;
    String[]  driveModeLabel = new String[] { "tank", "arcade", "left stick"}; 

    @Override protected void main() throws InterruptedException
        {
        // Initialize our hardware variables
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

        // Wait until the game begins
        this.waitForStart();

        // Enter a loop processing all the input we receive
        while (this.opModeIsActive())
            {
            if (this.updateGamepads())
                {
                if (this.gamepad1.a)
                    {
                    this.driveMode = DRIVEMODE.TANK;
                    }
                else if (this.gamepad1.b)
                    {
                    this.driveMode = DRIVEMODE.ARCADE;
                    }
                else if (this.gamepad1.y)
                    {
                    this.driveMode = DRIVEMODE.LEFT_STICK;
                    }

                // There is (likely) new gamepad input available.
                // Do something with that! Here, we just drive.
                this.doManualDrivingControl(this.gamepad1);
                }

            // Emit telemetry
            this.telemetry.addData("Drive mode", driveModeLabel[driveMode.ordinal()]);
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
        float powerLeft = 0;
        float powerRight = 0;
        
        switch (this.driveMode)
            {
        case TANK:
            {
            float leftPower = pad.left_stick_y;
            float rightPower = pad.right_stick_y;
            powerLeft = Range.clip(leftPower, -1f, 1f);
            powerRight = Range.clip(rightPower, -1f, 1f);
            }
            break;

        case ARCADE:
        case LEFT_STICK:
            {
            // Remember that the gamepad sticks range from -1 to +1, and that the motor
            // power levels range over the same amount
            float ctlPower    = pad.left_stick_y;
            float ctlSteering = this.driveMode==DRIVEMODE.ARCADE? pad.right_stick_x : pad.left_stick_x;
    
            // We're going to assume that the deadzone processing has been taken care of for us
            // already by the underlying system (that appears to be the intent). Were that not
            // the case, then we would here process ctlPower and ctlSteering to be exactly zero
            // within the deadzone.
    
            // Map the power and steering to have more oomph at low values (optional)
            ctlPower = this.xformDrivingPowerLevels(ctlPower);
            ctlSteering = -1 * this.xformDrivingPowerLevels(ctlSteering);
    
            // Dampen power to avoid clipping so we can still effectively steer even
            // under heavy throttle.
            //
            // We want
            //      -1 <= ctlPower - ctlSteering <= 1
            //      -1 <= ctlPower + ctlSteering <= 1
            // i.e
            //      ctlSteering -1 <= ctlPower <=  ctlSteering + 1
            //     -ctlSteering -1 <= ctlPower <= -ctlSteering + 1
            ctlPower = Range.clip(ctlPower, ctlSteering - 1, ctlSteering + 1);
            ctlPower = Range.clip(ctlPower, -ctlSteering - 1, -ctlSteering + 1);
    
            // Figure out how much power to send to each motor. Be sure
            // not to ask for too much, or the motor will throw an exception.
            powerLeft = Range.clip(ctlPower + ctlSteering, -1f, 1f);
            powerRight = Range.clip(ctlPower - ctlSteering, -1f, 1f);
            }
            break;

        // end switch
            }

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
    }
