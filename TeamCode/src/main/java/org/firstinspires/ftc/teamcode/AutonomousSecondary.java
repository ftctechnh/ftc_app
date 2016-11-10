package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;


/**
 * simple example of using a Step that makes a bot with "squirrely wheels" drive along a given course
 * Created by phanau on 10/31/16.
 */


// simple example sequence that tests either of gyro-based AzimuthCountedDriveStep or AzimuthTimedDriveStep to drive along a square path
@Autonomous(name="AutonomousSecondary", group="Main")
@Disabled
public class AutonomousSecondary extends OpMode {

    AutoLib.Sequence mSequence;             // the root of the sequence tree
    boolean bDone;                          // true when the programmed sequence is done
    DcMotor mMotors[];                      // motors, some of which can be null: assumed order is fr, br, fl, bl

    VuforiaLib_FTC2016 Vuf;

    //some constants to make navigating the field easier
    static final double mmToEncode = 1; //TODO: Find this value
    static final double inchToMm = 25.4;
    static final double footToMm = inchToMm * 12;
    static final double squareToMm = footToMm * 2;

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

        Vuf = new VuforiaLib_FTC2016();
        Vuf.init(this, null);     // pass it this OpMode (so it can do telemetry output) and use its license key for now

        // create an autonomous sequence with the steps to drive
        float power = 0.5f;
        float error = 254.0f;       // get us within 10" for this test
        float targetZ = 6*25.4f;

        // create the root Sequence for this autonomous OpMode
        mSequence = new AutoLib.LinearSequence();

        // drive a full square diagonnslly forward
        mSequence.add(new AutoLib.MoveSquirrelyByTimeStep(mMotors, 45, power, 0.6, true));
        mSequence.add(new AutoLib.VuforiaSquirrelyDriveStep(this, new VectorF(-1500,300,targetZ), Vuf, Vuf, mMotors, power, error));    // Wheels
        //pushy pushy
        mSequence.add(new AutoLib.VuforiaSquirrelyDriveStep(this, new VectorF(-1500,-914,targetZ), Vuf, Vuf, mMotors, power, error));   // Legos
        //pushy pushy
        mSequence.add(new AutoLib.VuforiaSquirrelyDriveStep(this, new VectorF((int)(-footToMm * 2), (int)(footToMm * 2),targetZ), Vuf, Vuf, mMotors, power, error));   // yoga ball

        // start out not-done
        bDone = false;
    }

    @Override
    public void loop() {

        // until we're done, keep looping through the current Step(s)
        if (!bDone)
            bDone = mSequence.loop();       // returns true when we're done
        else
            telemetry.addData("First sequence finished", "");
    }

    @Override
    public void stop() {
        super.stop();
    }
}

