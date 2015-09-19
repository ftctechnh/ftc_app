package com.fellowshipoftheloosescrews.testing;

import android.util.Log;

import com.fellowshipoftheloosescrews.utilities.opmode.FellowshipOpMode;
import com.fellowshipoftheloosescrews.utilities.opmode.Module;

public class CustomTestOpMode extends FellowshipOpMode {

    public class ModuleTest extends Module
    {
        public int numCycles = 0;

        @Override
        public void start() {
            Log.d("Module", "Start");
        }

        @Override
        public void update() {
            Log.d("Module", "Update");
            numCycles++;
            telemetry.addData("Module", numCycles);
        }

        @Override
        public void stop() {
            Log.d("Module", "Stop");
        }
    }

    @Override
    public void registerModules(ModuleManager manager)
    {
        manager.registerModule("Test", new ModuleTest());
    }

    @Override
    public void fellowshipInit() {
        Log.d("Fellowship", "Init");
    }

    @Override
    public void fellowshipRunOpMode() {
        Log.d("Fellowship", "Start");

        int numCycles = 0;

        while(true)
        {
            Log.d("Fellowship", "Loop");
            telemetry.addData("Linear", numCycles);
            telemetry.addData("Difference", numCycles -
                    ((ModuleTest)getOpMode().getModuleManager().getModule("Test")).numCycles);
            waitForHardwareUpdate();
            numCycles++;
        }
    }
}
