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
package org.firstinspires.ftc.teamcode.opmodes.old2017;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.vuforia.Image;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.libraries.OpenCVLib;
import org.firstinspires.ftc.teamcode.libraries.VuforiaLib_FTC2016;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;

import java.nio.ByteBuffer;

/**
 * This OpMode illustrates the basics of using the VuforiaLib_FTC2016 library to determine
 * positioning and orientation of robot on the FTC field.
 */

@Autonomous(name="OpenCV with Vuforia", group ="Test")
@Disabled
public class OpenCVVuforia extends OpenCVLib {

    VuforiaLib_FTC2016 mVLib;

    @Override public void init() {
        /**
         * Start up Vuforia using VuforiaLib_FTC2016
         */
        mVLib = new VuforiaLib_FTC2016();
        mVLib.init(this, null);     // pass it this OpMode (so it can do telemetry output) and use its license key for now

        initOpenCV();
    }

    @Override public void start()
    {
        /** Start tracking the data sets we care about. */
        mVLib.start();
    }

    @Override public void loop()
    {
        VuforiaLocalizer.CloseableFrame frame;
        mVLib.loop(true);       // update location info and do debug telemetry
        if(!mVLib.getFrames().isEmpty()){
            frame = mVLib.getFrames().poll();

            Image img = frame.getImage(0);

            ByteBuffer buf = img.getPixels();

            //this currently throws an unsupportedOperation exception
            byte[] ray = buf.array();
            Mat m = new Mat();
            m.put(0,0,ray);
            telemetry.addData("Mat Width:", m.width());
            telemetry.addData("Mat Height:", m.height());
        }

    }

    @Override public void stop()
    {
        mVLib.stop();
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame frame){
        return frame.gray();
    }

}
