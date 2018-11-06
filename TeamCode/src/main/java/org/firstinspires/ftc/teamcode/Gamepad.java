/* Copyright (c) 2017 FIRST. All rights reserved.
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

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.android.AndroidTextToSpeech;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Gamepad", group="Linear Opmode")
//@Disabled
public class Gamepad extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    Hardware15091 robot = new Hardware15091();
    boolean gamapad2_x_state = false;
    boolean gamapad2_b_state = false;
    boolean gamapad2_left_bumper_state = false;
    boolean gamapad2_right_bumper_state = false;
    double servo1Timer;

    // Setup a variable for each drive wheel to save power level for telemetry
    double leftPower;
    double rightPower;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            User1();

            User2();

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.addData("Servo", "Arm (%.2f) Hand (%.2f)", robot.armServo.getPosition(), robot.handServo.getPosition());
            telemetry.addData("Arm", "pos (%.2f) pow (%.2f) enc (%d)", robot.armAngle.getVoltage(), robot.armDrive.getPower(), robot.armDrive.getCurrentPosition());
            telemetry.update();
        }
    }

    void User1()
    {
        // Choose to drive using either Tank Mode, or POV Mode
        // Comment out the method that's not used.  The default below is POV.

        // POV Mode uses left stick to go forward, and right stick to turn.
        // - This uses basic math to combine motions and is easier to drive straight.
        double maxSpeed = 0.3d;
        if (gamepad1.left_bumper) {
            maxSpeed = 0.5d;
        } else if (gamepad1.right_bumper) {
            maxSpeed = 1d;
        }
        double drive = -gamepad1.right_stick_y;
        double turn  = gamepad1.left_stick_x;
        leftPower    = Range.scale(drive + turn, -1d, 1d, -maxSpeed, maxSpeed) ;
        rightPower   = Range.scale(drive - turn, -1d, 1d, -maxSpeed, maxSpeed) ;

        // Tank Mode uses one stick to control each wheel.
        // - This requires no math, but it is hard to drive forward slowly and keep straight.
        // leftPower  = -gamepad1.left_stick_y ;
        // rightPower = -gamepad1.right_stick_y ;

        // Send calculated power to wheels
        robot.leftDrive.setPower(leftPower);
        robot.rightDrive.setPower(rightPower);
    }

    void User2() {
        double p = 0d;
        double s1_auto = robot.armServo.getPosition(), s1_manual = robot.armServo.getPosition();

        if (gamepad2.left_trigger > 0d && robot.armAngle.getVoltage() > robot.ARM_MIN) { //Arm going up
            double m = robot.armAngle.getVoltage() < robot.ARM_MIN + 0.15d ? robot.ARM_POWER : 1d;
            p = Range.scale(gamepad2.left_trigger, 0d, 1d, 0d, m);
        } else if (gamepad2.right_trigger > 0d && robot.armAngle.getVoltage() < robot.ARM_MAX) { //Arm going down
            double m = robot.armAngle.getVoltage() > robot.ARM_MAX - 0.15d ? robot.ARM_POWER : 1d;
            p = -Range.scale(gamepad2.right_trigger, 0d, 1d, 0d, m);
        }

        if (robot.armSequence > 0 && robot.armSequence <= 3) {
            if (robot.armSequence == 3) {
                robot.autoArm = false;
                s1_manual = 0d;
                robot.armDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.armDrive.setTargetPosition(-7800);
                robot.armSequence = 2;
            }
            if (robot.armSequence == 2 && !robot.armDrive.isBusy()) {
                robot.armSequence = 1;
            }
            if (robot.armSequence == 1) {
                robot.tts.speak("Hook sequence complete.");
                robot.armSequence = 0;
                robot.armDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }
            p = 0.45d;
        }

        if (robot.armSequence > 3 && robot.armSequence <= 6) {
            if (robot.armSequence == 6) {
                robot.armDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.armDrive.setTargetPosition(-16330);
                robot.armSequence = 5;
            }
            if (robot.armSequence == 5 && !robot.armDrive.isBusy()) {
                robot.armSequence = 4;
            }
            if (robot.armSequence == 4) {
                robot.armSequence = 0;
                robot.armDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }
            p = 1d;
        }

        if (robot.armSequence > 6 && robot.armSequence <= 9) {
            if (robot.armSequence == 9) {
                robot.armDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.armDrive.setTargetPosition(-16330);
                robot.armSequence = 8;
            }
            if (robot.armSequence == 8 && !robot.armDrive.isBusy()) {
                robot.armSequence = 7;
            }
            if (robot.armSequence == 7) {
                robot.armSequence = 0;
                robot.armDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }
            p = 1d;
        }

        if (gamepad2.left_bumper != gamapad2_left_bumper_state) {
            gamapad2_left_bumper_state = gamepad2.left_bumper;
            if (gamepad2.left_bumper) {
                robot.initateShoot();
            }
        }
        else {
            gamapad2_left_bumper_state = gamepad2.left_bumper;
        }

        if (gamepad2.right_bumper != gamapad2_right_bumper_state) {
            gamapad2_right_bumper_state = gamepad2.right_bumper;
            if (gamepad2.right_bumper) {
                robot.initateScoop();
            }
        }
        else {
            gamapad2_right_bumper_state = gamepad2.right_bumper;
        }

        robot.armDrive.setPower(p);

        if (robot.armAngle.getVoltage() > robot.ARM_MAX || robot.armAngle.getVoltage() < (robot.ARM_MIN + 0.3d)) {
            s1_auto = 0d;
        } else if (robot.armAngle.getVoltage() > (robot.ARM_MIN + 0.3d) && robot.armAngle.getVoltage() < 1.72d) {
            s1_auto = 1d;
        } else if (robot.armAngle.getVoltage() > 1.75d && robot.armAngle.getVoltage() < robot.ARM_MAX) {
            s1_auto = Range.scale(robot.armAngle.getVoltage(), 1.75d, robot.ARM_MAX, 1d, 0.65d);
        }

        if (gamepad2.y && (runtime.milliseconds() - servo1Timer) > robot.ARM_SERVO_SPEED) {
            servo1Timer = runtime.milliseconds();
            s1_manual = robot.armServo.getPosition() + 0.015d;
        } else if (gamepad2.a && (runtime.milliseconds() - servo1Timer) > robot.ARM_SERVO_SPEED) {
            servo1Timer = runtime.milliseconds();
            s1_manual = robot.armServo.getPosition() - 0.015d;
        }

        if (gamepad2.x != gamapad2_x_state) {
            gamapad2_x_state = gamepad2.x;
            if (gamepad2.x) {
                robot.toggleAutoArm();

                if (!robot.autoArm) {
                    s1_manual = 0d;
                }
            }
        }
        else {
            gamapad2_x_state = gamepad2.x;
        }

        if (gamepad2.b != gamapad2_b_state) {
            gamapad2_b_state = gamepad2.b;
            if (gamepad2.b) {
                robot.initiateHook();
            }
        }
        else {
            gamapad2_b_state = gamepad2.b;
        }

        if (robot.autoArm) {
            robot.armServo.setPosition(s1_auto);
        } else {
            robot.armServo.setPosition(s1_manual);
        }

        double s2 = Range.scale(gamepad2.left_stick_x, -1d, 1d, 0.0d, 1d);
        double s3 = 0.8d, s3_mid = 0.8d;

        if (robot.autoArm) {
            if (robot.armAngle.getVoltage() < 1.75d && robot.armAngle.getVoltage() > 0.75d) {
                s3 = s3_mid = Range.scale(robot.armAngle.getVoltage(), 1.75d, 0.75d, 1d, 0.45d);
            } else if (robot.armAngle.getVoltage() < robot.ARM_MAX && robot.armAngle.getVoltage() > 1.75d) {
                s3 = 1d;
            }

            if (robot.armAngle.getVoltage() < (robot.ARM_MIN + 0.3d)) {
                s3 = 0d;
            }
        } else {
            s3 = s3_mid = 0d;
        }

        if (gamepad2.left_stick_y > 0d) {
            s3 = Range.scale(gamepad2.left_stick_y, 0d, 1d, s3_mid, 1d);
        } else if (gamepad2.left_stick_y < 0d) {
            s3 = Range.scale(gamepad2.left_stick_y, -1d, 0d, 0.0d, s3_mid);
        }

        robot.netServo.setPosition(s2);
        robot.handServo.setPosition(s3);

    }
}
