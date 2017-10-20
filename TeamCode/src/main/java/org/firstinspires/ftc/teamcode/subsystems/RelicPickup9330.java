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

    public RelicPickup9330(Hardware9330 robotMap){
        hwMap = robotMap;
        isHandClosed = false;
        isWristUp = false;
    }

    public void openHand(){
        if(isHandClosed){
            hwMap.relicHandServo.setPosition(OPEN_POS);
        }
    }

    public void closeHand(){
        if(!isHandClosed){
            hwMap.relicHandServo.setPosition(CLOSED_POS);
        }
    }

    public void raiseWrist(){
        if(!isHandClosed){
            hwMap.relicWristServo.setPosition(OPEN_POS);
        }
    }

    public void lowerWrist(){
        if(isHandClosed){
            hwMap.relicWristServo.setPosition(CLOSED_POS);
        }
    }
}
