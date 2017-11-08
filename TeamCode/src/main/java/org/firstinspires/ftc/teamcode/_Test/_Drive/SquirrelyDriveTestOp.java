package org.firstinspires.ftc.teamcode._Test._Drive;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode._Libs.AutoLib;


/**
 * simple example of using a Step that makes a bot with "squirrely wheels" drive along a given course
 * Created by phanau on 10/31/16.
 */


// simple example sequence that tests time based "squirrely wheel" drive steps to drive along a prescribed path
@Autonomous(name="Test: Squirrely Drive Test 1", group ="Test")
//@Disabled
public class SquirrelyDriveTestOp extends OpMode {

    AutoLib.Sequence mSequence;             // the root of the sequence tree
    boolean bDone;                          // true when the programmed sequence is done
    DcMotor mMotors[];                      // motors, some of which can be null: assumed order is fr, br, fl, bl

    @Override
    public void init() {
        AutoLib.HardwareFactory mf = null;
        final boolean debug = false;
        if (debug)
            mf = new AutoLib.TestHardwareFactory(this);
        else
            mf = new AutoLib.RealHardwareFactory(this);

        // get the motors: depending on the factory we created above, these may be
        // either dummy motors that just log data or real ones that drive the hardware
        // assumed order is fr, br, fl, bl
        mMotors = new DcMotor[4];
        mMotors[0] = mf.getDcMotor("fr");
        mMotors[1] = mf.getDcMotor("br");
        (mMotors[2] = mf.getDcMotor("fl")).setDirection(DcMotor.Direction.REVERSE);
        (mMotors[3] = mf.getDcMotor("bl")).setDirection(DcMotor.Direction.REVERSE);

        // create an autonomous sequence with the steps to drive
        // several legs of a polygonal course ---
        float power = 0.3f;

        // create the root Sequence for this autonomous OpMode
        mSequence = new AutoLib.LinearSequence();

        // add a bunch of timed "legs" to the sequence - use Gyro heading convention of positive degrees CCW from initial heading
        float leg = debug ? 2.0f : 3.0f;  // time along each leg of the polygon

        // drive a square
        mSequence.add(new AutoLib.MoveSquirrelyByTimeStep(mMotors, -90, power, leg/2, false));
        mSequence.add(new AutoLib.MoveSquirrelyByTimeStep(mMotors, 0, power, leg, false));
        mSequence.add(new AutoLib.MoveSquirrelyByTimeStep(mMotors, 90, power, leg, false));
        mSequence.add(new AutoLib.MoveSquirrelyByTimeStep(mMotors, 180, power, leg, false));
        mSequence.add(new AutoLib.MoveSquirrelyByTimeStep(mMotors, 270, power, leg/2, false));

       /* // ... and then a diamond
        mSequence.add(new AutoLib.MoveSquirrelyByTimeStep(mMotors, -45, power, leg, false));
        mSequence.add(new AutoLib.MoveSquirrelyByTimeStep(mMotors, 45, power, leg, false));
        mSequence.add(new AutoLib.MoveSquirrelyByTimeStep(mMotors, 135, power, leg, false));
        mSequence.add(new AutoLib.MoveSquirrelyByTimeStep(mMotors, 225, power, leg, false));

        // ... and then sort of a polygonal circle
        int n = 20;     // number of sides
        for (int i=0; i<n; i++) {
            double heading = 360*i/n - 90;
            boolean stop = (i == n-1);
            mSequence.add(new AutoLib.MoveSquirrelyByTimeStep(mMotors, heading, power, leg/n, stop));
        }
        */
        // start out not-done
        bDone = false;
    }

    @Override
    public void loop() {

        // until we're done, keep looping through the current Step(s)
        if (!bDone)
            bDone = mSequence.loop();       // returns true when we're done
        else
            telemetry.addData("sequence finished", "");
    }

    @Override
    public void stop() {
        super.stop();
    }
}

