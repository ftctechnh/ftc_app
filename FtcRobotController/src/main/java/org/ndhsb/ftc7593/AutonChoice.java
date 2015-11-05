/**
 * Created by mhaeberli on 10/29/15.
 */

package org.ndhsb.ftc7593;

public class AutonChoice {
    public double startTime;
    public double endTime;
    public double lMotor;
    public double rMotor;

    public AutonChoice(double startTimeI, double endTimeI, double lMotorI, double rMotorI) {
        this.startTime = startTimeI;
        this.endTime = endTimeI;
        this.lMotor = lMotorI;
        this.rMotor = rMotorI;
    }
}
