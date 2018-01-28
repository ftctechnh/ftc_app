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

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Servo;
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

public class    HardwareRobot {

    /* Public OpMode members. */
    public DcMotor leftDriveFront = null;
    public DcMotor leftDriveBack = null;
    public DcMotor rightDriveFront = null;
    public DcMotor rightDriveBack = null;
    public Servo s1 = null;
    public Servo s2 = null;
    public Servo s3 = null;
    public Servo s4 = null;
    public DcMotor elev1 = null;
    //
    //public DcMotor elev2 = null;

    final static double COUNTS_PER_MOTOR_REV = 1750;
    final static double DRIVE_GEAR_REDUCTION = 1  ;
    final static double WHEEL_DIAMETER_INCHES = 4;
    public final static double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);

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
        leftDriveFront  = hwMap.get(DcMotor.class, "m1");
        rightDriveFront = hwMap.get(DcMotor.class, "m2");
        leftDriveBack  = hwMap.get(DcMotor.class, "m3");
        rightDriveBack = hwMap.get(DcMotor.class, "m4");
        s1 = hwMap.get(Servo.class, "s1");
        s2 = hwMap.get(Servo.class, "s2");
        s3 = hwMap.get(Servo.class, "s3");
        s4 = hwMap.get(Servo.class, "s4");

        elev1 = hwMap.get(DcMotor.class, "e1");
        /*elev2 = hwMap.get(DcMotor.class, "e2");*/

        leftDriveFront.setDirection(DcMotor.Direction.FORWARD);
        rightDriveFront.setDirection(DcMotor.Direction.REVERSE);
        leftDriveBack.setDirection(DcMotor.Direction.FORWARD);
        rightDriveBack.setDirection(DcMotor.Direction.REVERSE);


        leftDriveFront.setPower(0.);
        leftDriveBack.setPower(0);
        rightDriveFront.setPower(0);
        rightDriveBack.setPower(0);

        elev1.setPower(0);

        s1.setPosition(0.8);
        s2.setPosition(0.5);
        s3.setPosition(0.3);
        s4.setPosition(0.9);



        /*cvn1.setDirection(CRServo.Direction.REVERSE);
        elev1.setDirection(DcMotor.Direction.REVERSE);*/

        /*leftDriveFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDriveFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftDriveBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDriveBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        leftDriveFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDriveFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftDriveBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDriveBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);*/
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

    public void runDistance (int distance, int left_power, int right_power) {
        int current = rightDriveFront.getCurrentPosition();
        int target = current + (int)(COUNTS_PER_INCH * distance);


        rightDriveFront.setTargetPosition(target);
        rightDriveBack.setTargetPosition(target);
        leftDriveFront.setTargetPosition(target);
        leftDriveBack.setTargetPosition(target);

        setAllLeftDrivePower(left_power);
        setAllRightDrivePower(right_power);
        while( Math.abs(target - leftDriveFront.getCurrentPosition()) > 100 && Math.abs(target - rightDriveFront.getCurrentPosition()) > 100 && Math.abs(target - leftDriveBack.getCurrentPosition()) > 100 && Math.abs(target - rightDriveBack.getCurrentPosition()) > 100)
        {

        }
        //robot.rightDriveFront.
        //robot.rightDriveFront.setPower(0);
        setAllLeftDrivePower(left_power/2);
        setAllRightDrivePower(right_power/2);

        while( Math.abs(target - leftDriveFront.getCurrentPosition()) > 50 && Math.abs(target - rightDriveFront.getCurrentPosition()) > 50 && Math.abs(target - leftDriveBack.getCurrentPosition()) > 50 && Math.abs(target - rightDriveBack.getCurrentPosition()) > 50)
        {

        }
        //robot.rightDriveFront.
        //robot.rightDriveFront.setPower(0);
        setAllLeftDrivePower(left_power/4);
        setAllRightDrivePower(right_power/4);

        while( Math.abs(target - leftDriveFront.getCurrentPosition()) > 1 && Math.abs(target - rightDriveFront.getCurrentPosition()) > 1 && Math.abs(target - leftDriveBack.getCurrentPosition()) > 1 && Math.abs(target - rightDriveBack.getCurrentPosition()) > 1)
        {

        }
        //robot.rightDriveFront.
        //robot.rightDriveFront.setPower(0);
        setAllLeftDrivePower(0);
        setAllRightDrivePower(0);

    }
    public void setAllLeftDrivePower(double power) {
        leftDriveFront.setPower(power);
        leftDriveBack.setPower(power);
    }

    public void setAllRightDrivePower(double power) {
        rightDriveFront.setPower(power);
        rightDriveBack.setPower(power);
    }
}
