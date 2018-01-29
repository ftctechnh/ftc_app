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

// import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;

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

@TeleOp(name="Pushbot: Drive Teleop", group="Pushbot")
//@Disabled
public abstract class driveTeleop extends OpMode{

    /* Declare OpMode members. */
    MyHardwarePushbot robot       = new MyHardwarePushbot(); // use the class created to define a Pushbot's hardware
    // could also use HardwarePushbotMatrix class.

    private ElapsedTime runtime = new ElapsedTime();
    double          clawOffset  = robot.OPEN_offset ;                  // Servo open position
    final double    CLAW_SPEED  = 0.02 ;                 // sets rate to move servo
    int             target = 0;                          // lift motor target
    double          LeftPower = 0;
    double          RightPower = 0;
    double          left = 0;
    double          right = 0;
    String          mode = "";

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
        robot.lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //resetting all the claws to open
        robot.leftClaw.setPosition(robot.MID_SERVO + clawOffset + robot.leftclawcorrection);
        robot.leftClawup.setPosition(robot.MID_SERVO - clawOffset + robot.leftclawupcorrection);
        robot.rightClaw.setPosition(robot.MID_SERVO - clawOffset + robot.rightclawcorrection);
        robot.rightClawup.setPosition(robot.MID_SERVO + clawOffset + robot.rightclawupcorrection);

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
        double left;
        double right;


        // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)

        if (mode =="normal"){
            normalDrive();
        }
        else if (mode =="oneStick"){
            onestick();
        }
        else if (mode == "rampUp"){
            MotorPower();
        }
        // use d-pad to back up and go forward straight
        if (gamepad1.dpad_down) {
            robot.leftDrive.setPower(.5);
            robot.rightDrive.setPower(.5);
        }
        if (gamepad1.dpad_up) {
            robot.leftDrive.setPower(-.5);
            robot.rightDrive.setPower(-.5);
        }
        // Use right Bumper to toggle claw between open and close position
        if (gamepad1.right_bumper) {
            if (clawOffset == robot.MID_offset)            //if mid, open
                clawOffset = robot.OPEN_offset;
            else if (clawOffset == robot.CLOSE_offset)    // if closed, mid
                clawOffset = robot.MID_offset;
            else if (clawOffset == robot.OPEN_offset)    // if open, close
                clawOffset = robot.CLOSE_offset;

            // Move both servos to new position.  Assume servos are mirror image of each other.
            // 0.06 correction factor for difference in hand attachment for bottom left and right servo
            clawOffset = Range.clip(clawOffset, -0.5, 0.5);
            robot.leftClaw.setPosition(robot.MID_SERVO + clawOffset + robot.leftclawcorrection);
            robot.leftClawup.setPosition(robot.MID_SERVO - clawOffset + robot.leftclawupcorrection);
            robot.rightClaw.setPosition(robot.MID_SERVO - clawOffset + robot.rightclawcorrection);
            robot.rightClawup.setPosition(robot.MID_SERVO + clawOffset + robot.rightclawupcorrection);
            runtime.reset();
            while (runtime.seconds() < .4) {    //wait for claw to finsh open or close
            }

            // every time the claw closes the lift rises by half an inch
            if (clawOffset == robot.CLOSE_offset){
                target = robot.lift.getCurrentPosition() + robot.liftclaw;
                robot.lift.setTargetPosition(target);
                robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.lift.setPower(0.2);
                while (robot.lift.isBusy() && (robot.lift.getCurrentPosition() < robot.maxlift)) {}   //wait for lift to stop
                robot.lift.setPower(0.0);
            }
            // every time the claw opens the lift lowers by half an inch
            if (clawOffset == robot.OPEN_offset){
                target = robot.lift.getCurrentPosition() - robot.liftclaw;
                robot.lift.setTargetPosition(target);
                robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.lift.setPower(0.2);
                while (robot.lift.isBusy() && (robot.lift.getCurrentPosition() > robot.minlift)) {}   //wait for lift to stop
                robot.lift.setPower(0.0);
            }
        }

