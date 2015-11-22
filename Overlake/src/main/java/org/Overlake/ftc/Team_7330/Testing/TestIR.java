/*
 * Copyright (c) 2014, 2015 Qualcomm Technologies Inc
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * (subject to the limitations in the disclaimer below) provided that the following conditions are
 * met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 * and the following disclaimer in the documentation and/or other materials provided with the
 * distribution.
 *
 * Neither the name of Qualcomm Technologies Inc nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS LICENSE. THIS
 * SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.overlake.ftc.team_7330.Testing;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;


/**
 * Follow an IR Beacon
 * <p>
 * How to use: <br>
 * Make sure the Modern Robotics IR beacon is off. <br>
 * Set it to 1200 at 180.  <br>
 * Make sure the side of the beacon with the LED on is facing the robot. <br>
 * Turn on the IR beacon. The robot will now follow the IR beacon. <br>
 * To stop the robot, turn the IR beacon off. <br>
 */
@TeleOp(name="TestIR")
public class TestIR extends OpMode {

    IrSeekerSensor irSeeker;
    int loopCount = 0;
    double oldAngle = -1;
    double oldStrength = -1;
    boolean oldSignalDetected = true;

    @Override
    public void init() {
        irSeeker = hardwareMap.irSeekerSensor.get("ir_seeker");
    }

    @Override
    public void loop() {
        double angle = 0;
        double strength = 0;
        boolean signalDetected = false;

        // Is an IR signal detected?
        if (irSeeker.signalDetected()) {
            // an IR signal is detected

            // Get the angle and strength of the signal
            angle = irSeeker.getAngle();
            strength = irSeeker.getStrength();
            signalDetected = true;
        }
        if (oldAngle != angle) {
            telemetry.addData("angle", angle);
        }

        if (oldStrength != strength){
            telemetry.addData("strength", strength);
        }

        if (loopCount == 0 || oldSignalDetected != signalDetected){
            telemetry.addData("signalDetected",signalDetected);
        }

        oldAngle = angle;
        oldStrength = strength;
        oldSignalDetected = signalDetected;
        loopCount ++;
        DbgLog.msg(irSeeker.toString());
    }
}
