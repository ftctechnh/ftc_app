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

    VuforiaLib_FTC2016 Vuf;

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
        final float power = 0.5f;
        final float error = 254.0f;       // get us within 10" for this test
        final float angleError = 5.0f;    //and within 5 degrees for turning
        final float targetZ = 6*25.4f;

        //constants for pushy pushy
        final double readPos = 0.2;
        final double pushPos = 0.4;
        final double time = 2.0;
        final boolean red = false;

        final boolean debug = false;

        boolean lastState = false;

        robot.init(this, debug);

        //init vuforia
        Vuf = new VuforiaLib_FTC2016();
        Vuf.init(this, null);     // pass it this OpMode (so it can do telemetry output) and use its license key for now

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // create the root Sequence for this autonomous OpMode
        AutoLib.DebugLinearSequence mSequence = new AutoLib.DebugLinearSequence(this);  //TODO: Un-Debug
        // drive to the first beacon
        mSequence.add(new AutoLib.MoveSquirrelyByTimeStep(robot.getMotorArray(), 45, power, 0.6, true));
        mSequence.add(new AutoLib.VuforiaSquirrelyDriveStep(this, new VectorF(-1500,300,targetZ), Vuf, Vuf, robot.getMotorArray(), power, error));    // Wheels
        mSequence.add(new AutoLib.GyroTurnStep(this, -90, Vuf, robot.getMotorArray(), power, angleError, true));
        //pushy pushy
        mSequence.add(new pushypushy(robot.leftSensor, robot.rightSensor, robot.leftServo, robot.rightServo, readPos, pushPos, time, red)); //SO. MANY. VARIABLES.
        //drive to the second beacon
        mSequence.add(new AutoLib.MoveSquirrelyByTimeStep(robot.getMotorArray(), 90, power, 0.6, true));
        mSequence.add(new AutoLib.VuforiaSquirrelyDriveStep(this, new VectorF(-1500,-914,targetZ), Vuf, Vuf, robot.getMotorArray(), power, error));   // Legos
        mSequence.add(new AutoLib.GyroTurnStep(this, -90, Vuf, robot.getMotorArray(), power, angleError, true));
        //pushy pushy
        mSequence.add(new pushypushy(robot.leftSensor, robot.rightSensor, robot.leftServo, robot.rightServo, readPos, pushPos, time, red));
        //drive to yoga ball
        mSequence.add(new AutoLib.VuforiaSquirrelyDriveStep(this, new VectorF((int)(-footToMm * 2), (int)(footToMm * 2),targetZ), Vuf, Vuf, robot.getMotorArray(), power, error));// yoga ball
        mSequence.add(new AutoLib.MoveByTimeStep(robot.getMotorArray(), 0.0, 0, true));

        // start out not-done
        boolean bDone = false;

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        Vuf.start();

        // until we're done, keep looping through the current Step(s)
        while (!bDone) {
            Vuf.loop(true);
            bDone = mSequence.loop();       // returns true when we're done

            //increment sequence on button push
            if(lastState != gamepad1.a){
                if(gamepad1.a) mSequence.incStep();
                lastState = gamepad1.a;
            }

            telemetry.update();
        }

        telemetry.addData("Sequence finished!", "");

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            Vuf.loop(true);
            telemetry.update();
        }
        Vuf.stop();
    }

    //function which handles pushing of beacons
    //assumes robot is in front of the beacon

    private class pushypushy extends AutoLib.LinearSequence{

        pushypushy(ColorSensor leftSensor, ColorSensor rightSensor, Servo leftServo, Servo rightServo, double readPos, double pushPos, double time, boolean red){
            //extend the right and left servo arms so color sensors get an accurate reading
            AutoLib.ConcurrentSequence servoExtend = new AutoLib.ConcurrentSequence();
            servoExtend.add(new AutoLib.TimedServoStep(leftServo, readPos, time, false));
            servoExtend.add(new AutoLib.TimedServoStep(rightServo, readPos, time, false));
            this.add(servoExtend);

            //run color detection and pushing
            this.add(new pushyDetect(leftSensor, rightSensor, leftServo, rightServo, pushPos, time, red));

            //pull servos back to default position
            AutoLib.ConcurrentSequence servoDetract = new AutoLib.ConcurrentSequence();
            servoDetract.add(new AutoLib.TimedServoStep(leftServo, leftServo.getPosition(), time, false));
            servoDetract.add(new AutoLib.TimedServoStep(rightServo, rightServo.getPosition(), time, false));
            this.add(servoDetract);
        }

    }

    private class pushyDetect extends AutoLib.Step {
        ColorSensor mLeftSensor;
        ColorSensor mRightSensor;
        Servo mLeftServo;
        Servo mRightServo;
        double mPushPos;
        AutoLib.Timer mTime;
        boolean mRed;

        public pushyDetect(ColorSensor leftSensor, ColorSensor rightSensor, Servo leftServo, Servo rightServo, double pushPos, double time, boolean red){
            mLeftSensor = leftSensor;
            mRightSensor = rightSensor;
            mLeftServo = leftServo;
            mRightServo = rightServo;
            mPushPos = pushPos;
            mTime = new AutoLib.Timer(time);
            mRed = red;
        }

        public boolean loop(){
            boolean left = false;
            if(firstLoopCall()){
                //compare sensor values
                if(mRed){
                    left = mLeftSensor.red() > mRightSensor.red();
                }
                else{
                    left = mLeftSensor.blue() > mRightSensor.blue();
                }

                //start servo timer
                mTime.start();
            }

            //if left side is color, push left, else push right
            if(left) mLeftServo.setPosition(mPushPos);
            else mRightServo.setPosition(mPushPos);

            //finish when time is up
            return mTime.done();
        }
    }
}
