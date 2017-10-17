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
package org.firstinspires.ftc.teamcode._Test._Sensors;

import android.hardware.Camera;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode._Libs.CameraLib;
import org.firstinspires.ftc.teamcode._Libs.VuforiaLib_FTC2017;

/**
 * This OpMode illustrates the basics of using the VuforiaLib_FTC2017 library to just determine
 * which column of the shelves to fill first and then shutting down Vuforia and starting up CameraLib
 * to let us use simple scanline searches to find the red and blue jewels.
 */

@Autonomous(name="Test: Vuforia-CameraLib Test 3", group ="Test")
//@Disabled
public class VuforiaNavigationTest3 extends OpMode {

    VuforiaLib_FTC2017 mVLib;
    CameraLib.CameraAcquireFrames mCamAcqFr;

    String mVuMarkString;

    enum Phase { eViewforia, eCameraLib, eError };
    Phase mPhase;

    @Override public void init() {
        /**
         * Start up Vuforia using VuforiaLib_FTC2017
         */
        mVLib = new VuforiaLib_FTC2017();
        mVLib.init(this, null);     // pass it this OpMode (so it can do telemetry output) and use its license key for now

        mPhase = Phase.eViewforia;     // Vuforia phase
        mVuMarkString = "none";
    }

    @Override public void start()
    {
        /** Start tracking the data sets we care about. */
        mVLib.start();
    }

    @Override public void loop()
    {
        if (mPhase == Phase.eViewforia) {
            mVLib.loop();       // update recognition info
            RelicRecoveryVuMark vuMark = mVLib.getVuMark();
            if (vuMark != RelicRecoveryVuMark.UNKNOWN) {

                // Found an instance of the template -- remember which
                mVuMarkString = vuMark.toString();

                // shut down Vuforia - we're done with it
                mVLib.stop();
                //mVLib = null;

                // start normal camera image acquisition
                mCamAcqFr = new CameraLib.CameraAcquireFrames();
                if (mCamAcqFr.init(2) == false)     // init camera at 2nd smallest size
                    mPhase = Phase.eError;
                else
                    mPhase = Phase.eCameraLib;
            }
        }
        else
        if (mPhase == Phase.eCameraLib){
            // get most recent frame from camera (may be same as last time or null)
            CameraLib.CameraImage frame = mCamAcqFr.loop();

            // log debug info ...
            if (frame != null) {
                // log text representations of several significant scanlines
                Camera.Size camSize = frame.cameraSize();
                final int bandSize = 10;
                telemetry.addData("hue a(1/3): ", frame.scanlineHue(camSize.height / 3, bandSize));
                telemetry.addData("hue b(1/2): ", frame.scanlineHue(camSize.height / 2, bandSize));
                telemetry.addData("hue c(2/3): ", frame.scanlineHue(2 * camSize.height / 3, bandSize));
            }
        }
        else
            telemetry.addData("eError:", "cannot start camera");

        if (mVuMarkString != null)
            telemetry.addData("VuMark", "%s found", mVuMarkString);

    }

    @Override public void stop()
    {
        if (mPhase == Phase.eViewforia)
            mVLib.stop();
        else
            mCamAcqFr.stop();
    }

}
