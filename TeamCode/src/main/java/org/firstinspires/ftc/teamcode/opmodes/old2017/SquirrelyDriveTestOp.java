package org.firstinspires.ftc.teamcode.opmodes.old2017;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.SquirrelyLib;


/**
 * simple example of using a Step that makes a bot with "squirrely wheels" drive along a given course
 * Created by phanau on 10/31/16.
 */


// simple example sequence that tests either of gyro-based AzimuthCountedDriveStep or AzimuthTimedDriveStep to drive along a square path
@Autonomous(name="Test: Squirrely Drive Test 1", group ="Test")
@Disabled
public class SquirrelyDriveTestOp extends OpMode {

    AutoLib.Sequence mSequence;             // the root of the sequence tree
    boolean bDone;                          // true when the programmed sequence is done
    DcMotor mMotors[];                      // motors, some of which can be null: assumed order is fr, br, fl, bl

    // parameters of the PID controller for this sequence
    float Kp = 0.035f;        // motor power proportional term correction per degree of deviation
    float Ki = 0.02f;         // ... integrator term
    float Kd = 0;             // ... derivative term
    float KiCutoff = 3.0f;    // maximum angle error for which we update integrator

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
        mMotors[0] = mf.getDcMotor("front_right");
        mMotors[1] = mf.getDcMotor("back_right");
        (mMotors[2] = mf.getDcMotor("front_left")).setDirection(DcMotor.Direction.REVERSE);
        (mMotors[3] = mf.getDcMotor("back_left")).setDirection(DcMotor.Direction.REVERSE);

        // create an autonomous sequence with the steps to drive
        // several legs of a polygonal course ---
        float power = 1.0f;

        // create the root Sequence for this autonomous OpMode
        mSequence = new AutoLib.LinearSequence();

        // add a bunch of timed "legs" to the sequence - use Gyro heading convention of positive degrees CCW from initial heading
        float leg = debug ? 6.0f : 3.0f;  // time along each leg of the polygon

        // drive a square
        mSequence.add(new SquirrelyLib.MoveSquirrelyByTimeStep(mMotors, -90, power, leg/2, false));
        mSequence.add(new SquirrelyLib.MoveSquirrelyByTimeStep(mMotors, 0, power, leg, false));
        mSequence.add(new SquirrelyLib.MoveSquirrelyByTimeStep(mMotors, 90, power, leg, false));
        mSequence.add(new SquirrelyLib.MoveSquirrelyByTimeStep(mMotors, 180, power, leg, false));
        mSequence.add(new SquirrelyLib.MoveSquirrelyByTimeStep(mMotors, 270, power, leg/2, false));

        // ... and then a diamond
        mSequence.add(new SquirrelyLib.MoveSquirrelyByTimeStep(mMotors, -45, power, leg, false));
        mSequence.add(new SquirrelyLib.MoveSquirrelyByTimeStep(mMotors, 45, power, leg, false));
        mSequence.add(new SquirrelyLib.MoveSquirrelyByTimeStep(mMotors, 135, power, leg, false));
        mSequence.add(new SquirrelyLib.MoveSquirrelyByTimeStep(mMotors, 225, power, leg, false));

        // ... and then sort of a polygonal circle
        int n = 20;     // number of sides
        for (int i=0; i<n; i++) {
            double heading = 360*i/n - 90;
            boolean stop = (i == n-1);
            mSequence.add(new SquirrelyLib.MoveSquirrelyByTimeStep(mMotors, heading, power, leg/n, stop));
        }

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

