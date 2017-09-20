package org.firstinspires.ftc.teamcode.VelocityVortex;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by spmce on 11/2/2016.
 */
public class OmniWheelDrive extends DriveTrain{

    private double angle; //cosine angle zero to PI (it is still zero to PI when y is positive)
    //private double ref;   //reference angle
    private double power; //power of robot form -1 to 1
    private boolean ifPositive; //determines if y is positive or negative used for calculating angles
    private double x;     //right stick x

    /**
     * OmniWheelDrive Constructor
     */
    public OmniWheelDrive() {
        angle = 0;
        //ref = 0;
        power = 0;
        ifPositive = true;
        x = 0;
    }

    /**
     * OmniWheelDrive Constructor
     * @param angle
     * @param power
     */
    OmniWheelDrive(double angle, double power) {
        if (angle < 0) {
            this.angle = -angle;
            ifPositive = false;
        } else {
            this.angle = angle;
            ifPositive = true;
        }
        this.power = power;
        //ref = 0;
        x = 0;
    }

    /**
     * @param angle
     * @param power
     * @param x
     */
    OmniWheelDrive(double angle, double power, double x) {
        if (angle < 0) {
            this.angle = -angle;
            ifPositive = false;
        } else {
            this.angle = angle;
            ifPositive = true;
        }
        this.power = power;
        //ref = 0;
        this.x = x;
    }
    /**
     * @param angle
     * @param power
     * @param ifPositive
     */
    OmniWheelDrive(double angle, double power, boolean ifPositive) {
        this.angle = angle;
        this.power = power;
        //ref = 0;
        this.ifPositive = ifPositive;
        x = 0;
    }

    /**
     * @param angle
     * @param power
     * @param ifPositive
     * @param x
     */
    OmniWheelDrive(double angle, double power, boolean ifPositive, double x) {
        this.angle = angle;
        this.power = power;
        //ref = 0;
        this.ifPositive = ifPositive;
        this.x = x;
    }

    @Override
    public void run() {
        super.run();
    }

    /**
     * @param angle
     * @param power
     */
    public double[] drive(double angle, double power) {
        if (angle < 0) {
            this.angle = -angle;
            ifPositive = false;
        } else {
            this.angle = angle;
            ifPositive = true;
        }
        this.power = power;
        return runDrive();
    }
    /**
     * @param angle
     * @param power
     * @param x
     */
    public double[] drive(double angle, double power, double x) {
        if (angle < 0) {
            this.angle = -angle;
            ifPositive = false;
        } else {
            this.angle = angle;
            ifPositive = true;
        }
        this.power = power;
        this.x = x;
        return runDrive();
    }
    /**
     * @param angle
     * @param power
     * @param ifPositive
     */
    public double[] drive(double angle, double power, boolean ifPositive) {
        this.ifPositive = ifPositive;
        this.angle = angle;
        this.power = power;
        return runDrive();
    }

    /**
     * @param angle
     * @param power
     * @param ifPositive
     * @param x
     */
    public double[] drive(double angle, double power, boolean ifPositive, double x) {
        this.ifPositive = ifPositive;
        this.angle = angle;
        this.power = power;
        this.x = x;
        return runDrive();
    }

    /**
     * @param pad
     * @return
     */
    public double[] drive(Gamepad pad) {
        double lx = pad.left_stick_x;
        double ly = -pad.left_stick_y;
        x = pad.right_stick_x;
        power = pythag(lx, ly);
        angle = Math.acos(lx / power);
        ifPositive = isIfPositive(ly);
        return runDrive();
    }

    public double[] drive(Gamepad pad, GyroSensor gyroSensor) {
        double lx = pad.left_stick_x;
        double ly = -pad.left_stick_y;
        x = pad.right_stick_x;
        power = pythag(lx, ly);
        angle = Math.acos(lx / power);
        if(!isIfPositive(ly)) {
            angle = -angle;
        }
        double gyroAng = gyroSensor.getHeading();
        gyroAng = Math.toRadians(gyroAng);
        if (gyroAng > Math.PI) {
            gyroAng -= 2*Math.PI;
        }
        angle = angle + gyroAng;
        if (angle < 0) {
            angle = -angle;
            ifPositive = false;
        } else {
            ifPositive = true;
        }
        return runDrive();
    }

