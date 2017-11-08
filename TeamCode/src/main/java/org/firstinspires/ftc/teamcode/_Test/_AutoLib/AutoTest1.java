package org.firstinspires.ftc.teamcode._Test._AutoLib;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode._Libs.AutoLib;


/**
 * A test example of autonomous opmode programming using AutoLib classes.
 * Created by phanau on 12/14/15.
 */


@Autonomous(name="Test: AutoLib Sequencer Test 1", group ="Test")
//@Disabled
public class AutoTest1 extends OpMode {

    AutoLib.Sequence mSequence;     // the root of the sequence tree
    boolean bDone;                  // true when the programmed sequence is done

    public AutoTest1() {
    }

    public void init() {
        // create the root Sequence for this autonomous OpMode
        mSequence = new AutoLib.LinearSequence();

        // add a first simple Step to the root Sequence
        mSequence.add(new AutoLib.LogTimeStep(this, "step1", 10));

        // create a ConcurrentSequence with 3 concurrent Steps
        AutoLib.ConcurrentSequence cs1 = new AutoLib.ConcurrentSequence();
            // step 1 of the 3 concurrent steps
            cs1.add(new AutoLib.LogTimeStep(this, "step2a", 10));
            // step 2 is itself a LinearSequence of two Steps
            AutoLib.LinearSequence cs1a = new AutoLib.LinearSequence();
                cs1a.add(new AutoLib.LogTimeStep(this, "step2b1", 6));
                cs1a.add(new AutoLib.LogTimeStep(this, "step2b2", 9));
            cs1.add(cs1a);
            // step 3 is a simple Step
            cs1.add(new AutoLib.LogTimeStep(this, "step2c", 5));
        // add the ConcurrentSequence to the root Sequence
        mSequence.add(cs1);

        // finish up with another simple Step
        mSequence.add(new AutoLib.LogTimeStep(this, "step3", 10));

        // start out not-done
        bDone = false;
    }

    public void loop() {
        // until we're done, keep looping through the current Step(s)
        if (!bDone)
            bDone = mSequence.loop();       // returns true when we're done
        else
            telemetry.addData("sequence finished", "");
    }

    public void stop() {
        telemetry.addData("stop() called", "");
    }
}
