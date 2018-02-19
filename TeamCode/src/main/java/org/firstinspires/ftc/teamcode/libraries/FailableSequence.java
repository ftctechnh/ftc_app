package org.firstinspires.ftc.teamcode.libraries;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.ArrayList;

/**
 * Created by Noah on 2/15/2018.
 * Sequentially fails through a list of steps until one completes without an exception
 */

public class FailableSequence extends FailableStep {
    private int failIndex = 0;
    private boolean runTerminate = false;

    protected ArrayList<AutoLib.Step> mSteps;  // expandable array containing the Steps in the Sequence
    protected final String mError;
    protected final OpMode debug;

    protected FailableSequence(String errorMsg, OpMode mode) {
        mSteps = new ArrayList<AutoLib.Step>(10);   // create the array with an initial capacity of 10
        mError = errorMsg;
        debug = mode;
    }

    protected FailableSequence(String errorMsg) {
        this(errorMsg, null);
    }

    // add a Step to the Sequence
    public FailableSequence add(AutoLib.Step step) {
        mSteps.add(step);
        return this;        // allows daisy-chaining of calls
    }

    public FailableSequence preAdd(AutoLib.Step step){
        mSteps.add(0, step);
        return this;
    }


    public boolean _loop() throws Exception {
        //super loop
        super.loop();
        if(debug != null) debug.telemetry.addData("Fail Index", failIndex);
        //check if we're not going to overflow
        if(failIndex < mSteps.size()) {
            //check if the step we're looping through is failable
            final AutoLib.Step temp = mSteps.get(failIndex);
            if(temp instanceof FailableStep) {
                FailableStep fail = (FailableStep)temp;
                //run an alternate state machine to handle the failure logic
                if(!runTerminate) {
                    try {
                        if(fail._loop()) return true;
                    }
                    catch (Exception e) {
                        runTerminate = true;
                    }
                }
                if(runTerminate && fail._terminate()) {
                    runTerminate = false;
                    failIndex++;
                }
            }
            //else run like a normal step
            else return temp.loop();
        }
        else throw new Exception(mError);
        return false;
    }

    public boolean _terminate() {
        //I dunno what to put here yet, but I'll figure it out
        return true;
    }
}
