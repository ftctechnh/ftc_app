package com.walnuthillseagles.walnutlibrary;

import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;

/**
 * Created by Yan Vologzhanin on 1/4/2016.
 */
public class WalnutServo implements Drivable, Auto {
    private Servo servo;
    private ArrayList<ButtonEvent> buttons;
    private double startPos;
    private boolean isToggle;

    public WalnutServo(Servo myServo, double myStartPos, boolean myToggle){
        servo = myServo;
        startPos = myStartPos;
        buttons = new ArrayList<ButtonEvent>();
        isToggle=myToggle;
        //Reset Servo
        stop();
    }

    public void addButton(String daButton, double myPos){
        ButtonEvent newButton = new ButtonEvent(daButton, myPos, true);
        buttons.add(newButton);
    }
    public void operate(){
        ButtonEvent temp;
        for(int i = 0;i<buttons.size();i++){
            temp = buttons.get(i);
            if(VirtualGamepad.boolValues[temp.getPos()]){
                //@TODO Change method name here to make more sense
                servo.setPosition(temp.getPow());
            }
            else if(!isToggle&&!VirtualGamepad.boolValues[temp.getPos()]){
                this.stop();
            }
        }
    }
    public void stop(){
        servo.setPosition(startPos);
    }
    //Auto Stuff
    public void operate(double pos){
        servo.setPosition(pos);
    }
    public Servo getServo(){
        return servo;
    }
    //@TODO Figure out how to make this more useful.
    public void waitForCompletion() throws InterruptedException{
        return;
    }
}
