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

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="Jewel R2", group ="Jewel")
public class JewelCode extends LinearOpMode {

    public ColorSensor colorSensorL;
    public Servo loweringJewelServo;
    public Servo turningJewelServo;

    public double downPos = 0.7;
    public final double UP_POS = 0.3;

    public final double LEFT_POS = .25;
    public final double RIGHT_POS = .75;

    public final double MIDDLE_POS = .5;

    public double increment = .07;

    public placement myPlacement;

    public alliance team;


    @Override public void runOpMode() {
        colorSensorL = hardwareMap.get(ColorSensor.class, "color sensor left");
        loweringJewelServo = hardwareMap.get(Servo.class, "lowering servo" );
        turningJewelServo = hardwareMap.get(Servo.class, "turning servo");

        telemetry.addData(">", "Press Play to start");
        telemetry.update();

        loweringJewelServo.setPosition(0);
        turningJewelServo.setPosition(.5);
        waitForStart();

        lower();

        while(opModeIsActive()) {
            telemetry.addData("Turing Servo:", turningJewelServo.getPosition());
            sleep(3000);
            red();
        }
        telemetry.addData("Running", "False");
        telemetry.update();
    }

    public boolean isLeft() {

        telemetry.addData("Red:", colorSensorL.red());
        telemetry.addData("Blue:", colorSensorL.blue());

        telemetry.update();

       if (colorSensorL.red() > colorSensorL.blue()) {
            telemetry.addLine("See Red");
           telemetry.update();
            return true;
        } else {
            telemetry.addLine("See Blue");
           telemetry.update();
            return false;
        }
    }

    public void lower() {

        loweringJewelServo.setPosition(downPos);
        telemetry.addData("lowerArm", loweringJewelServo.getPosition());
        telemetry.update();
    }


    public void red() {
        telemetry.addData("Red:", colorSensorL.red());
        telemetry.addData("Blue:", colorSensorL.blue());

        telemetry.update();

        if (colorSensorL.red() > colorSensorL.blue()) {
            turningJewelServo.setPosition(RIGHT_POS);
            telemetry.addLine("Moving Right");
        } else {
            turningJewelServo.setPosition(LEFT_POS);
            telemetry.addLine("Hitting Left");
        }
        telemetry.addData("Servo Pos", turningJewelServo.getPosition());
        telemetry.update();
    }

    public void blue() {
        if (isLeft()) {
            turningJewelServo.setPosition(RIGHT_POS);
        } else {
            turningJewelServo.setPosition(LEFT_POS);
        }
    }

    public enum alliance {
        RED, BLUE;
    }

    public enum placement {
        LEFT, RIGHT, NONE;
    }
}
