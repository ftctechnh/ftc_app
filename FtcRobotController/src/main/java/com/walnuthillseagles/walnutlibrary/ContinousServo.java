package com.walnuthillseagles.walnutlibrary;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Yan Vologzhanin on 2/8/2016.
 */
public class ContinousServo implements Drivable{
    private Servo servo;
    private int tablePos;
    private double deadZone;
    private int orientation;
    private String name;
    private double trueCenter;

    public ContinousServo(Servo myServo, String myName, double startPos,
                    String myControl, boolean reverse, double myDeadzone) {
        servo = myServo;
        name = myName;
        //Assign Table Position
        String nonCaseSensetive = myControl.toUpperCase();
        setTable(AnalogValues.valueOf(nonCaseSensetive));
        if(reverse)
            orientation = -1;
        else
            orientation = 1;
        deadZone = myDeadzone;
        //trueCenter = startPos;
        stop();
    }
    public void stop(){
        servo.setPosition(0.5);
    }
    private void setTable(AnalogValues myControl){
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

    public void setPower(double power) {
        servo.setPosition(power / 2 + 0.5);
    }
    public void operate(){
        double val = VirtualGamepad.doubleValues[tablePos];
        if(Math.abs(val)>deadZone )
            this.setPower(val*orientation);
        else if(Math.abs(val)>0.95)
            this.setPower(0.95 * orientation);
        else
            this.stop();
    }
}
