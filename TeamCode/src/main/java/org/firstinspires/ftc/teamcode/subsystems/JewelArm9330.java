package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.Hardware9330;

/**
 * Created by robot on 10/27/2017.
 */

public class JewelArm9330 {

    Hardware9330 hwMap;
    //Possible Positions for Servo
    private final double UP_POS = 0.6;
    private final double OUT_POS = 1.0;
    private boolean isArmUp;

    public JewelArm9330(Hardware9330 robotMap){
        hwMap = robotMap;
        hwMap.crystalArm.setPosition(UP_POS);
        isArmUp = true;
    }

    public void toggleArmServo(){
        if(isArmUp == true) lowerArmServo();
        else raiseArmServo();
    }

    public void raiseArmServo() {
        if(isArmUp == false){
            hwMap.crystalArm.setPosition(UP_POS);
            isArmUp = true;
        }
    }

    public void lowerArmServo() {
        if(isArmUp == true){
            hwMap.crystalArm.setPosition(OUT_POS);
            isArmUp = false;
        }
    }
}
