package org.firstinspires.ftc.teamcode.subsystems;
import org.firstinspires.ftc.teamcode.Hardware9330;

/**
 * Created by robot on 9/25/2017.
 */

public class Grabber9330 {

    private boolean grabberEngaged;

    //   Declaring Set positions for servo
    static final Double ENGAGED_POS = 0.8;
    static final Double RELEASE_POS = 0.25;
    //   accessing Hardware9330
    private Hardware9330 hwMap = null;
    //Constructor for brake class
    public Grabber9330 (Hardware9330 robotMap){

        hwMap = robotMap;
        hwMap.grabber.setPosition(RELEASE_POS);
        grabberEngaged = false;
    }
    //  Engaging the brake
    public void closeArms(){
        if(!grabberEngaged){
            grabberEngaged = true;
            hwMap.grabber.setPosition(ENGAGED_POS);
        }
    }
    // Releasing the brake
    public void openArms(){
        if(grabberEngaged){
            grabberEngaged = false;
            hwMap.grabber.setPosition(RELEASE_POS);
        }
    }

    // query brake state
    public boolean isBrakeEngaged() {
        return grabberEngaged;
    }
}
