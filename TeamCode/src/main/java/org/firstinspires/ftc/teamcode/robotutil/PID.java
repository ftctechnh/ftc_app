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
    private Logger l = new Logger("PID");


    private Telemetry.Item constants, telIntegral,telProportional,telRunningIntegral,telDerivative,telClampedOutput,telRawOutput,telError,telTimeDiff;

    public PID(double kp, double ki, double kd, Telemetry telemetry) {
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;

        this.previousError = 0;
        this.runningIntegral = 0;
        this.lastRegistereedTime = System.currentTimeMillis();

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

    public double getPreviousError() {
        return previousError;
    }

    public double getOutput(double error) {

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



        l.logData("Proportional",proportional);
        l.logData("telIntegral",newIntegral);
        l.logData("telRunningIntegral",runningIntegral);
        l.logData("telDerivative",derivative);
        l.logData("telRawOutput",rawOutput);
        l.logData("telClampedOutput",clampedOutput);
        l.logData("telError",error);
        l.logData("telTimeDiff",timeDifference);

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

    public double getOutput(double desired, double actual){
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