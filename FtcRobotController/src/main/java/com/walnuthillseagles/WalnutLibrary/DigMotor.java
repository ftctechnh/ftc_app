package com.walnutHillsEagles.WalnutLibrary;

import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.ArrayList;

/**
 * Created by Yan Vologzhanin on 1/2/2016.
 */
public class DigMotor extends WalnutMotor implements Drivable{
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
    public DigMotor(DcMotor myMotor, String myName, boolean encoderCheck,
                    String button, double power, boolean toggle){
        //Create the motor
        super(myMotor, myName, encoderCheck);
        //Initilize and add button events
        ButtonEvent forward = new ButtonEvent(button, power, toggle);
        //Create list of buttons and add our first one
        Buttons = new ArrayList<ButtonEvent>();
        Buttons.add(forward);
    }
    public void addButton(String button, double power, boolean toggle){
        Buttons.add(new ButtonEvent(button, power, toggle));
    }
    //Teleop Methods
    public void operate(){
        ButtonEvent temp;
        for(int i = 0;i<Buttons.size();i++){
            temp = Buttons.get(i);
            if(WalnutMotor.GamepadUpdater.boolValues[temp.getPos()]){
                this.power(temp.getPow());
            }
            else if(!temp.checkToggle()&&!WalnutMotor.GamepadUpdater.boolValues[temp.getPos()]){
                this.stop();
            }

        }
    }
    //Autonomous Methods in Walnut Motor (Called "power(int pow)")
}
