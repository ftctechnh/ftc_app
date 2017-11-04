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
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all iterative OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="Test Autonomous", group="Autonomous")
public class TestAutonomous extends OpMode {
    final static double PULSES_PER_INCH = (280 / (4 * Math.PI));
    private ElapsedTime runtime = new ElapsedTime();
    Hardware750 robot = new Hardware750();

    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void init_loop() {
    }


    @Override
    public void start() {
        runtime.reset();
    }


    @Override
    public void loop() {
        for (int i = 0; i < 1; i++) {
            encode(48, .25);
        }
    }

    public void encode(double distance, double speed) {
        int targetFL;
        int targetFR;
        int targetRL;
        int targetRR;

        robot.flDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.frDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rlDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rrDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        targetFL = robot.flDrive.getCurrentPosition() + (int)(distance * PULSES_PER_INCH);
        targetFR = robot.frDrive.getCurrentPosition() + (int)(distance * PULSES_PER_INCH);
        targetRL = robot.rlDrive.getCurrentPosition() + (int)(distance * PULSES_PER_INCH);
        targetRR = robot.rrDrive.getCurrentPosition() + (int)(distance * PULSES_PER_INCH);

        telemetry.addData("Current fl: ", robot.flDrive.getCurrentPosition());
        telemetry.addData("Current fr: ", robot.frDrive.getCurrentPosition());
        telemetry.addData("Current rl: ", robot.rlDrive.getCurrentPosition());
        telemetry.addData("Current rr: ", robot.rrDrive.getCurrentPosition());
        telemetry.addData("fl: ", targetFL);
        telemetry.addData("fr: ", targetFR);
        telemetry.addData("rl: ", targetRL);
        telemetry.addData("rr: ", targetRR);

        robot.flDrive.setTargetPosition(targetFL);
        robot.frDrive.setTargetPosition(targetFR);
        robot.rlDrive.setTargetPosition(targetRL);
        robot.rrDrive.setTargetPosition(targetRR);

        runtime.reset();
        robot.flDrive.setPower(Math.abs(speed));
        robot.frDrive.setPower(Math.abs(speed));
        robot.rlDrive.setPower(Math.abs(speed));
        robot.rrDrive.setPower(Math.abs(speed));


    }

    @Override
    public void stop() {
    }

}
