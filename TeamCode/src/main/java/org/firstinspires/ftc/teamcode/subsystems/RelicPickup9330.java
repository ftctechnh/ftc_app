package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.Hardware9330;

/**
 * Created by robot on 10/13/2017.
 */

public class RelicPickup9330 {
    //We will need to add 2 extra servos
    private boolean isHandClosed;
    private boolean isWristUp;
    private Hardware9330 hwMap;


    static final Double CLOSED_POS = 0.8;
    static final Double OPEN_POS = 0.25;

    static final Double LOW_POS = 0.8;
    static final Double HIGH_POS = 0.25;

    public RelicPickup9330(Hardware9330 robotMap){
        hwMap = robotMap;
        isHandClosed = false;
        isWristUp = false;
    }

    public void openHand(){
        if(isHandClosed){
            isHandClosed = false;
           // hwMap.relicHandServo.setPosition(OPEN_POS);
        }
    }

    public void closeHand(){
        if(!isHandClosed){
            isHandClosed = true;
           // hwMap.relicHandServo.setPosition(CLOSED_POS);
        }
    }

    public void raiseWrist(){
        if(!isWristUp){
            isWristUp = true;
           // hwMap.relicWristServo.setPosition(HIGH_POS);
        }
    }

    public void lowerWrist(){
        if(isWristUp){
            isWristUp = false;
           // hwMap.relicWristServo.setPosition(LOW_POS);

        }
    }

    public void toggleHand(){
        if(isHandClosed == false) closeHand();
        else openHand();
    }

    public void toggleWrist(){
        if(isWristUp == false) raiseWrist();
        else lowerWrist();
    }
}
