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
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


/**
 * Hey! This code runs mecanum drive!
 */

@Autonomous(name = "!Test Autonomous", group = "BACONbot")
//@Disabled
public class TestAutonomous extends LinearOpMode {

    /* This says to use the holnomic hardware class */
    HolonomicHardwareClass robot = new HolonomicHardwareClass();

    /* Allow This Teleop to run! */
    @Override
    public void runOpMode() throws InterruptedException {

        /* The init() method of the hardware class does all the work here*/
        robot.init(hardwareMap);

        /* Send telemetry message to signify that the robot's ready to play! */
        telemetry.addLine("♥ ♪ Ready to Run ♪ ♥");
        telemetry.update();

        /* Wait for the game to start (driver presses PLAY)*/
        waitForStart();

        while (opModeIsActive()) {

/*
double r = Math.hypot(-1, gamepad1.left_stick_y);
r = r / 2; //Don't let rotation dominate movement
double robotAngle = Math.atan2(gamepad1.left_stick_y, -1) - Math.PI / 4;
double rightX = gamepad1.right_stick_x;
*/


            double rot;
            rot = 1;

            if (rot <= 1 && rot >= -1) {
                setWheelPower(-rot, rot, rot, -rot, 5);
                setWheelPower(0, 0, 0, 0, 0);
            }
        }
    }

    /***********************************************************************************************
     * These are all of the methods used in the Teleop*
     ***********************************************************************************************/

    /* This function sets power to the wheels based on values given*/
    public void setWheelPower(double v1, double v2, double v3, double v4, double length) {
        /*These values are used for the drive*/
        double frontLeft;
        double frontRight;
        double backLeft;
        double backRight;
        length=length*1000;
        int temp=(int)(length);
        frontLeft = v1;
        frontRight = v2;
        backLeft = v3;
        backRight = v4;
        if (robot.FrontLeftPower != frontLeft) {
            robot.frontLeftMotor.setPower(v1);
            robot.FrontLeftPower = frontLeft;
        }
        if (robot.FrontRightPower != frontRight) {
            robot.frontRightMotor.setPower(v2);
            robot.FrontRightPower = frontRight;
        }
        if (robot.BackLeftPower != backLeft) {
            robot.backLeftMotor.setPower(v3);
            robot.BackLeftPower = backLeft;
        }
        if (robot.BackRightPower != backRight) {
            robot.backRightMotor.setPower(v4);
            robot.BackRightPower = backRight;
        }

        sleep(temp);
    }
}




