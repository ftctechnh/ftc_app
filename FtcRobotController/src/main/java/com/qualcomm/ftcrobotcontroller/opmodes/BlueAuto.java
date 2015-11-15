package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Eric on 11/15/2015.
 * Created for 2015-2016 FTC Season
 */
public class BlueAuto extends FTCCompetitionBase {
    enum Dstate {Stage1, Stage2, Stage3, Stage4}
    enum Astate {Stage1, Stage2, Stage3}

    Dstate dstate;
    Astate astate;

    BlueAuto(){
        dstate = Dstate.Stage1;
        astate = Astate.Stage1;
    }

    // TODO: 11/15/2015 ensure correct cycling
    @Override
    public void loop() {
       
    }
    
    void DriveStateMachine(){
        switch (dstate){
            // TODO: 11/15/2015 Drive Forward
            case Stage1:
                break;
            // TODO: 11/15/2015 Turn Left 45 degrees
            case Stage2:
                break;
            // TODO: 11/15/2015 Drive Backwards to Drive on Ramp
            case Stage3:
                break;
            // TODO: 11/15/2015 Winch up when ready 
            case Stage4:
                break;
        }
    }
    
    void ArmStateMachine(){
        switch (astate){

            // TODO: 11/15/2015 Extend Tape Measure 
            case Stage1:
                break;
            // TODO: 11/15/2015 Attach to bar 
            case Stage2:
                break;
            // TODO: 11/15/2015 Pull up 
            case Stage3:
                break;
        }
    }
}
