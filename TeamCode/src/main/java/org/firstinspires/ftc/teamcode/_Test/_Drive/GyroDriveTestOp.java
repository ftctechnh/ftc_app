package org.firstinspires.ftc.teamcode._Test._Drive;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode._Libs.AutoLib;
import org.firstinspires.ftc.teamcode._Libs.SensorLib;


/**
 * simple example of using a Step that uses gyro input to drive along a given course for a given time
 * Created by phanau on 1/3/16 as SensorDriveTestOp, modified 1/22/16 to use Gyro, renamed GyroDriveTestOp 2/16/16.
 */


// simple example sequence that tests either of gyro-based AzimuthCountedDriveStep or AzimuthTimedDriveStep to drive along a square path
@Autonomous(name="Test: Gyro Drive Test 1", group ="Test")
//@Disabled
public class GyroDriveTestOp extends OpMode {

    AutoLib.Sequence mSequence;             // the root of the sequence tree
    boolean bDone;                          // true when the programmed sequence is done
    DcMotor mMotors[];                      // motors, some of which can be null: assumed order is fr, br, fl, bl
    ModernRoboticsI2cGyro mGyro;            // gyro to use for heading information
    SensorLib.CorrectedMRGyro mCorrGyro;    // gyro corrector object
    boolean bSetup;                         // true when we're in "setup mode" where joysticks tweak parameters
    SensorLib.PID mPid;                     // PID controller for the sequence

    // parameters of the PID controller for this sequence
    float Kp = 0.035f;        // motor power proportional term correction per degree of deviation
    float Ki = 0.02f;         // ... integrator term
    float Kd = 0;             // ... derivative term
    float KiCutoff = 3.0f;    // maximum angle error for which we update integrator

