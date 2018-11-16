package org.firstinspires.ftc.teamcode.robotutil;

/**
 * PID Controller: kp * (e + (integral(e) / ti) + (td * derivative(e))).
 * https://en.wikipedia.org/wiki/PID_controller#Ideal_versus_standard_PID_form
 */

import android.util.Log;

import org.firstinspires.ftc.robotcore.external.Telemetry;
public class PID {
    private double kp;
    private double ki;
    private double kd;


    private double previousError;
    private double runningIntegral;
    private double lastRegistereedTime;
    private Telemetry tel;

    private Telemetry.Item constants, telIntegral,telProportional,telRunningIntegral,telDerivative,telClampedOutput,telRawOutput,telError,telTimeDiff;

    public PID(double kp, double ki, double kd, Telemetry telemetry) {
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;

        this.previousError = 0;
        this.runningIntegral = 0;
        this.lastRegistereedTime = System.currentTimeMillis();
//        this.constants = tel.addData("kP,kI,kD",String.format("%.2f || %.2f || %.2f",kp,ki,kd));

        this.tel = telemetry;
        this.telError = tel.addData("error","N/A");
        this.telTimeDiff = tel.addData("time diff","N/A");
        this.telProportional = tel.addData("P","N/A");
        this.telIntegral = tel.addData("Curr_I","N/A");
        this.telRunningIntegral = tel.addData("Running_I","N/A");
        this.telDerivative = tel.addData("D","N/A");
        this.telRawOutput = tel.addData("Raw out","N/A");
        this.telClampedOutput = tel.addData("clamp out","N/A");
        this.tel.update();



    }

    public Double getOutput(double desired, double actual){
        double error = (desired-actual);

        double time = System.currentTimeMillis();
        double timeDifference = time - lastRegistereedTime;

        double proportional = kp*error;


        double newIntegral = ki*error*time;
        runningIntegral += newIntegral;

        double derivative = kd * ((error - previousError) / timeDifference);

        lastRegistereedTime = time;
        previousError = error;

        double rawOutput = proportional + derivative + runningIntegral;
        double clampedOutput =  clampValue(rawOutput,-1.0,1.0);


        this.telProportional.setValue(proportional);
        this.telIntegral.setValue(newIntegral);
        this.telRunningIntegral.setValue(runningIntegral);
        this.telDerivative.setValue(derivative);
        this.telRawOutput.setValue(rawOutput);
        this.telClampedOutput.setValue(clampedOutput);
        this.telError.setValue(error);
        this.telTimeDiff.setValue(timeDifference);

        this.tel.update();
        return clampedOutput;

    }

    private static double clampValue(double value, double min, double max) {
        return Math.min(max, Math.max(min, value));
    }



}