package org.firstinspires.ftc.teamcode;

/**
 * Created by jxfio on 10/30/2018.
 */

public class RoboticsUtils {
    public class PID {
        public double kp;
        public double ki;
        public double kd;
        public double pi=0;
        public double ii=0;
        public double di=0;
        public double preverror = 0;
        public PID(double p, double i, double d){
            kp = p;
            ki = i;
            kd = d;
        }
        public void iteratePID (double error){
            pi=kp*error;
            ii+=ki*error;
            kd = di*error/preverror;
            preverror = error;
        }
    }
}
