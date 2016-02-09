package com.walnuthillseagles.walnutlibrary;

import java.util.ArrayList;

/**
 * Created by Yan Vologzhanin on 1/4/2016.
 */

public class DistanceDrive {
    private ArrayList<DistanceMotor> leftDrive;
    private ArrayList<DistanceMotor> rightDrive;
    private double robotWidth;

    //Just cause
    public static final int REVERSEORIENTATION = -1;

    public static final double MOTORADJUSTMENTPOW = 0.8;
    //Constructor used for first two motors
    public DistanceDrive(DistanceMotor myLeft, DistanceMotor myRight, double width){
        //Initilize ArrayLists
        leftDrive = new ArrayList<DistanceMotor>();
        rightDrive = new ArrayList<DistanceMotor>();
        //Add motors to these lists
        leftDrive.add(myLeft);
        rightDrive.add(myRight);
        //Initilize other variables
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
    public void linearDrive(double inches, double pow){
        //Tell motors to start
        operateMotors(leftDrive,inches,pow);
        operateMotors(rightDrive, inches, pow);
    }
    //Left is positive, right is negetive
    public void tankTurn(double degrees, double pow){
        double factor = 360/degrees;
        double distance = (Math.PI * robotWidth)/factor;
        //One is inverted to create a tank turn
        operateMotors(leftDrive,distance,pow);
        operateMotors(rightDrive, distance * REVERSEORIENTATION, pow * REVERSEORIENTATION);
    }
    public void stop(){
        for(int i=0;i<leftDrive.size();i++)
            leftDrive.get(i).fullStop();
        for(int i=0;i<rightDrive.size();i++)
            rightDrive.get(i).fullStop();
    }
    //Timers
    public void waitForCompletion() throws InterruptedException{
        for(int i=0;i<leftDrive.size();i++)
            leftDrive.get(i).waitForCompletion();
        for(int i=0;i<rightDrive.size();i++)
            rightDrive.get(i).waitForCompletion();
    }
    //Helpper Private methods
    private void operateMotors(ArrayList<DistanceMotor> myMotors, double distance, double pow){
        for(int i=0;i<myMotors.size();i++){
            myMotors.get(i).operate(distance,pow);
        }
    }
}