    @Override
    public void init() {
        bSetup = false;      // start out in Kp/Ki setup mode
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

        // get hardware gyro
        mGyro = (ModernRoboticsI2cGyro) hardwareMap.gyroSensor.get("gyro");

        // wrap gyro in an object that calibrates it and corrects its output
        mCorrGyro = new SensorLib.CorrectedMRGyro(mGyro);
        mCorrGyro.calibrate();

        // create a PID controller for the sequence
        mPid = new SensorLib.PID(Kp, Ki, Kd, KiCutoff);    // make the object that implements PID control algorithm

        // create an autonomous sequence with the steps to drive
        // several legs of a polygonal course ---
        float power = 0.25f;

        // create the root Sequence for this autonomous OpMode
        mSequence = new AutoLib.LinearSequence();

        boolean bUseEncoders = true;
        if (bUseEncoders) {
            // add a bunch of encoder-counted "legs" to the sequence - use Gyro heading convention of positive degrees CCW from initial heading
            int leg = 4000;  // motor-encoder count along each leg of the polygon
            mSequence.add(new AutoLib.AzimuthCountedDriveStep(this, 0, mCorrGyro, mPid, mMotors, power, leg * 2, false));
            mSequence.add(new AutoLib.AzimuthCountedDriveStep(this, 90, mCorrGyro, mPid, mMotors, power, leg, false));
            mSequence.add(new AutoLib.AzimuthCountedDriveStep(this, 180, mCorrGyro, mPid, mMotors, power, leg * 2, false));
            mSequence.add(new AutoLib.AzimuthCountedDriveStep(this, 270, mCorrGyro, mPid, mMotors, power, leg, false));
            mSequence.add(new AutoLib.AzimuthCountedDriveStep(this, 0, mCorrGyro, mPid, mMotors, power, leg * 2, false));
            mSequence.add(new AutoLib.AzimuthCountedDriveStep(this, 90, mCorrGyro, mPid, mMotors, power, leg, false));
            mSequence.add(new AutoLib.AzimuthCountedDriveStep(this, 0, mCorrGyro, mPid, mMotors, power, leg * 2, false));
            mSequence.add(new AutoLib.AzimuthCountedDriveStep(this, -90, mCorrGyro, mPid, mMotors, power, leg, false));
            mSequence.add(new AutoLib.AzimuthCountedDriveStep(this, -180, mCorrGyro, mPid, mMotors, power, leg * 2, false));
            mSequence.add(new AutoLib.AzimuthCountedDriveStep(this, -270, mCorrGyro, mPid, mMotors, power, leg, false));
            mSequence.add(new AutoLib.AzimuthCountedDriveStep(this, 0, mCorrGyro, mPid, mMotors, power, leg * 2, false));
            mSequence.add(new AutoLib.AzimuthCountedDriveStep(this, -90, mCorrGyro, mPid, mMotors, power, leg, false));
            mSequence.add(new AutoLib.AzimuthCountedDriveStep(this, -180, mCorrGyro, mPid, mMotors, power, leg * 4, true));
        }
        else  {
            // add a bunch of timed "legs" to the sequence - use Gyro heading convention of positive degrees CW from initial heading
            float leg = debug ? 10.0f : 3.0f;  // time along each leg of the polygon
            mSequence.add(new AutoLib.AzimuthTimedDriveStep(this, 0, mCorrGyro, mPid, mMotors, power, leg * 2, false));
            mSequence.add(new AutoLib.AzimuthTimedDriveStep(this, 90, mCorrGyro, mPid, mMotors, power, leg, false));
            mSequence.add(new AutoLib.AzimuthTimedDriveStep(this, 180, mCorrGyro, mPid, mMotors, power, leg * 2, false));
            mSequence.add(new AutoLib.AzimuthTimedDriveStep(this, 270, mCorrGyro, mPid, mMotors, power, leg, false));
            mSequence.add(new AutoLib.AzimuthTimedDriveStep(this, 0, mCorrGyro, mPid, mMotors, power, leg * 2, false));
            mSequence.add(new AutoLib.AzimuthTimedDriveStep(this, 90, mCorrGyro, mPid, mMotors, power, leg, false));
            mSequence.add(new AutoLib.AzimuthTimedDriveStep(this, 0, mCorrGyro, mPid, mMotors, power, leg * 2, false));
            mSequence.add(new AutoLib.AzimuthTimedDriveStep(this, -90, mCorrGyro, mPid, mMotors, power, leg, false));
            mSequence.add(new AutoLib.AzimuthTimedDriveStep(this, -180, mCorrGyro, mPid, mMotors, power, leg * 2, false));
            mSequence.add(new AutoLib.AzimuthTimedDriveStep(this, -270, mCorrGyro, mPid, mMotors, power, leg, false));
            mSequence.add(new AutoLib.AzimuthTimedDriveStep(this, 0, mCorrGyro, mPid, mMotors, power, leg * 2, false));
            mSequence.add(new AutoLib.AzimuthTimedDriveStep(this, -90, mCorrGyro, mPid, mMotors, power, leg, false));
            mSequence.add(new AutoLib.AzimuthTimedDriveStep(this, -180, mCorrGyro, mPid, mMotors, power, leg * 4, true));
        }

        // start out not-done
        bDone = false;
    }

    @Override
    public void loop() {

        if (gamepad1.y)
            bSetup = true;      // enter "setup mode" using controller inputs to set Kp and Ki
        if (gamepad1.x)
            bSetup = false;     // exit "setup mode"

        if (bSetup) {           // "setup mode"
            // adjust PID parameters by joystick inputs
            Kp -= (gamepad1.left_stick_y * 0.0001f);
            Ki -= (gamepad1.right_stick_y * 0.0001f);
            // update the parameters of the PID used by all Steps in this test
            mPid.setK(Kp, Ki, Kd, KiCutoff);
            // log updated values to the operator's console
            telemetry.addData("Kp = ", Kp);
            telemetry.addData("Ki = ", Ki);
            return;
        }

        // until we're done, keep looping through the current Step(s)
        if (!bDone)
            bDone = mSequence.loop();       // returns true when we're done
        else
            telemetry.addData("sequence finished", "");
    }

    @Override
    public void stop() {
        super.stop();
        mCorrGyro.stop();        // release the physical sensor(s) we've been using for azimuth data
    }
}

