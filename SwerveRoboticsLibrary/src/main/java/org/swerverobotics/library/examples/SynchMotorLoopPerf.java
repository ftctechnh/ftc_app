package org.swerverobotics.library.examples;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;

/**
 * An op mode that investigates how many loop() cycles it takes to do full
 * mode switching on a motor controller. Each main loop cycle does both
 * a read and a write to the motor.
 */
@TeleOp(name="Motor Perf", group="Swerve Examples")
@Disabled
public class SynchMotorLoopPerf extends SynchronousOpMode
    {
    DcMotor     motor;
    int         position;
    long        loopCountStart;
    int         spinCount;
    ElapsedTime elapsed = new ElapsedTime();

    public SynchMotorLoopPerf()
        {
        // Setting this flag will (for now; we may remove this later, or maybe change it to
        // a default, or ... we don't know yet :-) enable an alternate implementation of the legacy
        // motor controller, one built on our robust I2cDeviceClient instead of hand-rolling the 
        // I2C logic as is done in the robot controller runtime's ModernRoboticsNxtDcMotorController
        // implementation. If you're poking around our source code here, you can find that 
        // in internal\LegacyDcMotorControllerOnI2cDevice if you want to have a look.
        //
        // this.useExperimentalThunking = true;
        }
    
    public @Override void main() throws InterruptedException
        {
        motor = hardwareMap.dcMotor.get("motorLeft");

        waitForStart();
        loopCountStart = getLoopCount();
        spinCount      = 1;
        elapsed.reset();
        
        while (this.opModeIsActive())
            {
            position = motor.getCurrentPosition();
            long loopCount = getLoopCount() - loopCountStart;
            double ms = elapsed.time() * 1000;

            // Need to call this or we won't see any gamepad changes
            updateGamepads();

            // Note: normally you'd only bother putting code that looked at the game pads
            // inside of the if (updateGamepads()) clause. But here we're trying to do some
            // performance analysis, and we don't want that to be subject to human issues.
            // Instead, we always set the power exactly once each spin around the loop.
            if (gamepad1.left_bumper)
                motor.setPower(0.5);
            else
                motor.setPower(0.25);

            telemetry.addData("position",      position);
            telemetry.addData("#loop()",       loopCount);
            telemetry.addData("#spin",         spinCount);
            telemetry.addData("#loop()/#spin", String.format("%.1f", loopCount / (double)spinCount));
            telemetry.addData("ms/spin",       String.format("%.1f ms", ms / spinCount));
            telemetry.addData("ms/loop",       String.format("%.1f ms", ms / loopCount));
            telemetry.update();
            idle();
            
            spinCount++;
            }
        }
    }
