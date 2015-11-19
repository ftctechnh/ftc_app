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
    boolean EncoderTrigger, HookRelease;

    BlueAuto(){
        dstate = Dstate.Stage1;
        astate = Astate.Stage1;
        HookRelease = false;
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
                    //dstate = Dstate.Stage4;
                    astate = Astate.Stage2;
                }
                break;
            // TODO: 11/15/2015 Winch up when ready 
            case Stage4:
                this.pullUpMountain(true);
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
                    //Stop Tape Measure, start pushing down
                    astate = Astate.Stage3;
                    this.PullupHook(false, false);
                    resetStartTime();
                }
                else{
                    // run tape measure
                    this.PullupHook(true, false);
                }

                // TODO: 11/18/2015 Operate Drop hook
                if(!HookRelease){
                    this.AutonFlag(true, false);
                    HookRelease = true;
                }

                break;
            // TODO: 11/15/2015 Push down the tape measure
            case Stage3:
                // start pushing down tape
                if (getRuntime() > 5000){
                    // Stop pushing down tape
                    this.setArmTilt(0);
                    dstate = Dstate.Stage4;
                }
                else{
                    // Start pushing down tape
                    this.setArmTilt(-1.0);
                }
                break;
            case Stage4:
                // stop tape measure?
                break;
        }
    }
}
