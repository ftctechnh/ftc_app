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

package org.firstinspires.ftc.teamcode.TeleOp;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.TeleOp.WLP_MecanumWheels;

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

@TeleOp(name="MecanumDriveTrain", group="We Love PI")

public class WLP_MecanumDriveTrain {
    
    // Global variables to be initialized in the constructor
    private Telemetry telemetry = null;
    private HardwareMap hardwareMap = null;
    private Gamepad gamepad1 = null;
    private Gamepad gamepad2 = null;


    // Declare Drive Train modors
    protected DcMotor frontLeft = null;
    protected DcMotor frontRight = null;
    protected DcMotor rearLeft = null;
    protected DcMotor rearRight = null;

    protected WLP_MecanumWheels wheels = new WLP_MecanumWheels(0,0,0);


    double limiter = 1;

    // Constructors

    // Don't allow default constructors
    private WLP_MecanumDriveTrain() {
        // Throw an error in the private constructor avoids call it within the class.
        throw new AssertionError();
    }

    // This is the only valid consturctor
    public WLP_MecanumDriveTrain(Telemetry telemetry, HardwareMap hardwareMap,
                                 Gamepad gamepad1, Gamepad gamepad2){
        this.telemetry = telemetry;
        this.hardwareMap = hardwareMap;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }


    /*
     * Code to run ONCE when the driver hits INIT
     */


    public void init(Telemetry telemetry, HardwareMap hardwareMap,
                     Gamepad gamepad1, Gamepad gamepad2) {

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        //true initialization
        this.telemetry = telemetry;
        this.hardwareMap = hardwareMap;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;

        frontLeft  = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        rearLeft  = hardwareMap.get(DcMotor.class, "rearLeft");
        rearRight = hardwareMap.get(DcMotor.class, "rearRight");


        //frontLeft.setDirection(DcMotor.Direction.REVERSE);

        frontRight.setDirection(DcMotor.Direction.REVERSE);

        //rearLeft.setDirection(DcMotor.Direction.REVERSE);
        rearRight.setDirection(DcMotor.Direction.REVERSE);


        // Send telemetry message to signify robot waiting;
        telemetry.addData("Drive Train", "Init complete");    //
    }

