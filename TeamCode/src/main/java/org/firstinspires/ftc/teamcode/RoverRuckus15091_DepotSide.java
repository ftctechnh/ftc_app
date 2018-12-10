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

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Locale;

/**
 * This file illustrates the concept of driving a path based on encoder counts.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 * <p>
 * The code REQUIRES that you DO have encoders on the wheels,
 * otherwise you would use: PushbotAutoDriveByTime;
 * <p>
 * This code ALSO requires that the drive Motors have been configured such that a positive
 * power command moves them forwards, and causes the encoders to count UP.
 * <p>
 * The desired path in this example is:
 * - Drive forward for 48 inches
 * - Spin right for 12 Inches
 * - Drive Backwards for 24 inches
 * - Stop and close the claw.
 * <p>
 * The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 * that performs the actual movement.
 * This methods assumes that each movement is relative to the last stopping place.
 * There are other ways to perform encoder based moves, but this method is probably the simplest.
 * This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 * <p>
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name = "Rover Ruckus", group = "Pushbot")
//@Disabled
public class RoverRuckus15091_DepotSide extends RoverRuckus15091 {

    @Override
    public void runOpMode() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        initDetector(480d, 120d);
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        robot.leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot.leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0", "Starting at %7d :%7d",
                robot.leftDrive.getCurrentPosition(),
                robot.rightDrive.getCurrentPosition());
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        while (!isStarted()) {
            telemetry.addData(">", "Robot Heading = %.4f", robot.getHeading());
            telemetry.update();
        }

        if (opModeIsActive()) {
            // The robot will be hung on the handle to start
            // so, first thing to do is unlatch
            landing();

            // turn robot 180 degree to the right
            //encoderDrive(TURN_SPEED, -40d, 40d, 5d);
            gyroTurn(0.75d, -111d);

            //scan gold mineral for position 1,2,3
            int goldMineralLocation = -1;
            double targetHeading = robot.getHeading();
            for (int i = 0; i < 20; i++) {
                if (!detector.getAligned()) {
                    targetHeading -= 6d;
                    gyroTurn(0.9d, targetHeading);
                } else {
                    if (i < 6) {
                        goldMineralLocation = 1;
                        break;
                    } else if (i < 13) {
                        goldMineralLocation = 2;
                        break;
                    } else {
                        goldMineralLocation = 3;
                        break;
                    }
                }
            }

            double goldMineralDistance = robot.sensorDistance.getDistance(DistanceUnit.INCH);
            robot.tts.speak("Gold on position " + goldMineralLocation);

            //base on gold mineral position, continue path for 1,2,3
            switch (goldMineralLocation) {
                case 1:
                    gyroDrive(DRIVE_SPEED, -28d, targetHeading);
                    gyroDrive(DRIVE_SPEED, -13d, 180d);
                    gyroTurn(TURN_SPEED, 145d);
                    gyroDrive(DRIVE_SPEED, -31d, 145d);
                    break;
                case 2:
                    gyroDrive(DRIVE_SPEED, -10d, targetHeading);
                    gyroDrive(DRIVE_SPEED, -37d, 180d);
                    gyroTurn(TURN_SPEED, 155d);
                    break;
                case 3:
                    gyroDrive(DRIVE_SPEED, -21d, targetHeading);
                    gyroDrive(DRIVE_SPEED, -44d, -150d);
                    gyroTurn(TURN_SPEED, 160d);
                    break;
            }

            robot.markerServo.setPosition(0d);
            sleep(400L);
            detector.disable();

            /*gyroTurn(TURN_SPEED, 90d);
            gyroDrive(DRIVE_SPEED, -4, 90d);
            gyroTurn(TURN_SPEED, 50d);
            gyroDrive(DRIVE_SPEED, -24d, 50d);
            gyroDrive(DRIVE_SPEED, -24d, 45d);*/

            telemetry.addData("Path", "Complete");
            telemetry.update();
        }
    }
}
