package com.walnuthillseagles.walnutlibrary;

import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.ArrayList;
//Comment
/**
 * Created by Yan Vologzhanin on 1/2/2016.
 */
public class DigMotor extends TeleMotor implements Drivable{
    //Fields
    //All available buttons... EVER (pls ;-;)
    public enum digValues{
        A1, B1, X1, Y1, BACK1, START1, GUIDE1, LEFT1,RIGHT1,
        DOWN1, UP1,LBUMP1,RBUMP1,LSTICK1, RSTICK1,
        A2, B2, X2, Y2, BACK2, START2, GUIDE2, LEFT2, RIGHT2,
        DOWN2, UP2,LBUMP2, RBUMP2, LSTICK2, RSTICK2;
    }
    private ArrayList<ButtonEvent> Buttons;
    //Constructors
    //Create a forward and backward button event
    private boolean isToggle;
    public DigMotor(DcMotor myMotor, String myName, boolean encoderCheck, boolean myToggle){
        //Create the motor
        super(myMotor, myName, encoderCheck);
        Buttons = new ArrayList<ButtonEvent>();
        isToggle=myToggle;
    }
    public void addButton(String button, double power){
        Buttons.add(new ButtonEvent(button, power, true));
    }
    //Teleop Methods
    public void operate(){
        ButtonEvent temp;
        for(int i = 0;i<Buttons.size();i++){
            temp = Buttons.get(i);
            if(VirtualGamepad.boolValues[temp.getPos()]){
                this.power(temp.getPow());
            }
            else if(!isToggle&&!VirtualGamepad.boolValues[temp.getPos()]){
                this.stop();
            }

        }
    }
    //Autonomous Methods in Walnut Motor (Called "power(int pow)")
}
