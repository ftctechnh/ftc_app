package com.qualcomm.ftcrobotcontroller.opmodes.ResQ_2016;

/**
 * Created by Peter&Kaitlin on 1/15/2016.
 */
public class LinearOpTest extends AutonomousTime_T
{

    @Override
    public void runOpMode() throws InterruptedException
    {
        initialize();
        waitForStart();
        Forwards(24, true);
        turnOnCenter(48, true);
        Forwards((float) 76.25, true);
        turnOnCenter(48, true);
        Forwards(15, true);
        //Forwards(6,true);
        //turnOnCenter(90, false);
    }
}
