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

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * This file provides basic Telop driving for a Pushbot robot.
 * The code is structured as an Iterative OpMode
 *
 * This OpMode uses the common Pushbot hardware class to define the devices on the robot.
 * All device access is managed through the HardwarePushbot class.
 *
 * This particular OpMode executes a basic Tank Drive Teleop for a PushBot
 * It raises and lowers the claw using the Gampad Y and A buttons respectively.
 * It also opens and closes the claws slowly using the left and right Bumper buttons.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Pushbot: Teleop Tank", group="Pushbot")
//@Disabled
public class MyPushbotTeleopTankRampUp_Iterative extends OpMode{

    /* Declare OpMode members. */
    MyHardwarePushbot robot       = new MyHardwarePushbot(); // use the class created to define a Pushbot's hardware
                                                         // could also use HardwarePushbotMatrix class.

    private ElapsedTime runtime = new ElapsedTime();
    double          clawOffset  = 0.0 ;                  // Servo mid position
    final double    CLAW_SPEED  = 0.02 ;                 // sets rate to move servo
    int             target = 0;                          // lift motor target
    int             maxlift = 7500;                     // maxiumum lift height
    int             minlift = 0;                         // minimum lift height


    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);
        robot.leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
     //   robot.lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot.ballArm.setPosition(.7);

        // Send telemetry message to signify robot waiting
        telemetry.addData("Say", "Hello Driver");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        MotorPower();

        // Use right Bumper to toggle claw between open and close position
        // 0 is open, -0.30 is closed
        if (gamepad1.right_bumper) {
            if (clawOffset == 0)            //if opened, close
                clawOffset = -0.5;
            else if (clawOffset == -0.50)    // if closed, open
                clawOffset = -0.25;
            else if (clawOffset == -0.25)    // if closed, open
                clawOffset = 0;

            // Move both servos to new position.  Assume servos are mirror image of each other.
            // 0.15 correction factor for difference in hand attachment to left and right servo
            clawOffset = Range.clip(clawOffset, -0.5, 0.5);
            robot.leftClaw.setPosition(robot.MID_SERVO + clawOffset);
            robot.rightClaw.setPosition(robot.MID_SERVO - clawOffset - .15);
            runtime.reset();
            while (runtime.seconds() < .4) {    //wait for claw to finsh open or close
            }

            // every time the claw closes the lift rises by half an inch
            if (clawOffset == -0.5){
                target = robot.lift.getCurrentPosition() + 500;
                robot.lift.setTargetPosition(target);
                robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.lift.setPower(0.6);
                while (robot.lift.isBusy() && (robot.lift.getCurrentPosition() < maxlift)) {}   //wait for lift to stop
                robot.lift.setPower(0.0);
            }
            // every time the claw opens the lift lowers by half an inch
            if (clawOffset == 0){
                target = robot.lift.getCurrentPosition() - 500;
                robot.lift.setTargetPosition(target);
                robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.lift.setPower(0.6);
                while (robot.lift.isBusy() && (robot.lift.getCurrentPosition() > minlift)) {}   //wait for lift to stop
                robot.lift.setPower(0.0);
            }
        }

        // Use gamepad buttons to move the arm up (Y) and down (A)
        // 556 motor rotations = 1 inch of lift
        // upper and lower limits of 0 to ~13 inches
        if (gamepad1.y  && (robot.lift.getCurrentPosition() < maxlift)) {
            target = robot.lift.getCurrentPosition() + 3614;
            robot.lift.setTargetPosition(target);
            robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.lift.setPower(0.8);
            while (robot.lift.isBusy() && (robot.lift.getCurrentPosition() < maxlift)) {
              MotorPower();
            }   //wait for lift to stop
            robot.lift.setPower(0.0);
        }
        else if (gamepad1.a && (robot.lift.getCurrentPosition() > minlift)) {
            target = robot.lift.getCurrentPosition() - 3614;
            robot.lift.setTargetPosition(target);
            robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.lift.setPower(0.8);
            while (robot.lift.isBusy() && (robot.lift.getCurrentPosition() > minlift)) {
                MotorPower();
            }   // wait for lift to stop
            robot.lift.setPower(0.0);
        }

        // end telemetry message to signify robot running;
        telemetry.addData("claw",  "Offset = %.2f", clawOffset);
        telemetry.addData("lift",  "Running to:%7d", robot.lift.getCurrentPosition());
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

    }
    public void MotorPower(){
        double leftStick;
        double rightStick;
        double LeftPower;
        double RightPower;
        leftStick = gamepad1.left_stick_y;
        rightStick = gamepad1.right_stick_y;
        LeftPower = robot.leftDrive.getPower();
        RightPower = robot.rightDrive.getPower();
        if (leftStick > 0) {
            if (leftStick - LeftPower >= 0.1) {
                LeftPower += 0.1;
            }
            else {
                LeftPower = leftStick;
            }
        }
        else{
            if (leftStick - LeftPower <= -0.1) {
                LeftPower -= 0.1;
            }
            else {
                LeftPower = leftStick;
            }
        }
        if (rightStick > 0) {
            if (rightStick -  RightPower >= 0.1) {
                RightPower += 0.1;
            }
            else {
                RightPower = rightStick;
            }
        }
        else{
            if (rightStick -  RightPower <= -0.1) {
                RightPower -= 0.1;
            }
            else {
                RightPower = rightStick;
            }
        }

        robot.leftDrive.setPower(LeftPower * (gamepad1.left_trigger + 1) / 2);
        robot.rightDrive.setPower(RightPower * (gamepad1.left_trigger + 1) / 2);

        telemetry.addData("left",  "%.2f", LeftPower);
        telemetry.addData("right", "%.2f", RightPower);
        telemetry.update();
    }
}
