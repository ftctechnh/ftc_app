package org.swerverobotics.library.examples;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;

/**
 * An op mode that investigates how fast in a Linear OpMode we can execute
 * a main loop that both reads and writes to the a motor. This OpMode can
 * be used with both legacy and Modern Robotics motor controllers. It expects
 * two motors, named "motorLeft" and "motorRight" that are mounted on the
 * same motor controller.
 */
@TeleOp(name="Motor Perf (linear)", group="Swerve Examples")
@Disabled
public class LinearMotorLoopPerf extends LinearOpMode
    {
    // Declare variables for the two motors
    DcMotor         leftMotor;
    DcMotor         rightMotor;

    // Create a utility that keeps track of time for us
    ElapsedTime     elapsed = new ElapsedTime();

    // Create a little helper utility that counts loops for us
    final IOpModeLoopCounter loopCounter = ClassFactory.createLoopCounter(this);
    long                     loopCountStart;

    public LinearMotorLoopPerf()
        {
        }

    public @Override void runOpMode() throws InterruptedException
        {
        leftMotor  = hardwareMap.dcMotor.get("motorLeft");
        rightMotor = hardwareMap.dcMotor.get("motorRight");
        ClassFactory.createEasyMotorController(this, leftMotor, rightMotor);

        waitForStart();

        loopCountStart = this.loopCounter.getLoopCount();
        int spinCount = 1;
        elapsed.reset();
        
        while (this.opModeIsActive())
            {
            int position = leftMotor.getCurrentPosition();

            long loopCount = this.loopCounter.getLoopCount() - loopCountStart;
            double ms = elapsed.time() * 1000;

            if (gamepad1.left_bumper)
                leftMotor.setPower(0.5);
            else
                leftMotor.setPower(0.25);

            telemetry.addData("position",      position);
            telemetry.addData("#loop()",       loopCount);
            telemetry.addData("#spin",         spinCount);
            telemetry.addData("#loop()/#spin", String.format("%.1f", loopCount / (double)spinCount));
            telemetry.addData("ms/spin",       String.format("%.1f ms", ms / spinCount));
            telemetry.addData("ms/loop",       String.format("%.1f ms", ms / loopCount));

            spinCount++;
            Thread.yield();
            }
        }
    }
