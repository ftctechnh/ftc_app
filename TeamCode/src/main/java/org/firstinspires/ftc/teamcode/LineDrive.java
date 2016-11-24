package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.opencv.core.Mat;

import java.util.ArrayList;


/**
 * simple example of using a Step that uses gyro input to drive along a given course for a given time
 * Created by phanau on 1/3/16 as SensorDriveTestOp, modified 1/22/16 to use Gyro, renamed GyroDriveTestOp 2/16/16.
 */


// simple example sequence that tests either of gyro-based AzimuthCountedDriveStep or AzimuthTimedDriveStep to drive along a square path
@Autonomous(name="Line Drive", group ="Line Follow")
//@Disabled
public class LineDrive extends OpenCVLib implements HeadingSensor {

    private int yVal;
    private double lastLinePos = 0;

    AutoLib.Sequence mSequence;             // the root of the sequence tree
    boolean bDone;                          // true when the programmed sequence is done
    BotHardware robot;                      // robot hardware object
    boolean bSetup;                         // true when we're in "setup mode" where joysticks tweak parameters
    SensorLib.PID mPid;                     // PID controller for the sequence

    // parameters of the PID controller for this sequence
    float Kp = 1.000f;        // motor power proportional term correction per unit of deviation
    float Ki = 0.02f;         // ... integrator term
    float Kd = 0;             // ... derivative term
    float KiCutoff = 0.05f;    // maximum angle error for which we update integrator

    @Override
    public void init() {
        bSetup = false;      // start out in Kp/Ki setup mode

        //init hardware objects
        final boolean debug = true;
        robot = new BotHardware();
        robot.init(this, debug);

        // create a PID controller for the sequence
        mPid = new SensorLib.PID(Kp, Ki, Kd, KiCutoff);    // make the object that implements PID control algorithm

        // create an autonomous sequence with the steps to drive
        // several legs of a polygonal course ---
        final float power = 0.5f;
        final float time = 30.0f;

        // create the root Sequence for this autonomous OpMode
        mSequence = new AutoLib.LinearSequence();

        mSequence.add(new AutoLib.AzimuthTimedDriveStep(this, 0, this, mPid, robot.getMotorArray(), power, time, true));

        // start out not-done
        bDone = false;

        //start up opencv
        initOpenCV();
        startCamera();

        //catch a frame
        Mat frame = getCameraFrame();

        //init scanline Y values
        yVal = frame.cols() / 8;

        //log all the data
        telemetry.addData("Y Value", yVal);

        telemetry.addData("Frame Width", frame.width());
        telemetry.addData("Frame Height", frame.height());
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
        stopCamera();
    }

    //heading sensor code for line following
    public float getHeading(){
        Mat frame = getCameraFrame();
        //get line displacement
        double linePos = LineFollowLib.getDisplacment(frame, yVal);
        //if it's an error, turn to where line was last
        if(linePos == LineFollowLib.ERROR_TOO_NOISY){
            if(lastLinePos > 0) return 1.0f;
            else return -1.0f;
        }

        return (float)linePos;
    }
}