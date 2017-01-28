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
package org.firstinspires.ftc.teamcode.Matthew;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.InvadersVelocityVortexBot;

/**
 *
 */

@Autonomous(name="New Blue 2", group="Pushbot")
//@Disabled
public class New_Blue_2 extends LinearOpMode {

    /* Declare OpMode members. */
    InvadersVelocityVortexBot robot   = new InvadersVelocityVortexBot();   // Use our custom hardware

    /// This method assumes the robot is aligned to the white line on the floor in front of the beacon


    @Override
    public void runOpMode() {

        robot.init(this);
        waitForStart();

        robot.sleepMs(5000);                 // Sleep for 10s - Let our alliance partner do great things
        robot.ohshoot();                     // SHOOT! - This takes 12 seconds
        robot.encoderDrive(0.3,6,6,2500);    // Move out slightly to clear away from the wall
        robot.simpleGyroTurn(0.3,-40, 2500); // Turn CCW ~45 degrees
        robot.encoderDrive(0.3,30,30,5000);  // Drive forward 30in
        robot.simpleGyroTurn(0.3,81,2500);   // Turn CW ~90 degrees
        robot.encoderDrive(0.3,30,30,5000);  // Drive to the center pedestal and park on it (knocking the cap ball off in the process)

    }
}
