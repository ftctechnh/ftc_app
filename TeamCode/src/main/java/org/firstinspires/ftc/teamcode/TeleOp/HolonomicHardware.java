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

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

// Created by Roma Bhatia  on 9/21/17
//
//
//  Last edit: 10/21/17 BY MRINAAL RAMACHANDRAN

public class HolonomicHardware {

    // PUBLIC OPMODE MEMBERS
    public DcMotor  F_L = null;
    public DcMotor  F_R = null;
    public DcMotor  R_L = null;
    public DcMotor  R_R = null;
    public DcMotor elevator = null;
    public DcMotor clamp = null;


    public Servo dropper = null;

    // LOCAL OPMODE MEMBERS
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    // Constructor
    public HolonomicHardware(){

    }

    // Init function for Hardware
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        F_L = hwMap.get(DcMotor.class, "F_L");
        F_R = hwMap.get(DcMotor.class, "F_R");
        R_L = hwMap.get(DcMotor.class, "R_L");
        R_R = hwMap.get(DcMotor.class, "R_R");
        clamp = hwMap.get(DcMotor.class, "clamp");
        elevator = hwMap.get(DcMotor.class, "upThingy");


        dropper = hwMap.get(Servo.class, "dropper");
        dropper.setDirection(Servo.Direction.REVERSE);

        // REVERSE THE MOTORS
        F_L.setDirection(DcMotor.Direction.REVERSE);
        F_R.setDirection(DcMotor.Direction.REVERSE);
        R_L.setDirection(DcMotor.Direction.REVERSE);
        R_R.setDirection(DcMotor.Direction.REVERSE);
        clamp.setDirection(DcMotor.Direction.REVERSE);

        // SET ALL MOTOR POWERS TO ZERO
        F_L.setPower(0);
        F_R.setPower(0);
        R_L.setPower(0);
        R_R.setPower(0);
        clamp.setPower(0);

        //SET ALL SERVOS TO POSITION




        // SET THE MOTORS TO RUN WITHOUT ENCODERS
        F_L.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        F_R.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        R_L.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        R_R.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }
 }