    public void loop() {


        // THE X AND Y AXIS FOR EACH SIDE (LEFT AND RIGHT)
        double left_y;
        double left_x;
        double right_y;
        double right_x;

        // pass the left x_y and right x_y to mechanum wheels
        // get the power for the motors
        // multiply with the limiter
        left_y = gamepad1.left_stick_y ;
        left_x = gamepad1.left_stick_x ;
        // SETTING VALUES FOR RIGHT X AND Y
        right_y = gamepad1.right_stick_y ;
        right_x = gamepad1.right_stick_x ;
        telemetry.addData("Drive Train::Left_X","(%.2f)", left_x);
        telemetry.addData("Drive Train::Left_Y", "(%.2f)",left_y);
        telemetry.addData("Drive Train::Right_X","(%.2f)", right_x);


        if ( gamepad1.dpad_down == false && gamepad1.dpad_left == false && gamepad1.dpad_right == false && gamepad1.dpad_up == false) {

            wheels.UpdateInput(left_x, left_y, right_x);

            if (gamepad1.left_trigger > 0 && gamepad1.right_trigger == 0) {
                limiter = 0.2;

                telemetry.addData("limiter is", limiter);
            } else if (gamepad1.right_trigger > 0 && gamepad1.left_trigger == 0) {
                limiter = 0.3;
                telemetry.addData("limiter is", limiter);

            } else {
                limiter = 0.5;
                telemetry.addData("Limiter is ", limiter);
            }

            telemetry.addData("Drive Train::FrontLeftPower", "(%.2f)", wheels.getFrontLeftPower());
            telemetry.addData("Drive Train::FrontRightPower", "(%.2f)", wheels.getFrontRightPower());
            telemetry.addData("Drive Train::RearRightPower", "(%.2f)", wheels.getRearLeftPower());
            telemetry.addData("Drive Train::RearLeftPower", "(%.2f)", wheels.getRearRightPower());


            frontLeft.setPower(wheels.getFrontLeftPower());
            frontRight.setPower(wheels.getFrontRightPower());
            rearRight.setPower(wheels.getRearRightPower());
            rearLeft.setPower(wheels.getRearLeftPower());

        }


        //DPAD DIRECTIONS
        if (gamepad1.dpad_up) {
            //DPAD FORWARD 0.5
            frontLeft.setPower(1 * limiter);
            frontRight.setPower(-1 * limiter);
            rearLeft.setPower(1 * limiter);
            rearRight.setPower(-1 * limiter);
        } else if (gamepad1.dpad_down) {
            //DPAD BACKWARD 0.5
            frontLeft.setPower(-1 * limiter);
            frontRight.setPower(1 * limiter);
            rearLeft.setPower(-1 * limiter);
            rearRight.setPower(1 * limiter);
        } else if (gamepad1.dpad_left) {
            //DPAD LEFT 0.5
            frontLeft.setPower(1 * limiter);
            frontRight.setPower(1 * limiter);
            rearLeft.setPower(-1 * limiter);
            rearRight.setPower(-1 * limiter);
        } else if (gamepad1.dpad_right) {
            //DPAD RIGHT 0.5
            frontLeft.setPower(-1 * limiter);
            frontRight.setPower(-1 * limiter);
            rearLeft.setPower(1 * limiter);
            rearRight.setPower(1 * limiter);
        } else if (gamepad1.dpad_up && gamepad1.dpad_right) {
            //DPAD NORTHEAST (RIGHT FRONT DIAGONAL)
            frontLeft.setPower(1* limiter);
            frontRight.setPower(0 * limiter);
            rearLeft.setPower(0 * limiter);
            rearRight.setPower(-1 * limiter);
        } else if (gamepad1.dpad_up && gamepad1.dpad_left) {
            //DPAD NORTHWEST (LEFT FRONT DIAGONAL
            frontLeft.setPower(0 * limiter);
            frontRight.setPower(-1 * limiter);
            rearLeft.setPower(1 * limiter);
            rearRight.setPower(0 * limiter);
        } else if (gamepad1.dpad_down && gamepad1.dpad_right) {
            frontLeft.setPower(0 * limiter);
            frontRight.setPower(1 * limiter);
            rearLeft.setPower(-1 * limiter);
            rearRight.setPower(0 * limiter);
        } else if (gamepad1.dpad_down && gamepad1.dpad_left) {
            frontLeft.setPower(-1 * limiter);
            frontRight.setPower(0 * limiter);
            rearLeft.setPower(0 * limiter);
            rearRight.setPower(1 * limiter);
        }

        /*

        // SETTING VALUES FOR LEFT X AND Y
        left_y = -this.gamepad1.left_stick_y * limiter;
        left_x = gamepad1.left_stick_x * limiter;
        // SETTING VALUES FOR RIGHT X AND Y
        right_y = -gamepad1.right_stick_y * limiter;
        right_x = gamepad1.right_stick_x * limiter;


        // SETTING THE POWER TO MOVE THE ROBOT WITH EACH MOTOR

        if ((right_x == 0) && gamepad1.dpad_down == false && gamepad1.dpad_left == false && gamepad1.dpad_right == false && gamepad1.dpad_up == false) {

            frontLeft.setPower(left_y + left_x);
            frontRight.setPower(left_x - left_y);
            rearRight.setPower(-left_y - left_x);
            rearLeft.setPower(left_y - left_x);
        }

        if (gamepad1.left_trigger > 0 && gamepad1.right_trigger == 0) {
            limiter = 0.2;

            telemetry.addData("limiter is", limiter);
        } else if (gamepad1.right_trigger > 0 && gamepad1.left_trigger == 0) {
            limiter = 0.3;
            telemetry.addData("limiter is", limiter);

        } else {
            limiter = 0.5;
            telemetry.addData("Limiter is ", limiter);
        }


        // MAKE THE ROBOT ROTATE
        if ((left_y + left_x == 0) && (right_x != 0)) {
            int dir = 1;
            if (right_x < 0) dir = -1;

            frontLeft.setPower(dir * limiter);

            frontRight.setPower(dir * limiter);
            rearRight.setPower(dir * limiter);
            rearLeft.setPower(dir * limiter);

            frontRight.setPower(dir * limiter);
            rearRight.setPower(dir * limiter);
            rearLeft.setPower(dir * limiter);

        }

        //DPAD DIRECTIONS
        if (gamepad1.dpad_up) {
            //DPAD FORWARD 0.5
            frontLeft.setPower(1 * limiter);
            frontRight.setPower(-1 * limiter);
            rearLeft.setPower(1 * limiter);
            rearRight.setPower(-1 * limiter);
        } else if (gamepad1.dpad_down) {
            //DPAD BACKWARD 0.5
            frontLeft.setPower(-1 * limiter);
            frontRight.setPower(1 * limiter);
            rearLeft.setPower(-1 * limiter);
            rearRight.setPower(1 * limiter);
        } else if (gamepad1.dpad_left) {
            //DPAD LEFT 0.5
            frontLeft.setPower(1 * limiter);
            frontRight.setPower(1 * limiter);
            rearLeft.setPower(-1 * limiter);
            rearRight.setPower(-1 * limiter);
        } else if (gamepad1.dpad_right) {
            //DPAD RIGHT 0.5
            frontLeft.setPower(-1 * limiter);
            frontRight.setPower(-1 * limiter);
            rearLeft.setPower(1 * limiter);
            rearRight.setPower(1 * limiter);
        } else if (gamepad1.dpad_up && gamepad1.dpad_right) {
            //DPAD NORTHEAST (RIGHT FRONT DIAGONAL)
            frontLeft.setPower(1* limiter);
            frontRight.setPower(0 * limiter);
            rearLeft.setPower(0 * limiter);
            rearRight.setPower(-1 * limiter);
        } else if (gamepad1.dpad_up && gamepad1.dpad_left) {
            //DPAD NORTHWEST (LEFT FRONT DIAGONAL
            frontLeft.setPower(0 * limiter);
            frontRight.setPower(-1 * limiter);
            rearLeft.setPower(1 * limiter);
            rearRight.setPower(0 * limiter);
        } else if (gamepad1.dpad_down && gamepad1.dpad_right) {
            frontLeft.setPower(0 * limiter);
            frontRight.setPower(1 * limiter);
            rearLeft.setPower(-1 * limiter);
            rearRight.setPower(0 * limiter);
        } else if (gamepad1.dpad_down && gamepad1.dpad_left) {
            frontLeft.setPower(-1 * limiter);
            frontRight.setPower(0 * limiter);
            rearLeft.setPower(0 * limiter);
            rearRight.setPower(1 * limiter);
        }
        */

    }
}
