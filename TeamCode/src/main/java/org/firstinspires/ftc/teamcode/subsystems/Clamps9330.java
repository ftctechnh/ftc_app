package org.firstinspires.ftc.teamcode.subsystems;
import org.firstinspires.ftc.teamcode.Hardware9330;

/**
 * Created by robot on 9/25/2017.
 */

public class Clamps9330 {

    private boolean lowClampEngaged = true;
    private boolean highClampEngaged = true;
    private boolean highClampPivoted;

    //   Declaring Set positions for servo
    static final Double ENGAGED_POS = 0.1;
    static final Double RELEASE_POS = 0.5;
    //   Declaring set positions of the pivot
    static final Double PIVOTED_POS = 0.8;
    static final Double STRAIGHT_POS = 0.25;
    //   accessing Hardware9330
    private Hardware9330 hwMap = null;
    //Constructor for brake class
    public Clamps9330(Hardware9330 robotMap){
        hwMap = robotMap;
        hwMap.lowGlyphClamp.setPosition(ENGAGED_POS);
        hwMap.highGlyphClamp.setPosition(ENGAGED_POS);
    }
    //  Engaging the low clamp
    public void toggleLowClamp(){
        if(lowClampEngaged == false){
            lowClampEngaged = true;
            hwMap.lowGlyphClamp.setPosition(ENGAGED_POS);
        } else {
            lowClampEngaged = false;
            hwMap.lowGlyphClamp.setPosition(RELEASE_POS);
        }
    }

    public void toggleHighClamp(){
        if(highClampEngaged == false){
            highClampEngaged = true;
            hwMap.highGlyphClamp.setPosition(ENGAGED_POS);
        } else {
            highClampEngaged = false;
            hwMap.highGlyphClamp.setPosition(RELEASE_POS);
        }
    }

    public void closeLowClamp(){
        if(!lowClampEngaged){
            lowClampEngaged = true;
            hwMap.lowGlyphClamp.setPosition(ENGAGED_POS);
        }
    }
    // Releasing the low clamp
    public void openLowClamp(){
        if(lowClampEngaged){
            lowClampEngaged = false;
            hwMap.lowGlyphClamp.setPosition(RELEASE_POS);
        }
    }
    //Engaging the high clamp
    public void closeHighClamp(){
        if(!highClampEngaged){
            highClampEngaged = true;
            hwMap.highGlyphClamp.setPosition(ENGAGED_POS);
        }
    }
    // Releasing the high clamp
    public void openHighClamp(){
        if(highClampEngaged){
            highClampEngaged = false;
            hwMap.highGlyphClamp.setPosition(RELEASE_POS);
        }
    }
    /*Pivots the high clamp

    public void toggleClampPivot(){
        if(highClampPivoted == false){
            highClampPivoted = true;
            hwMap.glyphPivotServo.setPosition(PIVOTED_POS);
        } else{
            highClampPivoted = false;
            hwMap.glyphPivotServo.setPosition(STRAIGHT_POS);
        }
    }

    public void pivotHighClamp(){
        if(!highClampPivoted){
            highClampPivoted = true;
            hwMap.glyphPivotServo.setPosition(PIVOTED_POS);
        }
    }
    //Straightens the high clamp
    public void straightenHighClamp(){
        if(highClampPivoted){
            highClampPivoted = false;
            hwMap.glyphPivotServo.setPosition(STRAIGHT_POS);
        }
    }
    */

    // query brake state
    public boolean islowClampEngaged() {
        return lowClampEngaged;
    }
}
