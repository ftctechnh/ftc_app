package org.firstinspires.ftc.teamcode._Test._Drive;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode._Libs.AutoLib;
import org.firstinspires.ftc.teamcode._Libs.CameraLib;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * a simple example of using the camera to steer in autonomous mode.
 * Created by phanau on 12/30/15.
 */

// a simple example of a camera-directed drive step that moves toward or away from a given
// pattern described by its "rgbcymw" signature until it is bigger or smaller than a given size

class CameraDriveStep1 extends AutoLib.Step {

    CameraLib.CameraAcquireFrames mCamAcqFr;    // the camera this step uses
    int mPatternSize;                           // we're done when the (pattern) size crosses this number of bands
    int mBandSize;                              // combine this many pixels into each band (using histogram to find most common rgbcymw value)
    DcMotor mMotors[];                          // the (4) motors we're controlling - assumed order is fr, br, fl, bl
    Pattern mPattern;                           // compiled regexp pattern we'll use to find the pattern we're looking for
    OpMode mOpMode;                             // needed so we can log output
    float mPower;                               // basic power setting of all 4 motors -- adjusted for steering to/from target

    public CameraDriveStep1(OpMode opMode, CameraLib.CameraAcquireFrames camAcqFr, String pattern, DcMotor motors[], float power, int patternSize, int bandSize) {
        mOpMode = opMode;
        mCamAcqFr = camAcqFr;
        mPattern = Pattern.compile(pattern);
        mPower = power;
        mPatternSize = patternSize;
        mMotors = motors;
        mBandSize = bandSize;
    }

    public boolean loop() {
        super.loop();

        mOpMode.telemetry.addData("loop count: ", loopCount());

        // get the current frame from the camera (may be same as previous frame or null)
        CameraLib.CameraImage frame = mCamAcqFr.loop();

        // figure out where the target pattern is and steer toward it -
        // for now, just use the horizontal midline of the frame, using bands of given number of pixels.
        String scanline = frame.scanlineHue(frame.cameraSize().height / 2, mBandSize);

        // log some debug data -- show the raw scanline
        mOpMode.telemetry.addData("scanline(raw): ", scanline);

        // left and right motor power settings based on computed target sighting --
        // default to stopping if we don't find anything.
        double leftPower = 0.0;
        double rightPower = 0.0;

        // search the scanline for the target pattern
        int patternStart = 0;
        int patternSize = 0;
        for (int i=0; i<scanline.length(); i++) {
            // starting at position (i), look for the given pattern in the encoded (rgbcymw) scanline
            Matcher m = mPattern.matcher(scanline.substring(i));
            if (m.lookingAt() && m.groupCount() == 1) {

                patternStart = i + m.start(1);
                patternSize = i + m.end(1) - patternStart;
                int patternCenter = i + (m.start(1) + m.end(1))/2;

                // compute steering correction proportional to how far off center the target is
                double steeringCorrection = ((double)patternCenter - 0.5*scanline.length()) / (0.5*scanline.length());  // -1 .. +1

                // steering correction is reversed when we're backing away from the target
                if (mPower < 0)
                    steeringCorrection = -steeringCorrection;

                // compute right and left motor powers from the steering correction
                final double sensitivity = 0.2;
                leftPower = mPower * (1.0 + steeringCorrection*sensitivity);
                rightPower = mPower * (1.0 - steeringCorrection*sensitivity);

                break;
            }
        }

        // update the motors -- note that we'll stop whenever no pattern is found
        mMotors[0].setPower(rightPower);   // right
        mMotors[1].setPower(rightPower);
        mMotors[2].setPower(leftPower);    // left
        mMotors[3].setPower(leftPower);

        // log some debug data -- show the detected pattern in upper-case
        if (patternSize > 0)
            mOpMode.telemetry.addData("scanline(det): ",
                scanline.substring(0,patternStart) +
                        scanline.substring(patternStart,patternStart+patternSize).toUpperCase() +
                        scanline.substring(patternStart+patternSize)
            );

        // this step is done when the target reaches the desired size --
        // (i.e. we're at the desired distance from it) depending on whether we're going toward
        // it (speed > 0) or away from it (speed < 0)
        boolean bDone = (mPatternSize > 0) &&       // don't quit just because we "lost signal"
                ((mPower > 0) ? (patternSize > mPatternSize) : (patternSize < mPatternSize));

        // when the step is done, turn off all motors
        if (bDone) {
            for (DcMotor motor : mMotors)
                motor.setPower(0);
        }

        // return done-ness
        return (bDone);
    }

}

    @Autonomous(name="Test: Camera Drive Test 1", group ="Test")
    //@Disabled
    public class CameraDriveTest1Op extends OpMode {

    AutoLib.Sequence mSequence;     // the root of the sequence tree
    boolean bDone;                  // true when the programmed sequence is done

    CameraLib.CameraAcquireFrames mCamAcqFr;
    DcMotor mMotors[];

    public CameraDriveTest1Op() {
    }

    public void init() {
        // get the camera that our sequence will use
        mCamAcqFr = new CameraLib.CameraAcquireFrames();
        if (mCamAcqFr.init(1) == false)     // init camera at the smallest available size
            telemetry.addData("error: ", "cannot initialize camera");

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
        mMotors[2] = mf.getDcMotor("fl");
        mMotors[3] = mf.getDcMotor("bl");
        mMotors[2].setDirection(DcMotor.Direction.REVERSE);
        mMotors[3].setDirection(DcMotor.Direction.REVERSE);

        // create an autonomous sequence with two steps in it (for now)
        // create the root Sequence for this autonomous OpMode
        mSequence = new AutoLib.LinearSequence();

        // define the color pattern we're looking for:
        // String pattern = "r+[w0-7]+([cb]+)[^cb]*";    // RED, WHITE, CYAN/BLUE shelter lights pattern
        String pattern = "r+([gc]+)b+";             // RGB stripes (where green may read as cyan)

        // add a camera-directed Step to the root Sequence that advances until the pattern is >5 bands of 6 pixels
        mSequence.add(new CameraDriveStep1(this, mCamAcqFr, pattern, mMotors, 0.5f, 5, 6));

        // add a second camera-directed Step that backs away until the pattern is <2 bands of 6 pixels
        mSequence.add(new CameraDriveStep1(this, mCamAcqFr, pattern, mMotors, -0.5f, 2, 6));

        // start out not-done
        bDone = false;
    }

    public void loop() {
        // until we're done, keep looping through the current Step(s)
        if (!bDone)
            bDone = mSequence.loop();       // returns true when we're done
        else
            telemetry.addData("sequence finished", "");
    }

    public void stop() {
        mCamAcqFr.stop();
        telemetry.addData("stop() called", "");
    }
}


