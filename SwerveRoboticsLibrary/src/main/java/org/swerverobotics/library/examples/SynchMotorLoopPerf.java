package org.swerverobotics.library.examples;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;

/**
 * An op mode that investigates how many loop() cycles it takes to do full
 * mode switching on a motor controller.
 */
@TeleOp(name="Motor Perf")
@Disabled
public class SynchMotorLoopPerf extends SynchronousOpMode
    {
    DcMotor     motor;
    int         position;
    long        loopCountStart;
    int         spinCount;
    ElapsedTime elapsedTime = new ElapsedTime();
    
    public @Override void main() throws InterruptedException
        {
        motor = hardwareMap.dcMotor.get("motorLeft");
        configureTelemetry();
        
        waitForStart();
        loopCountStart = getLoopCount();
        spinCount      = 1;
        elapsedTime.reset();
        
        while (this.opModeIsActive())
            {
            position = motor.getCurrentPosition();
            
            if (gamepad1.left_bumper)
                motor.setPower(0.5);
            else
                motor.setPower(0);
            
            telemetry.update();
            idle();
            
            spinCount++;
            }
        }
    
    void configureTelemetry()
        {
        telemetry.addLine(
            telemetry.item("pos:", new IFunc<Object>()
                {
                @Override public Object value()
                    {
                    return position;
                    }}),
            telemetry.item("loop:", new IFunc<Object>()
                {
                @Override public Object value()
                    {
                    return getLoopCount() - loopCountStart;
                    }}),
            telemetry.item("spin:", new IFunc<Object>()
                {
                @Override public Object value()
                    {
                    return spinCount;
                    }})
             );
        telemetry.addLine(
            telemetry.item("loop/spin:", new IFunc<Object>()
                {
                @Override public Object value()
                    {
                    return format((getLoopCount() - loopCountStart) / (double) spinCount);
                    }}),
            telemetry.item("ms/spin:", new IFunc<Object>()
                {
                @Override public Object value()
                    {
                    return format(elapsedTime.time() * 1000 / (double) spinCount);
                    }})
            );
        }

    String format(double d)
        {
        return String.format("%.1f", d);
        }
    }
