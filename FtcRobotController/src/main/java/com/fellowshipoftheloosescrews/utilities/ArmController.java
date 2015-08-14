package com.fellowshipoftheloosescrews.utilities;

import com.qualcomm.robotcore.hardware.Servo;

public class ArmController {
    //l1 is the upper arm, l2 is the lower arm
    public double upperArm, lowerArm;

    private DcServo shoulderMotor;
    private Servo elbowMotor;

    //theta1 is the shoulder, theta2 is the elbow
    private double shoulder, elbow;

    public ArmController(double l1, DcServo shoulderMotor, double l2, Servo elbowMotor)
    {
        this.upperArm = l1;
        this.lowerArm = l2;

        this.shoulderMotor = shoulderMotor;
        this.elbowMotor = elbowMotor;
    }

    public void init(double x, double y)
    {
        moveToPosition(x, y);
    }

    public void moveToPosition(double x, double y)
    {
        doCalculations(x, y);

    }

    private void doCalculations(double targetX, double targetY)
    {
        //finds the distance to the target point
        double distance = Math.sqrt((targetX * targetX) + (targetY * targetY));
        if(distance > upperArm + upperArm)
        {
            System.err.println("ERROR: Target is outside reach");
            return;
        }

        elbow = Math.acos(((upperArm * upperArm) + (lowerArm * lowerArm) - (distance * distance))
                / (2 * upperArm * lowerArm));
        shoulder = Math.atan2(targetY, targetX)
                + Math.acos(((upperArm * upperArm) + (distance * distance) - (lowerArm * lowerArm))
                / (2 * upperArm * distance));



        System.out.println("Distance " + distance);
        System.out.println("theta1 " + Math.toDegrees(shoulder));
        System.out.println("theta2 " + Math.toDegrees(elbow));
    }

    public double getShoulder()
    {
        return shoulder;
    }

    public double getElbow()
    {
        return elbow;
    }
}
