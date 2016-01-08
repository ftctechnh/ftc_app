package com.qualcomm.ftcrobotcontroller.walnutLibrary;

import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.ArrayList;

public class SimpleDrive {
    private ArrayList<IncSpinner> rightMotors;
    private ArrayList<IncSpinner>  leftMotors;

    //Constructors
    //Constructor for IncSpinners
    public SimpleDrive(IncSpinner myLeft, IncSpinner myRight)
    {
        rightMotors = new ArrayList<IncSpinner>();
        leftMotors = new ArrayList<IncSpinner>();
        rightMotors.add(myRight);
        leftMotors.add(myLeft);
    }
    //Constructor for motors
    public SimpleDrive(DcMotor myLeft, String myLeftName, boolean checkLeftEncoders,
                       String myLeftControl, boolean leftReverse,
                       double myLeftDeadzone,
                       DcMotor myRight, String myRightName, boolean checkRightEncoders,
                       String myRightControl, boolean rightReverse,
                       double myRightDeadzone)
    {
        //Create new IncSpinners and assign to other constructor
        this(new IncSpinner(myLeft, myLeftName, checkLeftEncoders,
                        myLeftControl, leftReverse, myLeftDeadzone),
                new IncSpinner(myRight, myRightName, checkRightEncoders,
                        myRightControl, rightReverse, myRightDeadzone));

    }
    //Getters and Setters
    public ArrayList<IncSpinner> getLeftMotors(){
        return leftMotors;
    }
    public ArrayList<IncSpinner> getRightMotors(){
        return rightMotors;
    }
    //Will return null if it cannot find motor
    public IncSpinner getMotor(String name){
        IncSpinner ret;
        for(int i=0;i<rightMotors.size();i++){
            ret = rightMotors.get(i);
            if(ret.toString().equals(name))
                return ret;
        }
        for(int i=0;i<leftMotors.size();i++){
            ret = leftMotors.get(i);
            if(ret.toString().equals(name))
                return ret;
        }
        //All else has failed. Cry ;-;
        return null;
    }

    public void addLeftMotor(IncSpinner myLeft){
        leftMotors.add(myLeft);
    }
    public void addRightMotor(IncSpinner myRight){
        rightMotors.add(myRight);
    }
    //Teleop
    public void operateDrive(){
        IncSpinner ret;
        for(int i=0;i<rightMotors.size();i++){
            rightMotors.get(i).operate();
        }
        for(int i=0;i<leftMotors.size();i++){
            leftMotors.get(i).operate();
        }
    }
    //Autonomous
    protected void operateSpecificDrive(ArrayList<IncSpinner> myMotors, double pow){
        for(int i=0;i<myMotors.size();i++){
            myMotors.get(i).powerMotor(pow);
        }
    }

}
