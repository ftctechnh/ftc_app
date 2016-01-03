package com.qualcomm.ftcrobotcontroller.walnutLibrary;

import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.ArrayList;

/**
 * Created by Yan Vologzhanin on 1/2/2016.
 */
public class DigSpinner extends walnutMotor {
    //Fields
    //All available buttons... EVER (pls ;-;)
    public enum digValues{
        A1, B1, X1, Y1, BACK1, START1, GUIDE1, LEFT1,RIGHT1,
        DOWN1, UP1,LBUMP1,RBUMP1,LSTICK1, RSTICK1,
        A2, B2, X2, Y2, BACK2, START2, GUIDE2, LEFT2, RIGHT2,
        DOWN2, UP2,LBUMP2, RBUMP2, LSTICK2, RSTICK2;
    }
    //Used to program buttons
    public class buttonEvent{
        private int tablePos;
        private float pow;
        private boolean isSticky;
        //Construcor
        public buttonEvent(String daButton, float myPow){
            //Get table position from noncase sensitive button name
            tablePos = findTablePos(digValues.valueOf(daButton.toUpperCase()));
            pow = myPow;
            //Congradulatiionsns, you found a secret :D
            isSticky = true;
        }
        public buttonEvent(String daButton, float myPow, boolean sticky){
            //Get table position from noncase sensitive button name
            tablePos = findTablePos(digValues.valueOf(daButton.toUpperCase()));
            pow = myPow;
            isSticky = sticky;
        }
        //Getters
        public float getPow(){
            return pow;
        }
        public boolean checkSticky(){
            return isSticky;
        }
        public int getPos(){
            return tablePos;
        }
        public void setSticky(boolean sticky){
            isSticky = sticky;
        }
        //Utility Methods
        private int findTablePos(digValues daButton){
            switch(daButton){
                case A1:
                    return 0;
                case B1:
                    return 1;
                case X1:
                    return 2;
                case Y1:
                    return 3;
                case BACK1:
                    return 4;
                case START1:
                    return 5;
                case GUIDE1:
                    return 6;
                case LEFT1:
                    return 7;
                case RIGHT1:
                    return 8;
                case DOWN1:
                    return 9;
                case UP1:
                    return 10;
                case LBUMP1:
                    return 11;
                case RBUMP1:
                    return 12;
                case LSTICK1:
                    return 13;
                case RSTICK1:
                    return 14;
                case A2:
                    return 15;
                case B2:
                    return 16;
                case X2:
                    return 17;
                case Y2:
                    return 18;
                case BACK2:
                    return 19;
                case START2:
                    return 20;
                case GUIDE2:
                    return 21;
                case LEFT2:
                    return 22;
                case RIGHT2:
                    return 23;
                case DOWN2:
                    return 24;
                case UP2:
                    return 25;
                case LBUMP2:
                    return 26;
                case RBUMP2:
                    return 27;
                case LSTICK2:
                    return 28;
                case RSTICK2:
                    return 29;
                default:
                    throw new IndexOutOfBoundsException();
            }
        }
        public int findTable(String daButton){
            return findTablePos(digValues.valueOf(daButton.toUpperCase()));
        }

    }
    private ArrayList<buttonEvent> Buttons;
    //Constructors
    //Create a forward and backward button event
    public DigSpinner(DcMotor myMotor, String myName, boolean encoderCheck,
                      String forwardButton, float forwardPower, boolean forwardSticky,
                      String backwardButton, float backwardPower, boolean backwardSticky){
        //Create the motor
        super(myMotor, myName, encoderCheck);
        //Initilize and add button events
        buttonEvent forward =
                new buttonEvent(forwardButton, forwardPower, forwardSticky);
        buttonEvent backward =
                new buttonEvent(backwardButton, backwardPower, backwardSticky);

        Buttons = new ArrayList<buttonEvent>();
        Buttons.add(forward);
        Buttons.add(backward);
    }
    //Do all of the above, but make a "stop" button event
    //(only useful for non-sticky buttons)
    public DigSpinner(DcMotor myMotor, String myName, boolean encoderCheck,
                      String forwardButton, float forwardPower, boolean forwardSticky,
                      String backwardButton, float backwardPower, boolean backwardSticky,
                      String stopButton, float stopPower, boolean stopSticky){
        //Call original constructor to do most work
        this(myMotor, myName,encoderCheck,
        forwardButton,forwardPower, forwardSticky,
        backwardButton, backwardPower, backwardSticky);

        buttonEvent stop =
                new buttonEvent(stopButton, stopPower, stopSticky);
        Buttons.add(stop);
    }
    //Same as the ones above, but all power is the same so it is declared once


    //Getters and Setters
    //Changers

    //Teleop Methods
    public void operate(){
        buttonEvent temp;
        for(int i = 0;i<Buttons.size();i++){
            temp = Buttons.get(i);
            if(walnutMotor.GamepadUpdater.boolValues[temp.getPos()]){
                this.powerMotor(temp.getPow());
            }
            if(!temp.checkSticky()&&!walnutMotor.GamepadUpdater.boolValues[temp.getPos()]){
                this.stopMotor();
            }

        }
    }
    //Autonomous Methods in Walnut Motor (Called "powerMotor(int pow)")
}
