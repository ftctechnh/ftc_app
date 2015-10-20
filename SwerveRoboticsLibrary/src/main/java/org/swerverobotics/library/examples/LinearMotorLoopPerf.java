package org.swerverobotics.library.examples;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;

/**
 * An op mode that investigates how fast in a Linear OpMode we can execute
 * a main loop that both reads and writes to the a motor. This OpMode can
 * be used with both legacy and ModernRobotics motor controllers.
 */
@TeleOp(name="Motor Perf (linear)", group="Swerve Examples")
@Disabled
public class LinearMotorLoopPerf extends LinearOpMode
    {
    DcMotor leftMotor, rightMotor;
    ElapsedTime elapsed = new ElapsedTime();

    public LinearMotorLoopPerf()
        {
        }

    public @Override void runOpMode() throws InterruptedException
        {
        leftMotor  = hardwareMap.dcMotor.get("motorLeft");
        rightMotor = hardwareMap.dcMotor.get("motorRight");
        ClassFactory.createEasyLegacyMotorController(this, leftMotor, rightMotor);

        waitForStart();

        int spinCount = 1;
        elapsed.reset();
        
        while (this.opModeIsActive())
            {
            int position = leftMotor.getCurrentPosition();
            double ms = elapsed.time() * 1000;

            if (gamepad1.left_bumper)
                leftMotor.setPower(0.5);
            else
                leftMotor.setPower(0.25);

            telemetry.addData("position",      position);
            telemetry.addData("#spin",         spinCount);
            telemetry.addData("ms/spin",       String.format("%.1f ms", ms / spinCount));

            spinCount++;
            Thread.yield();
            }
        }
    }
