package com.walnuthillseagles.walnutlibrary;

import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;

/**
 * Created by Yan Vologzhanin on 1/4/2016.
 */
public class WalnutServo implements Drivable{
    private Servo servo;
    private ArrayList<ButtonEvent> buttons;

    private double startPos;


    public WalnutServo(Servo myServo, double myStartPos,
                       String daButton, double myPos, boolean toggle){
        Servo servo = myServo;
        buttons = new ArrayList<ButtonEvent>();
        ButtonEvent firstButton = new ButtonEvent(daButton,myPos,toggle);
        buttons.add(firstButton);
        startPos = myStartPos;
        //Reset Servo
        stop();

    }
    public void addButton(String daButton, double myPos, boolean toggle){
        ButtonEvent newButton = new ButtonEvent(daButton,myPos,toggle);
        buttons.add(newButton);
    }
    public void operate(){
        ButtonEvent temp;
        for(int i = 0;i<buttons.size();i++){
            temp = buttons.get(i);
            if(WalnutMotor.GamepadUpdater.boolValues[temp.getPos()]){
                //@TODO Change method name here to make more sense
                servo.setPosition(temp.getPow());
            }
            else if(!temp.checkToggle()&&!WalnutMotor.GamepadUpdater.boolValues[temp.getPos()]){
                this.stop();
            }

        }
    }
    public void stop(){
        servo.setPosition(startPos);
    }
}
