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
import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is NOT an opmode.
 * <p>
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a K9 robot.
 * <p>
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 * <p>
 * Motor channel:  Left  drive motor:        "left_drive"
 * Motor channel:  Right drive motor:        "right_drive"
 * Servo channel:  Servo to raise/lower arm: "arm"
 * Servo channel:  Servo to open/close claw: "claw"
 * <p>
 * Note: the configuration of the servos is such that:
 * As the arm servo approaches 0, the arm position moves up (away from the floor).
 * As the claw servo approaches 0, the claw opens up (drops the game element).
 */

public class HardwareRobot {

    /* Public OpMode members. */
    public DcMotor leftDriveFront = null;
    public DcMotor leftDriveBack = null;
    public DcMotor rightDriveFront = null;
    public DcMotor rightDriveBack = null;

    final static double COUNTS_PER_MOTOR_REV = 1760;
    final static double DRIVE_GEAR_REDUCTION = 2;
    final static double WHEEL_DIAMETER_INCHES = 4;
    public final static double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
//    public Servo    arm         = null;
//    public Servo    claw        = null;
//    public final static double ARM_HOME = 0.2;
//    public final static double CLAW_HOME = 0.2;
//    public final static double ARM_MIN_RANGE  = 0.20;
//    public final static double ARM_MAX_RANGE  = 0.90;
//    public final static double CLAW_MIN_RANGE  = 0.20;
//    public final static double CLAW_MAX_RANGE  = 0.7;

    /* Local OpMode members. */
    HardwareMap hwMap = null;
    private ElapsedTime period = new ElapsedTime();

    /* Constructor */
    public HardwareRobot() {
    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {

        // save reference to HW Map
        hwMap = ahwMap;

        // Define and Initialize Motors
        leftDriveFront = hwMap.get(DcMotor.class, "leftMotorFront");
        rightDriveFront = hwMap.get(DcMotor.class, "rightMotorFront");
        leftDriveBack = hwMap.get(DcMotor.class, "leftMotorBack");
        rightDriveBack = hwMap.get(DcMotor.class, "rightMotorBack");
        leftDriveFront.setDirection(DcMotor.Direction.REVERSE);
        leftDriveBack.setDirection(DcMotor.Direction.REVERSE);

        // Set all motors to zero power
        setAllLeftDrivePower(0);
        setAllRightDrivePower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        leftDriveFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDriveFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftDriveBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDriveBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Define and initialize ALL installed servos.
        //arm  = hwMap.get(Servo.class, "arm");
        //claw = hwMap.get(Servo.class, "claw");
        //arm.setPosition(ARM_HOME);
        //claw.setPosition(CLAW_HOME);

    }

    public void encoderSwitch() {

        if (leftDriveFront.getMode().equals(DcMotor.RunMode.RUN_WITHOUT_ENCODER)) {

            leftDriveFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightDriveFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftDriveBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightDriveBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        }
        else
            {

            leftDriveFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightDriveFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            leftDriveBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightDriveBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        }
    }

    public void setAllLeftDrivePower(double power) {

        leftDriveFront.setPower(power);
        leftDriveBack.setPower(power);

    }

    public void setAllRightDrivePower(double power) {

        rightDriveFront.setPower(power);
        rightDriveBack.setPower(power);

    }

    public void driveWithEncoder(DcMotor motor, double target, double power) {

        int currentPos = motor.getCurrentPosition();
        int targetPos = currentPos + (int)(COUNTS_PER_INCH * target);

        motor.setTargetPosition(targetPos);

        motor.setPower(power);

        while (Math.abs(motor.getCurrentPosition() - targetPos) > 1) {

        }

        motor.setPower(0);
    }

    public void driveMotorsWithEncoder(DcMotor[] motor, double target, double power) {

        int currentPos;
        int targetPos;

        for (int i = 0; i < motor.length; i += 1) {

            currentPos = motor[i].getCurrentPosition();
            targetPos = currentPos + (int)(COUNTS_PER_INCH * target);

            motor[i].setTargetPosition(targetPos);

            motor[i].setPower(power);

        }
        
        while (motor[0].getCurrentPosition() > 1) {

        }

        for (int i = 0; i < motor.length; i += 1) {

            motor[i].setPower(0);

        }
    }
}