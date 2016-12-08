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

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;

@TeleOp(name="Robot Teleop", group="Pushbot")
//@Disabled
public class RobotTeleopTank extends OpMode{

    /* Declare OpMode members. */
    org.firstinspires.ftc.teamcode.HardwarePushbot robot       = new org.firstinspires.ftc.teamcode.HardwarePushbot();
    /*
     * Code to run ONCE when the driver hits INIT
     */

    private final double THRESHOLD = 0.1;


    @Override
    public void init() {
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver");    //
        telemetry.addData("Say", "Nalu Zou");
        // Send telemetry message to signify robot running;
        // *******************************************
        telemetry.addData("This is our debug message", "");
        // *******************************************
        updateTelemetry(telemetry);
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
        double MAX_FWD      =  1.0;     // Maximum FWD power applied to motor
        double MAX_REV      = -1.0;     // Maximum REV power applied to motor
        double leftPower    = 0;
        double rightPower   = 0;
    }

    private void move(double left, double right) {
        robot.leftFrontMotor.setPower(left);
        robot.leftBackMotor.setPower(left);
        robot.rightFrontMotor.setPower(right);
        robot.rightBackMotor.setPower(right);
    }
    /*
    private void vertical(boolean dir) {

    }
    private void horizontal (boolean dir) {

    }
    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {

        //double leftStick;
        //double rightStick;
        //leftStick   = -gamepad1.left_stick_y;
        //rightStick  = -gamepad1.right_stick_y;

        // Use gamepad left & right Bumpers to open and close the claw
        //if (gamepad1.right_bumper)
        //    clawOffset += CLAW_SPEED;
        //else if (gamepad1.left_bumper)
        //    clawOffset -= CLAW_SPEED;

        // Use gamepad buttons to drive up, down, left, or right
        //press a to launch beacon0

        if (gamepad1.left_trigger > 0.1) { //collect
            robot.collectorMotor.setPower(1);
        }
        else if (gamepad1.right_trigger > 0.1) { //release
            robot.collectorMotor.setPower(-1);
        }
        if (gamepad1.left_bumper) {
            telemetry.addData("drive", "beacon");
            telemetry.addData("This is our debug message", "");
            robot.pusherServo.setPosition(1.0);
        } else if (gamepad1.right_bumper) {
            telemetry.addData("drive", "beacon2");
            robot.pusherServo.setPosition(0);
        } else {  //release of both bumpers
            robot.pusherServo.setPosition(0.5);
        }

        double y = 0 - gamepad1.right_stick_x;
        double x = 0 - gamepad1.left_stick_y;


        x = (Math.abs(x) < THRESHOLD) ? 0 : x;
        y = (Math.abs(y) < THRESHOLD) ? 0 : y;

        telemetry.addData("Driver", x + " " + y);

        if (x > 0 || x < 0) {
            if (y > 0) {
                move(((x + y) > 1 ? 1 : x + y ), x - y);
                telemetry.addData("Driver","turning moving forward");
            }
            else if (y < 0) {
                move(y - x, y + x);
                telemetry.addData("Driver","turning moving backward");
            }
            else {
                telemetry.addData("Driver","just turning");
                move(-x, x);
            }
        }
        else {
            telemetry.addData("Driver","just moving");
            move(y, y);
        }


        /* if (gamepad1.right_stick_x > 0 && gamepad1.left_stick_y > 0) //turn right while moving forward
        {
            /*robot.leftBackMotor.setPower(robot.TURN_POWER);
            robot.rightBackMotor.setPower(robot.FORWARD_POWER);
            robot.leftFrontMotor.setPower(robot.TURN_POWER);
            robot.rightFrontMotor.setPower(robot.FORWARD_POWER);

            moveSide(Direction.LEFT, robot.TURN_POWER);
            moveSide(Direction.RIGHT, robot.FORWARD_POWER);
            telemetry.addData("drive", "right forward");
        }
        else if (gamepad1.right_stick_x < 0 && gamepad1.left_stick_y > 0) //turn left while moving forward
        {
            robot.leftBackMotor.setPower(robot.FORWARD_POWER);
            robot.rightBackMotor.setPower(robot.TURN_POWER);
            robot.leftFrontMotor.setPower(robot.FORWARD_POWER);
            robot.rightFrontMotor.setPower(robot.TURN_POWER);
            telemetry.addData("drive", "left forward");
        }
        else if (gamepad1.right_stick_x > 0 && gamepad1.left_stick_y < 0) //turn right while moving backwards
        {
            robot.leftBackMotor.setPower(robot.BACKWARD_TURN_POWER);
            robot.rightBackMotor.setPower(robot.BACKWARD_POWER);
            robot.leftFrontMotor.setPower(robot.BACKWARD_TURN_POWER);
            robot.rightFrontMotor.setPower(robot.BACKWARD_POWER);
            telemetry.addData("drive", "right backwards");
        }
        else if (gamepad1.right_stick_x > 0 && gamepad1.left_stick_y > 0) //turn left while moving backwards
        {
            robot.leftBackMotor.setPower(robot.BACKWARD_POWER);
            robot.rightBackMotor.setPower(robot.BACKWARD_TURN_POWER);
            robot.leftFrontMotor.setPower(robot.BACKWARD_POWER);
            robot.rightFrontMotor.setPower(robot.BACKWARD_TURN_POWER);
            telemetry.addData("drive", "left backwards");
        }
        else if (gamepad1.left_stick_y > 0) { //forward
            robot.leftBackMotor.setPower(robot.FORWARD_POWER);
            robot.rightBackMotor.setPower(robot.FORWARD_POWER);
            robot.leftFrontMotor.setPower(robot.FORWARD_POWER);
            robot.rightFrontMotor.setPower(robot.FORWARD_POWER);
            telemetry.addData("drive", "forwards");
        }
        else if (gamepad1.left_stick_y < 0){ //backward
            robot.leftBackMotor.setPower(robot.BACKWARD_POWER);
            robot.rightBackMotor.setPower(robot.BACKWARD_POWER);
            robot.leftFrontMotor.setPower(robot.BACKWARD_POWER);
            robot.rightFrontMotor.setPower(robot.BACKWARD_POWER);
            telemetry.addData("drive", "backwards");
        }
        else if (gamepad1.right_stick_x > 0){ //turn right
            robot.leftBackMotor.setPower(robot.BACKWARD_POWER);
            robot.rightBackMotor.setPower(robot.FORWARD_POWER);
            robot.leftFrontMotor.setPower(robot.BACKWARD_POWER);
            robot.rightFrontMotor.setPower(robot.FORWARD_POWER);
            telemetry.addData("drive", "turn right");
        }
        else if (gamepad1.right_stick_x < 0){ //turn left
            robot.leftBackMotor.setPower(robot.FORWARD_POWER);
            robot.rightBackMotor.setPower(robot.BACKWARD_POWER);
            robot.leftFrontMotor.setPower(robot.FORWARD_POWER);
            robot.rightFrontMotor.setPower(robot.BACKWARD_POWER);
            telemetry.addData("drive", "turn left");
        }
        else {
            robot.leftBackMotor.setPower(robot.STOP_POWER);
            robot.rightBackMotor.setPower(robot.STOP_POWER);
            robot.leftFrontMotor.setPower(robot.STOP_POWER);
            robot.rightFrontMotor.setPower(robot.STOP_POWER);
        }
        */




        //telemetry.addData("right", "%.2f", right);
        updateTelemetry(telemetry);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }
}