package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.Hardware9330;

/**
 * Created by robot on 10/27/2017.
 */

public class BBoopJR9330 {

    Hardware9330 hwMap;
    //Possible Positions for Servo
    private final float UP_POS = 1;
    private final float OUT_POS = 0;
    private boolean isArmUp;

    public BBoopJR9330(Hardware9330 robotMap){
        hwMap = robotMap;
        hwMap.BBoopJR.setPosition(UP_POS);
        isArmUp = true;
    }

    public void toggleArmServo(){
        if(isArmUp == true){
            hwMap.BBoopJR.setPosition(OUT_POS);
            isArmUp = false;
        }else{
            hwMap.BBoopJR.setPosition(UP_POS);

        }
    }
}
