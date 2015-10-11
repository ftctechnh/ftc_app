package org.swerverobotics.library.examples;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.swerverobotics.library.SynchronousOpMode;
import org.swerverobotics.library.interfaces.Disabled;
import org.swerverobotics.library.interfaces.TeleOp;

/**
 * An op mode that investigates how many loop() cycles it takes to do full
 * mode switching on a motor controller. Each main loop cycle does both
 * a read and a write to the motor.
 */
@TeleOp(name="Motor Perf (linear)", group="Swerve Examples")
@Disabled
public class LinearMotorLoopPerf extends LinearOpMode
    {
    DcMotor     motor;
    int         position;
    long        loopCountStart;
    int         spinCount;
    ElapsedTime elapsed = new ElapsedTime();

    public LinearMotorLoopPerf()
        {
        }

    public @Override void runOpMode() throws InterruptedException
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

            Thread.yield();
            
            spinCount++;
            }
        }

    int getLoopCount()
    // The intent here is to return the number of times that the loop() method
    // has been called. Unfortunately, at the moment we know of no way of obtaining
    // that information, so we stub this out.
        {
        return 0;
        }
    }
