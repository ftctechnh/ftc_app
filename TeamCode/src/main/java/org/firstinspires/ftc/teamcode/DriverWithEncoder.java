package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by jxfio on 12/15/2017.
 */

public class DriverWithEncoder extends Driver{
    public DcMotor leftMotor;
    public DcMotor rightMotor;
    public double wheelradius;
    public double ticsPerRevolution = 1416;
    public double wheelSeperation;
    public DriverWithEncoder(DcMotor left, DcMotor right, double radius, double wheelSep){
        leftMotor = left;
        rightMotor = right;
        wheelradius = radius;
        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        wheelSeperation = wheelSep;
    }
    public void forward(double distance, double power){
        double wheelcirc = 2 * Math.PI * wheelradius;
        double distanceInTics = (distance/wheelcirc)* ticsPerRevolution;
        double RightTicsGoal = distanceInTics + rightMotor.getCurrentPosition();
        double LeftTicsGoal = distanceInTics + leftMotor.getCurrentPosition();
        double RTtG = RightTicsGoal - rightMotor.getCurrentPosition();
        double LTtG = LeftTicsGoal - leftMotor.getCurrentPosition();
        double eps = 5;
        leftMotor.setTargetPosition(((int)LeftTicsGoal));
        rightMotor.setTargetPosition((int)RightTicsGoal);
        leftMotor.setPower(power);
        rightMotor.setPower(power);
        while (Math.abs(RTtG)>eps && Math.abs(LTtG)>eps){
            RTtG = RightTicsGoal - rightMotor.getCurrentPosition();
            LTtG = LeftTicsGoal - leftMotor.getCurrentPosition();
        }
        leftMotor.setPower(0);
        rightMotor.setPower(0);
    }
    public void turn (double degrees, double power){
        double arcLength = 1.2*degrees*wheelSeperation*Math.PI/360;
        double wheelcirc = 2 * Math.PI * wheelradius;
        double arcLengthInTics = (arcLength/wheelcirc)* ticsPerRevolution;
        double RightTicsGoal = rightMotor.getCurrentPosition() + arcLengthInTics;
        double LeftTicsGoal = leftMotor.getCurrentPosition() - arcLengthInTics;
        double RTtG = RightTicsGoal - rightMotor.getCurrentPosition();
        double LTtG = LeftTicsGoal - leftMotor.getCurrentPosition();
        double eps = 5;
        leftMotor.setTargetPosition(((int)LeftTicsGoal));
        rightMotor.setTargetPosition((int)RightTicsGoal);
        leftMotor.setPower(power);
        rightMotor.setPower(power);
        while (Math.abs(RTtG)>eps && Math.abs(LTtG)>eps){
            RTtG = RightTicsGoal - rightMotor.getCurrentPosition();
            LTtG = LeftTicsGoal - leftMotor.getCurrentPosition();
        }
        leftMotor.setPower(0);
        rightMotor.setPower(0);
    }
}
