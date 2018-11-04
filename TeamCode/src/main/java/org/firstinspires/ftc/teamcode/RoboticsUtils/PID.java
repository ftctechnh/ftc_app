package org.firstinspires.ftc.teamcode.RoboticsUtils;

public class PID {
    public double kp;
    public double ki;
    public double kd;
    public double pi=0;
    public double ii=0;
    public double di=0;
    public double preverror = 0;
    public PID(double kp, double ki, double kd){
        this.kp = kp; //making 3 number variables to set later on
        this.ki = ki;
        this.kd = kd;
    }
    public void iteratePID (double error,double dt){
        pi=kp*error;
        ii+=ki*error*dt;
        kd = di*error/preverror;
        this.preverror = error;
    }
    public double getPID(){
        return pi+ki+di;
    }
}
