package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Eric on 11/15/2015.
 * Created for 2015-2016 FTC Season
 */
public class BlueAuto extends FTCCompetitionBase {
    enum Dstate {Stage1, Stage2, Stage3, Stage4}
    enum Astate {Stage1, Stage2, Stage3, Stage4}

    Dstate dstate;
    Astate astate;
    boolean EncoderTrigger;

    BlueAuto(){
        dstate = Dstate.Stage1;
        astate = Astate.Stage1;
    }

    // TODO: 11/15/2015 ensure correct cycling
    @Override
    public void loop() {
        DriveStateMachine();
        ArmStateMachine();
    }
    
    void DriveStateMachine(){
        switch (dstate){
            // TODO: 11/15/2015 Drive Forward
            case Stage1:
                if(AutonDrive(2000, 0.75D, 0.0D)){
                    dstate = Dstate.Stage2;
                }
                break;
            // TODO: 11/15/2015 Turn Left 45 degrees
            case Stage2:
                if(AutonDrive(500, 0.0D, 0.75D)){
                    dstate = Dstate.Stage3;
                }
                break;
            // TODO: 11/15/2015 Drive Backwards to Drive on Ramp
            case Stage3:
                if (AutonDrive(750, -0.75D, 0.0D)){
                    dstate = Dstate.Stage4;
                    astate = Astate.Stage2;
                }
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
                resetStartTime();
                break;
            // TODO: 11/15/2015 Attach to bar 
            case Stage2:
                if(getRuntime() > 500000){
                    //Stop Tape Measure and start pushing down
                }
                else{
                    // run tape measure
                }
                break;
            // TODO: 11/15/2015 Push down the tape measure
            case Stage3:
                // Pull up tape measure

                break;
            case Stage4:
                // stop tape measure?
                break;
        }
    }
}
