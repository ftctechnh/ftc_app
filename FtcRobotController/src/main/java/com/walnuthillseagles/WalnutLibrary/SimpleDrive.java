package com.walnuthillseagles.WalnutLibrary;

import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.ArrayList;

public class SimpleDrive {
    private ArrayList<IncMotor> rightMotors;
    private ArrayList<IncMotor>  leftMotors;

    //Constructors
    //Constructor for IncSpinners
    public SimpleDrive(IncMotor myLeft, IncMotor myRight)
    {
        rightMotors = new ArrayList<IncMotor>();
        leftMotors = new ArrayList<IncMotor>();
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
        this(new IncMotor(myLeft, myLeftName, checkLeftEncoders,
                        myLeftControl, leftReverse, myLeftDeadzone),
                new IncMotor(myRight, myRightName, checkRightEncoders,
                        myRightControl, rightReverse, myRightDeadzone));

    }
    //Getters and Setters
    public ArrayList<IncMotor> getLeftMotors(){
        return leftMotors;
    }
    public ArrayList<IncMotor> getRightMotors(){
        return rightMotors;
    }
    //Will return null if it cannot find motor
    public IncMotor getMotor(String name){
        IncMotor ret;
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

    public void addLeftMotor(IncMotor myLeft){
        leftMotors.add(myLeft);
    }
    public void addRightMotor(IncMotor myRight){
        rightMotors.add(myRight);
    }
    //Teleop
    public void operateDrive(){
        IncMotor ret;
        for(int i=0;i<rightMotors.size();i++){
            rightMotors.get(i).operate();
        }
        for(int i=0;i<leftMotors.size();i++){
            leftMotors.get(i).operate();
        }
    }
    //Autonomous
    protected void operateSpecificDrive(ArrayList<IncMotor> myMotors, double pow){
        for(int i=0;i<myMotors.size();i++){
            myMotors.get(i).powerMotor(pow);
        }
    }

}
