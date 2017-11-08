package org.firstinspires.ftc.teamcode._Test._AutoLib;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode._Libs.AutoLib;


/**
 * A test example of autonomous opmode programming using AutoLib classes.
 * This one tests the Do-Until Step construct.
 * Created by phanau on 2/7/17.
 */


@Autonomous(name="Test: AutoLib Do-Until Test", group ="Test")
//@Disabled
public class AutoTest2 extends OpMode {

    AutoLib.Sequence mSequence;     // the root of the sequence tree
    boolean bDone;                  // true when the programmed sequence is done

    public AutoTest2() {
    }

    public void init() {
        // create the root Sequence for this autonomous OpMode
        mSequence = new AutoLib.LinearSequence();

        // create the "do" Sequence for a do-until Step - here it just contains a couple of LogTime Steps
        AutoLib.LinearSequence doSequence = new AutoLib.LinearSequence();
        doSequence.add(new AutoLib.LogTimeStep(this, "LogTimeStep 1", 2.0));
        doSequence.add(new AutoLib.LogTimeStep(this, "LogTimeStep 2", 1.0));

        // add a do-until Step to the root Sequence that repeats the "do" Sequence three times
        // using a LogCountStep as the "until" completion test.
        mSequence.add(new AutoLib.DoUntilStep(doSequence, new AutoLib.LogCountStep(this, "do-until count", 3)));

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
