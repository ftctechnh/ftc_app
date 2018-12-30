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

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Size;

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


        initDetector(new Size(100d, 480d), 120d);
        this.robot.init(hardwareMap);

        resetMotors();

        while (!robot.imu.isGyroCalibrated()) {
            idle();
        }

        Telemetry.Item headingItem = telemetry.addData("Heading: ", "%.4f", this.robot.getHeading());

        robot.beep();

        // Wait for the game to start (driver presses PLAY)
        while (!isStarted()) {
            headingItem.setValue("%.4f", this.robot.getHeading());
            telemetry.update();
        }

        if (opModeIsActive()) {
            // The robot will be hung on the handle to start
            // so, first thing to do is unlatch
            landing();

            // Then turn and stop at gold mineral
            sampling(-111d);

            //base on gold mineral position, continue path for 1,2,3
            switch (goldMineralLocation) {
                case 1:
                    gyroDrive(DRIVE_SPEED, 29d, targetHeading);
                    gyroDrive(DRIVE_SPEED, 42d, 140d);
                    break;
                case 2:
                    gyroDrive(DRIVE_SPEED, 11d, targetHeading);
                    gyroDrive(DRIVE_SPEED, 37d, 175);
                    break;
                case 3:
                    gyroDrive(DRIVE_SPEED, 27d, targetHeading);
                    gyroDrive(DRIVE_SPEED, 31d, -155d);
                    break;
            }

            this.robot.markerServo.setPosition(1d);
            sleep(500L);
            robot.beep();

            /*gyroTurn(TURN_SPEED, 90d);
            gyroDrive(DRIVE_SPEED, -4, 90d);
            gyroTurn(TURN_SPEED, 50d);
            gyroDrive(DRIVE_SPEED, -24d, 50d);
            gyroDrive(DRIVE_SPEED, -24d, 45d);*/
        }

        detector.disable();
    }
}
