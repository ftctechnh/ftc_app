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

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;



@TeleOp(name="DANAAAAA", group="Linear Opmode")

public class TankMode extends LinearOpMode {

    // Declaram motoarele si runtime-ul pentru telemetrie
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor FrontRightMotor = null;
    private DcMotor BackRightMotor = null;
    private DcMotor FrontLeftMotor = null;
    private DcMotor BackLeftMotor = null;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initializam motoare cu nume explicite pentru a putea fi selectate eficient la configurare

        FrontRightMotor  = hardwareMap.get(DcMotor.class, " Right_Front");
        FrontLeftMotor = hardwareMap.get(DcMotor.class, "Left_Front");
        BackRightMotor = hardwareMap.get (DcMotor.class, "Right_Back");
        BackLeftMotor = hardwareMap.get (DcMotor.class, "Left_Back");

        // Datorita regulii maini drepte, inversam motoarele din dreapta si le lasam normale pe cele din dreapta pentru a putea merge drept

        FrontLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        FrontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        BackLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        BackRightMotor.setDirection(DcMotor.Direction.REVERSE);

        // De aici coach-ul apasa start si OpMode-ul va rula pana va apasa stop de pe Driver Station

        waitForStart();
        runtime.reset();

        // Atata timp cat OpMode-ul este activ va rula pana la oprire urmatorul cod
        while (opModeIsActive()) {

            // Declaram o variabila pentru fiecare set e motoare, stanga si dreapta

            double leftPower;
            double rightPower;
            double rotateRightPower;

            // Le initializam astfel incat:
            // Atunci cand joystick-ul din stanga este actionat pe axa Y, motoarele din stanga vor porni iar robotul va merge spre dreapta
            // Atunci cand joystick-ul din dreapta, este actionat pe axa Y, motoarele din dreapta vor porni si vor duce robotul in stanga


            // Setam puterea pentru triggere
            leftPower  = gamepad1.right_trigger;
            rightPower = gamepad1.left_trigger;

            rotateRightPower=gamepad1.left_stick_y;




            // Trimitem valorile initializate la motoare
            if(gamepad1.a){

                BackLeftMotor.setPower(-leftPower);
                BackRightMotor.setPower(-rightPower);
                FrontRightMotor.setPower(-rightPower);
                FrontLeftMotor.setPower(-leftPower);

            }
            else {

                BackLeftMotor.setPower(leftPower);
                BackRightMotor.setPower(rightPower);
                FrontRightMotor.setPower(rightPower);
                FrontLeftMotor.setPower(leftPower);


                BackLeftMotor.setPower(-rotateRightPower);
                BackRightMotor.setPower(rotateRightPower);
                FrontRightMotor.setPower(rotateRightPower);
                FrontLeftMotor.setPower(-rotateRightPower);
            }
            // Afisam pe Driver Station timpul in care robotul a rulat si puterea rotilor
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.update();
        }
    }
}
