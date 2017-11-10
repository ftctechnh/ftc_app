package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.SquirrelyLib;
import org.firstinspires.ftc.teamcode.libraries.VuforiaLib_FTC2016;


/**
 * simple example of using a Step that makes a bot with "squirrely wheels" drive to given locations on the field using Vuforia input
 * Created by phanau on 11/01/16.
 */


// example sequence that tests VuforiaSquirrelyDriveStep to drive to a sequence of given locations on the field
// using Squirrely wheels. It should work regardless of the initial orientation of the robot, as long as at least
// one of the target images is visible.

@Autonomous(name="Test: Vuforia Squirrely Drive Test 1", group ="Test")
@Disabled
public class VuforiaSquirrelyDriveTestOp extends OpMode {

    AutoLib.Sequence mSequence;             // the root of the sequence tree
    boolean bDone;                          // true when the programmed sequence is done
    DcMotor mMotors[];                      // motors, some of which can be null: assumed order is fr, br, fl, bl
    VuforiaLib_FTC2016 mVLib;


    @Override
    public void init() {

         // Start up Vuforia using VuforiaLib_FTC2016
        mVLib = new VuforiaLib_FTC2016();
        mVLib.init(this, null);     // pass it this OpMode (so it can do telemetry output) and use its license key for now

        // get physical or test hardware factory
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
        float power = 0.5f;
        float error = 254.0f;       // get us within 10" for this test
        float targetZ = 6*25.4f;

        // create the root Sequence for this autonomous OpMode
        mSequence = new AutoLib.LinearSequence();

        // add a bunch of "legs" to the sequence - use Vuforia convention for locations on the field
        // with X and Y axes pointing away from the walls with beacons and targets

        mSequence.add(new SquirrelyLib.VuforiaSquirrelyDriveStep(this, new VectorF(-1500,300,targetZ), mVLib, mVLib, mMotors, power, error));    // Wheels
        mSequence.add(new SquirrelyLib.VuforiaSquirrelyDriveStep(this, new VectorF(-1500,-914,targetZ), mVLib, mVLib, mMotors, power, error));   // Legos
        mSequence.add(new SquirrelyLib.VuforiaSquirrelyDriveStep(this, new VectorF(-914,-1500,targetZ), mVLib, mVLib, mMotors, power, error));   // Tools
        mSequence.add(new SquirrelyLib.VuforiaSquirrelyDriveStep(this, new VectorF(300,-1500,targetZ), mVLib, mVLib, mMotors, power, error));    // Gears

        mSequence.add(new SquirrelyLib.VuforiaSquirrelyDriveStep(this, new VectorF(0,0,targetZ), mVLib, mVLib, mMotors, power, error));
        mSequence.add(new SquirrelyLib.VuforiaSquirrelyDriveStep(this, new VectorF(-1000,0,targetZ), mVLib, mVLib, mMotors, power, error));
        mSequence.add(new SquirrelyLib.VuforiaSquirrelyDriveStep(this, new VectorF(-1000,-1000,targetZ), mVLib, mVLib, mMotors, power, error));
        mSequence.add(new SquirrelyLib.VuforiaSquirrelyDriveStep(this, new VectorF(0,-1000,targetZ), mVLib, mVLib, mMotors, power, error));

        // start out not-done
        bDone = false;
    }

    @Override public void start()
    {
        // Start tracking the data sets we care about.
        mVLib.start();
    }

    @Override
    public void loop() {
        // update Vuforia location info and do debug telemetry
        mVLib.loop(true);

        // until we're done, keep looping through the current Step(s)
        if (!bDone)
            bDone = mSequence.loop();       // returns true when we're done
        else
            telemetry.addData("sequence finished", "");
    }

    @Override
    public void stop() {
        super.stop();
        mVLib.stop();
    }


}

