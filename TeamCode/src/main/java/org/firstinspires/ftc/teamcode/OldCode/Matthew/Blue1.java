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
package org.firstinspires.ftc.teamcode.OldCode.Matthew;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.OldCode.Competition.InvadersVelocityVortexBot;

/**
 * This file illustrates the concept of driving a path based on Gyro heading and encoder counts.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByTime;
 *
 *  This code ALSO requires that you have a Modern Robotics I2C gyro with the name "gyro"
 *   otherwise you would use: PushbotAutoDriveByEncoder;
 *
 *  This code requires that the drive Motors have been configured such that a positive
 *  power command moves them forward, and causes the encoders to count UP.
 *
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 *  In order to calibrate the Gyro correctly, the robot must remain stationary during calibration.
 *  This is performed when the INIT button is pressed on the Driver Station.
 *  This code assumes that the robot is stationary when the INIT button is pressed.
 *  If this is not the case, then the INIT should be performed again.
 *
 *  Note: in this example, all angles are referenced to the initial coordinate frame set during the
 *  the Gyro Calibration process, or whenever the program issues a resetZAxisIntegrator() call on the Gyro.
 *
 *  The angle of movement/rotation is assumed to be a standardized rotation around the robot Z axis,
 *  which means that a Positive rotation is Counter Clock Wise, looking down on the field.
 *  This is consistent with the FTC field coordinate conventions set out in the document:
 *  ftc_app\doc\tutorial\FTC_FieldCoordinateSystemDefinition.pdf
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="Comp Blue 1", group="Pushbot")
@Disabled
public class Blue1 extends LinearOpMode {

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
// This bit was tested with FauxBot
//        robot.encoderDrive(0.2, 55, 55, 1.7); //Drive forwards to the plywood base with the capball.
//        robot.simpleGyroTurn(0.1, 90, 1500); //Turn left towards the beacons.
//        robot.encoderDrive (0.2, 55, 55, 1.7); //Drive forwards about half way to the wall. Then we will switch to using the distance sensor. We don't want the ball to confuse us though.
//        robot.DriveToWall(6, DistanceUnit.INCH, 0.1); //Use the range sensor to get nice and close to the wall.
//        robot.simpleGyroTurn(0.1, -90, 1500); //Turn right to drive alongside the beacons.
//        robot.DriveToWhiteLine(0.05,8,true,20000); // Drive to the white line

//  This bit was tested with Ankle-biter
        robot.timedDrive(0.2,1900); //Drive forwards to the plywood base with the capball.
        //robot.simpleGyroTurn(0.3, -90, 1500); //Turn left towards the beacons.
        robot.turnToAbsoluteHeading(0.3,90,3000);
        robot.timedDrive(0.2, 3000); //Drive forwards about half way to the wall. Then we will switch to using the distance sensor. We don't want the ball to confuse us though.
        robot.DriveToWall(6, DistanceUnit.INCH, 0.15, 10000); //Use the range sensor to get nice and close to the wall.
        //robot.simpleGyroTurn(0.3, -90, 1500); //Turn right to drive alongside the beacons.
        robot.turnToAbsoluteHeading(0.3,0,3000);
        robot.DriveToWhiteLine(0.3,8,true,5000); // Drive to the white line

        telemetry.addData("Path", "Complete");
        //telemetry.update();
    }
}
