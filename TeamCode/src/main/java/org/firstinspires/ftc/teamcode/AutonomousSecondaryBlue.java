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

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
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

@Autonomous(name="Blue Auto", group="Main")  // @Autonomous(...) is the other common choice
public class AutonomousSecondaryBlue extends LinearOpMode {

    BotHardware robot = new BotHardware();
    /* Declare OpMode members. */

    //some constants to make navigating the field easier
    static final double mmToEncode = 1; //TODO: Find this value
    static final double inchToMm = 25.4;
    static final double footToMm = inchToMm * 12;
    static final double squareToMm = footToMm * 2;

    // parameters of the PID controller for this sequence
    final float Kp = 0.035f;        // motor power proportional term correction per degree of deviation
    final float Ki = 0.02f;         // ... integrator term
    final float Kd = 0;             // ... derivative term
    final float KiCutoff = 3.0f;    // maximum angle error for which we update integrator

    @Override
    public void runOpMode() {
        // / create an autonomous sequence with the steps to drive
        final float power = 0.2f;

        //constants for pushy pushy
        final double pushPos = 1.0;
        final double time = 2.0;
        final boolean red = false;
        final int colorThresh = 200;
        final float driveTime = 0.1f;
        final int driveLoopCount = 2;

        final boolean debug = false;

        robot.init(this, debug);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // create the root Sequence for this autonomous OpMode
        AutoLib.Sequence mSequence = new AutoLib.LinearSequence();
        // drive to the first beacon



        mSequence.add(new AutoLib.TurnByTimeStep(robot.frontRightMotor, robot.backRightMotor, robot.frontLeftMotor, robot.backLeftMotor, power, -power, 1.5, true));  //turn
        mSequence.add(new AutoLib.MoveByTimeStep(robot.getMotorArray(), power, 3.0, true)); //drive towards beacon

        //line following here

        mSequence.add(new AutoLib.MoveByTimeStep(robot.getMotorArray(), 0.0, 0, true));

        //pushy pushy
        mSequence.add(new PushyLib.pushypushy(robot.getMotorArray(), robot.leftSensor, robot.rightSensor, robot.leftServo, robot.rightServo,
                    pushPos, time, red, colorThresh, power, driveTime, driveLoopCount)); //SO. MANY. VARIABLES.

        // start out not-done
        boolean bDone = false;

        // Wait for the game to start (driver presses PLAY)
        waitForStart();


        // until we're done, keep looping through the current Step(s)
        while (!bDone) {
            bDone = mSequence.loop();       // returns true when we're done

            telemetry.update();
        }

        telemetry.addData("Sequence finished!", "");

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.update();
        }
    }
}
