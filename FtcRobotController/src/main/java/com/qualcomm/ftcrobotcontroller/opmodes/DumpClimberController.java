package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Christopher on 12/29/2015.
 */
public class DumpClimberController
{
    ServoManager8737 dumperMgr;
    boolean dumping = false;

    public DumpClimberController(Servo dumper)
    {
        dumperMgr = new ServoManager8737(dumper);
    }

    //initializes dumper servo to home position
    public void dumperInit()
    {
        dumperMgr.setPosition(1, 0);
    }

    // starts dump action
    public void startDump()
    {
        dumping=true;
        dumperMgr.setPosition(0, 1.5);    //moves basket to release position in 1.5 seconds when startDump is called
    }

    // called within loop to perform any dump needs
    public void check()
    {
        dumperMgr.check();
        if (dumping && ! dumperMgr.isBusy())
        {
            //sets dumping to false, because the basket will have been set to home position and action is complete
            dumping = false;
            dumperMgr.setPosition(1, 0);    //sets basket to home position immediately
        }
    }
}
