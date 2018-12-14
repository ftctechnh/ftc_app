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

package org.firstinspires.ftc.teamcode.HardwareMap;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import static java.lang.Math.PI;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a Pushbot.
 * See PushbotTeleopTank_Iterative and others classes starting with "Pushbot" for usage examples.
 *
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 *
 * Motor channel:  Left  drive motor:        "left_drive"
 * Motor channel:  Right drive motor:        "right_drive"
 * Motor channel:  Manipulator drive motor:  "left_arm"
 * Servo channel:  Servo to open left claw:  "left_hand"
 * Servo channel:  Servo to open right claw: "right_hand"
 */
public class BasicChasieHardware
{
    /* Public OpMode members. */
    double left;
    double right;

    public DcMotor  leftDriveFront   = null;
    public DcMotor  rightDriveFront  = null;
    public DcMotor  rightDriveBack   = null;
    public DcMotor  leftDriveBack    = null;
    public DcMotor  arm     = null;
    public Servo    markerDropper   = null;

    public static final double MID_SERVO       =  0.5 ;
    public static final double ARM_UP_POWER    =  0.45 ;
    public static final double ARM_DOWN_POWER  = -0.45 ;

    /* local OpMode members. */
    HardwareMap hwMap           =  null;

    // motor ticks per revolution
    public static double REV_CORE_HEX_TICKS = 288;

    private double wheelDiameter = 3.54331;
    private double motorTicksPerRotation = REV_CORE_HEX_TICKS;
    private double motorTicksPerInch;
    private double motorDistancePerRotation;

    // TODO: Digital switches for Alliance, Side
//    DigitalChannel dcAlliance;
//    DigitalChannel dcSide;

    /* Constructor */
    public BasicChasieHardware(){

    }

    public double ticksToMove(double distance) {
        return distance * motorTicksPerInch;
    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {

        // Calculations needed for distance driving are based on wheel size
        motorDistancePerRotation = PI * wheelDiameter;
        motorTicksPerInch = motorTicksPerRotation / motorDistancePerRotation;

        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors

        leftDriveFront  = hwMap.get(DcMotor.class, "LFD");
        rightDriveFront = hwMap.get(DcMotor.class, "RFD");

        leftDriveBack  = hwMap.get(DcMotor.class, "LBD");
        rightDriveBack = hwMap.get(DcMotor.class, "RBD");

        arm = hwMap.get(DcMotor.class, "arm");

        markerDropper = hwMap.get(Servo.class, "mark");

        leftDriveFront.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        rightDriveFront.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
        leftDriveBack.setDirection(DcMotor.Direction.FORWARD);
        rightDriveBack.setDirection(DcMotor.Direction.FORWARD);
        arm.setDirection(DcMotorSimple.Direction.FORWARD);

        // Set all motors to zero power
        leftDriveFront.setPower(0);
        rightDriveFront.setPower(0);
        leftDriveBack.setPower(0);
        rightDriveBack.setPower(0);
        arm.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        leftDriveFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightDriveFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftDriveBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightDriveBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Set the zero power position for all motors to BRAKE (hold position)
        leftDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftDriveBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDriveBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Set the initial position of the servo to 0
        markerDropper.setDirection(Servo.Direction.REVERSE);
        markerDropper.setPosition(1);

        // TODO: Get the digital channels for Alliance and Side
//        dcAlliance = ahwMap.get(DigitalChannel.class, "alliance");
//        dcSide = ahwMap.get(DigitalChannel.class, "side");

        // TODO: Set mode for digital channels
//        dcAlliance.setMode(DigitalChannel.Mode.INPUT);
//        dcSide.setMode(DigitalChannel.Mode.INPUT);

    }

}

