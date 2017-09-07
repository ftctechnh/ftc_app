package org.firstinspires.ftc.teamcode._Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;

import org.firstinspires.ftc.teamcode._Libs.AutoLib;
import org.firstinspires.ftc.teamcode._Libs.VuforiaLib_FTC2016;


/**
 * simple example of using a Step that makes a bot drive to given locations on the field using Vuforia input
 * Created by phanau on 11/21/16.
 */


// example sequence that tests VuforiaDriveStep to drive to a sequence of given locations on the field
// using normal wheels. It should work regardless of the initial orientation of the robot, as long as at least
// one of the target images is visible.

@Autonomous(name="Vuforia Drive Test 1", group ="Test")
//@Disabled
public class VuforiaDriveTestOp extends OpMode {

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
        final boolean debug = true;
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
        float power = 1.0f;
        float inches = 25.4f;
        float error = 4*inches;       // get us within 4" for this test
        float targetZ = 0*inches;     // since we put the height of the camera into the Vuforia camera position matrix, the Z value of the robot is zero
        boolean stop = true;
        double pauseSec = 4.0;

        // create the root Sequence for this autonomous OpMode
        mSequence = new AutoLib.LinearSequence();

        // add a bunch of "legs" to the sequence - use Vuforia convention for locations on the field
        // with X and Y axes pointing away from the walls with beacons and targets

        // start with a step that heads toward the targets by dead reckoning until something is detected.
        // it appears that Vuforia only initially detects targets when they are about 5' away, but once detected, can track them from >10' away.
        AutoLib.ConcurrentSequence approach = new AutoLib.MoveByTimeStep(mMotors, 0.5, 0, false);     // the base step just moves forward by dead reckoning
        approach.add(new AutoLib.LocationHeadingWaitStep(mVLib, mVLib));    // add step that will complete the "approach" step when Vuforia data is found
        mSequence.add(approach);        // add the composite step to the linear sequence
        mSequence.add(new AutoLib.LogTimeStep(this, "Vuforia acquired", pauseSec));

        // these steps visit the four target images, pausing at each one
        mSequence.add(new AutoLib.VuforiaDriveStep(this, new VectorF(-64*inches,12*inches,targetZ), mVLib, mVLib, mMotors, power, error, stop));    // Wheels
        mSequence.add(new AutoLib.LogTimeStep(this, "at Wheels", pauseSec));
        mSequence.add(new AutoLib.VuforiaDriveStep(this, new VectorF(-64*inches,-36*inches,targetZ), mVLib, mVLib, mMotors, power, error, stop));   // Legos
        mSequence.add(new AutoLib.LogTimeStep(this, "at Legos", pauseSec));
        mSequence.add(new AutoLib.VuforiaDriveStep(this, new VectorF(-36*inches,-64*inches,targetZ), mVLib, mVLib, mMotors, power, error, stop));   // Tools
        mSequence.add(new AutoLib.LogTimeStep(this, "at Tools", pauseSec));
        mSequence.add(new AutoLib.VuforiaDriveStep(this, new VectorF(12*inches,-64*inches,targetZ), mVLib, mVLib, mMotors, power, error, stop));    // Gears
        mSequence.add(new AutoLib.LogTimeStep(this, "at Gears", pauseSec));

        //mSequence.add(new AutoLib.VuforiaDriveStep(this, new VectorF(0,0,targetZ), mVLib, mVLib, mMotors, power, error));
        mSequence.add(new AutoLib.VuforiaDriveStep(this, new VectorF(-1000,0,targetZ), mVLib, mVLib, mMotors, power, error, stop));
        mSequence.add(new AutoLib.VuforiaDriveStep(this, new VectorF(-1000,-1000,targetZ), mVLib, mVLib, mMotors, power, error, stop));
        mSequence.add(new AutoLib.VuforiaDriveStep(this, new VectorF(0,-1000,targetZ), mVLib, mVLib, mMotors, power, error, stop));

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

