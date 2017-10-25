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
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;

import static java.lang.Math.abs;

/**
 * This file illustrates the concept of driving a path based on encoder counts.
 * It uses the common Pushbot hardware class to define the drive on the gromit.
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

@Autonomous(name="Nathan", group="Pushbot")
//@Disabled
public class Mecanum_auto extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareGromit gromit      = new HardwareGromit(); // use the class for variables and setup
    private ElapsedTime     runtime = new ElapsedTime();

//    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
//    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
//    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
//    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
//                                                      (WHEEL_DIAMETER_INCHES * 3.1415);
//    static final double     DRIVE_SPEED             = 0.6;
//    static final double     TURN_SPEED              = 0.5;

    @Override
    public void runOpMode() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        gromit.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
//        telemetry.addData("Status", "Resetting Encoders");    //
//        telemetry.update();

//        gromit.left_front.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        gromit.left_back.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        gromit.right_front.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        gromit.right_back.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
//        telemetry.addData("Path0",  "Starting at %7d :%7d",
//                          gromit.leftDrive.getCurrentPosition(),
//                          gromit.rightDrive.getCurrentPosition());
//        telemetry.update();

        /** Wait for the game to start (driver presses PLAY)**/
        waitForStart();


    sleep(1000);

        telemetry.addData("Path", "Complete");
        telemetry.update();

        mecanumDrive(1,25,34);
        sleep(2000);
        stop_drivetrain();
        mecanumDrive(-1,45,34);
        sleep(2000);
        stop_drivetrain();


    }

    /*
     *  Method to perfmorm a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    public void mecanumDrive(double speed, double direction,double distance){
        if(abs(speed)> 1) speed = speed/abs(speed);


        double max;
        double multiplier;
        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            double lfpower = Math.cos(Math.toRadians(direction+45));
            double lrpower = Math.sin(Math.toRadians(direction+45));
            double rfpower = Math.sin(Math.toRadians(direction+45));
            double rrpower = Math.cos(Math.toRadians(direction+45));

            //Determine largest power being applied
            max = abs(lfpower);
            if (abs(lrpower) > max) max = abs(lrpower);
            if (abs(rfpower) > max) max = abs(rfpower);
            if (abs(rrpower) > max) max = abs(rrpower);

            multiplier = speed/max; //multiplier to adjust speeds of each wheel so you can have a max power of 1 on atleast 1 wheel

            lfpower = multiplier*lfpower;
            lrpower = multiplier*lrpower;
            rfpower = multiplier*rfpower;
            rrpower = multiplier*rrpower;

            gromit.left_front.setPower(lfpower);
            gromit.left_back.setPower(lrpower);
            gromit.right_front.setPower(rfpower);
            gromit.right_back.setPower(rrpower);

            //  sleep(250);   // optional pause after each move
        }
    }
     /**
     *  Method to Move in a Given direction at a certain Orientation or heading
     *
     */
    public void mecanumDriveGyro(double speed,double direction, double orientation, double distance)
    {
        if(abs(speed)> 1) speed = speed/abs(speed);                                                 //Correct Speed if outside of Range

        /**
        * VARIABLES FOR THIS METHOD
        */
        double max;
        double multiplier;
        double turn_correction;
        double correction_speed =0;
        double lfpower;
        double lrpower;
        double rfpower;
        double rrpower;

        //Figure out better way to determine which way to turn


        /**
        * Drive Loop of this method
        */
        if(opModeIsActive()) {                                                                     // Ensure that the opmode is still active
             /**
             * READ IMU
             */
            turn_correction = orientation /**- IMU reading*/;

            if (turn_correction <= -180) turn_correction +=360;   // correction should be +/- 180 (to the left negative, right positive)
            if (turn_correction >=  180) turn_correction -=360;

            correction_speed = Math.signum(turn_correction) * gromit.drive_COEF * turn_correction / 180;





            lfpower = Math.sin(Math.toRadians(direction+45)); //+ correction_speed;
            lrpower = Math.cos(Math.toRadians(direction+45)); //+ correction_speed;
            rfpower = Math.cos(Math.toRadians(direction+45)); //- correction_speed;
            rrpower = Math.sin(Math.toRadians(direction+45)); //- correction_speed;

            //Determine largest power being applied
            max = lfpower;
            if (lrpower > max) max = lrpower;
            if (rfpower > max) max = rfpower;
            if (rrpower > max) max = rrpower;

            multiplier = speed/max;                                                                 //multiplier to adjust speeds of each wheel so you can have a max power of 1 on atleast 1 wheel

            lfpower = multiplier*lfpower;
            lrpower = multiplier*lrpower;
            rfpower = multiplier*rfpower;
            rrpower = multiplier*rrpower;

            gromit.left_front.setPower(lfpower);
            gromit.left_back.setPower(lrpower);
            gromit.right_front.setPower(rfpower);
            gromit.right_back.setPower(rrpower);

            //  sleep(250);   // optional pause after each move
        }
    }
    public void mecanumTurn( double speed,
                             double target_heading,
                            String type) {
        if(speed > 1) speed = 1.0;
        else if(speed <= 0) speed = 0.1;
        /**READ IMU

         */
        double correction = target_heading /**- IMU reading*/;
        if (correction <= -180) correction +=360;   // correction should be +/- 180 (to the left negative, right positive)
        if (correction >=  180) correction -=360;

        //
            while(abs(correction) >= gromit.turn_THRESHOLD &&  opModeIsActive()) {
                /**READ IMU*/

                correction = target_heading; /** - CURRENT HEADING*/
                if(abs(correction) >= gromit.turn_THRESHOLD) break;

                if (correction <= -180) correction +=360;   // correction should be +/- 180 (to the left negative, right positive)
                if (correction >=  180) correction -=360;
                /**^^^^^^^^^^^MAYBE WE ONLY NEED TO DO THIS ONCE?????*/

                double adjustment = Range.clip((Math.signum(correction) * gromit.turn_MIN_SPEED + gromit.turn_COEF * correction / 180), -1, 1);  // adjustment is motor power: sign of correction *0.07 (base power)  + a proportional bit

                gromit.left_front.setPower(adjustment * speed);
                gromit.left_back.setPower(adjustment * speed);
                gromit.right_front.setPower(-(adjustment * speed));
                gromit.right_back.setPower(-(adjustment * speed));
            }
            stop_drivetrain();
    }

    /**
    * STOP ALL DRIVETRAIN MOTORS
    */
    public void stop_drivetrain(){
        gromit.left_front.setPower(0.0);
        gromit.left_back.setPower(0.0);
        gromit.right_front.setPower(0.0);
        gromit.right_back.setPower(0.0);
    }
}
