package org.firstinspires.ftc.teamcode.libraries;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.libraries.hardware.MatbotixUltra;

/**
 * Created by Noah on 4/7/2018.
 */

public class UltraHoneStep extends AutoLib.Step {
    private final OpMode mode;
    private final MatbotixUltra ultra;
    private final int dist;
    private final int error;
    private final int count;
    private final SensorLib.PID errorPid;
    private final GyroCorrectStep gyroStep;

    private double lastTime = 0;
    private int currentCount = 0;
    private float pError = 0;
    private int curError = 0;

    public UltraHoneStep(OpMode mode, MatbotixUltra ultra, int dist, int error, int count, SensorLib.PID errorPid, GyroCorrectStep gyroStep) {
        this.mode = mode;
        this.ultra = ultra;
        this.dist = dist;
        this.error = error;
        this.count = count;
        this.errorPid = errorPid;
        this.gyroStep = gyroStep;
    }

    public boolean loop() {
        super.loop();
        if(firstLoopCall()) lastTime = mode.getRuntime() - 1;
        if(currentCount > 0) {
            curError = dist - ultra.getReading();
            if(Math.abs(curError) <= error) {
                setMotorsWithoutGyro(0);
                return ++currentCount >= count;
            }
            else currentCount = 0;
            //PID
            final double time = mode.getRuntime();
            pError = errorPid.loop(curError, (float)(time - lastTime));
            lastTime = time;
            mode.telemetry.addData("power error", pError);
        }
        else {
            //get the distance and error
            final int read = ultra.getReadingNoDelay();
            if(read > 0) {
                curError = dist - read;
                //if we found it, stop
                //if the peak is within stopping margin, stop
                if(Math.abs(curError) <= error) {
                    setMotorsWithoutGyro(0);
                    currentCount = 1;
                }
                else currentCount = 0;
                //PID
                final double time = mode.getRuntime();
                pError = errorPid.loop(curError, (float)(time - lastTime));
                lastTime = time;
                mode.telemetry.addData("power error", pError);
            }

            //cut out a middle range, but handle positive and negative
            float power;
            if(pError >= 0) power = Range.clip(pError, gyroStep.getMinPower(), gyroStep.getMaxPower());
            else power = Range.clip(pError, -gyroStep.getMaxPower(), -gyroStep.getMinPower());
            //reverse if necessary
            if(gyroStep.getStartPower() < 0) power = -power;
            /*
            if(gyroStep.getStartPower() >= 0){
                if(pError >= 0) power = Range.clip(gyroStep.getStartPower() + pError, gyroStep.getMinPower(), gyroStep.getMaxPower());
                else power = Range.clip(pError - gyroStep.getStartPower(), -gyroStep.getMaxPower(), -gyroStep.getMinPower());
            }
            else {
                if(pError >= 0) power = Range.clip(gyroStep.getStartPower() - pError, -gyroStep.getMaxPower(), -gyroStep.getMinPower());
                else power = Range.clip(Math.abs(gyroStep.getStartPower() + pError), gyroStep.getMinPower(), gyroStep.getMaxPower());
            }
            */
            gyroStep.setPower(power);
            gyroStep.loop();
            //telem
            mode.telemetry.addData("Power", -power);
            mode.telemetry.addData("Ultra error", curError);
            mode.telemetry.addData("Ultra", read);
        }
        //return
        return false;
    }

    private void setMotorsWithoutGyro(float power) {
        for(DcMotor motor : gyroStep.getMotors()) motor.setPower(power);
    }
}