package org.swerverobotics.library.examples;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;

/**
 * An op mode that investigates how many loop() cycles it takes to do full
 * mode switching on a motor controller. Each main loop cycle does both
 * a read and a write to motors.
 *
 * This OpMode expects two motors, named 'motorLeft' and 'motorRight'. A configuration
 * flag, set in the constructor, controls whether just one or both of the motors are used.
 */
@TeleOp(name="Motor Perf (sync)", group="Swerve Examples")
// @Disabled
public class SynchMotorLoopPerf extends SynchronousOpMode
    {
    final boolean useBothMotors;

    DcMotor     leftMotor, rightMotor;
    int         leftPosition, rightPosition;
    long        loopCountStart;
    int         spinCount;
    ElapsedTime elapsed = new ElapsedTime();

    public SynchMotorLoopPerf()
        {
        useBothMotors = true;
        }
    
    public @Override void main() throws InterruptedException
        {
        leftMotor  = hardwareMap.dcMotor.get("motorLeft");
        rightMotor = hardwareMap.dcMotor.get("motorRight");

        waitForStart();

        loopCountStart = getLoopCount();
        spinCount      = 1;
        elapsed.reset();

        while (this.opModeIsActive())
            {
            leftPosition  = leftMotor.getCurrentPosition();
            rightPosition = useBothMotors ? rightMotor.getCurrentPosition() : 0;

            long loopCount = getLoopCount() - loopCountStart;
            double ms = elapsed.time() * 1000;

            // Need to call this or we won't see any gamepad changes
            updateGamepads();

            // Note: normally you'd only bother putting code that looked at the game pads
            // inside of the if (updateGamepads()) clause. But here we're trying to do some
            // performance analysis, and we don't want that to be subject to human issues.
            // Instead, we always set the power exactly once each spin around the loop.
            if (gamepad1.left_bumper)
                powerMotors(0.5);
            else
                powerMotors(0.25);

            telemetry.addData("position",      String.format("left=%d right=%d", leftPosition, rightPosition));
            telemetry.addData("#loop()",       loopCount);
            telemetry.addData("#spin",         spinCount);
            telemetry.addData("#loop()/#spin", String.format("%.1f", loopCount / (double)spinCount));
            telemetry.addData("ms/spin",       String.format("%.1f ms", ms / spinCount));
            telemetry.addData("ms/loop",       String.format("%.1f ms", ms / loopCount));
            telemetry.update();
            idle();
            
            spinCount++;
            }

        // powerMotors(0);
        }

    public void powerMotors(double power)
        {
        leftMotor.setPower(power);
        if (useBothMotors)
            rightMotor.setPower(power);
        }
    }
