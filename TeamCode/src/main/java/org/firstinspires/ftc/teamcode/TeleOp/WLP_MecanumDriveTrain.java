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
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * This file provides necessary functionality for We Love Pi Team's 2017 Relic
 * Recovery Robot's robot's Mecanum Wheel Drivetrain
 */


public class WLP_MecanumDriveTrain {

    // Declare Drive Train modors
    protected DcMotor frontLeft = null;
    protected DcMotor frontRight = null;
    protected DcMotor rearLeft = null;
    protected DcMotor rearRight = null;
    protected WLP_MecanumWheels wheels = new WLP_MecanumWheels();

    // Global variables to be initialized in the constructor
    private Telemetry telemetry = null;
    private HardwareMap hardwareMap = null;
    private Gamepad gamepad1 = null;
    private Gamepad gamepad2 = null;

    private boolean isInitialized =false;

    // Constructors

    WLP_MecanumDriveTrain() {
        // Default consturctor
    }

    // Code to run ONCE when the driver hits INIT
    public void init(Telemetry telemetry, HardwareMap hardwareMap,
                     Gamepad gamepad1, Gamepad gamepad2) {

        // First initialize the hardware parameters
        this.telemetry = telemetry;
        this.hardwareMap = hardwareMap;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;


        // Initialize motors.
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        rearLeft = hardwareMap.get(DcMotor.class, "rearLeft");
        rearRight = hardwareMap.get(DcMotor.class, "rearRight");


        // Set Motor Directions
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        rearLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        rearRight.setDirection(DcMotor.Direction.REVERSE);


        // finally
        isInitialized = true;

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Drive Train", "Init complete");

    }

    // During teleop this function runs repeatedly
    public void loop() {

        if (isInitialized == false) {
            telemetry.addData("Drive Train::", " Not initialized");
            return;
        }

        double left_x = gamepad1.left_stick_x;
        double left_y = gamepad1.left_stick_y;
        double right_x = gamepad1.right_stick_x;

        // Update the joystick input to calculate  wheel powers
        wheels.UpdateInput(left_x, left_y, right_x);

        frontLeft.setPower(wheels.getFrontLeftPower());
        frontRight.setPower(wheels.getFrontRightPower());
        rearRight.setPower(wheels.getRearRightPower());
        rearLeft.setPower(wheels.getRearLeftPower());


        telemetry.addData("Drive Train::Left_X", "(%.2f)", left_x);
        telemetry.addData("Drive Train::Left_Y", "(%.2f)", left_y);
        telemetry.addData("Drive Train::Right_X", "(%.2f)", right_x);

        telemetry.addData("Drive Train::FrontLeftPower", "(%.2f)", frontLeft.getPower());
        telemetry.addData("Drive Train::FrontRightPower", "(%.2f)", frontRight.getPower());
        telemetry.addData("Drive Train::RearRightPower", "(%.2f)", rearRight.getPower());
        telemetry.addData("Drive Train::RearLeftPower", "(%.2f)", rearLeft.getPower());

    }
}
