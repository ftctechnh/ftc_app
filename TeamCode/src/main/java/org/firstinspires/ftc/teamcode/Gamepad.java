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
            telemetry.addData("Motors", "left (%.4f), right (%.4f)", leftPower, rightPower);
            telemetry.addData("Servo", "Arm (%.4f) Hand (%.4f)", robot.armServo.getPosition(), robot.handServo.getPosition());
            telemetry.addData("Arm", "pos (%.4f) pow (%.4f) enc (%d)", robot.armAngle.getVoltage(), robot.armDrive.getPower(), robot.armDrive.getCurrentPosition());
            telemetry.update();
        }
    }

    void User1() {
        // Choose to drive using either Tank Mode, or POV Mode
        // Comment out the method that's not used.  The default below is POV.

        // POV Mode uses left stick to go forward, and right stick to turn.
        // - This uses basic math to combine motions and is easier to drive straight.
        double drive = gamepad1.right_stick_y;
        double turn = gamepad1.left_stick_x;
        if (gamepad1.y) {
            drive = 1d;
        } else if (gamepad1.b) {
            drive = -1d;
        }
        leftPower = Range.scale(drive - turn, -1d, 1d, -1.0, 1.0);
        rightPower = Range.scale(drive + turn, -1d, 1d, -1.0, 1.0);

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

        double armPosition = robot.armServo.getPosition();
        double handPosition = robot.handServo.getPosition();

        if (gamepad2.left_trigger > 0d && robot.armAngle.getVoltage() > robot.ARM_MIN) { //Arm going up
            double m = robot.armAngle.getVoltage() < robot.ARM_MIN + 0.15d ? robot.ARM_POWER : 1d;
            p = Range.scale(gamepad2.left_trigger, 0d, 1d, 0d, m);
        } else if (gamepad2.right_trigger > 0d && robot.armAngle.getVoltage() < robot.ARM_MAX) { //Arm going down
            double m = robot.armAngle.getVoltage() > robot.ARM_MAX - 0.15d ? robot.ARM_POWER : 1d;
            p = -Range.scale(gamepad2.right_trigger, 0d, 1d, 0d, m);
        }

        if (gamepad2.dpad_up) {
            int gap = robot.setArmTarget(1.2860d);
            p = robot.ARM_POWER;
            if (Math.abs(gap) < 100) {
                armPosition = 0.3239d;
                handPosition = 0.1833d;
            }
        } else if (gamepad2.dpad_down) {
            int gap = robot.setArmTarget(2.1030d);
            p = robot.ARM_POWER;
            if (Math.abs(gap) < 100) {
                armPosition = 0.0739d;
                handPosition = 0.6439d;
            }
        } else {
            robot.armDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        if (gamepad2.a) {
            robot.pickupServo.setPosition(1d);
        } else if (gamepad2.b) {
            robot.pickupServo.setPosition(0d);
        } else {
            robot.pickupServo.setPosition(0.5d);
        }

        robot.armDrive.setPower(p);

        if (gamepad2.left_stick_y < 0d && armtime.milliseconds() > 45d) {
            armtime.reset();
            armPosition += Range.scale(gamepad2.left_stick_y, 0d, -1d, 0.004d, 0.0125d);
        } else if (gamepad2.left_stick_y > 0d && armtime.milliseconds() > 45d) {
            armtime.reset();
            armPosition -= Range.scale(gamepad2.left_stick_y, 0d, 1d, 0.004d, 0.0125d);
        }

        if (gamepad2.right_stick_y < 0d && armtime.milliseconds() > 45d) {
            handtime.reset();
            handPosition += Range.scale(gamepad2.right_stick_y, 0d, -1d, 0.004d, 0.0125d);
        } else if (gamepad2.right_stick_y > 0d && armtime.milliseconds() > 45d) {
            handtime.reset();
            handPosition -= Range.scale(gamepad2.right_stick_y, 0d, 1d, 0.004d, 0.0125d);
        }

        robot.armServo.setPosition(armPosition);
        robot.handServo.setPosition(handPosition);
    }
}
