package org.firstinspires.ftc.teamcode._Test._AutoLib;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode._Libs.AutoLib;

/**
 * Created by phanau on 3/26/17.
 */


/**
 * A test example of autonomous opmode programming using AutoLib classes.
 * This one tests the If-Then-Else Step construct using interactive input from the gamepad.
 * Created by phanau on 2/7/17.
 */

class ButtonXStep extends AutoLib.Step {
    OpMode mOpMode;
    public ButtonXStep(OpMode opMode) {
        mOpMode = opMode;
    }
    public boolean loop() {
        return mOpMode.gamepad1.x;
    }
}


@Autonomous(name="Test: AutoLib If-Then-Else Test", group ="Test")
//@Disabled
public class AutoTest3 extends OpMode {

    AutoLib.Sequence mSequence;     // the root of the sequence tree
    boolean bDone;                  // true when the programmed sequence is done

    public AutoTest3() {
    }

    public void init() {
        // create the root Sequence for this autonomous OpMode
        mSequence = new AutoLib.LinearSequence();

        // add an If-Then-Else Step that logs either a "Then" or "Else" message depending on whether or not the X-button is pushed.
        // note that the evaluation of the If clause happens EVERY TIME through the loop(), not just once.
        mSequence.add(new AutoLib.IfThenStep(new ButtonXStep(this), new AutoLib.LogTimeStep(this, "Then", 5.0), new AutoLib.LogTimeStep(this, "Else", 8.0)));

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
