package com.qualcomm.ftcrobotcontroller.opmodes;


/**
 * Created by Peter on 2/4/2016.
 */
public class BlueClimberDropWithTime extends AutonomousTime_T
{
        @Override
        public void runOpMode() throws InterruptedException
            {
                initialize();
                waitForStart();
                Forwards(24, true);
                turnOnCenter(48, false);
                Forwards((float) 76.25, true);
                turnOnCenter(48, false);
                Forwards(15, true);
                //try to do the flips
            }

}
