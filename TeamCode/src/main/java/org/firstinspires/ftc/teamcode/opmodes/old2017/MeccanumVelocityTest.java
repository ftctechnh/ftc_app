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

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardwareOld;
import org.firstinspires.ftc.teamcode.libraries.SensorLib;
import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.PushyLib;

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

@Autonomous(name="Blue Auto", group="Main")  // @Autonomous(...) is the other common choice
@Disabled
public class MeccanumVelocityTest extends OpMode {

    BotHardwareOld robot = new BotHardwareOld();
    /* Declare OpMode members. */

    //some constants to make navigating the field easier
    static final double mmToEncode = 1; //TODO: Find this value
    static final double inchToMm = 25.4;
    static final double footToMm = inchToMm * 12;
    static final double squareToMm = footToMm * 2;

    final float power = 0.2f;

    //constants for pushy pushy
    final double pushPos = 1.0;
    final double time = 2.0;
    final boolean red = false;
    final int colorThresh = 200;
    final float driveTime = 0.1f;
    final int driveLoopCount = 2;

    final boolean debug = false;

    // parameters of the PID controller for this sequence's first part
    final float Kp = 1.0f;        // degree heading proportional term correction per degree of deviation
    final float Ki = 0.2f;         // ... integrator term
    final float Kd = 0;             // ... derivative term
    final float KiCutoff = 1.0f;    // maximum angle error for which we update integrator

    SensorLib.PID mdPid = new SensorLib.PID(Kp, Ki, Kd, KiCutoff);

    // create the root Sequence for this autonomous OpMode
    AutoLib.Sequence mSequence = new AutoLib.LinearSequence();

    // start out not-done
    boolean bDone = false;

    @Override
    public void init(){
        robot.init(this, debug);

        // drive to the first beacon
        //mSequence.add(new SquirrleyAzimuthTimedDriveStep(this, -90.0f, robot.getNavXHeadingSensor(), mdPid, robot.getMotorArray(), power, 10000, true));
        //mSequence.add(new SquirrleyAzimuthTimedDriveStep(this, 0, robot.getNavXHeadingSensor(), mdPid, robot.getMotorArray(), power, 5000, true));

        //line following here

        mSequence.add(new AutoLib.MoveByTimeStep(robot.getMotorArray(), 0.0, 0, true));

        //pushy pushy
        mSequence.add(new PushyLib.pushypushy(this, robot.getMotorArray(), robot.leftSensor, robot.rightSensor, robot.leftServo, robot.rightServo,
                pushPos, time, red, colorThresh, power, driveTime, driveLoopCount)); //SO. MANY. VARIABLES.

        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void start(){
        robot.startNavX();
    }

    @Override
    public void loop(){
        // until we're done, keep looping through the current Step(s)
        while (!bDone) {
            bDone = mSequence.loop();       // returns true when we're done
            telemetry.update();
        }

        telemetry.addData("Sequence finished!", "");
    }

    @Override
    public void stop(){
        robot.navX.close();
    }
}
