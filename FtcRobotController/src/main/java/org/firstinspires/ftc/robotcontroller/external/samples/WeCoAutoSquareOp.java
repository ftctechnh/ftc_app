/* Copyright (c) 2014, 2015 Qualcomm Technologies Inc

Some rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TeleOp Mode
 * <p>
 *Enables control of the robot via the gamepad
 */
public class WeCoAutoSquareOp extends OpMode {


    @Override
    public void init() {

        count = 0;
    }

    DcMotor motor1;
    DcMotor motor2;

    float resetValue1, resetValue2 = 0;
    float motorPosition1, motorPosition2 = 0;
    double lastposition = 0;
    double targetposition = 0;
    double position1, position2;
    double count;
    double etime;
    int whattodo = 0; //0 do nothing; 1 moveforward; 2 turn


    public WeCoAutoSquareOp(){

    }

    static final float normalTurnSpeed = (float) 0.75;
    static final float normalSpeed = (float) 0.75;

    ElapsedTime elapsedTime = new ElapsedTime();



    float motor1Power = 0;
    float motor2Power = 0;

    @Override
    public void start() {
        motor1 = hardwareMap.dcMotor.get("motorRight");
        motor2 = hardwareMap.dcMotor.get("motorLeft");

        motor1.setDirection(DcMotor.Direction.REVERSE) ;
        count = 0;
        whattodo = 1;

    }

    @Override
    public void loop() {
        etime = elapsedTime.time() * 100;
        // double etimemod2 = etime % 2;


        switch (whattodo) {
            case 1:
                resetValue1 = motor1.getCurrentPosition();
                resetValue2 = motor2.getCurrentPosition();
                moveForward();
                whattodo = 2;
            case 2:
                // gets current position and uses formula to find rotations or distance in inches
                position1 = -motor1.getCurrentPosition();
                position2 = motor2.getCurrentPosition();

                position1 = (position1 / 2500);//(wheelDiameter*3.14159265358)
                position2 = (position2 / 2500); //(wheelDiameter*3.14159265358)
                if((Math.abs(position1) > 8) && (position2 > 8))
                    whattodo =3;
            case 3:
                stopMove();
                resetValue1 = motor1.getCurrentPosition();
                resetValue2 = motor2.getCurrentPosition();
                startLeftTurn();
                whattodo = 4;
            case 4:
                // gets current position and uses formula to find rotations or distance in inches
                position1 = motorPosition(motor1, resetValue1);
                position2 = motorPosition(motor2, resetValue2);

                position1 = (position1 / 2500);//(wheelDiameter*3.14159265358)
                position2 = (position2 / 2500); //(wheelDiameter*3.14159265358)
                if((Math.abs(position1) > 2) && (position2 > 2))
                    whattodo = 5;
            case 5:
                stopMove();
                resetValue1 = motor1.getCurrentPosition();
                resetValue2 = motor2.getCurrentPosition();
                count ++;
                if (count == 4) {
                    whattodo = 6;
                }
                else {
                    whattodo = 1;
                }
            case 6:
                stopMove();
        }


        motor1.setPower(motor1Power);

        telemetry.addData("Motor 1 Power", "Motor 1 power is " + String.format("%.2f", motor1Power));
        telemetry.addData("Motor 2 Power", "Motor 2 power is " + String.format("%.2f", motor2Power));
        telemetry.addData("Position1", "Position1 is " + String.format("%.2f", position1));
        telemetry.addData("Count", "Count is " + String.format("%.2f", count));
        telemetry.addData("Position2", "Position2 is " + String.format("%.2f", position2));
        telemetry.addData("WhatToDo", "WhatToDo is " + String.format("%d", whattodo));
    }
    public void startLeftTurn(){
        motor1Power = -normalTurnSpeed;
        motor2Power = normalTurnSpeed;


    }
    public void moveForward(){
        motor1Power = normalSpeed;
        motor2Power = normalSpeed;

    }
    public void stopMove(){
        motor1Power = 0;
        motor2Power = 0;
    }

    public float motorPosition(DcMotor motor, float resetValue) {
        return(motor.getCurrentPosition() - resetValue) ;

    }




}

