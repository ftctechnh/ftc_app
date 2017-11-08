package org.firstinspires.ftc.teamcode._Test._Drive;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode._Libs.AutoLib;
import org.firstinspires.ftc.teamcode._Libs.HeadingSensor;
import org.firstinspires.ftc.teamcode._Libs.SensorLib;


/**
 * simple example of using a Step that makes a bot with "squirrely wheels" drive along a given course
 * Created by phanau on 10/31/16.
 */


// simple example sequence that tests time based "squirrely wheel" drive steps to drive along a prescribed path
// while stabilizing robot orientation with gyro inputs
@Autonomous(name="Test: Squirrely Gyro Drive Test 1", group ="Test")
//@Disabled
public class SquirrelyGyroDriveTestOp extends OpMode {

    AutoLib.Sequence mSequence;             // the root of the sequence tree
    boolean bDone;                          // true when the programmed sequence is done
    DcMotor mMotors[];                      // motors in assumed order fr, br, fl, bl
    HeadingSensor mGyro;                    // gyro used to maintain robot orientation while "squirreling" around

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

        // get hardware gyro and wrap it in an object that calibrates it and corrects its output
        ModernRoboticsI2cGyro gyro = (ModernRoboticsI2cGyro) hardwareMap.gyroSensor.get("gyro");
        gyro.calibrate();
        mGyro = new SensorLib.CorrectedMRGyro(gyro);

        // create an autonomous sequence with the steps to drive
        // several legs of a polygonal course ---
        float power = 1.0f;

        // create the root Sequence for this autonomous OpMode
        mSequence = new AutoLib.LinearSequence();

        // add a bunch of timed "legs" to the sequence - use Gyro heading convention of positive degrees CCW from initial heading
        float leg = debug ? 6.0f : 3.0f;  // time along each leg of the polygon
        SensorLib.PID pid = null;         // use default PID provided by the step

        // drive a square while maintaining constant orientation (0)
        mSequence.add(new AutoLib.SquirrelyGyroTimedDriveStep(this, -90, 0, mGyro, pid, mMotors, power, leg/2, false));
        mSequence.add(new AutoLib.SquirrelyGyroTimedDriveStep(this,   0, 0, mGyro, pid, mMotors, power, leg, false));
        mSequence.add(new AutoLib.SquirrelyGyroTimedDriveStep(this,  90, 0, mGyro, pid, mMotors, power, leg, false));
        mSequence.add(new AutoLib.SquirrelyGyroTimedDriveStep(this, 180, 0, mGyro, pid, mMotors, power, leg, false));
        mSequence.add(new AutoLib.SquirrelyGyroTimedDriveStep(this, 270, 0, mGyro, pid, mMotors, power, leg/2, false));

        // ... and then a diamond
        mSequence.add(new AutoLib.SquirrelyGyroTimedDriveStep(this, -45, 0, mGyro, pid, mMotors, power, leg, false));
        mSequence.add(new AutoLib.SquirrelyGyroTimedDriveStep(this,  45, 0, mGyro, pid, mMotors, power, leg, false));
        mSequence.add(new AutoLib.SquirrelyGyroTimedDriveStep(this, 135, 0, mGyro, pid, mMotors, power, leg, false));
        mSequence.add(new AutoLib.SquirrelyGyroTimedDriveStep(this, 225, 0, mGyro, pid, mMotors, power, leg, false));

        // ... and then sort of a polygonal circle
        int n = 20;     // number of sides
        for (int i=0; i<n; i++) {
            float heading = 360*i/n - 90;
            boolean stop = (i == n-1);
            mSequence.add(new AutoLib.SquirrelyGyroTimedDriveStep(this, heading, 0, mGyro, pid, mMotors, power, leg/n, stop));
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

