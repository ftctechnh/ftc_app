/* Copyright (c) 2014, 2015 Qualcomm Technologies Inc

All rights reserved.

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
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;


/**
 * TeleOp Mode
 * <p>
 *Enables control of the robot via the gamepad
 */
public class OmniBotTouchOp extends OpMode {



  /*
   * Code to run when the op mode is first enabled goes here
   * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
   */


    @Override
    public void init() {

    }
    DcMotor motor1;
    DcMotor motor2;
    DcMotor motor3;
    DcMotor motor4;

    public OmniBotTouchOp() {

    }

    ElapsedTime elapsedTime = new ElapsedTime();
    TouchSensor touchSensor;

    @Override
    public void start() {

        touchSensor = hardwareMap.touchSensor.get("touchSensor");
        motor1 = hardwareMap.dcMotor.get("motor_1");
        motor2 = hardwareMap.dcMotor.get("motor_2");
        motor3 = hardwareMap.dcMotor.get("motor_3");
        motor4 = hardwareMap.dcMotor.get("motor_4");

        motor1.setDirection(DcMotor.Direction.REVERSE);
        motor4.setDirection(DcMotor.Direction.REVERSE);

        elapsedTime.reset();
        elapsedTime.startTime();

    }

    @Override
    public void loop() {

        double etouch;
        etouch = touchSensor.getValue();
        double etime;
        etime = elapsedTime.time();

        if (etouch == 0) {
            motor1.setPower(0.25);
            motor2.setPower(0.25);
            motor3.setPower(0.25);
            motor4.setPower(0.25);
        } else {
            elapsedTime.reset();
            elapsedTime.startTime();

            if (etime < 1) {
                motor1.setPower(0);
                motor2.setPower(0);
                motor3.setPower(0);
                motor4.setPower(0);
            } else {
                elapsedTime.reset();
                elapsedTime.startTime();
                if (etime < 1) {
                    motor1.setPower(-0.15);
                    motor2.setPower(0.15);
                    motor3.setPower(0.15);
                    motor4.setPower(-0.15);
                } else {
                    elapsedTime.reset();
                    elapsedTime.startTime();
                    if (etime < 1) {
                        motor1.setPower(0);
                        motor2.setPower(0);
                        motor3.setPower(0);
                        motor4.setPower(0);
                    } else {
                        elapsedTime.reset();
                        elapsedTime.startTime();
                        if (etime < 5) {
                            motor1.setPower(0.25);
                            motor2.setPower(0.25);
                            motor3.setPower(0.25);
                            motor4.setPower(0.25);
                        }
                        else {
                            motor1.setPower(0);
                            motor2.setPower(0);
                            motor3.setPower(0);
                            motor4.setPower(0);
                        }
                    }

                }
            }
            telemetry.addData("Touch Sensor", "Touch sensor is " + String.format("%d", etouch));


        }


    }
    @Override
    public void stop(){

    }
}