    /**
     * @return
     */
    public double[] runDrive() {
        double[] F = new double[4];
        double ref;
        if (power == 0) {
            F[0] = 0;   //front left
            F[1] = 0;   //front right
            F[2] = F[0];//back right
            F[3] = F[1];//back left
        } else if (angle < Math.PI / 4) {
            ref = Math.PI / 4 - angle;
            if (ifPositive) {
                F[0] = power * Math.cos(ref);
                F[1] =-power * Math.sin(ref);
                F[2] = F[0];
                F[3] = F[1];
            } else {
                F[0] = power * Math.sin(ref);
                F[1] =-power * Math.cos(ref);
                F[2] = F[0];
                F[3] = F[1];
            }
        } else if (angle < Math.PI / 4 + 0.00000000000002) { //add small number because rounding is off
            if (ifPositive) {
                F[0] = power;
                F[1] = 0;
                F[2] = F[0];
                F[3] = F[1];
            } else {
                F[0] = 0;
                F[1] = -power;
                F[2] = F[0];
                F[3] = F[1];
            }
        } else if (angle < 3 * Math.PI / 4) {
            ref = 3 * Math.PI / 4 - angle;
            if (ifPositive) {
                F[0] = power * Math.sin(ref);
                F[1] = power * Math.cos(ref);
                F[2] = F[0];
                F[3] = F[1];
            } else {
                F[0] = -power * Math.cos(ref);
                F[1] = -power * Math.sin(ref);
                F[2] = F[0];
                F[3] = F[1];
            }
        } else if (angle == 3 * Math.PI / 4) {
            if (ifPositive) {
                F[0] = 0;
                F[1] = power;
                F[2] = F[0];
                F[3] = F[1];
            } else {
                F[0] =-power;
                F[1] = 0;
                F[2] = F[0];
                F[3] = F[1];
            }
        } else if (angle == Math.PI) { //180 degrees
            double num = Math.sqrt(2)/2;
            F[0] = -power*num;
            F[1] = power*num;
            F[2] = F[0];
            F[3] = F[1];
        } else {
            //ref = angle - 3 * Math.PI / 4;
            ref = 5 * Math.PI / 4 - angle;
            if (ifPositive) {
                F[0] = -power * Math.cos(ref);
                F[1] = power * Math.sin(ref);
                F[2] = F[0];
                F[3] = F[1];
            } else {
                F[0] = -power * Math.sin(ref);
                F[1] = power * Math.cos(ref);
                F[2] = F[0];
                F[3] = F[1];
            }
        }
        F = turn(F,x);
        for (int i = 0; i < 4; i++) {
            if (F[i] > 1)
                F[i] = 1;
            else if (F[i] < -1)
                F[i] = -1;
            else if (isNaN(F[i]))
                F[i] = 0;
        }
        return F;
    }

    /**
     * @param a
     * @param b
     * @return
     */
    public double pythag(double a, double b) {
        double a2;
        double b2;
        double c2;
        double c;
        a2 = Math.pow(a,2);
        b2 = Math.pow(b,2);
        c2 = a2+b2;
        c = Math.sqrt(c2);
        return c;
    }

    /**
     * @param F
     * @param rx
     * @return
     */
    public double[] turn (double[] F, double rx) {
        F[0] = F[0] + rx;   // front left
        F[1] = F[1] - rx;   // front right
        F[2] = F[2] - rx;   // back right
        F[3] = F[3] + rx;   // back left
        for (int i = 0; i < 4; i++) {
            int k = i + 2;
            if (k >= 4)
                k -= 4;
            if (F[i] > 1) {
                double temp = F[i] - 1;
                F[i] = 1;
                F[k] -= temp;
            }
            if (F[i] < -1) {
                double temp = F[i] + 1;
                F[i] = -1;
                F[k] -= temp;
            }
        }
        return F;
    }

    /**
     * @param x
     * @return
     */
    boolean isNaN(double x){return x != x;}

    /**
     * @param y
     * @return
     */
    boolean isIfPositive(double y) {return y >= 0;}
}
