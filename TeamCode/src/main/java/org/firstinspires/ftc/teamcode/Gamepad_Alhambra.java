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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


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
public class Gamepad_Alhambra extends LinearOpMode {

    private final double SERVO_CYCLE = 50d;
    private final double SERVO_INCREMENT_MIN = 0.005d;
    private final double SERVO_INCREMENT_MAX = 0.01d;
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private HardwareAlhambra robot = new HardwareAlhambra();
    private ElapsedTime armTime = new ElapsedTime();
    private ElapsedTime handTime = new ElapsedTime();
    private ElapsedTime driveTime = new ElapsedTime();
    private double lastRuntime = 0d;
    private boolean wasBeep = false, doorFlag = false, handFlag = false;
    private boolean aPressed = false, yPressed = false;
    private int armSequence = 0;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        robot.beep();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        new Thread() {
            public void run() {
                while (opModeIsActive()) {// Show the elapsed game time and wheel power.
                    double elapsed = runtime.milliseconds() - lastRuntime;
                    lastRuntime = runtime.milliseconds();
                    telemetry.addData("Status", "Run Time: %s (%.1f ms)",
                            runtime.toString(), elapsed);
                    telemetry.addData("Motors", "left (%.4f), right (%.4f), arm (%.4f)",
                            robot.leftDrive.getPower(), robot.rightDrive.getPower(), robot.armDrive.getPower());
                    telemetry.addData("Servo", "Arm (%.4f) Hand (%.4f)",
                            robot.armServo.getPosition(), robot.handServo.getPosition());
                    telemetry.addData("Arm", "pos (%.3f)",
                            robot.armAngle.getVoltage());
                    /*
                    telemetry.addData("Heading", "%.4f", robot.getHeading());
                    telemetry.addData("Distance (inch)",
                            String.format(Locale.US, "%.2f", robot.sensorDistance.getDistance(DistanceUnit.INCH)));
                    */
                    telemetry.update();
                }
            }
        }.start();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            ArmControl();

            DriveControl();
        }
    }

    private void DriveControl() {
        double drive = 0d;
        double turn = 0d;
        if (gamepad1.dpad_down || gamepad2.dpad_down) {
            drive = -0.5d - (driveTime.milliseconds() / 800d);
        } else if (gamepad1.dpad_up || gamepad2.dpad_up) {
            drive = 0.5d + (driveTime.milliseconds() / 800d);
        }

        if (gamepad1.dpad_right || gamepad2.dpad_right) {
            turn = -0.5d - (driveTime.milliseconds() / 1200d);
        } else if (gamepad1.dpad_left || gamepad2.dpad_left) {
            turn = 0.5d + (driveTime.milliseconds() / 1200d);
        }

        if (!gamepad1.dpad_down && !gamepad2.dpad_down &&
                !gamepad1.dpad_up && !gamepad2.dpad_up &&
                !gamepad1.dpad_right && !gamepad2.dpad_right &&
                !gamepad1.dpad_left && !gamepad2.dpad_left) {
            driveTime.reset();
        }

        double leftPower = Range.clip(drive - turn, -1d, 1d);
        double rightPower = Range.clip(drive + turn, -1d, 1d);

        // Send calculated power to wheels
        robot.leftDrive.setPower(leftPower);
        robot.rightDrive.setPower(rightPower);
    }

    private void ArmControl() {
        double armPower = 0d;
        double armPosition = robot.armServo.getPosition();
        double handPosition = robot.handServo.getPosition();

        if (gamepad2.left_bumper || gamepad1.left_bumper) { //set arm to drop mineral
            if (armSequence == 0) {
                HardwareAlhambra.ArmInfo armInfo = robot.setArmTarget(1.5d);
                armPower = armInfo.PowerToSet;
                if (armInfo.Done) {
                    armPosition = 0.25d;
                    armSequence = 1;
                }
            }
            if (armSequence == 1) {
                armPower = robot.setArmTarget(1d).PowerToSet;
            }
        } else if (gamepad2.right_bumper || gamepad1.right_bumper) { //set arm to pickup mineral
            armPower = robot.setArmTarget(2.329d).PowerToSet;
            armPosition = 0.7578d;
            handPosition = 0.1284d;
        } else {
            robot.armDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            wasBeep = false;
            armSequence = 0;

            if (gamepad2.left_stick_y < 0d && armTime.milliseconds() > SERVO_CYCLE) {
                armTime.reset();
                armPosition += Range.scale(gamepad2.left_stick_y, 0d, -1d, SERVO_INCREMENT_MIN, SERVO_INCREMENT_MAX);
            } else if (gamepad2.left_stick_y > 0d && armTime.milliseconds() > SERVO_CYCLE) {
                armTime.reset();
                armPosition -= Range.scale(gamepad2.left_stick_y, 0d, 1d, SERVO_INCREMENT_MIN, SERVO_INCREMENT_MAX);
            }

            if (gamepad2.right_stick_y < 0d && handTime.milliseconds() > SERVO_CYCLE) {
                handTime.reset();
                handPosition += Range.scale(gamepad2.right_stick_y, 0d, -1d, SERVO_INCREMENT_MIN, SERVO_INCREMENT_MAX);
            } else if (gamepad2.right_stick_y > 0d && handTime.milliseconds() > SERVO_CYCLE) {
                handTime.reset();
                handPosition -= Range.scale(gamepad2.right_stick_y, 0d, 1d, SERVO_INCREMENT_MIN, SERVO_INCREMENT_MAX);
            }

            if (gamepad2.left_trigger > 0d) { //Arm going up
                armPower = Range.clip(gamepad2.left_trigger, 0d, 1d);
            } else if (gamepad2.right_trigger > 0d) { //Arm going down
                armPower = -Range.clip(gamepad2.right_trigger, 0d, 1d);
            } else if (gamepad1.left_trigger > 0d) { //Arm going up
                armPower = Range.clip(gamepad1.left_trigger, 0d, 1d);
            } else if (gamepad1.right_trigger > 0d) { //Arm going down
                armPower = -Range.clip(gamepad1.right_trigger, 0d, 1d);
            }
        }

        if (gamepad2.a || gamepad1.a) {
            if (!aPressed) {
                aPressed = true;
                doorFlag = !doorFlag;
                robot.doorServo.setPosition(doorFlag ? 0.3d : 1d);
            }
        } else {
            aPressed = false;
        }

        if (gamepad2.y || gamepad1.y) {
            if (!yPressed) {
                yPressed = true;
                handFlag = !handFlag;
                handPosition = handFlag ? 0.5d : 0.15d;
            }
        } else {
            yPressed = false;
        }

        robot.setArmPower(armPower);
        robot.armServo.setPosition(armPosition);
        robot.handServo.setPosition(handPosition);
    }
}
