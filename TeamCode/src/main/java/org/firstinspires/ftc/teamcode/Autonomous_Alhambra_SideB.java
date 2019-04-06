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
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import static com.qualcomm.robotcore.hardware.DistanceSensor.distanceOutOfRange;

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

@TeleOp(name = "Alhambra (Side B)", group = "Linear Opmode")
//@Disabled
public class Autonomous_Alhambra_SideB extends Autonomous_Alhambra {

    @Override
    public void runOpMode() {
        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        resetMotors();

        while (!robot.imu.isGyroCalibrated()) {
            idle();
        }

        Telemetry.Item headingItem = telemetry.addData("Heading: ", "%.4f", robot.getHeading());

        robot.beep();
        runtime.reset();

        // Wait for the game to start (driver presses PLAY)
        while (!isStarted()) {
            headingItem.setValue("%.4f", robot.getHeading());
            telemetry.update();
        }

        if (opModeIsActive()) {
            runtime.reset();

            //move arm up
            moveArm(0.988d, true);
            turnAndDrive(50d, 0d);
            turnAndDrive(50d, 270d);
            turnAndDrive(25d, 180d);
            turnAndDrive(15d, 90d);
            turnAndDrive(25d, 180d);
            turnAndDrive(35d, 270d);
            turnAndDrive(50d, 0d);
            turnAndDrive(35d, 270d);
            turnAndDrive(45d, 180d);
            turnAndDrive(20d, 270d);

            //scooping stuff
            //move arm servo
            robot.armServo.setPosition(0.7578d);
            //move hand
            robot.handServo.setPosition(0.1d);
            //move arm down
            moveArm(2.329d);

            turnAndDrive(0d, 0d);
            sleep(160L);

            //open door
            robot.doorServo.setPosition(1d);
            sleep(100L);

            turnAndDrive(35d, 0d, false);

            //close door
            robot.doorServo.setPosition(0.3d);
            sleep(500L);

            //move arm up and servo together
            moveArm(0.988d, true);

            turnAndDrive(-25d, 0d);
            turnAndDrive(-25d, 270d);
            turnAndDrive(-50d, 180d);
            turnAndDrive(-37.5d, 2700d);
            turnAndDrive(-50d, 0d);
            turnAndDrive(-35d, 270d);
            turnAndDrive(-25d, 180d);
            turnAndDrive(-15d, 90d);
            turnAndDrive(-25d, 180d);
            turnAndDrive(-40d, 270d);
            turnAndDrive(-50d, 0d);

            robot.beep();
        }

        resetMotors(DcMotor.ZeroPowerBehavior.FLOAT);

        while (opModeIsActive()) {
            DriveControl();

            ArmControl();
        }
    }
}
