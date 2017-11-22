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
 * Motor channel:  Manipulator drive motor:  "left_arm"
 * Servo channel:  Servo to open left claw:  "left_hand"
 * Servo channel:  Servo to open right claw: "right_hand"
 */
public class HardwareGromit
{
    /* Public OpMode members. */
    public DcMotor  right_front   = null;
    public DcMotor  right_back  = null;
    public DcMotor  left_front   = null;
    public DcMotor  left_back  = null;
//    public DcMotor  leftArm     = null;
//    public Servo    leftClaw    = null;
    public Servo    jewelsservo   = null;
    public Servo    leftlower   = null;
    public Servo    rightlower   = null;


    /**
        _         _                                                                _       _     _
       /_\  _   _| |_ ___  _ __   ___  _ __ ___   ___  _   _ ___  /\   /\__ _ _ __(_) __ _| |__ | | ___  ___
      //_\\| | | | __/ _ \| '_ \ / _ \| '_ ` _ \ / _ \| | | / __| \ \ / / _` | '__| |/ _` | '_ \| |/ _ \/ __|
     /  _  \ |_| | || (_) | | | | (_) | | | | | | (_) | |_| \__ \  \ V / (_| | |  | | (_| | |_) | |  __/\__ \
     \_/ \_/\__,_|\__\___/|_| |_|\___/|_| |_| |_|\___/ \__,_|___/   \_/ \__,_|_|  |_|\__,_|_.__/|_|\___||___/
     */
    public static final double turn_THRESHOLD      =  2.0 ;
    public static final double turn_MIN_SPEED    =  0.3 ;
    public static final double turn_COEF  = 0.90 ;
    public static final double drive_COEF  = 0.90 ; //Maximum additional speed to add to a motor during a gyro drive

    /**
      _____     _              ___                         _       _     _
     /__   \___| | ___        /___\_ __   /\   /\__ _ _ __(_) __ _| |__ | | ___  ___
       / /\/ _ \ |/ _ \_____ //  // '_ \  \ \ / / _` | '__| |/ _` | '_ \| |/ _ \/ __|
      / / |  __/ |  __/_____/ \_//| |_) |  \ V / (_| | |  | | (_| | |_) | |  __/\__ \
      \/   \___|_|\___|     \___/ | .__/    \_/ \__,_|_|  |_|\__,_|_.__/|_|\___||___/
                                  |_|
     */





    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public HardwareGromit(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;


        // Define and Initialize Motors
        right_front  = hwMap.get(DcMotor.class, "right_front");
        right_back  = hwMap.get(DcMotor.class, "right_rear");
        left_front = hwMap.get(DcMotor.class, "left_front");
        left_back = hwMap.get(DcMotor.class, "left_rear");

        right_front.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        left_front.setDirection(DcMotor.Direction.FORWARD); // Set to Forward if using AndyMark motors
        right_back.setDirection(DcMotor.Direction.REVERSE);
        left_back.setDirection(DcMotor.Direction.FORWARD);

        // Set all motors to zero power
        right_front.setPower(0.0);
        right_back.setPower(0.0);
        left_front.setPower(0.0);
        left_back.setPower(0.0);


        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        right_front.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right_back.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        left_front.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        left_back.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Define and initialize ALL installed servos.
//        leftClaw  = hwMap.get(Servo.class, "left_hand");
        jewelsservo = hwMap.get(Servo.class, "jewel_arm");
        rightlower = hwMap.get(Servo.class, "right_lower");
        leftlower = hwMap.get(Servo.class, "left_lower");
//        leftClaw.setPosition(MID_SERVO);
//        rightClaw.setPosition(MID_SERVO);
    }
 }

