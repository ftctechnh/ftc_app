/* Copyright (c) 2019 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;


import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Mat;

import detectors.FoundationPipeline.SkyStone;
import detectors.ImageDetector;

import java.util.Iterator;
import java.util.List;

import detectors.ImageDetector;
import detectors.OpenCvDetector;

import detectors.StoneDetector;

/*

    If you're using this library, THANKS! I spent a lot of time on it.

    However, stuff isn't as well-documented as I like...still working on that

    So if you have questions, email me at xchenbox@gmail.com and I will get back to you in about a day (usually)

    Enjoy!

    Below is the Opmode example, which shows all the features of the library. 99% of my time has
    been spent on the OpenCVDetector, so don't criticize the 10 minutes I spent on the other stuff...

 */

@TeleOp(name = "The Three <<Holy Systems>>", group = "Primordial Artifact")
public class DuoDou extends LinearOpMode {

    public void runOpMode() throws InterruptedException {

        telemetry.setAutoClear(true);

        telemetry.addData("Booting Up", " . . .");
        telemetry.update();
		OpenCvDetector fieldDetector = new OpenCvDetector(this);
        //ImageDetector detector = new ImageDetector(this, false);
        //StoneDetector stone = new StoneDetector(this, false);

        //stone.start();
        //detector.start();
        fieldDetector.start();
        //imu.start();tr

        while (!isStopRequested()) {
            //detector.printposition(detector.getPosition());

            //fieldDetector.print(fieldDetector.getObjectsFoundations());
	        //Log.d("GO TO MO","go");

            SkyStone[] skyStone = fieldDetector.getObjectsSkyStones();

            telemetry.addData("Skystones Found",skyStone.length);
            int i = 0;
            for(SkyStone s : skyStone){
            	i++;
	            telemetry.addLine(""+s);
            }

	        Mat m = new Mat();
            m = null;

            //also, enabling/disabling detection for individual field elements:
            /*
            Pipeline.doFoundations=false;
            Pipeline.doStones=false;
            Pipeline.doSkyStones=false;
            */


            //getting positions of each element. Will return Objects for each element that all contain
            // Phone camera <x,y> coordinates for each element's center.
            /*
            List<Foundation> foundations = Pipeline.foundations;
            List<Stone> stones = Pipeline.stones;
            List<SkyStone> skystones = Pipeline.skyStones;
            */



            //stone.print(stone.getObjectsFoundations());

            //imu.printposition(imu.getDeltaPosition());

            telemetry.addData("==========", "Loop delimiter");

            telemetry.update();
        }

        // Disable Tracking when we are done
        //detector.stop();
        //stone.stop();
        fieldDetector.stop();
        //imu.stop();
    }

    public void listhardware() {
        telemetry.setAutoClear(false);

        Iterator<com.qualcomm.robotcore.hardware.HardwareDevice> t = hardwareMap.iterator();
        while (t.hasNext()) {

            telemetry.addData("device found", (t.next().getDeviceName()));
            telemetry.update();
        }

        telemetry.setAutoClear(true);

    }
}
