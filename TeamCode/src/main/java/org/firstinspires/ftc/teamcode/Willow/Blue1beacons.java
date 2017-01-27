/*
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
package org.firstinspires.ftc.teamcode.Willow;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.InvadersVelocityVortexBot;

/**
 *
 */

@Autonomous(name="Comp Blue 1 Beacons", group="Pushbot")
//@Disabled
public class Blue1beacons extends LinearOpMode {

    /* Declare OpMode members. */
    InvadersVelocityVortexBot robot   = new InvadersVelocityVortexBot();   // Use our custom hardware

    /// This method assumes the robot is aligned to the white line on the floor in front of the beacon
    private void doRightSideBeaconPushingStuff()
    {
        if(robot.doIseeBlueRight())
        {
            robot.encoderDrive(0.2,1,1,1000);           // Drive forward 1" so the beacon pusher is aligned with the Right beacon button
            robot.beaconRight.setPosition(1);            // Press the Red Beacon to claim it.
            robot.sleepMs(1000);
            robot.beaconRight.setPosition(0);            // Retract the beacon pusher
        }
        else
        {
            robot.encoderDrive(0.2,4,4,1000);           // Drive forward 4" so the beacon pusher is aligned with the right beacon light
            if(robot.doIseeBlueRight()) {
                robot.encoderDrive(0.2, 1, 1, 1000);    // Drive forward 1" so the beacon pusher is aligned with the right beacon button
                robot.beaconRight.setPosition(1);        // Press the Red Beacon to claim it.
                robot.sleepMs(1000);
                robot.beaconRight.setPosition(0);        // Retract the beacon pusher
            }
        }
        robot.sleepMs(1000);                            // Final sleep so beacon pusher can retract
    }

    public void goForTheBeacons()
    {
        robot.encoderDrive(0.5, 30, 30, 10);            // Drive forwards towards center vortex
        robot.simpleGyroTurn(0.3, 35, 5000);           // Complete the rest of the Right turn towards the beacon-wall.
        robot.encoderDrive(0.5, 80, 80, 10);            // Drive forwards about half way to the beacon-wall (and to get past CapBall)
        robot.DriveToWall(3, DistanceUnit.INCH, 0.15, 5000);  // Use the range sensor to get 3" away from the wall.
        robot.AlignToWall(3, DistanceUnit.INCH);
        robot.simpleGyroTurn(0.3, -162, 5000);           // About Face!  Turn front of robot away from beacon wall
        //robot.encoderDrive(0.2,-12,-12,5000);           // Crash our butt into the wall... slowly!... to make sure we're square with the wall
        robot.encoderDrive(0.3, -12, -12, 5000);           // Back up slowly towards the wall
        robot.simpleGyroTurn(0.3, 81, 5000);           // Turn left towards the beacons

        // Find the 1st beacon and try to press the correct button
        robot.DriveToWhiteLine(0.1, 5, true, 5000);        // Slowly creep foward, looking for the white line
        robot.encoderDrive(0.2, -2, -2, 1000);             // Drive back 2" so the color sensor is looking at the left beacon light
        doRightSideBeaconPushingStuff();

        // Find the 2nd beacon and try to press the correct button
        robot.encoderDrive(0.5, 36, 36, 5000);             // Fast drive towards the next beacon line
        robot.DriveToWhiteLine(0.1, 5, true, 5000);        // Slowly creep forward looking for the white line
        doRightSideBeaconPushingStuff();
    }

    /// This should knock the ball off and leave our robot partially on the board - 10pts total
    public void goForThePedestal()
    {
        // Go for knocking the cap ball off the pedestal and parking there
        robot.encoderDrive(0.5, 50, 50, 5);            // Drive forwards towards center vortex
    }


    
    @Override
    public void runOpMode() {

        /*
         * Initialize the standard drive system variables.
         * The init() method of the hardware class does most of the work here
         */
        robot.init(this);

        // Send telemetry message to alert driver that we are calibrating;
        telemetry.addData(">", "Mommy says people my age shouldn't suck their thumbs.");    //
        telemetry.update();
        telemetry.addData(">", "Robot Ready.");    //
        telemetry.update();

        // Wait for the game to start (Display Gyro value), and reset gyro before we move..
        while (!isStarted()) {
            telemetry.addData(">", "Robot Heading = %d", robot.gyro.getIntegratedZValue());
            telemetry.addData(">", "Floor AlphaLv = %d", robot.floorSensor.alpha());
            telemetry.addData(">", "Floor Hue     = %d", robot.floorSensor.argb());
            telemetry.addData("L_Red", "%d", robot.beaconSensorRight.red());
            telemetry.addData("L_Blue", "%d", robot.beaconSensorRight.blue());
            telemetry.addData("L_DoISeeRed", "%s", robot.doIseeRedRight() ? "YES" : "NO");
            telemetry.addData("L_DoISeeBlue", "%s", robot.doIseeBlueRight() ? "YES" : "NO");
            telemetry.addData("R_Red", "%d", robot.beaconSensorRight.red());
            telemetry.addData("R_Blue", "%d", robot.beaconSensorRight.blue());
            telemetry.addData("R_DoISeeRed", "%s", robot.doIseeRedRight() ? "YES" : "NO");
            telemetry.addData("R_DoISeeBlue", "%s", robot.doIseeBlueRight() ? "YES" : "NO");
            telemetry.update();
            idle();
        }

        //All the actual opmode code goes here.
        robot.encoderDrive(0.5, 15, 15, 10);            // Drive forwards towards center vortex
        robot.simpleGyroTurn(0.3,30,3000);             // Slight Left Turn to take shot at Center Vortex

        // Decide whether to attempt the beacon run (if the color sensors are working, then go for it)
        if(robot.beaconSensorRight.red() != 255) {
            // Take the shot right away, we've got to keep moving if we want to finish all of our movements
            robot.ohshoot();                            // SHOOT! - This takes 12 seconds (+30-points)
            goForTheBeacons();                          // Go for the beacons (+60 points)
            // Target Point Count: 90pts
        }
        else {
            robot.sleepMs(10000);                       // Take a break to let our alliance partner do their thing
            robot.ohshoot();                            // SHOOT! - This takes 12 seconds (+30-points)
            goForThePedestal();                         // Knock the CapBall off the pedestal and park (+10 points)
            // Target Point Count: 40pts
        }
    }
}
