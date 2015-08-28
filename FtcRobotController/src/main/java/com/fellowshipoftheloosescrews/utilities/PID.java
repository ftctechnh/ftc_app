package com.fellowshipoftheloosescrews.utilities;

/**
 * Created by Thomas on 8/13/2015.
 */
public class PID {

    // Tuning variables for the PID loop
    private double kp;
    private double ki;
    private double kd;

    // the target of the PID loop
    private double target;

    private double error;
    private double lastError;
    private double totalError;

    private double lastTime;

    public PID(double kp, double ki, double kd, double target)
    {
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
        setTarget(target);

        error = 0;
        lastError = 0;
        totalError = 0;
        lastTime = getTimeMs();
    }

    /**
     * Sets the new target for the pid loop
     * @param newTarget the new target
     */
    public void setTarget(double newTarget)
    {
        // maybe reset error?
        target = newTarget;
    }

    /**
     * Gets the pid output
     * @param currentValue the current value from the sensor
     * @return PID output
     */
    public double calculate(double currentValue)
    {
        // gets the current time in milliseconds
        double currentTime = getTimeMs();
        double deltaTime = currentTime - lastTime;

        // calculates the error
        double error = target - currentValue;
        double deltaError = error - lastError;

        double derivative = deltaError / deltaTime;

        // adds the error to the integral
        totalError += error * deltaTime / 1000d;

        lastTime = currentTime;
        lastError = error;
        return (kp * error) + (ki * totalError) + (kd * -derivative);
    }

    /**
     * Gets the time in ms
     * @return milliseconds from the start of the program
     */
    private double getTimeMs()
    {
        return System.nanoTime() / 1000000d;
    }
}
