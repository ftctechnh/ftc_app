package com.walnutHillsEagles.WalnutLibrary;

import java.util.ArrayList;

/**
 * Created by Yan Vologzhanin on 1/4/2016.
 */
public class DistanceDrive {
    private ArrayList<DistanceMotor> leftDrive;
    private ArrayList<DistanceMotor> rightDrive;
    private double robotWidth;

    public static final double MOTORADJUSTMENTPOW = 0.8;
    //Constructor used for first two motors
    public DistanceDrive(DistanceMotor myLeft, DistanceMotor myRight, double width){
        leftDrive = new ArrayList<DistanceMotor>();
        rightDrive = new ArrayList<DistanceMotor>();
        leftDrive.add(myLeft);
        rightDrive.add(myRight);
        robotWidth = width;
    }
    //Add additional motors
    public void addLeft(DistanceMotor myLeft){
        leftDrive.add(myLeft);
    }
    public void addRight(DistanceMotor myRight){
        rightDrive.add(myRight);
    }
    //Autonomous Methods
    public void linearDrive(double inches, double pow ,boolean isForward){
        //Initilize with assumption of going backwards, then check direction
        double orientation = -1;
        if(isForward){
            orientation = 1;
        }
        //do Necessary calculation
        double distance = inches*orientation;
        //Tell motors to start
        operateMotors(leftDrive,distance,pow);
        operateMotors(rightDrive,distance,pow);
    }
    //Left is positive, right is negetive
    public void tankTurn(double degrees, double pow){
        double factor = 360/degrees;
        double distance = (Math.PI * robotWidth)/factor;
        //One is inverted to create a tank turn
        operateMotors(leftDrive,distance,pow);
        operateMotors(rightDrive,distance*-1,pow*-1);

    }
    //Used to activate motors
    private void operateMotors(ArrayList<DistanceMotor> myMotors, double distance, double pow){
        for(int i=0;i<myMotors.size();i++){
            myMotors.get(i).operate(distance,pow);
        }
    }
}
