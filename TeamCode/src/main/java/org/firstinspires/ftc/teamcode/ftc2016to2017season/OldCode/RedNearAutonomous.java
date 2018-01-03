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
package org.firstinspires.ftc.teamcode.ftc2016to2017season.OldCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.ftc2016to2017season.Main.beta.AutonomousGeneral;

/**
 * This file illustrates the concept of driving a path based on encoder counts.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByTime;
 *
 *  This code ALSO requires that the drive Motors have been configured such that a positive
 *  power command moves them forwards, and causes the encoders to count UP.
 *
 *   The desired path in this example is:
 *   - Drive forward for 48 inches
 *   - Spin right for 12 Inches
 *   - Drive Backwards for 24 inches
 *   - Stop and close the claw.
 *
 *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 *  that performs the actual movement.
 *  This methods assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform encoder based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="Pushbot: NewAutonomousRedNear", group="Pushbot")
@Disabled

public class RedNearAutonomous extends AutonomousGeneral {


    private ElapsedTime     runtime = new ElapsedTime();

    static  int             INITIAL_SHOOTERPOS;

    @Override
    public void runOpMode() {

        initiate();

        INITIAL_SHOOTERPOS = shooting_motor.getCurrentPosition();

        telemetry.addData("Inital Shooter Position", INITIAL_SHOOTERPOS);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        // Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)

        //drive forward a general distance
        encoderDrive(DRIVE_SPEED,  45.72,  45.72, 5.0);  // S1: Forward 47 Inches with 5 Sec timeout
        sleep(1000);     // pause for servos to move


        //shoot three times
        encoderShoot(0.8);
        sleep(500);     // pause for servos to move
        intakeDrive(0.8, 900);
        encoderShoot(0.8);
        sleep(500);     // pause for servos to move
        intakeDrive(0.8, 1100);
        encoderShoot(0.8);
        sleep(500);     // pause for servos to move


        encoderDrive(DRIVE_SPEED, 48, 48, 5.0);
        sleep(1000);

        //turn to face wall
        gyro.calibrate();
        while(gyro.isCalibrating()){

        }
        turnLeft(0.4);
        while(gyro.getHeading() > 310 || gyro.getHeading() < 10){ //turn left until the angle becomes as small as you want it
            //gyro.getHeading() returns values from 0 to 359

        }
        stopMotors();

        //drive forward to wall
        while(rangeSensor.getDistance(DistanceUnit.CM) > 15){//distance is the desired distance from the wall
            front_left_motor.setPower(0.5);
            back_left_motor.setPower(0.5);
            front_right_motor.setPower(0.5);
            back_right_motor.setPower(0.5);
        }
        stopMotors();



        //turn left so that the beacon pusher faces the wall
        gyro.calibrate();
        while(gyro.isCalibrating()){

        }

        turnLeft(0.5);
        while(gyro.getHeading() > 270 || gyro.getHeading() < 10 ){

        }
        stopMotors();

        //drive





        telemetry.addData("Path", "Complete");
        telemetry.update();

    }





    /**public void resetShooter(double speed){
        shooting_motor.setTargetPosition(INITIAL_SHOOTERPOS);
        shooting_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        shooting_motor.setPower(Math.abs(speed));

        while(shooting_motor.isBusy()){

        }
        shooting_motor.setPower(0);

        shooting_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


    }*/
}
