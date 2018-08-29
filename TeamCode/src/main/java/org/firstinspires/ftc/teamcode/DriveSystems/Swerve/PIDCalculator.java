package org.firstinspires.ftc.teamcode.DriveSystems.Swerve;

public class PIDCalculator {
    double error = 0;
    double time = 0;
    double oldTime = 0;
    double oldError = 0;
    double rollingError = 0;
    double PIDValue;
    double kP;
    double kI;
    double kD;
    //constructor
    public PIDCalculator(double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }

    double getPID(double error, double time){
        this.error = error;
        this.time = time;
        rollingError += error*deltaTime();
        PIDValue = kP*error+kI*rollingError+kD*deltaError()/deltaTime();
        oldTime = time;
        oldError = error;
        return PIDValue;
    }
    //calculate change in time
private double deltaTime(){
        return time - oldTime;
}
private double deltaError(){
        return error - oldError;
}


}
