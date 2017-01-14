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
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

/*
PushBall is for the Moses Lake FTC Interleague Competition (1/14/17)
Goal: The robot drives forward, knocks the ball off and parks on the platform
 */

@Autonomous(name="Comp_PushBall", group="Competition")  // @Autonomous(...) is the other common choice
//@Disabled
public class Comp_PushBallOpmode extends LinearOpMode
{
    //This is our timer
    private ElapsedTime timer = new ElapsedTime();
    private double timeBallDetected = 0;

    Bogg bogg;

    //Time constants
    private static final double LOW_POWER_TIME = 2;     //start slow, don't burn rubber
    private static final double MID_POWER_TIME = 4;     //start going faster
    private static final double PARKING_TIME = 1;      //time between ball detection and robot parking
    private static final double MAX_DRIVE_TIME = 20;    //failsafe, stop the robot if ball isn't seen

    //Power constants
    private static final double LOW_POWER = .1;
    private static final double MID_POWER = .15;
    private static final double HIGH_POWER = .2;

    //Sensor constants
    private static final double OPTICAL_BALL_THRESHOLD = 0.02;  //magic number, 0-threshold is nothing seen
                                                                //getLightDetected() ranges from 0 - 1

    @Override
    public void runOpMode() {
        bogg = new Bogg(hardwareMap, gamepad1);
        //Wait for the game to start (driver presses PLAY)
        waitForStart();
        timer.reset();
        bogg.driveEngine.invertDirection();
        //Run until the end of autonomous
        while (opModeIsActive()) {
            //Run until we see something
            while (bogg.sensors.opSensorFront.getLightDetected() < OPTICAL_BALL_THRESHOLD) {
                if (timer.seconds() < LOW_POWER_TIME) {
                    bogg.driveEngine.drive(LOW_POWER);

                } else if (timer.seconds() < MID_POWER_TIME) {
                    bogg.driveEngine.drive(MID_POWER);

                } else if (timer.seconds() > MAX_DRIVE_TIME){
                    bogg.driveEngine.stop();
                    telemetry.addData("Status", "MAX TIME Exceeded");
                    break;

                } else {
                    bogg.driveEngine.drive(HIGH_POWER);
                }

                telemetry.addData("Status", "Light: " + ((int)(1000*bogg.sensors.opSensorFront.getLightDetected())/1000.));
                telemetry.update();
            }
            timeBallDetected = timer.seconds();

            //Reset the timer
            timer.reset();
            //Keep moving just a little longer to land on the black square
            while (timer.seconds() < PARKING_TIME) {
                telemetry.addData("Parking", timer.seconds());
                telemetry.update();
                bogg.driveEngine.drive(HIGH_POWER);
            }

            bogg.driveEngine.stop();

            break;    //all done, exit
        }

    }

 }
