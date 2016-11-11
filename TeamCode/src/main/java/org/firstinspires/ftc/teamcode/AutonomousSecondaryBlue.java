/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.opencv.android.OpenCVLoader;

/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a PushBot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Blue Auto", group="Main")  // @Autonomous(...) is the other common choice
public class AutonomousSecondaryBlue extends LinearOpMode {

    /* Declare OpMode members. */
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
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

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
        AutoLib.Sequence mSequence = new AutoLib.LinearSequence();
        // drive a full square diagonnslly forward
        mSequence.add(new AutoLib.MoveSquirrelyByTimeStep(mMotors, 45, power, 0.6, true));
        mSequence.add(new AutoLib.VuforiaSquirrelyDriveStep(this, new VectorF(-1500,300,targetZ), Vuf, Vuf, mMotors, power, error));    // Wheels
        //pushy pushy
        //drive to the second beacon
        AutoLib.Sequence mSequence2 = new AutoLib.LinearSequence();
        mSequence2.add(new AutoLib.VuforiaSquirrelyDriveStep(this, new VectorF(-1500,-914,targetZ), Vuf, Vuf, mMotors, power, error));   // Legos
        //pushy pushy
        AutoLib.Sequence mSequence3 = new AutoLib.LinearSequence();
        mSequence3.add(new AutoLib.VuforiaSquirrelyDriveStep(this, new VectorF((int)(-footToMm * 2), (int)(footToMm * 2),targetZ), Vuf, Vuf, mMotors, power, error));// yoga ball
        mSequence3.add(new AutoLib.MoveByTimeStep(mMotors, 0.0, 0, true));

        // start out not-done
        boolean bDone = false;

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        Vuf.start();

        // until we're done, keep looping through the current Step(s)
        while (!bDone) {
            Vuf.loop(true);
            bDone = mSequence.loop();       // returns true when we're done
            telemetry.update();
        }

        telemetry.addData("First sequence finished", "");

        pushypushy();

        bDone = false;

        while (!bDone) {
            Vuf.loop(true);
            bDone = mSequence2.loop();       // returns true when we're done
            telemetry.update();
        }

        telemetry.addData("Second sequence finished", "");

        pushypushy();

        bDone = false;

        while (!bDone) {
            Vuf.loop(true);
            bDone = mSequence2.loop();       // returns true when we're done
            telemetry.update();
        }

        telemetry.addData("Third sequence finished", "");

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            Vuf.loop(true);
            telemetry.update();
        }
        Vuf.stop();
    }

    //function which handles pushing of beacons
    //assumes robot is in front of the beacon
    private void pushypushy(){
        //extend color sensor
        //decide
        //pushy
        return;
    }
}
