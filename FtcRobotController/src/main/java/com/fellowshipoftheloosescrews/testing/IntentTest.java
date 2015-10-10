package com.fellowshipoftheloosescrews.testing;

import com.fellowshipoftheloosescrews.utilities.DataLog;
import com.fellowshipoftheloosescrews.utilities.opmode.FellowshipOpMode;

/**
 * Created by Thomas on 10/9/2015.
 */
public class IntentTest extends FellowshipOpMode {
    @Override
    public void fellowshipInit() {

    }

    @Override
    public void fellowshipRunOpMode() {
        DataLog log = new DataLog();
        log.saveFile("testing", "test.txt", "HELLO WORLD");
    }

    @Override
    public void registerModules(ModuleManager manager) {

    }
}
