package org.firstinspires.ftc.teamcode._Test._AutoLib;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode._Libs.AutoLib;

/**
 * Created by phanau on 3/26/17.
 */


/**
 * A test example of autonomous opmode programming using AutoLib classes.
 * This example investigates what happens if we try to implement the pattern of
 * putting "bookkeeping" code (like calling super.loop()) in a non-virtual loop() function
 * (except that there's no such thing in Java) which calls the class-specific virtual _loop()
 * function. It appears that the _loop() function of a given level of derivation is only called
 * if loop() is defined at that level of derivation as well. What???
 * Created by phanau on 2/15/17.
 */

class Step1 extends AutoLib.Step {
    OpMode mOpMode;
    int mCount;
    public Step1(OpMode opmode, int count) {
        mOpMode = opmode; mCount = count;
    }
    public boolean loop() { super.loop(); return _loop(); }
    private boolean _loop() {
        mOpMode.telemetry.addData("Step1._loop called ", loopCount());
        return mCount < loopCount();
    }
}

class Step2 extends Step1 {
    public Step2(OpMode opmode, int count) {
        super(opmode, count);
    }
    public boolean loop() { super.loop(); return _loop(); }
    private boolean _loop() {
        mOpMode.telemetry.addData("Step2._loop called ", loopCount());
        return mCount < loopCount();
    }
}

class Step3 extends Step2 {
    public Step3(OpMode opmode, int count) {
        super(opmode, count);
    }
    public boolean loop() { super.loop(); return _loop(); }
    private boolean _loop() {
        mOpMode.telemetry.addData("Step3._loop called ", loopCount());
        return mCount < loopCount();
    }
}


@Autonomous(name="Test: AutoLib Sequencer Test 0", group ="Test")
//@Disabled
public class AutoTest0 extends OpMode {

    AutoLib.Sequence mSequence;     // the root of the sequence tree
    boolean bDone;                  // true when the programmed sequence is done

    public AutoTest0() {
    }

    public void init() {
        // create the root Sequence for this autonomous OpMode
        mSequence = new AutoLib.LinearSequence();

        // add a first simple Step to the root Sequence
        mSequence.add(new Step3(this, 1000));

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
