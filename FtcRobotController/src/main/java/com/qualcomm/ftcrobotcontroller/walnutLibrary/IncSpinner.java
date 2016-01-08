package com.qualcomm.ftcrobotcontroller.walnutLibrary;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Yan Vologzhanin on 1/2/2016.
 */
public class IncSpinner extends WalnutMotor {
    public enum analogValues{
        LEFTX1(),LEFTY1(),RIGHTX1(),RIGHTY1(),LEFTZ1(),RIGHTZ1(),
                LEFTX2(),LEFTY2(),RIGHTX2(),RIGHTY2(),LEFTZ2(),RIGHTZ2();
        analogValues(){}
    }
    private analogValues tableConstant;
    private int tablePos;
    //Used if you need to reverse orientation
    private double orientation;
    private double deadZone;

    //Constructor
    public IncSpinner(DcMotor myMotor, String myName, boolean checkEncoders,
                      String myControl, boolean reverse, double myDeadzone) {
        super(myMotor,myName, checkEncoders);
        //Assign Table Position
        String nonCaseSensetive = myControl.toUpperCase();
        //@TODO Do I Need this field?
        tableConstant = analogValues.valueOf(nonCaseSensetive);
        setTable(analogValues.valueOf(nonCaseSensetive));
        if(reverse)
            orientation = -1;
        else
            orientation = 1;
        deadZone = myDeadzone;
    }

    //Extra Getters and Setters
    public String getTablePos(){
        //@DEBUG this != NULL;
        switch(tablePos){
            case(0):
                return "LEFTX1";
            case(1):
                return "LEFTY1";
            case(2):
                return "RIGHTX1";
            case(3):
                return "RIGHTY1";
            case(4):
                return "LEFTZ1";
            case(5):
                return "RIGHTZ1";
            case(6):
                return "LEFTX2";
            case(7):
                return "LEFTY2";
            case(8):
                return "RIGHTX2";
            case(9):
                return "RIGHTY2";
            case(10):
                return "LEFTZ2";
            case(11):
                return "RIGHTZ2";
            default:
                return "RIGHTZ2";
        }
    }
    private void setTable(analogValues myControl){
        switch(myControl){
            case LEFTX1:
                tablePos = 0;
                break;
            case LEFTY1:
                tablePos = 1;
                break;
            case RIGHTX1:
                tablePos = 2;
                break;
            case RIGHTY1:
                tablePos = 3;
                break;
            case LEFTZ1:
                tablePos = 4;
                break;
            case RIGHTZ1:
                tablePos = 5;
                break;
            case LEFTX2:
                tablePos = 6;
                break;
            case LEFTY2:
                tablePos = 7;
                break;
            case RIGHTX2:
                tablePos = 8;
                break;
            case RIGHTY2:
                tablePos = 9;
                break;
            case LEFTZ2:
                tablePos = 10;
                break;
            case RIGHTZ2:
                tablePos = 11;
                break;
            default:
                tablePos = 0;
        }
    }
    public void setTablePos(String myControl){
        String nonCaseSensetive = myControl.toUpperCase();
        setTable(analogValues.valueOf(nonCaseSensetive));
    }
    public void directSetTablePos(int n){
        if(n>=0&&n<=11)
            tablePos = n;
        else{
            tablePos =0;
        }

    }
    //Teleop Methods
    //@Override
    public void operate(){
        double val = WalnutMotor.GamepadUpdater.doubleValues[tablePos];
        if(Math.abs(val)>deadZone)
            this.getMotor().setPower(val*orientation);
        else
            this.stopMotor();
    }
    //Autonomous Methods in Walnut Motor (Called "powerMotor(int pow)")
}
