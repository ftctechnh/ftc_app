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
package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 *
 */

@Autonomous(name="Comp Red 1 Beacons", group="Pushbot")
//@Disabled
public class GyroOpmode extends LinearOpMode {

    /* Declare OpMode members. */
    InvadersVelocityVortexBot robot   = new InvadersVelocityVortexBot();   // Use our custom hardware

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
            telemetry.update();
            idle();
        }

        //All the actual opmode code goes here.
        robot.encoderDrive(0.2, 55, 55, 1.7); //Drive forwards to the plywood base with the capball.
        robot.simpleGyroTurn(0.3, -90, 1500); //Turn left towards the beacons.
        robot.encoderDrive(0.2, 55, 55, 2.0); //Drive forwards about half way to the wall. Then we will switch to using the distance sensor. We don't want the ball to confuse us though.
        robot.DriveToWall(6, DistanceUnit.INCH, 0.1); //Use the range sensor to get nice and close to the wall.
        robot.simpleGyroTurn(0.3, -90, 1500); //Turn right to drive alongside the beacons.
        robot.DriveToWhiteLine(-0.3,8,true,8000); // Drive to the white line
    }
}
