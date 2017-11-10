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
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 ☺ Hi! This is the perfect teleop code for December 16, 2017! ☺
 */
@Autonomous(name = "Arleth Auto Drive", group = "Concept")
//@Disabled
public class ArlethAutonomousDrive extends LinearOpMode {

    /* this says use ArmHardwareClass */
HolonomicHardwareClass robot = new HolonomicHardwareClass();

    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        /* The init() method of the hardware class does all the work here*/
        robot.init(hardwareMap);


        // Wait for the start button
        telemetry.addLine("!☻ Ready to Run ☻!");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {

            goForwardTime(3);

            }
        }


    /***********************************************************************************************
     * These are all of the methods used in the Teleop*
     ***********************************************************************************************/


    /* This function sets power to the wheels based on values given*/
    public void goForwardTime (double time) {
        /*These values are used for the drive*/
        double frontLeft;
        double frontRight;
        double backLeft;
        double backRight;

        frontLeft = 1;
        frontRight = 1;
        backLeft = 1;
        backRight = 1;

        runtime.reset();

        if (robot.FrontLeftPower != frontLeft) {
            robot.frontLeftMotor.setPower(-1);
            robot.FrontLeftPower = frontLeft;
        }
        if (robot.FrontRightPower != frontRight) {
            robot.frontRightMotor.setPower(1);
            robot.FrontRightPower = frontRight;
        }
        if (robot.BackLeftPower != backLeft) {
            robot.backLeftMotor.setPower(-1);
            robot.BackLeftPower = backLeft;
        }
        if (robot.BackRightPower != backRight)
            robot.backRightMotor.setPower(1);
            robot.BackRightPower = backRight;

 /* Using the light value given, run the robot using given parameters until light value is found*/
        while (opModeIsActive() && runtime.seconds() < time) {

        }
        wheelsOff();
        }


    /* This method simply sets all motor to zero power*/
    public void wheelsOff() {
        robot.frontLeftMotor.setPower(0);
        robot.frontRightMotor.setPower(0);
        robot.backLeftMotor.setPower(0);
        robot.backRightMotor.setPower(0);
    }

}


