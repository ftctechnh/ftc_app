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
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


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

@TeleOp(name="Driver Controlled", group="Linear Opmode")
//@Disabled
public class BasicOpMode_Linear extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor left_front = null;
    private DcMotor right_front = null;
    private DcMotor left_back = null;
    private DcMotor right_back = null;

    private DcMotor arm_1 = null;
    private DcMotor arm_2 = null;
    private DcMotor out = null;
    private DcMotor grab = null;

    private CRServo foundation = null;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        left_front  = hardwareMap.get(DcMotor.class, "left_front");
        right_front = hardwareMap.get(DcMotor.class, "right_front");
        left_back = hardwareMap.get(DcMotor.class, "left_back");
        right_back = hardwareMap.get(DcMotor.class, "right_back");

        arm_1 = hardwareMap.get(DcMotor.class, "arm_1");
        arm_2 = hardwareMap.get(DcMotor.class, "arm_2");
        out = hardwareMap.get(DcMotor.class, "out");
        grab = hardwareMap.get(DcMotor.class, "grab");

        foundation = hardwareMap.get(CRServo.class, "foundation");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        left_front.setDirection(DcMotor.Direction.FORWARD);
        right_front.setDirection(DcMotor.Direction.REVERSE);
        left_back.setDirection(DcMotor.Direction.FORWARD);
        right_back.setDirection(DcMotor.Direction.REVERSE);

//        arm_1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        arm_1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Setup a variable for each drive wheel to save power level for telemetry
            double leftPower;
            double rightPower;

            //left stick position

            leftPower  = - gamepad1.left_stick_y - gamepad1.left_stick_x;
            rightPower = - gamepad1.left_stick_y + gamepad1.left_stick_x;

            //if left bumper, sideways movement
            if (gamepad1.left_bumper) {
                left_front.setPower(leftPower);
                left_back.setPower(-leftPower);
                right_front.setPower(-rightPower);
                right_back.setPower(rightPower);
            } else {
                left_front.setPower(0.5*leftPower);
                left_back.setPower(0.5*leftPower);
                right_front.setPower(0.5*rightPower);
                right_back.setPower(0.5*rightPower);
            }

            //foundation
            if (gamepad1.left_trigger > 0.05) {
                foundation.setPower(1);
            } else if (gamepad1.right_trigger > 0.05) {
                foundation.setPower(-1);
            } else {
                foundation.setPower(0);
            }

            //arm movement
            if (gamepad2.x) {
//                if(arm_1.getCurrentPosition() > -200) {
                    arm_1.setPower(1);
//                } else {
//                    arm_1.setPower(0.25);
//                }
            } else if (gamepad2.y) {
//                if(arm_1.getCurrentPosition() < -2000) {
                    arm_1.setPower(-1);
//                } else {
//                    arm_1.setPower(0.5);
//                }
            } else {
                arm_1.setPower(0);
            }

            if(gamepad2.a) {
                arm_2.setPower(1);
            } else if (gamepad2.b) {
                arm_2.setPower(-1);
            } else {
                arm_2.setPower(0);
            }

            //out
            if(gamepad2.dpad_up) {
                out.setPower(1);
            } else if(gamepad2.dpad_down) {
                out.setPower(-1);
            } else {
                out.setPower(0);
            }

            //grab
            int grabstate = 0;

            if(gamepad1.x) {
                grab.setPower(1);
            } else if (gamepad1.y) { //hold soon (tm)
                grab.setPower(-1);
            } else {
                grab.setPower(0);
            }

            telemetry.addData("toehnu", arm_1.getCurrentPosition());
            telemetry.addData("runtime", getRuntime());
            telemetry.update();
        }
    }
}
