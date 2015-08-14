package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;

/**
 * An op mode that investigates how many loop() cycles it takes to do full
 * mode switching on a motor controller.
 */
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
            
            if (gamepad1.left_bumper())
                motor.setPower(0.5);
            else
                motor.setPower(0);
            
            telemetry.dashboard.update();
            idle();
            
            spinCount++;
            }
        }
    
    void configureTelemetry()
        {
        telemetry.dashboard.line(
            telemetry.dashboard.item("pos:", new IFunc<Object>() { @Override public Object value() {
                return position;
                }}),
            telemetry.dashboard.item("loop:", new IFunc<Object>() { @Override public Object value() {
                return getLoopCount() - loopCountStart;
                }}),
            telemetry.dashboard.item("spin:", new IFunc<Object>() { @Override public Object value() {
                return spinCount;
                }}));
        telemetry.dashboard.line(
            telemetry.dashboard.item("loop/spin:", new IFunc<Object>() { @Override public Object value() {
                return format((getLoopCount() - loopCountStart) / (double)spinCount);
                }}),
            telemetry.dashboard.item("ms/spin:", new IFunc<Object>() { @Override public Object value() {
                return format(elapsedTime.time() * 1000 / (double)spinCount);
                }})
            );
        }

    String format(double d)
        {
        return String.format("%.1f", d);
        }
    }
