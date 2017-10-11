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
package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


/**
 * Hey! This code is for a holonomic drive configuration!
 */

@TeleOp(name = "Holonomic Drive!", group = "BACONbot")
//@Disabled
public class HolonomicDrive extends LinearOpMode {

    /* This says to use BACONbot hardware */
    HardwareBACONbot robot = new HardwareBACONbot();

    /* Allow This Teleop to run! */
    @Override
    public void runOpMode() throws InterruptedException {

        /* The init() method of the hardware class does all the work here*/
        robot.init(hardwareMap);

        /*These values are used for the drive*/
        double x;
        double y;
        double r;

        /* Send telemetry message to signify that the robot's ready to play! */
        telemetry.addLine("♥ ♪ Ready to Run, btw I was uploaded wirelessly! ♪ ♥");
        telemetry.update();

        /* Wait for the game to start (driver presses PLAY)*/
        waitForStart();

        while (opModeIsActive()) {

            /* This is the code that controls the robot's drive!*/

            /*If the left bumper is being pressed it allows the robot to run at only 1/5 of its full speed*/
            if ((gamepad1.left_bumper)) {
                y = -gamepad1.left_stick_y / 5;
                x = gamepad1.left_stick_x / 5;
                r = gamepad1.right_stick_x / 5;

                /*do not let rotation dominate movement*/
                r = r / 2;

                /*use the setpower function to do all the math using the given x, y, and r values*/
                setWheelPower(x, y, r);
            }
            /*If the left bumper is not being pressed, this allows the robot to run at full speed*/
            else {
                y = -gamepad1.left_stick_y;
                x = gamepad1.left_stick_x;
                r = gamepad1.right_stick_x;

                /*do not let rotation dominate movement*/
                r = r / 2;
                setWheelPower(x, y, r);
            }
        }
    }
            /* This method does all of the math that calculates the power to set on the wheels*/

    public void setWheelPower(double x, double y, double r) {

        double frontLeft;
        double frontRight;
        double backLeft;
        double backRight;
        double max;

        /*calculate the power for each wheel*/
        frontLeft = -y - x - r;
        frontRight = +y - x - r;
        backLeft = -y + x - r;
        backRight = +y + x - r;

        /*Normalize the values so none exceeds +/- 1.0*/
        max = Math.max(Math.max(Math.abs(frontLeft), Math.abs(frontRight)), Math.max(Math.abs(frontRight), Math.abs(frontRight)));
        if (max > 1.0) {
            frontLeft = frontLeft / max;
            frontRight = frontRight / max;
            backLeft = backLeft / max;
            backRight = backRight / max;
        }

        /*Set power on each wheel*/
        if (robot.FrontLeftPower != frontLeft) {
            robot.frontLeftMotor.setPower(frontLeft);
            robot.FrontLeftPower = frontLeft;
        }
        if (robot.FrontRightPower != frontRight) {
            robot.frontRightMotor.setPower(frontRight);
            robot.FrontRightPower = frontRight;
        }
        if (robot.BackLeftPower != backLeft) {
            robot.backLeftMotor.setPower(-backLeft);
            robot.BackLeftPower = backLeft;
        }
        if (robot.BackRightPower != backRight) {
            robot.backRightMotor.setPower(backRight);
            robot.BackRightPower = backRight;
        }
    }

}

