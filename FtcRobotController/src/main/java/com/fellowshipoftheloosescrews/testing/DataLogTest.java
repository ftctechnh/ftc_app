package com.fellowshipoftheloosescrews.testing;

import android.util.Log;

import com.fellowshipoftheloosescrews.utilities.opmode.FellowshipOpMode;
import com.fellowshipoftheloosescrews.utilities.opmode.Module;

/**
 * Created by Thomas on 10/10/2015.
 */
public class DataLogTest extends FellowshipOpMode {

    public class TestModule extends Module
    {

        @Override
        public void start() {
            getDataLog().addVariable("test");
            getDataLog().addVariable("test2");
        }

        int data = 0;

        @Override
        public void update() {
            if(data % 2 == 0) getDataLog().addData("test", data);
            getDataLog().addData("test2", data * 2);
            data++;
        }

        @Override
        public void stop() {

        }
    }

    @Override
    public void fellowshipInit() {

    }

    @Override
    public void fellowshipRunOpMode() {
        while(opModeIsActive()){}
    }

    @Override
    public void registerModules(ModuleManager manager) {
        manager.registerModule("dataTesting", new TestModule());
    }
}
