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
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Locale;


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
    private ElapsedTime armTime = new ElapsedTime();
    private ElapsedTime handTime = new ElapsedTime();
    private final double SERVO_CYCLE = 50d;
    private final double SERVO_INCREMENT_MIN = 0.005d;
    private final double SERVO_INCREMENT_MAX = 0.01d;
    private final double DRIVE_SPEED_MAX = 1d;
    private final double ARM_POWER_MAX = 1d;
    private final double ARM_POWER_MID = 0.6d;
    private final double ARM_POWER_MIN = 0.3d;

    // Setup a variable for each drive wheel to save power level for telemetry
    double leftPower;
    double rightPower;
    double drive, turn;
    int armSequence = 0;

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
                turn = 0.7d;
            } else if (gamepad1.dpad_left || gamepad2.dpad_left) {
                turn = -0.7d;
            }

            ArmControl();

            leftPower = Range.scale(drive - turn, -1d, 1d, -DRIVE_SPEED_MAX, DRIVE_SPEED_MAX);
            rightPower = Range.scale(drive + turn, -1d, 1d, -DRIVE_SPEED_MAX, DRIVE_SPEED_MAX);

            // Send calculated power to wheels
            robot.leftDrive.setPower(leftPower);
            robot.rightDrive.setPower(rightPower);

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "left (%.4f), right (%.4f)",
                    leftPower, rightPower);
            telemetry.addData("Servo", "Arm (%.4f) Hand (%.4f)",
                    robot.armServo.getPosition(), robot.handServo.getPosition());
            telemetry.addData("Arm", "pos (%.4f) pow (%.4f) enc (%d)",
                    robot.armAngle.getVoltage(), robot.armDrive.getPower(), robot.armDrive.getCurrentPosition());
            telemetry.addData("Heading", "%.4f", robot.getHeading());
            telemetry.addData("Distance (cm)",
                    String.format(Locale.US, "%.02f", robot.sensorDistance.getDistance(DistanceUnit.CM)));
            telemetry.update();
        }
    }

    double getArmPower(ARM_STATUS armStatus) {
        switch (armStatus) {
            case ALMOST:
                return ARM_POWER_MIN;
            case CLOSE:
                return ARM_POWER_MID;
            case DONE:
                return 0d;
            default:
                return ARM_POWER_MAX;
        }
    }

    void ArmControl() {
        double armPower = 0d;
        double armPosition = robot.armServo.getPosition();
        double handPosition = robot.handServo.getPosition();

        if (gamepad2.left_trigger > 0d) { //Arm going up
            armPower = Range.scale(gamepad2.left_trigger, 0d, 1d, 0d, 1d);
        } else if (gamepad2.right_trigger > 0d) { //Arm going down
            armPower = -Range.scale(gamepad2.right_trigger, 0d, 1d, 0d, 1d);
        } else if (gamepad1.left_trigger > 0d) { //Arm going up
            armPower = Range.scale(gamepad1.left_trigger, 0d, 1d, 0d, 1d);
        } else if (gamepad1.right_trigger > 0d) { //Arm going down
            armPower = -Range.scale(gamepad1.right_trigger, 0d, 1d, 0d, 1d);
        }

        if (gamepad2.left_bumper || gamepad1.left_bumper || gamepad2.y || gamepad1.y) { //set arm to drop mineral
            if (armSequence == 0) {
                ARM_STATUS status = robot.setArmTarget(1.3050d);
                armPower = getArmPower(status);
                if (status == ARM_STATUS.DONE) {
                    if (gamepad2.left_bumper || gamepad1.left_bumper) {
                        armPosition = 0.3489d;
                        handPosition = 0.3728d;
                    } else {
                        handPosition = 0d;
                        armPosition = 1d;
                    }
                    armSequence = 2;
                }
            }
        } else if (gamepad2.right_bumper || gamepad1.right_bumper) { //set arm to pickup mineral
            if (armSequence == 0) {
                ARM_STATUS status = robot.setArmTarget(2.2600d);
                armPower = getArmPower(status);
                armPosition = 0.1528d;
                handPosition = 0.8261d;
                if (status == ARM_STATUS.DONE) {
                    armSequence = 3;
                }
            }

            /*if (armSequence == 0) {
                ARM_STATUS status = robot.setArmTarget(1.6760d);
                armPower = getArmPower(status);

                if (status == ARM_STATUS.DONE) {
                    armPosition = 0.1528d;
                    handPosition = 0.8261d;
                    armSequence = 3;
                }
            }

            if (armSequence == 3) {
                ARM_STATUS status = robot.setArmTarget(2.2600d);
                armPower = getArmPower(status);
                if (status == ARM_STATUS.DONE) {
                    armSequence = 4;
                }
            }*/
        } else if (gamepad2.x || gamepad1.x)

        { //retract arm
            if (armSequence == 0) {
                ARM_STATUS status = robot.setArmTarget(0.9800d);
                armPower = ARM_POWER_MAX;
                if (status == ARM_STATUS.DONE) {
                    handPosition = 0d;
                    armPosition = 1d;
                    armSequence = 1;
                }
            }

            if (armSequence == 1) {
                robot.setArmTarget(0.7450d);
                armPower = ARM_POWER_MAX;
            }
        } else

        {
            armSequence = 0;
            robot.armDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        if (gamepad2.a || gamepad1.a)

        {
            robot.pickupServo.setPosition(0.9d);
        } else if (gamepad2.b || gamepad1.b)

        {
            robot.pickupServo.setPosition(0.1d);
        } else

        {
            robot.pickupServo.setPosition(0.5d);
        }

        if (gamepad2.left_stick_y < 0d && armTime.milliseconds() > SERVO_CYCLE)

        {
            armTime.reset();
            armPosition += Range.scale(gamepad2.left_stick_y, 0d, -1d, SERVO_INCREMENT_MIN, SERVO_INCREMENT_MAX);
        } else if (gamepad2.left_stick_y > 0d && armTime.milliseconds() > SERVO_CYCLE)

        {
            armTime.reset();
            armPosition -= Range.scale(gamepad2.left_stick_y, 0d, 1d, SERVO_INCREMENT_MIN, SERVO_INCREMENT_MAX);
        }

        if (gamepad2.right_stick_y < 0d && handTime.milliseconds() > SERVO_CYCLE)

        {
            handTime.reset();
            handPosition += Range.scale(gamepad2.right_stick_y, 0d, -1d, SERVO_INCREMENT_MIN, SERVO_INCREMENT_MAX);
        } else if (gamepad2.right_stick_y > 0d && handTime.milliseconds() > SERVO_CYCLE)

        {
            handTime.reset();
            handPosition -= Range.scale(gamepad2.right_stick_y, 0d, 1d, SERVO_INCREMENT_MIN, SERVO_INCREMENT_MAX);
        }

        robot.setArmPower(armPower);
        robot.armServo.setPosition(armPosition);
        robot.handServo.setPosition(handPosition);
    }
}
