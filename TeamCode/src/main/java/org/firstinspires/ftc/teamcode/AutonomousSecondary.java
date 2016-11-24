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
@Autonomous(name="Testing Vuforia Turn", group="Test")
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

    @Override
    public void init() {
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
        mMotors[0] = mf.getDcMotor("front_right");
        mMotors[1] = mf.getDcMotor("back_right");
        (mMotors[2] = mf.getDcMotor("front_left")).setDirection(DcMotor.Direction.REVERSE);
        (mMotors[3] = mf.getDcMotor("back_left")).setDirection(DcMotor.Direction.REVERSE);

        Vuf = new VuforiaLib_FTC2016();
        Vuf.init(this, null);     // pass it this OpMode (so it can do telemetry output) and use its license key for now

        // create an autonomous sequence with the steps to drive
        float power = 0.5f;
        float error = 5.0f;       // get us within 10 degrees for this test
        float targetZ = 6*25.4f;

        // create the root Sequence for this autonomous OpMode
        mSequence = new AutoLib.LinearSequence();

        mSequence.add(new AutoLib.GyroTurnStep(this, 45.0f, Vuf, mMotors, power, error, true));

        // start out not-done
        bDone = false;
    }

    @Override
    public void start(){
        Vuf.start();
    }

    @Override
    public void loop() {

        // until we're done, keep looping through the current Step(s)
        if (!bDone)
            bDone = mSequence.loop();       // returns true when we're done
        else
            telemetry.addData("First sequence finished", "");

        Vuf.loop(true);
    }

    @Override
    public void stop() {
        super.stop();
        Vuf.stop();
    }
}

