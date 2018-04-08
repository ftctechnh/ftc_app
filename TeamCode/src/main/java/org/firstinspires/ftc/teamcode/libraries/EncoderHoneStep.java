package org.firstinspires.ftc.teamcode.libraries;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Noah on 4/7/2018.
 * Step to drive to a specific encoder count
 */

public class EncoderHoneStep extends AutoLib.Step {
    private final OpMode mode;
    private final DcMotor[] encode;
    private final int dist;
    private final int error;
    private final int count;
    private final SensorLib.PID errorPid;
    private final GyroCorrectStep gyroStep;

    private double lastTime = 0;
    private int currentCount = 0;
    private int[] startPos;

    public EncoderHoneStep(OpMode mode, int distCounts, int error, int count, SensorLib.PID errorPid, GyroCorrectStep gyroStep, DcMotor[] encoderRay) {
        this.mode = mode;
        this.encode = encoderRay;
        this.dist = distCounts;
        this.error = error;
        this.count = count;
        this.errorPid = errorPid;
        this.gyroStep = gyroStep;
    }

    public boolean loop() {
        super.loop();
        if(firstLoopCall()) {
            gyroStep.reset();
            lastTime = mode.getRuntime() - 1;
            startPos = new int[encode.length];
            for (int i = 0; i < encode.length; i++) startPos[i] = encode[i].getCurrentPosition();
        }
        //get the distance delta
        int total = 0;
        for (int i = 0; i < encode.length; i++) {
            total += encode[i].getCurrentPosition() - startPos[i];
            //mode.telemetry.addData("Delta " + i, encode[i].getCurrentPosition() - startPos[i]);
        }

        final float read = -((float)total / encode.length);
        float curError = read - dist;
        //if we found it, stop
        //if the peak is within stopping margin, stop
        if(Math.abs(curError) <= error) {
            setMotorsWithoutGyro(0);
            return ++currentCount >= count;
        }
        else currentCount = 0;

        //PID
        final double time = mode.getRuntime();
        final float pError = errorPid.loop(curError, (float)(time - lastTime));
        lastTime = time;
        mode.telemetry.addData("power error", pError);

        //cut out a middle range, but handle positive and negative
        float power;
        if(pError >= 0) power = Range.clip(pError, gyroStep.getMinPower(), gyroStep.getMaxPower());
        else power = Range.clip(pError, -gyroStep.getMaxPower(), -gyroStep.getMinPower());
        //reverse if necessary
        if(gyroStep.getStartPower() < 0) power = -power;

        gyroStep.setPower(power);
        gyroStep.loop();
        //telem
        mode.telemetry.addData("Power", -power);
        mode.telemetry.addData("Encoder error", curError);
        mode.telemetry.addData("Encoder", read);
        mode.telemetry.addData("Dist", dist);
        //return
        return false;
    }

    private void setMotorsWithoutGyro(float power) {
        for (DcMotor motor : gyroStep.getMotors()) motor.setPower(power);
    }
}
