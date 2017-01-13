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
package org.firstinspires.ftc.teamcode.Steven;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.AutonomousGeneral;
import org.firstinspires.ftc.teamcode.AutonomousGeneral_newName;

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

@Autonomous(name="Pushbot: AutonomousRedNearLine", group="Pushbot")
@Disabled

public class AutonomousRedNearColor extends AutonomousGeneral_newName {


    private ElapsedTime runtime = new ElapsedTime();
    OpticalDistanceSensor ODS;
    OpticalDistanceSensor ODS2;
    double baseline1;
    double baseline2;
    static int INITIAL_SHOOTERPOS;

    @Override
    public void runOpMode() {

        initiate();

        ODS = hardwareMap.opticalDistanceSensor.get("ODS");
        ODS2 = hardwareMap.opticalDistanceSensor.get("ODS2");

        baseline1 = ODS.getRawLightDetected();
        baseline2 = ODS2.getRawLightDetected();
        INITIAL_SHOOTERPOS = ballShooterMotor.getCurrentPosition();

        telemetry.addData("Inital Shooter Position", INITIAL_SHOOTERPOS);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        // Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)
        encoderDrive(DRIVE_SPEED, 62.23, 62.23, 5.0);  // S1: Forward 47 Inches with 5 Sec timeout
        sleep(1000);     // pause for servos to move



        encoderShoot(0.8);



        sleep(500);     // pause for servos to move
        intakeDrive(0.8, 1800);
        sleep(500);
        encoderShoot(0.8);
        //shootingDrive(0.8, 850);


        sleep(500);     // pause for servos to move
        intakeDrive(0.8, 1800);
        sleep(500);
        encoderShoot(0.8);
        //shootingDrive(0.8, 850);
        sleep(500);


        gyro.calibrate();
        while(gyro.isCalibrating()){

        }
        //turn horizontal to wall
        while(gyro.getHeading() < 45 && gyro.getHeading() > 350){
            turnLeft(TURN_SPEED);
        }
        stopMotors();


        //drive until white line
        while(!whiteLineDetected()){
            straightDrive(DRIVE_SPEED);
        }
        stopMotors();


        //follow white line







        ////encoderDrive(DRIVE_SPEED,  -49,  -35, 5.0);  // S1: Forward 47 Inches with 5 Sec timeout
        telemetry.addData("Path", "Complete");
        telemetry.update();
        //encoderDrive(DRIVE_SPEED, -16, -40, 5.0);  // S3: Reverse 24 Inches with 4 Sec timeout


    }
    public boolean whiteLineDetected(){
        if ((ODS.getRawLightDetected() > (baseline1*5))||(ODS2.getRawLightDetected()> baseline2*5)){
            return true;
        }
        return false;
    }
}