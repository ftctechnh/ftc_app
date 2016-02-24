/**
 * Created by mhaeberli on 10/29/15.
 */

package org.ndhsb.ftc7593;

public class AutonChoice {
    public double startTime;
    public double endTime;
    public double lMotor;
    public double rMotor;
    public double turnDegrees;
    public double duration; // vs startTime / endTime - needs to be "compiled" into the table

    public AutonChoice(double startTimeI, double endTimeI, double lMotorI, double rMotorI) {
        this.startTime = startTimeI;
        this.endTime = endTimeI;
        this.lMotor = lMotorI;
        this.rMotor = rMotorI;
        this.turnDegrees = 0.0;
        this.duration = 0.0;
    }

    public AutonChoice(double durationI, double startTimeI, double endTimeI, double lMotorI, double rMotorI, double turnDegreesI) {
        this.startTime = startTimeI;
        this.endTime = endTimeI;
        this.lMotor = lMotorI;
        this.rMotor = rMotorI;
        this.duration = durationI;
        this.turnDegrees = turnDegreesI;
    }
}
