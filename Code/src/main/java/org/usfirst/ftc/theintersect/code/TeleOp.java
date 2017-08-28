/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.usfirst.ftc.theintersect.code;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleopOld")
public class TeleOp extends LinearOpMode {
    static DcMotor rFmotor, rBmotor, lFmotor, lBmotor, flywheel1, flywheel2, sweeper ;
    public static boolean flywheelStarted = false;

    // RampFlywheel rampFlywheel = new RampFlywheel();
    // RampDownFlywheel rampDownFlywheel = new RampDownFlywheel();
    public void runOpMode() throws InterruptedException {
        double flywheelSpeed = 0;
        double fwdPower, strafePower, rotationPower;
        boolean fwdSweeper, bkwdSweeper;
        boolean extendPusher, retractPusher,
                decreaseFlywheel, increaseFlywheel, pushBeacon;
        rFmotor = hardwareMap.dcMotor.get("rF");
        rBmotor = hardwareMap.dcMotor.get("rB");
        lFmotor = hardwareMap.dcMotor.get("lF");
        lBmotor = hardwareMap.dcMotor.get("lB");
        flywheel1 = hardwareMap.dcMotor.get("flywheel1");
        flywheel2 = hardwareMap.dcMotor.get("flywheel2");
        sweeper = hardwareMap.dcMotor.get("sweeper");
        Servo buttonPusher = hardwareMap.servo.get("buttonPusher");
        lBmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lFmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rBmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rBmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rFmotor.setDirection(DcMotorSimple.Direction.FORWARD);
        rBmotor.setDirection(DcMotorSimple.Direction.FORWARD);
        lBmotor.setDirection(DcMotorSimple.Direction.REVERSE);
        lFmotor.setDirection(DcMotorSimple.Direction.REVERSE);
        flywheel1.setDirection(DcMotorSimple.Direction.REVERSE);
        sweeper.setDirection(DcMotorSimple.Direction.REVERSE);
        waitForStart();

        while (opModeIsActive()) {
            fwdPower = gamepad1.left_stick_y;
            strafePower = -gamepad1.left_stick_x;
            rotationPower = -gamepad1.right_stick_x;
            fwdSweeper = gamepad1.a||gamepad2.b;
            bkwdSweeper = gamepad1.b||gamepad2.a;
            pushBeacon = gamepad1.dpad_down||gamepad2.dpad_down;
            increaseFlywheel = gamepad2.right_bumper||gamepad1.right_bumper;
            decreaseFlywheel = gamepad2.left_bumper||gamepad1.left_bumper;
            extendPusher = gamepad1.dpad_up || gamepad2.dpad_up;
            retractPusher = gamepad1.dpad_down || gamepad2.dpad_up;

            lFmotor.setPower(fwdPower + strafePower + rotationPower);
            lBmotor.setPower(fwdPower - strafePower + rotationPower);
            rFmotor.setPower(fwdPower - strafePower - rotationPower);
            rBmotor.setPower(fwdPower + strafePower - rotationPower);

            if(extendPusher){
                buttonPusher.setPosition(Functions.pusherDownPos);
            }
            if(retractPusher){
                buttonPusher.setPosition(Functions.pusherUpPos);
            }
            if (increaseFlywheel && flywheelSpeed < 1) {
                flywheelSpeed = flywheelSpeed + 0.1;
                flywheel1.setPower(flywheelSpeed);
                flywheel2.setPower(flywheelSpeed);
                Functions.waitFor(100);
            } else if (decreaseFlywheel && flywheelSpeed >= -.5) {
                flywheelSpeed = flywheelSpeed - 0.1;
                flywheel1.setPower(flywheelSpeed);
                flywheel2.setPower(flywheelSpeed);
                Functions.waitFor(100);
            }

            if (fwdSweeper) {
                sweeper.setPower(.6);
            } else if (bkwdSweeper) {
                sweeper.setPower(-.6);
            } else{
                sweeper.setPower(0);
            }
            idle();
        }
    }
}
