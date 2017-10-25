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

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

/**
 ☺ Hi! This is the perfect teleop code for December 16, 2017! ☺
 */
@TeleOp(name = "♪ Perfect Teleop ♪", group = "Concept")
//@Disabled
public class PerfectTeleop extends LinearOpMode {

    /* this says use ArmHardwareClass */
ArmHardwareClass robot = new ArmHardwareClass();

    @Override
    public void runOpMode() {

        /* The init() method of the hardware class does all the work here*/
        robot.init(hardwareMap);

        // Wait for the start button
        telemetry.addLine("!☻ Start Claw Control ☻!");
        telemetry.update();
        waitForStart();

        while(opModeIsActive()){

            // Display the current value
            telemetry.addData("Servo Controls", "X is OPEN, Y is CLOSE");
            telemetry.addData("Motor Controls","Use the D-Pad ↑ & ↓ buttons!");
            telemetry.update();

        /* Servo Control */
            if(gamepad1.x){
                robot.clawServo.setPower(robot.clawOpen);
            }
            if(gamepad1.y){
                robot.clawServo.setPower(robot.clawClose);
            }

            if (gamepad1.y != true && gamepad1.x != true){
                robot.clawServo.setPower(robot.clawStill);
            }

        /* Vertical Arm Motor */
            if (gamepad1.dpad_up) {
                robot.verticalArmMotor.setPower(1);
            } else {
                robot.verticalArmMotor.setPower(0);
            }
            if (gamepad1.dpad_down) {
                robot.verticalArmMotor.setPower(-1);
            } else {
                robot.verticalArmMotor.setPower(0);
            }

        }
    }

    /***********************************************************************************************
     * These are all of the methods used in the Teleop*
     ***********************************************************************************************/

}
