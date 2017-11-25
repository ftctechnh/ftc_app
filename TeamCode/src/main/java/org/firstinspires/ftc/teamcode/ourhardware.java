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

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a Pushbot.
 * See PushbotTeleopTank_Iterative and others classes starting with "Pushbot" for usage examples.
 *
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 */
public class ourhardware {
    /* Public OpMode members. */
    public DcMotor leftfront = null;
    public DcMotor rightfront = null;
    public DcMotor leftback = null;
    public DcMotor rightback = null;
    public DcMotor armmotor = null;
    public DcMotor lazysusan = null;
    public Servo rightClaw = null;
    public Servo leftClaw = null;



    static final double MIN_ARM_POSITION = -400;
    static final double MAX_ARM_POSITION = 600;
    static final int MULTIPLIER = 2;
    public static final double MID_SERVO = 0.5;
    static final double jawsopen = 0.7;
    static final double jawsclosed = 0;
    static final double lazyleft = -600;
    static final double lazyright = 600;
    /* local OpMode members. */
    HardwareMap hwMap = null;
    private ElapsedTime period = new ElapsedTime();

    /* Constructor */
    public ourhardware() {

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;


        // Define and Initialize Motors
        leftfront = hwMap.get(DcMotor.class, "leftfront");
        rightfront = hwMap.get(DcMotor.class, "rightfront");
        leftback = hwMap.get(DcMotor.class, "leftback");
        rightback = hwMap.get(DcMotor.class, "rightback");
        armmotor = hwMap.get(DcMotor.class, "armmotor");
        lazysusan = hwMap.get(DcMotor.class, "lazysusan");

        leftfront.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        rightfront.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors
        leftback.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        rightback.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
        armmotor.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        lazysusan.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors


        // Set all motors to zero power
        leftfront.setPower(0);
        rightfront.setPower(0);
        leftback.setPower(0);
        rightback.setPower(0);
        armmotor.setPower(0);
        lazysusan.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        leftfront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightfront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftback.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightback.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Define and initialize ALL installed servos.
        rightClaw = hwMap.get(Servo.class, "rightClaw");
        rightClaw.setPosition(MID_SERVO);
        //servo
        rightClaw = hwMap.servo.get("rightClaw");

        leftClaw = hwMap.get(Servo.class, "leftClaw");
        leftClaw.setPosition(MID_SERVO);
        //servo
        leftClaw = hwMap.servo.get("leftClaw");

        armmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armmotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        lazysusan.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lazysusan.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lazysusan.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void move_arm_function(float stick_y_value) {

        int stick = (int) stick_y_value;
        // If stick is > 0 set an up variable
        Boolean up = false;
        if (stick > 0) {
            up = true;
        }

        if (stick == 0) {
            armmotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            armmotor.setPower(0);
            return;
        }

        int change_amount = (stick * 10 * MULTIPLIER);
        int armPosition = (armmotor.getCurrentPosition() + change_amount);

        float armPower = (Range.clip(stick, -1, 1));

        if (armPosition >= MAX_ARM_POSITION && up) {
            armmotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            armmotor.setPower(0);
            return;
        }

        if (armPosition <= MIN_ARM_POSITION && !up) {
            armmotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            armmotor.setPower(0);
            return;
        }

        // armmotor.setTargetPosition(safePosition);
        armmotor.setPower(armPower / 4);
    }

    public void lazy_susan_function(float stick_x_value) {

        int stick = (int) stick_x_value;
        // If stick is > 0 set an up variable
        Boolean up = false;
        if (stick > 0) {
            up = true;
        }

        if (stick == 0) {
            lazysusan.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            lazysusan.setPower(0);
            return;
        }

        int change_amount = (stick * 10 * MULTIPLIER);
        int lazySusanPosition = (lazysusan.getCurrentPosition() + change_amount);
        // int safePosition = (Range.clip(armPosition,MIN_ARM_POSITION,MAX_ARM_POSITION));

        float lazySusanPower = (Range.clip(stick, -1, 1));

        if (lazySusanPosition >= lazyright && up) {
            lazysusan.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            lazysusan.setPower(0);
            return;
        }

        if (lazySusanPosition <= lazyleft && !up) {
            lazysusan.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            lazysusan.setPower(0);
            return;
        }

        // armmotor.setTargetPosition(safePosition);
        lazysusan.setPower(lazySusanPower / 4);
    }

    /* We stole this code from https://github.com/ftc-9773/ftc_app_9773/blob/master/FtcRobotController/src/main/java/com/qualcomm/ftcrobotcontroller/opmodes/TeleOp.java */
    public void mecanumWheelDrive(float strafeDirection, float strafeThrottle, float turnDirection, float turnThrottle) {
        strafeDirection = Range.clip(strafeDirection, -1, 1);
        strafeThrottle = Range.clip(strafeThrottle, -1, 1);
        turnThrottle = Range.clip(turnThrottle, -1, 1);
        turnDirection = Range.clip(turnDirection, -1, 1);

        float frontLeftPwr = (Range.clip(strafeThrottle + strafeDirection, -1, 1) - Range.clip(turnThrottle + turnDirection, -1, 1));
        float frontRightPwr = (Range.clip(strafeDirection - strafeThrottle, -1, 1) + Range.clip(turnThrottle - turnDirection, -1, 1));
        float rearLeftPwr = Range.clip(strafeDirection - strafeThrottle, -1, 1) + Range.clip(turnThrottle + turnDirection, -1, 1);
        float rearRightPwr = Range.clip(strafeThrottle + strafeDirection, -1, 1) - Range.clip(turnThrottle - turnDirection, -1, 1);

        leftfront.setPower(Range.clip(frontLeftPwr, -1, 1));
        rightback.setPower(Range.clip(rearRightPwr, -1, 1));
        rightfront.setPower(Range.clip(frontRightPwr, -1, 1));
        leftback.setPower(Range.clip(rearLeftPwr, -1, 1));
    }

    public void driveForward(double speed) {
        // Do driving forward stuff
        leftfront.setPower(speed);
        rightfront.setPower(speed);
        leftback.setPower(speed);
        rightback.setPower(speed);
    }

    public void driveBackward(double speed) {
        //Do driving backward stuff
        leftfront.setPower(-speed);
        rightfront.setPower(-speed);
        leftback.setPower(-speed);
        rightback.setPower(-speed);
    }

    public void driveRight(double speed) {
        // Do driving right stuff
        leftfront.setPower(speed);
        rightfront.setPower(-speed);
        leftback.setPower(-speed);
        rightback.setPower(speed);
    }

    public void driveLeft(double speed) {
        // Do driving left stuff
        leftfront.setPower(-speed);
        rightfront.setPower(speed);
        leftback.setPower(speed);
        rightback.setPower(-speed);
    }

    public void turnRight(double speed) {
        // Do turning right stuff
        leftfront.setPower(speed);
        rightfront.setPower(-speed);
        leftback.setPower(speed);
        rightback.setPower(-speed);
    }

    public void turnLeft(double speed) {
        // Do turning left stuff
        leftfront.setPower(-speed);
        rightfront.setPower(speed);
        leftback.setPower(-speed);
        rightback.setPower(speed);
    }

    public void diagonalForwardRight(double speed) {
        // Do driving diagonal forward right stuff
        leftfront.setPower(speed);
        rightback.setPower(speed);
    }

    public void diagonalForwardLeft(double speed) {
        // Do driving diagonal forward left stuff
        rightfront.setPower(speed);
        leftback.setPower(speed);
    }

    public void diagonalBackwardRight(double speed) {
        // Do driving diagonal backward right stuff
        rightfront.setPower(-speed);
        leftback.setPower(-speed);
    }

    public void diagonalBackwardLeft(double speed) {
        // Do driving diagonal backward left stuff
        leftfront.setPower(-speed);
        rightback.setPower(-speed);
    }


    public void motorStop(double speed) {
        // Do stopping stuff
        leftfront.setPower(0);
        rightfront.setPower(0);
        leftback.setPower(0);
        rightback.setPower(0);
    }
}