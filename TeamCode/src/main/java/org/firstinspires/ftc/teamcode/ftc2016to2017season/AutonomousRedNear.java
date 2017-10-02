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
package org.firstinspires.ftc.teamcode.ftc2016to2017season;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;
//<<<<<<< HEAD
//<<<<<<< HEAD

import org.firstinspires.ftc.teamcode.ftc2016to2017season.Main.beta.AutonomousGeneral;

//=======
/*>>>>>>> dfbedde30115dd7e00d6e2695e8f485c459c3180
=======*/
//>>>>>>> 55f5f2acfb873f3f0a66e8649853b5de3df60115

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

@Autonomous(name="Pushbot: AutonomousRedNear", group="Pushbot")
@Disabled
public class AutonomousRedNear extends AutonomousGeneral {


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
        encoderDrive(DRIVE_SPEED,  62.23,  62.23, 5.0);  // S1: Forward 47 Inches with 5 Sec timeout
      //  encoderDrive(DRIVE_SPEED,  -6,  6, 5.0);
         // S1: Forward 47 Inches with 5 Sec timeout
        //encoderDrive(TURN_SPEED,   9, -9, 4.0);  // S2: Turn Right 12 Inches with 4 Sec timeout

        //encoderDrive(DRIVE_SPEED,  6,  -6, 5.0);
        sleep(1000);     // pause for servos to move

        // intakeDrive(0.8, 900);

        shootingDrive(0.8, 850);

        sleep(500);     // pause for servos to move
        intakeDrive(0.8, 1800);

        sleep(500);
        shootingDrive(0.8, 850);
        sleep(500);     // pause for servos to move
        intakeDrive(0.8, 1800);
        sleep(500);

        shootingDrive(0.8, 850);
        sleep(500);

        if (operation_beacon_press == false)
         {
            // pause for servos to move
            encoderDrive(DRIVE_SPEED, 6, -6, 5.0);
            // encoderDrive(DRIVE_SPEED, 15,-15 , 5.0);
            //  encoderDrive(DRIVE_SPEED, -25,-25 , 5.0);
            encoderDrive(DRIVE_SPEED, 285, 99, 5.0);
            encoderDrive(DRIVE_SPEED, 10, 10, 5.0);
            encoderDrive(DRIVE_SPEED, -10, -10, 5.0);
            encoderDrive(DRIVE_SPEED, 15, 15, 5.0);
        /*while(gyro.getHeading() < 40 || gyro.getHeading() >350 ){
            turnRight(0.5);
        }*/
            ////encoderDrive(DRIVE_SPEED,  -49,  -35, 5.0);  // S1: Forward 47 Inches with 5 Sec timeout
            telemetry.addData("Path", "Complete");
            telemetry.update();
            //encoderDrive(DRIVE_SPEED, -16, -40, 5.0);  // S3: Reverse 24 Inches with 4 Sec timeout
        }
        else{

            encoderDrive(0.6, 55, 55, 10);

            //turn to face wall
            sleep(500);
            gyro.calibrate();
            while(gyro.isCalibrating()){

            }
            turnLeft(0.05);
            while((gyro.getHeading() > 235) ||(gyro.getHeading() < 10)){ //turn left until the angle becomes as small as you want it
                //gyro.getHeading() returns values from 0 to 359
                telemetry.addData("current gyro pos", gyro.getHeading());
                telemetry.update();
            }
            stopMotors();

            //drive forward to wall
//            while(rangeSensor.getDistance(DistanceUnit.CM) > 15){//distance is the desired distance from the wall
//                front_left_motor.setPower(0.5);
//                back_left_motor.setPower(0.5);
//                front_right_motor.setPower(0.5);
//                back_right_motor.setPower(0.5);
//            }
//            stopMotors();



            //turn left so that the beacon pusher faces the wall

           // encoderDrive(0.6, 30, 30, 5);

            //newBeacon("red", 15);

//            while(rangeSensor.getDistance(DistanceUnit.CM) > 16) {
//                front_left_motor.setPower(-0.5);
//                back_left_motor.setPower(-0.5);
//                front_right_motor.setPower(-0.35);
//                back_right_motor.setPower(-0.35);
//            } stopMotors();
//
//            while(colorSensor.red() < colorSensor.blue()) {
//                front_left_motor.setPower(-0.3);
//                back_left_motor.setPower(-0.3);
//                front_right_motor.setPower(-0.3);
//                back_right_motor.setPower(-0.3);
//            }
//
//            beaconPress.setPosition(0.2);
//
//            sleep(5000);
//
//            beaconPress.setPosition(0.8);
//
//            sleep(1000);
//
//            while(rangeSensor.getDistance(DistanceUnit.CM ) > 13){
//                front_left_motor.setPower(-0.35);
//                back_left_motor.setPower(-0.35);
//                front_right_motor.setPower(-0.5);
//                back_right_motor.setPower(-0.5);
//            } stopMotors();


//            gyro.calibrate();
//            while(gyro.isCalibrating()){
//
//            }
//
//            turnLeft(0.5);
//            while(gyro.getHeading() > 270 || gyro.getHeading() < 10 ){
//
//            }
            stopMotors();




            //drive

            telemetry.addData("Path", "Complete");
            telemetry.update();


        }

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
