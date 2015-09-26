package org.swerverobotics.library.examples;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;

/**
 * An op mode that investigates how many loop() cycles it takes to do full
 * mode switching on a motor controller. Each main loop cycle does both
 * a read and a write to the motor. On the legacy motoro controller, this
 * will
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
            
            if (gamepad1.left_bumper)
                motor.setPower(0.5);
            else
                motor.setPower(0);

            telemetry.addData("position",      position);
            telemetry.addData("#loop()",       getLoopCount() - loopCountStart);
            telemetry.addData("#spin",         spinCount);
            telemetry.addData("#loop()/#spin", (getLoopCount() - loopCountStart) / (double)spinCount);
            telemetry.addData("ms/spin",       String.format("%.1f ms", (elapsed.time()*1000) / (double)spinCount));
            telemetry.update();
            idle();
            
            spinCount++;
            }
        }
    }
