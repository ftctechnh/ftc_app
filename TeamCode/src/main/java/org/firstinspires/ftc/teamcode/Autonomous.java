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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;

/**
 *Created by Pramodh and Diego and Ronit on 10/27/2017.
 */
@TeleOp
public class Autonomous extends LinearOpMode {
    NormalizedColorSensor colorSensor;
    private DcMotor motor0;
    private Servo servo2;
    private Servo servo3;

    @Override
    public void runOpMode() {

        waitForStart();
        motor0 = hardwareMap.get(DcMotor.class, "motor0");
        servo2 = hardwareMap.get(Servo.class, "servo2");
        servo3 = hardwareMap.get(Servo.class, "servo3");
       colorSensor = hardwareMap.get(NormalizedColorSensor.class, "colorSensor");

        VerticalLift lift = new VerticalLift(servo2);
        Dropdown dropdown = new Dropdown(servo3, motor0, colorSensor);

        int scale = 10000;

        //run until the end of the match (driver presses STOP)
        while(opModeIsActive()) {
            dropdown.runDrop();

            // Read the sensor
            NormalizedRGBA colors = colorSensor.getNormalizedColors();
           int color = colors.toColor();

            lift.Lift(this.gamepad1.left_bumper, this.gamepad2.left_bumper, this.gamepad1.left_trigger, this.gamepad2.left_trigger);


            telemetry.addLine("raw Android color: ");
            telemetry.addData("a", (int)(scale*colors.alpha));
            telemetry.addData("r", (int)(scale*colors.red));
            telemetry.addData("g", (int)(scale*colors.green));
            telemetry.addData("b", (int)(scale*colors.blue));
            telemetry.update();

        }
    }
}