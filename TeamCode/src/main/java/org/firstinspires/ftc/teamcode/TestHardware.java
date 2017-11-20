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
 * Motor channel:  Lift motor:               "lift"
 */
public class TestHardware
{
    /* Public OpMode members. */
    public DcMotor  leftDrive   = null;
    public DcMotor  rightDrive  = null;
    public DcMotor  lift        = null;
   // public Servo leftClaw = null;
    //public Servo rightClaw = null;
    public int liftPosition = 0;


    // lift constants
    public static final int COUNT_ONE_REV = 280;

    public static final int MINIMUM_LIFT_POSITION = 2240;
    public static final int ONE_INCH_POSITION = 2240;
    public static final int SEVEN_INCH_POSITION = 4480;
    public static final int THIRTEEN_INCH_POSITION = 6720;
    public static final int NINETEEN_INCH_POSITION = 8960;
    public static final int MAXIMUM_LIFT_POSITION = 8960;

    public static final double LIFT_POWER = 1.0;

    // claw constants
    public static final double LCLAW_START = 0;
    public static final double LCLAW_DRIVE = .4;
    public static final double LCLAW_CLOSED = 1;
    public static final double RCLAW_START = 0;
    public static final double RCLAW_DRIVE = .4;
    public static final double RCLAW_CLOSED = 1;

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public TestHardware(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        leftDrive  = hwMap.get(DcMotor.class, "left_drive");
        rightDrive = hwMap.get(DcMotor.class, "right_drive");
        lift    = hwMap.get(DcMotor.class,    "lift");
        // Define and initialize servos
       // leftClaw = hwMap.get(Servo.class,     "left_claw");
       // rightClaw = hwMap.get(Servo.class,    "right_claw");

        // Set Direction of motors
        leftDrive.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        rightDrive.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors
        lift.setDirection(DcMotor.Direction.FORWARD);


        // Set all motors to zero power
        leftDrive.setPower(0);
        rightDrive.setPower(0);
        lift.setPower(0);




        // Drive motors set to run without encoder
        leftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // lift is set to run to position
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        // Define and initialize ALL installed servos.
        //lift.setTargetPosition(ONE_INCH_POSITION);

      //  leftClaw.setPosition(LCLAW_START);
      //
        //  rightClaw.setPosition(RCLAW_START);
    }
 }