        // Use gamepad buttons to move the arm up (Y) and down (A)
        // upper and lower limits of 0 to ~13 inches
        if (gamepad1.y  && (robot.lift.getCurrentPosition() < robot.maxlift)) {
            target = robot.lift.getCurrentPosition() + robot.liftstep;
            robot.lift.setTargetPosition(target);
            robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.lift.setPower(.8);
            while (robot.lift.isBusy() && (robot.lift.getCurrentPosition() < robot.maxlift)) {
                if (mode =="normal"){
                    normalDrive();
                }
                else if (mode =="oneStick"){
                    onestick();
                }
                else if (mode == "rampUp"){
                    MotorPower();
                }
            }   //wait for lift to stop
            robot.lift.setPower(0.0);
        }
        else if (gamepad1.a && (robot.lift.getCurrentPosition() > robot.minlift)) {
            target = robot.lift.getCurrentPosition() - robot.liftstep;
            robot.lift.setTargetPosition(target);
            robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.lift.setPower(.8);
            while (robot.lift.isBusy() && (robot.lift.getCurrentPosition() > robot.minlift)) {
                if (mode =="normal"){
                    normalDrive();
                }
                else if (mode =="oneStick"){
                    onestick();
                }
                else if (mode == "rampUp"){
                    MotorPower();
                }
            }   // wait for lift to stop
            robot.lift.setPower(0.0);
        }
        if (gamepad1.a  && (gamepad1.right_trigger > 0)) {
            target = robot.lift.getCurrentPosition() - robot.liftstep;
            robot.lift.setTargetPosition(target);
            robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.lift.setPower(1);
            while (robot.lift.isBusy() ){
               /* left = gamepad1.left_stick_y;
                right = gamepad1.right_stick_y;

                //robot.leftDrive.setPower(left);
                //robot.rightDrive.setPower(right);
                // when you click R2 it goes double the speed
                robot.leftDrive.setPower(left * (gamepad1.left_trigger + 1) / 2);
                robot.rightDrive.setPower(right * (gamepad1.left_trigger + 1) / 2); */
            }   //wait for lift to stop
            robot.lift.setPower(0.0);
            robot.lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
        else if (gamepad1.y && (gamepad1.right_trigger > 0)) {
            target = robot.lift.getCurrentPosition() - robot.liftclaw;
            robot.lift.setTargetPosition(target);
            robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.lift.setPower(1);
            while (robot.lift.isBusy()) {
                /*left = gamepad1.left_stick_y;
                right = gamepad1.right_stick_y;

                //robot.leftDrive.setPower(left);
                //robot.rightDrive.setPower(right);
                // when you click R2 it goes double the speed
                robot.leftDrive.setPower(left * (gamepad1.left_trigger + 1) / 2);
                robot.rightDrive.setPower(right * (gamepad1.left_trigger + 1) / 2); */
            }   // wait for lift to stop
            robot.lift.setPower(0.0);
            robot.lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
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
        //This lowers the lift after you stop it so thw spool isn't to loose.

  /*      target = robot.minlift;
        robot.lift.setTargetPosition(target);
        robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lift.setPower(0.7);
        while (robot.lift.isBusy() && (robot.lift.getCurrentPosition() > robot.minlift)) {
        }
        robot.lift.setPower(0.0); */

    }
    public void MotorPower(){
        double leftStick;
        double rightStick;
        leftStick = gamepad1.left_stick_y;
        rightStick = gamepad1.right_stick_y;
        if (leftStick > 0) {
            LeftPower = Math.min(leftStick, (LeftPower + (2 - gamepad1.left_trigger)* .02));
        } else if (leftStick < 0) {
            LeftPower = Math.max(leftStick, (LeftPower - (2 - gamepad1.left_trigger)* .02));
        }
        else if(leftStick == 0) {
            LeftPower = 0 ;
        }
        if (rightStick > 0) {
            RightPower = Math.min(rightStick, (RightPower + (2 - gamepad1.left_trigger)* .02));
        } else if (rightStick < 0) {
            RightPower = Math.max(rightStick, (RightPower - (2 - gamepad1.left_trigger)* .02));
        }
        else if(rightStick == 0) {
            RightPower = 0 ;
        }

        robot.leftDrive.setPower(LeftPower * (gamepad1.left_trigger + 1) / 2);
        robot.rightDrive.setPower(RightPower * (gamepad1.left_trigger + 1) / 2);

        telemetry.addData("left", "%.2f", LeftPower);
        telemetry.addData("right", "%.2f", RightPower);
        telemetry.update();
    }
    public void onestick(){
        float X = gamepad1.left_stick_x;
        float Y = -(gamepad1.left_stick_y); //Pushing stick forward is -1 Y.
        float Left = Y + X;
        float Right = Y - X;
        float Max = Math.max(Math.abs(Left),Math.abs(Right));
        Left/=Max;
        Right/=Max;
        double Radius = Math.sqrt(X*X + Y*Y);
        Left *= -Radius;
        Right *= -Radius;
        robot.leftDrive.setPower(Left * (gamepad1.left_trigger + 1) / 2);
        robot.rightDrive.setPower(Right * (gamepad1.left_trigger + 1) / 2);
        telemetry.addData("X",X);
        telemetry.addData("Y",Y);
        telemetry.addData("Left",Left);
        telemetry.addData("Right",Right);
    }
    public void normalDrive(){
        left = gamepad1.left_stick_y;
        right = gamepad1.right_stick_y;

        //robot.leftDrive.setPower(left);
        //robot.rightDrive.setPower(right);
        // when you click R2 it goes double the speed
        robot.leftDrive.setPower(left * (gamepad1.left_trigger + 1) / 2);
        robot.rightDrive.setPower(right * (gamepad1.left_trigger + 1) / 2);
    }
}
