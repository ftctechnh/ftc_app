package org.usfirst.ftc.theintersect.code;

/**
 * utility class to calculate RPM
 */

public class Rpm {

    private int clickPerRotation;
    private int prevEncoder;
    private double prevTime;
    private double currentRPM;

    /**
     * Creates a Rpm calculator
     *
     * @param clickPerRotation encoder reading per rotation of wheel
     */
    public Rpm(int clickPerRotation) {
        this.clickPerRotation = clickPerRotation;
        this.prevEncoder = 0;
        this.prevTime = 0;
    }

    /**
     * Performs a PID update and returns the output control.
     *
     * @param currentEncoder    current encoder reading
     * @param currentTime       current time in nano second
     * @return current RPM
     */
    public double update(int currentEncoder, double currentTime) {

        double deltaTime; // Elapse time between measurement

        if (prevTime == 0) { // init value
            this.prevEncoder = currentEncoder;
            this.prevTime = currentTime;
            this.currentRPM = 0;
            return 0.0;
        }
        deltaTime = (currentTime - this.prevTime) / 1000000000; // looptime in msecond
        if (deltaTime == 0.0) {
            return currentRPM;
        }
        currentRPM = (currentEncoder - prevEncoder) / deltaTime * 60 / clickPerRotation;

        // store current measurement
        prevEncoder = currentEncoder;
        prevTime = currentTime;
        return this.currentRPM;
    }

    public double getCurrentRPM(){
        return this.currentRPM;
    }
}
