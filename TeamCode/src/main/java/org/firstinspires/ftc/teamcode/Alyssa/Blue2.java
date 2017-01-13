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
package org.firstinspires.ftc.teamcode.Alyssa;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.InvadersVelocityVortexBot;

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

@Autonomous(name="Blue2", group="Pushbot")
//@Disabled
public class Blue2 extends LinearOpMode {

    /* Declare OpMode members. */


    InvadersVelocityVortexBot robot   = new InvadersVelocityVortexBot();   // Use a Pushbot's hardware

    private ElapsedTime     runtime = new ElapsedTime();

    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;

    @Override
    public void runOpMode() throws InterruptedException {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(this);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        robot.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();

        robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d",
                robot.leftMotor.getCurrentPosition(),
                robot.rightMotor.getCurrentPosition());
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

//        // Step through each leg of the path,
//        // Note: Reverse movement is obtained by setting a negative distance (not speed)
//        encoderDrive(DRIVE_SPEED,  48,  48, 10.0);  // Drive to Cap Ball
//        encoderDrive(DRIVE_SPEED, -12, -12, 10); //Back away to avoid plywood
//        encoderDrive(TURN_SPEED,   30, -30, 4.0);  // S2: Turn Right 12 Inches with 4 Sec timeout
//        encoderDrive(DRIVE_SPEED, -60, -60, 4.0);  // S3: Reverse 24 Inches with 4 Sec timeout
//
//        //robot.leftClaw.setPosition(1.0);            // S4: Stop and close the claw.
//        //robot.rightClaw.setPosition(0.0);
//        sleep(1000);     // pause for servos to move
//


        /* @todo: Alyssa now that you have our ten steps clearly defined, its time to start calling
           the robot functions required to make the robot do the things we want it to.  Matthew
           added some functions to InvadersVelocityVortexBot.java that you can use to do
           gyro turns and driving using the ultrasonic sensor.  You can access them by typing:
               robot.GyroTurn(...);
               robot.DistanceDrive(...);
           You can read the color sensor value by typing
                robot.color1.red();
                robot.color1.blue();
           Of course, our robot should have two color sensors (one for the beacon and one for the
           line, so hopefully someone changes color1's name to something more descriptive, so we
           aren't accidentally trying to read the line color sensor when trying to decide which
           beacon to press.
        */

        //
        // 0.  Use our shoot function to shoot the particles into the center vortex.
        // 1.drive forward 4 inches so we can turn
        // 2. turn -40 degrees
        // 3.  Drive forward using the encoders (or timing)
        // 4.  Use our gyro turn function to turn 40 degrees.
        // 5. ajust to get near the beacons
        // 6.   Make a function that drives us forward until the line sensor sees the white tape.
        // 7. Drive forward abut 2 inches.
        // 8.  Find out what color the beacon is. If it's blue, push the button. If it's red, drive backwards and press the other button.
        //      a.extend beacon pusher to about 80 ish
        //      b. read color
        //      c. if its blue, press the button.
        //      d. if its red, drive 14 cm forward
        // 9.   Do the same for the next beacon.
        // 10.  Drive backwards onto the corner vortex.

        

// Willow and James tested this color sensing on the beacon successfully
//     //  Color sensor.
//     robot.beaconSensor.enableLed(false);
//     int red = robot.beaconSensor.red();
//     int blue = robot.beaconSensor.blue();
//
//     while (robot.beaconSensor.blue() < 5){
//         robot.leftMotor.setPower(1);
//         robot.rightMotor.setPower(1);
//     }
//     robot.leftMotor.setPower(0);
//     robot.rightMotor.setPower(0);




        //step 0: shoot the particles into the center vortex
        robot.ohshoot();


      // step 1:drive forward 4 inches so we can actually do things
        robot.DistanceDrive(10,DistanceUnit.CM,1);

      // step 2: turn -40 degrees
      robot.GyroTurn(1,-40);


     //step 3: drive forward for god knows how long (about 5 ft right now) ;)
        robot.DistanceDrive(154,DistanceUnit.CM,1);

       // step 4:turn 40 degrees to align with the wall.
       robot.GyroTurn(1,40);


// Step 5: drive backwards a bit and adjust
        robot.DistanceDrive(-20,DistanceUnit.CM,1);


        // Step 6: Drive to the white tape on the floor
        robot.WaitForReflectedLight(6,true,3500);



        // step 7: drive 2.5 centimeters or two inches
        robot.DistanceDrive(5,DistanceUnit.CM,1);


        // Step 8: Push Beacon if Blue
        if(robot.doIseeBlue()) {
            // Do Step 8.c
        } else { robot.DistanceDrive(14,DistanceUnit.CM,1);
            // Do Step 8.d
        }

        // Going to next beacon.

        // Step 9 (repeating step 6) : Drive to the white tape on the floor
        robot.WaitForReflectedLight(6,true,3500);



        // step 10 (repeating step 7) : drive 2.5 centimeters or two inches
        robot.DistanceDrive(5,DistanceUnit.CM,1);


        // Step 11 (repeating step 8) : Push Beacon if Blue
        if(robot.doIseeBlue()) {
            // Do Step 8.c
        } else {
            // Do Step 8.d
        }






        telemetry.addData("Path", "Complete");
        telemetry.update();





    }




    /*
     *  Method to perfmorm a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) throws InterruptedException {
        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = robot.leftMotor.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = robot.rightMotor.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            robot.leftMotor.setTargetPosition(newLeftTarget);
            robot.rightMotor.setTargetPosition(newRightTarget);
            // reset the timeout time and start motion.
            runtime.reset();
            robot.leftMotor.setPower(Math.abs(speed));
            robot.rightMotor.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.leftMotor.isBusy() && robot.rightMotor.isBusy())) {

                // Turn On RUN_TO_POSITION
                robot.leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);


                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        robot.leftMotor.getCurrentPosition(),
                        robot.rightMotor.getCurrentPosition());
                telemetry.update();


                // Allow time for other processes to run.
                idle();
            }

            // Stop all motion;
            robot.leftMotor.setPower(0);
            robot.rightMotor.setPower(0);
            // Turn off RUN_TO_POSITION
            robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }
}
