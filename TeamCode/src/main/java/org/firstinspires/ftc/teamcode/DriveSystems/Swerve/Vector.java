package org.firstinspires.ftc.teamcode.DriveSystems.Swerve;

public class Vector {
    double FWD;
    double STR;
    double RCW;
    //wheelbase
    double L = 15.375;
    //trackwidth
    double W = 14;
    //diagonal
    double R = Math.sqrt(Math.pow(L, 2) + Math.pow(W, 2));
    //variable A,B,C,D
    double A;
    double B;
    double C;
    double D;
    //wheel speed
    double ws1;
    double ws2;
    double ws3;
    double ws4;
    double multiplier = 1.0;
    Vector(double gamepadY1, double gamepadX1, double gamepadX2) {
        this.FWD = gamepadY1*-1 *multiplier;
        this.STR = gamepadX1*multiplier;
        this.RCW = gamepadX2*multiplier;
        //variables
        A = STR - RCW * (L / R);
        B = STR + RCW * (L / R);
        C = FWD - RCW * (W / R);
        D = FWD + RCW * (W / R);
        //wheel speed
        ws1 = Math.sqrt(Math.pow(B, 2) + Math.pow(C, 2));
        ws2 = Math.sqrt(Math.pow(B, 2) + Math.pow(D, 2));
        ws3 = Math.sqrt(Math.pow(A, 2) + Math.pow(D, 2));
        ws4 = Math.sqrt(Math.pow(A, 2) + Math.pow(C, 2));
    }


    public void normalizeWS() {
        double max = ws1;
        if (ws2 > max) {
            max = ws2;
        }
        if (ws3 > max) {
            max = ws3;
        }
        if (ws4 > max) {
            max = ws4;
        }
        if(max>1){
            ws1/=max;
            ws2/=max;
            ws3/=max;
            ws4/=max;
        }
    }

    //return wheel speeds
    public double frontRightMotor() {
        normalizeWS();
        return ws1;
    }

    public double frontLeftMotor() {
        normalizeWS();
        return ws2;
    }

    public double backLeftMotor() {
        normalizeWS();
        return ws3;
    }

    public double backRightMotor() {
        normalizeWS();
        return ws4;
    }


    //return wheel angles
    public double frontRightAngle() {
        double angle1 = Math.atan2(B, C) * 180 / Math.PI;
        return Math.toRadians(angle1);
    }

    public double frontLeftAngle() {
        double angle2 = Math.atan2(B, D) * 180 / Math.PI;
        return Math.toRadians(angle2);
    }

    public double backLeftAngle() {
       double angle3 = Math.atan2(A, D) * 180 / Math.PI;
       return Math.toRadians(angle3);
    }

    public double backRightAngle() {
        double angle4 = Math.atan2(A, C) * 180 / Math.PI;
        return Math.toRadians(angle4);
    }

    public double[] getAngles() {
        return new double[] {frontLeftAngle(), frontRightAngle(), backLeftAngle(), backRightAngle()};
    }
    public double[] getMotors() {
        return new double[] {frontLeftMotor(), frontRightMotor(), backLeftMotor(), backRightMotor()};
    }

public double joystick(){
        return A;
    }
}
