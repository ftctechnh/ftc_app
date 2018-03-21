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

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 */
public class HardwareRobot
{
    /* Public OpMode members. */
    /** MOTOARE **/
    public DcMotor Lift = null;
    public DcMotor FrontLeftMotor = null;
    public DcMotor FrontRightMotor = null;
    public DcMotor BackLeftMotor = null;
    public DcMotor BackRightMotor = null;
    public DcMotor Brat = null;
    public DcMotor BratRetreat = null;

    /** SERVO **/
    public Servo Up_Hand = null;
    public Servo Down_Hand = null;
    public Servo Color_Hand = null;
    public Servo Pivot_Relic = null;
    public Servo Claw_Relic = null;
    public Servo Jewel_Hand = null;

    /** SENSORI **/
    public ColorSensor colorSensor;
    public IntegratingGyroscope gyro;
    public ModernRoboticsI2cGyro modernRoboticsI2cGyro;

    /** VARIABILE **/
    public static final double START_POS_UP     = 0.2;///0 open
    public static final double GRAB_POS_UP      =  0.5;///1 closed
    public static final double START_POS_DOWN   = 0.8 ;///0 open
    public static final double GRAB_POS_DOWN    =  1;///1 closed okish

    public static final double RELIC_POS_PIVOT_INIT = 0;
    public static final double RELIC_POS_PIVOT_UP = 1;
    public static final double RELIC_POS_CLAW_INIT  = 0.2;
    public static final double RELIC_POS_CLAW_HAND  = 0.9;

    public static final double COLOR_POS_INIT = 0.4;
    public static final double COLOR_POS_DOWN = 0.7;
    public static final double JEWEL_START_POS = 0.5;
    public static final double JEWEL_LEFT_POS = 0.5;
    public static final double JEWEL_RIGHT_POS = 0.5;

    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();
    public HardwareRobot(){

    }

    public void init(HardwareMap ahwMap) {
        hwMap = ahwMap;
        /** MOTOARE **/
        Lift = hwMap.get(DcMotor.class, "Lift");
        FrontLeftMotor = hwMap.get(DcMotor.class, "FrontLeftMotor");
        FrontRightMotor = hwMap.get(DcMotor.class, "FrontRightMotor");
        BackLeftMotor = hwMap.get(DcMotor.class, "BackLeftMotor");
        BackRightMotor = hwMap.get(DcMotor.class, "BackRightMotor");
        Brat = hwMap.get(DcMotor.class, "Brat");
        BratRetreat = hwMap.get(DcMotor.class, "BratRetreat");

        // DIRECTIE
        Lift.setDirection(DcMotor.Direction.FORWARD);
        FrontLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        FrontRightMotor.setDirection(DcMotor.Direction.FORWARD);
        BackLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        BackRightMotor.setDirection(DcMotor.Direction.FORWARD);
        Brat.setDirection(DcMotor.Direction.FORWARD);
        BratRetreat.setDirection(DcMotor.Direction.FORWARD);

        // Set all motors to zero power
        Lift.setPower(0);
        FrontLeftMotor.setPower(0);
        FrontRightMotor.setPower(0);
        BackLeftMotor.setPower(0);
        BackRightMotor.setPower(0);
        Brat.setPower(0);
        BratRetreat.setPower(0);

        /** MOTOARE **/
        Lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FrontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FrontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BackLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BackRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        /** SERVO **/
        Up_Hand = hwMap.get(Servo.class, "Up_Hand");
        Down_Hand = hwMap.get(Servo.class, "Down_Hand");
        Color_Hand = hwMap.get(Servo.class, "Color_Hand");
        Pivot_Relic = hwMap.get(Servo.class, "Pivot_Relic");
        Claw_Relic = hwMap.get(Servo.class, "Claw_Relic");
        Jewel_Hand = hwMap.get(Servo.class, "Jewel_Hand");

        Up_Hand.setPosition(START_POS_UP);
        Down_Hand.setPosition(START_POS_DOWN);
        Color_Hand.setPosition(COLOR_POS_INIT);
        Pivot_Relic.setPosition(RELIC_POS_PIVOT_INIT);
        Claw_Relic.setPosition(RELIC_POS_CLAW_INIT);
        Jewel_Hand.setPosition(JEWEL_START_POS);

        /** SENSORI **/
        colorSensor = hwMap.get(ColorSensor.class, "sensor_color");///nu merge
        boolean bLedOn = true;///nu merge
        colorSensor.enableLed(bLedOn);///nu merge
        modernRoboticsI2cGyro = hwMap.get(ModernRoboticsI2cGyro.class, "sensor_gyro");
        gyro = (IntegratingGyroscope)modernRoboticsI2cGyro;

    }
 }

