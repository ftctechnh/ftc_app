package org.firstinspires.ftc.teamcode._Test._AutoLib;

/**
 * OpMode to test AutoLib driving of motors by time or encoder counts
 * Created by phanau on 12/14/15.
 */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode._Libs.AutoLib;


/**
 * A test example of autonomous opmode programming using AutoLib classes.
 * Created by phanau on 12/14/15.
 */



@Autonomous(name="Test: AutoLib Motor Step Test 1", group ="Test")
//@Disabled
public class AutoMotorTest1 extends OpMode {

    AutoLib.Sequence mSequence;     // the root of the sequence tree
    boolean bDone;                  // true when the programmed sequence is done
    boolean bFirst;                 // true first time loop() is called

    DcMotor mFr, mBr, mFl, mBl;     // four drive motors (front right, back right, front left, back left)
    DcMotor mIo, mUd;               // two arm motors (in-out, up-down) OPTIONAL

    boolean debug = true;           // run in test/debug mode with dummy motors and data logging
    boolean haveEncoders = false;   // robot has Encoder-based motors

    public AutoMotorTest1() {
    }

    public void init() {

        AutoLib.HardwareFactory mf = null;
        if (debug)
            mf = new AutoLib.TestHardwareFactory(this);
        else
            mf = new AutoLib.RealHardwareFactory(this);

        // get the motors: depending on the factory we created above, these may be
        // either dummy motors that just log data or real ones that drive the hardware
        mFr = mf.getDcMotor("fr");
        mFl = mf.getDcMotor("fl");
        mBr = mf.getDcMotor("br");
        mBl = mf.getDcMotor("bl");

        // OPTIONAL arm motors
        try {
            mIo = mf.getDcMotor("io");
            mUd = mf.getDcMotor("ud");
        }
        catch (IllegalArgumentException iax) {
            mIo = mUd = null;
        }

        mFl.setDirection(DcMotor.Direction.REVERSE);
        mBl.setDirection(DcMotor.Direction.REVERSE);

        // create the root Sequence for this autonomous OpMode
        mSequence = new AutoLib.LinearSequence();

        // add a Step (actually, a ConcurrentSequence under the covers) that
        // drives all four motors forward at half power for 2 seconds
        mSequence.add(new AutoLib.MoveByTimeStep(mFr, mBr, mFl, mBl, 0.5, 2.0, false));

        // create a second sequence that drives motors at different speeds
        // to turn left for 3 seconds, then stop all motors
        mSequence.add(new AutoLib.TurnByTimeStep(mFr, mBr, mFl, mBl, 0.5, 0.2, 3.0, true));

        // raise the arm using encoders while also extending it for 1 second
        AutoLib.ConcurrentSequence cs1 = new AutoLib.ConcurrentSequence();
        if (mUd != null && (debug || !haveEncoders))
            cs1.add(new AutoLib.TimedMotorStep(mUd, 0.75, 1.0, true)); // we don't support encoders yet in debug mode
        //else
        //    cs1.add(new AutoLib.EncoderMotorStep(new EncoderMotor(mUd), 0.75, 1000, true));
        if (mIo != null)
            cs1.add(new AutoLib.TimedMotorStep(mIo, 0.5, 1.0, true));
        mSequence.add(cs1);

        // start out not-done, first time
        bDone = false;
        bFirst = true;
    }

    public void loop() {
        // reset the timer when we start looping
        if (bFirst) {
            this.resetStartTime();      // OpMode provides a timer
            bFirst = false;
        }

        // until we're done with the root Sequence, perform the current Step(s) each time through the loop
        if (!bDone) {
            bDone = mSequence.loop();       // returns true when we're done

            if (debug)
                telemetry.addData("elapsed time", this.getRuntime());
        }
    }

    public void stop() {
        telemetry.addData("stop() called", "");
    }
}
