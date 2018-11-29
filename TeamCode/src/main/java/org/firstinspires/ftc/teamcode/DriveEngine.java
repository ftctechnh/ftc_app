package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class DriveEngine {
    DcMotor back;
    DcMotor right;
    DcMotor left;

    private static final double ticksPerRev = 1120;
    private static final double inPerRev = Math.PI * 5;
    public static final double inPerTicks = inPerRev /ticksPerRev;

    double theta;
    double xOut, yOut;

    public DriveEngine(HardwareMap hardwareMap) {
        back  = hardwareMap.dcMotor.get("back");
        right = hardwareMap.dcMotor.get("right");
        left  = hardwareMap.dcMotor.get("left");
        theta = 0;

        back.setMode (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        left.setMode (DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        back.setMode (DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        left.setMode (DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        back.setDirection (DcMotor.Direction.FORWARD);
        right.setDirection(DcMotor.Direction.FORWARD);
        left.setDirection (DcMotor.Direction.FORWARD);

    }

    public void drive(double x, double y) {
        double xprime = x * Math.cos(theta) - y * Math.sin(theta);
        double yprime = x * Math.sin(theta) + y * Math.cos(theta);

        xOut = x = xprime;
        yOut = y = yprime;

        back.setPower(x);
        right.setPower( (-x/2) + ( (y*Math.sqrt(3)) / 2 ) );
        left.setPower ( (-x/2) + ( (-y*Math.sqrt(3)) / 2 ) );
    }


    public void drive(double x, double y,boolean op) {
        double xprime = x * Math.cos(theta) - y * Math.sin(theta);
        double yprime = x * Math.sin(theta) + y * Math.cos(theta);

        x = xprime;
        y = yprime;

        double backPower = x;
        double rightPower = (-x/2) + y * (Math.sqrt(3)/2) ;
        double leftPower  = (-x/2) - y * (Math.sqrt(3)/2) ;

        if(op && Math.sqrt(x*x + y*y) > .90)
        {
            double max = Math.max(Math.max(backPower,rightPower),leftPower);
            backPower  *= 1 / max;
            rightPower *= 1 / max;
            leftPower  *= 1 / max;
        }

        xOut = backPower;
        yOut = (rightPower - leftPower)/2;

        back.setPower(backPower);
        right.setPower(rightPower);
        left.setPower (leftPower);
    }

    public void driveAtAngle(double theta)
    {
        this.theta = theta;
    }

    public void rotate(double y) {
        back.setPower(y);
        right.setPower(y);
        left.setPower(y);
    }
}