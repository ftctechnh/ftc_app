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

    /* Create a "timer" that begins once the OpMode begins */
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        /* The init() method of the hardware class does all the work here*/
        robot.init(hardwareMap);

        // Wait for the start button
        telemetry.addLine("!☻ Ready to Autonomous ☻!");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {

           // goForwardTime(1, .5);

//            goBackwardTime(1, .5);

            goLeftTime(1, .5);

  //          goRightTime(1, .5);

        }
    }

    /***********************************************************************************************
     * These are all of the methods used in the Autonomous *
     ***********************************************************************************************/

/* This method moves the robot forward for time and power indicated*/
    public void goForwardTime (double time, double power) {
    /* reset the "timer" to 0 */
        runtime.reset();
    /* This runs the wheel power so it moves forward, the powers for the left wheels
    are inversed so that it runs properly on the robot
     */
        setWheelPower(-power, power, -power, power);
    /* If the timer hasn't reached the time that is indicated do nothing and keep the wheels powered */
        while (opModeIsActive() && runtime.seconds() < time) {

        }
    /* Once the while loop above finishes turn off the wheels */
        wheelsOff();
    }


    /* This method moves the robot backward for time and power indicated*/
    public void goBackwardTime (double time, double power) {

    /* reset the "timer" to 0 */
        runtime.reset();
    /* This runs the wheel power so it moves forward, the powers for the left wheels
    are inversed so that it runs properly on the robot
     */
        setWheelPower(power, power, -power, -power);
    /* If the timer hasn't reached the time that is indicated do nothing and keep the wheels powered */
        while (opModeIsActive() && runtime.seconds() < time) {

        }
    /* Once the while loop above finishes turn off the wheels */
        wheelsOff();
    }


    /* This method moves the robot right for time and power indicated*/
    public void goRightTime (double time, double power) {
    /* reset the "timer" to 0 */
        runtime.reset();
    /* This runs the wheel power so it moves forward, the powers for the left wheels
    are inversed so that it runs properly on the robot
     */
        setWheelPower(-power, -power, power, power);
    /* If the timer hasn't reached the time that is indicated do nothing and keep the wheels powered */
        while (opModeIsActive() && runtime.seconds() < time) {

        }
    /* Once the while loop above finishes turn off the wheels */
        wheelsOff();
    }


    /* This method moves the robot left for time and power indicated*/
    public void goLeftTime (double time, double power) {
    /* reset the "timer" to 0 */
        runtime.reset();
    /* This runs the wheel power so it moves forward, the powers for the left wheels
    are inversed so that it runs properly on the robot
     */
        setWheelPower(power, -power, -power, power);
    /* If the timer hasn't reached the time that is indicated do nothing and keep the wheels powered */
        while (opModeIsActive() && runtime.seconds() < time) {

        }
    /* Once the while loop above finishes turn off the wheels */
        wheelsOff();
    }


    /* This method simply sets all motor to zero power*/
    public void wheelsOff() {
        setWheelPower(0,0,0,0);
    }

    /* This method powers each wheel to whichever power is desired */
    public void setWheelPower(double fl, double fr, double bl, double br) {

        /* Create power variables */
        double frontLeft;
        double frontRight;
        double backLeft;
        double backRight;

        /* Initialize the powers with the values input whenever this method is called */
        frontLeft   =   fl;
        frontRight  =   fr;
        backLeft    =   bl;
        backRight   =   br;

        /* set each wheel to the power indicated whenever this method is called */
        if (robot.FrontLeftPower != frontLeft) {
            robot.frontLeftMotor.setPower(fl);
            robot.FrontLeftPower = frontLeft;
        }
        if (robot.FrontRightPower != frontRight) {
            robot.frontRightMotor.setPower(fr);
            robot.FrontRightPower = frontRight;
        }
        if (robot.BackLeftPower != backLeft) {
            robot.backLeftMotor.setPower(bl);
            robot.BackLeftPower = backLeft;
        }
        if (robot.BackRightPower != backRight)
            robot.backRightMotor.setPower(br);
        robot.BackRightPower = backRight;
    }
}
