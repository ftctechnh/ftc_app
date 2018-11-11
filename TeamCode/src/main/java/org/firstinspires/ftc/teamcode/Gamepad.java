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
 * <p>
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 * <p>
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name = "Gamepad", group = "Linear Opmode")
//@Disabled
public class Gamepad extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    Hardware15091 robot = new Hardware15091();
    private ElapsedTime armtime = new ElapsedTime();
    private ElapsedTime handtime = new ElapsedTime();
    private final double SERVO_CYCLE = 50d;
    private final double SERVO_INCREMENT_MIN = 0.005d;
    private final double SERVO_INCREMENT_MAX = 0.01d;

    // Setup a variable for each drive wheel to save power level for telemetry
    double leftPower;
    double rightPower;
    double drive, turn;
    int armsequence = 0;

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
            drive = gamepad1.right_stick_y;
            turn = gamepad1.left_stick_x;
            if (gamepad1.dpad_down || gamepad2.dpad_down) {
                drive = 1d;
            } else if (gamepad1.dpad_up || gamepad2.dpad_up) {
                drive = -1d;
            }

            if (gamepad1.dpad_right || gamepad2.dpad_right) {
                turn = 1d;
            } else if (gamepad1.dpad_left || gamepad2.dpad_left) {
                turn = -1d;
            }

            ArmControl();

            leftPower = Range.scale(drive - turn, -1d, 1d, -1.0, 1.0);
            rightPower = Range.scale(drive + turn, -1d, 1d, -1.0, 1.0);

            // Send calculated power to wheels
            robot.leftDrive.setPower(leftPower);
            robot.rightDrive.setPower(rightPower);

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "left (%.4f), right (%.4f)", leftPower, rightPower);
            telemetry.addData("Servo", "Arm (%.4f) Hand (%.4f)", robot.armServo.getPosition(), robot.handServo.getPosition());
            telemetry.addData("Arm", "pos (%.4f) pow (%.4f) enc (%d)", robot.armAngle.getVoltage(), robot.armDrive.getPower(), robot.armDrive.getCurrentPosition());
            telemetry.update();
        }
    }

    void ArmControl() {
        double p = 0d;

        double armPosition = robot.armServo.getPosition();
        double handPosition = robot.handServo.getPosition();

        if (gamepad2.left_trigger > 0d && robot.armAngle.getVoltage() > robot.ARM_MIN) { //Arm going up
            double m = robot.armAngle.getVoltage() < robot.ARM_MIN + 0.15d ? robot.ARM_POWER : 1d;
            p = Range.scale(gamepad2.left_trigger, 0d, 1d, 0d, m);
        } else if (gamepad2.right_trigger > 0d && robot.armAngle.getVoltage() < robot.ARM_MAX) { //Arm going down
            double m = robot.armAngle.getVoltage() > robot.ARM_MAX - 0.15d ? robot.ARM_POWER : 1d;
            p = -Range.scale(gamepad2.right_trigger, 0d, 1d, 0d, m);
        }

        if (gamepad2.left_bumper) {
            int gap = robot.setArmTarget(1.2860d);
            p = robot.ARM_POWER;
            if (gap < 100) {
                armPosition = 0.3239d;
                handPosition = 0.3856d;
            }
        } else if (gamepad2.right_bumper) {
            int gap = robot.setArmTarget(2.1030d);
            p = robot.ARM_POWER;
            if (gap < 100) {
                armPosition = 0.0739d;
                handPosition = 0.8528d;
            }
        } else if (gamepad2.x) {
            if (armsequence == 0) {
                int howmanyTurnLeft = robot.setArmTarget(0.9800d);
                p = 0.2d;
                if (howmanyTurnLeft <= 100) {
                    handPosition = 0;
                    armPosition = 1;
                    armsequence = 1;
                }
            }

            if (armsequence == 1) {
                robot.setArmTarget(0.7540d);
                p = 0.2d;
            }
        } else {
            armsequence = 0;
            robot.armDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        if (gamepad2.a) {
            robot.pickupServo.setPosition(1d);
        } else if (gamepad2.b) {
            robot.pickupServo.setPosition(0d);
        } else {
            robot.pickupServo.setPosition(0.5d);
        }


        if (gamepad2.left_stick_y < 0d && armtime.milliseconds() > SERVO_CYCLE) {
            armtime.reset();
            armPosition += Range.scale(gamepad2.left_stick_y, 0d, -1d, SERVO_INCREMENT_MIN, SERVO_INCREMENT_MAX);
        } else if (gamepad2.left_stick_y > 0d && armtime.milliseconds() > SERVO_CYCLE) {
            armtime.reset();
            armPosition -= Range.scale(gamepad2.left_stick_y, 0d, 1d, SERVO_INCREMENT_MIN, SERVO_INCREMENT_MAX);
        }

        if (gamepad2.right_stick_y < 0d && armtime.milliseconds() > SERVO_CYCLE) {
            handtime.reset();
            handPosition += Range.scale(gamepad2.right_stick_y, 0d, -1d, SERVO_INCREMENT_MIN, SERVO_INCREMENT_MAX);
        } else if (gamepad2.right_stick_y > 0d && armtime.milliseconds() > SERVO_CYCLE) {
            handtime.reset();
            handPosition -= Range.scale(gamepad2.right_stick_y, 0d, 1d, SERVO_INCREMENT_MIN, SERVO_INCREMENT_MAX);
        }


        robot.armDrive.setPower(p);
        robot.armServo.setPosition(armPosition);
        robot.handServo.setPosition(handPosition);
    }
}
