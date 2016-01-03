package com.qualcomm.ftcrobotcontroller.walnutLibrary;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.ftcrobotcontroller.walnutLibrary.walnutMotor;

import java.util.ArrayList;

public class Drive{
    private ArrayList<IncSpinner> rightMotors;
    private ArrayList<IncSpinner>  leftMotors;

    public enum AutoType{
        TIME, DISTANCE, ROTATIONS, SENSORS;
    }
    private AutoType mode;

    //Diameter of wheel or tooth-to-tooth distance of tread sprocket
    //Please measure in Inches, cause we are silly Americans
    private float diameter;
    //Gear Ratio between encoder(not motor) and driven wheel
    private float gearRatio;

    //Constructors
    //TeleOp Constructor
    public Drive(IncSpinner myLeft, IncSpinner myRight)
    {
        rightMotors.add(myRight);
        leftMotors.add(myLeft);
    }
    //Autonomous Constructor
    public Drive(IncSpinner myLeft, IncSpinner myRight,
                 float myDiameter, float myGearRatio, AutoType myMode){
        rightMotors.add(myRight);
        leftMotors.add(myLeft);
        mode = myMode;
        diameter = myDiameter;
        gearRatio = myGearRatio;
    }
    //Getters and Setters
    public ArrayList<IncSpinner> getLeftMotors(){
        return leftMotors;
    }
    public ArrayList<IncSpinner> getRightMotors(){
        return rightMotors;
    }
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
    public float getDiameter(){
        return diameter;
    }
    public float getGearRatio(){
        return gearRatio;
    }

    public void addLeftMotor(IncSpinner myLeft){
        leftMotors.add(myLeft);
    }
    public void addRightMotor(IncSpinner myRight){
        rightMotors.add(myRight);
    }
    public void setDiameter(float newDiameter){
        diameter = newDiameter;
    }
    public void setGearRatio(float newGearRatio){
        gearRatio = newGearRatio;
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
    //NOTE about length: Length changes based on what mode this is
    //TIME - Seconds, ROTATIONS - Num rotations, DISTANCE - Inches
    public void linearDrive(boolean isForward, float length){

    }
    public void tankTurn(float degrees){

    }
    public void pointTurn(){

    }
    public void arcadeTurn(){

    }

}