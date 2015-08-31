package com.fellowshipoftheloosescrews.testing;

import com.qualcomm.ftcrobotcontroller.opmodes.LinearIrExample;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by FTC7123A on 8/31/2015.
 */
public class HardwareCycleTesting extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();

        double lastTime = time;
        double currentTime;

        while(true)
        {
            currentTime = time;
            double deltaTime = currentTime - lastTime;

            telemetry.addData("time", deltaTime);
            waitOneHardwareCycle();
            lastTime = currentTime;
        }
    }
}
