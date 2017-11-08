package org.firstinspires.ftc.teamcode._Test._AutoLib;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode._Libs.AutoLib;
import org.firstinspires.ftc.teamcode._Libs.SensorLib;

// example of a base class for a bunch of Autonomous OpModes that all use the same (or nearly same) hardware
public class AutoOpModeBase extends OpMode {

    AutoLib.Sequence mSequence;             // the root of the sequence tree
    boolean bDone;                          // true when the programmed sequence is done
    DcMotor mMotors[];                      // motors, some of which can be null: assumed order is fr, br, fl, bl
    ModernRoboticsI2cGyro mGyro;  	        // gyro to use for heading information
    SensorLib.CorrectedMRGyro mCorrGyro;    // gyro corrector object
    SensorLib.PID mPid;                     // PID controller for the sequence
    AutoLib.HardwareFactory mFactory;       // hardware factory - may be used by derived classes to get additional hardware

    // parameters of the PID controller for this sequence
    float Kp = 0.035f;        // motor power proportional term correction per degree of deviation
    float Ki = 0.02f;         // ... integrator term
    float Kd = 0;             // ... derivative term
    float KiCutoff = 3.0f;    // maximum angle error for which we update integrator

    // implement this function in derived classes to do additional init() stuff like finding more hardware
    // and -especially- building the autonomous sequence for the particular derived OpMode
    protected void _init() {}

    // implement this function to do additional loop() stuff specific to the derived OpMode.
    // if this function returns true, then the default loop() action of running the sequence is skipped.
    protected boolean _loop() { return false; }

    @Override
    public void init() {

        boolean test = true;
        mFactory = test ? new AutoLib.RealHardwareFactory(this) : new AutoLib.TestHardwareFactory(this);

        // get the motors: assumed order is fr, br, fl, bl
        mMotors = new DcMotor[4];
        mMotors[0] = mFactory.getDcMotor("fr");
        mMotors[1] = mFactory.getDcMotor("br");
        (mMotors[2] = mFactory.getDcMotor("fl")).setDirection(DcMotor.Direction.REVERSE);
        (mMotors[3] = mFactory.getDcMotor("bl")).setDirection(DcMotor.Direction.REVERSE);

        // get hardware gyro
        mGyro = (ModernRoboticsI2cGyro)hardwareMap.gyroSensor.get("gyro");

        // wrap gyro in an object that calibrates it and corrects its output
        mCorrGyro = new SensorLib.CorrectedMRGyro(mGyro);
        mCorrGyro.calibrate();

        // create a PID controller for the sequence
        mPid = new SensorLib.PID(Kp, Ki, Kd, KiCutoff);    // make the object that implements PID control algorithm

        // create the root Sequence for this autonomous OpMode
        mSequence = new AutoLib.LinearSequence();

        // start out not-done
        bDone = false;

	    // let derived OpMode do initialization like creating the autonomous sequence and discovering additional hardware
	    _init();
    }

    @Override
    public void loop() {

        if (_loop())
            return;

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

