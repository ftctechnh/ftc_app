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
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name="RedAutoOpmodeBallAndBeacons", group="Testing")  // @Autonomous(...) is the other common choice
//@Disabled
public class RedAutoOpmodeJustBeacons extends LinearOpMode
{
    //Variable code by what mechanical pieces we have
    int numSideColorSensors = 1;
    int numButtonPushers = 1;

    public enum Goal
    {
        ballColors,
        justColors,
        justBall,
    }

    public enum PusherMode
    {
        //How many pushers, how many color sensors
        P1S1,
        P1S2,
        P2S1,
        P2S2,
    }
    PusherMode pusherMode = PusherMode.P1S1;

    //This is our timer
    private ElapsedTime timer = new ElapsedTime();
    private double timeToHit = 0;

    //The engine which controls our drive motors
    DriveEngine engine = null;

    //Sensors
    Sensors sensors=null;
    RangePair rangepair = null;

    //Time constants
    private static final double TIME_ONE = 2;
    private static final double TIME_TWO = 10;
    private static final double TIME_EXTRA = .2;

    //Power constants
    private static final double LOW_POWER = .1;
    private static final double MID_POWER = .15;
    private static final double HIGH_POWER = .2;

    //If blue
    boolean blue = false;

    RedAutoOpmodeJustBeacons(boolean blue)
    {
        this.blue = blue;
    }

    @Override
    public void runOpMode() {
        engine = new DriveEngine(DriveEngine.engineMode.directMode, hardwareMap, gamepad1);
        sensors = new Sensors(hardwareMap);
        rangepair = new RangePair(hardwareMap, 9, sensors);
        //Wait for the game to start (driver presses PLAY)
        waitForStart();
        timer.reset();

        //Run until the end of autonomous
        while (opModeIsActive()) {
            driveAlongWall(8, engine.inchesBetweenMotors, .7);
        }

    }

   public void driveAlongWall(double targetDistanceFromWall, double distanceBetweenMotors, double maxPower)
   {
       double dBM = distanceBetweenMotors;
       double inchesAway = rangepair.minDistanceAway();
       double distanceToTarget = inchesAway - targetDistanceFromWall;
       //finds the radius of the target circular path
       double circleRadius = Math.tan(90-rangepair.angleToWall()) * Math.abs(distanceToTarget);
       //found at https://botballprogramming.org/site-maps/right-brain-site-map/bonus-supplemental-information-tips/assignments/formula-for-turn-radius/
       double minPower = dBM/circleRadius*(maxPower*(circleRadius/dBM)-maxPower);
       if(distanceToTarget > 0)
       {
           //right is greater
           engine.setEngineToPower(minPower, maxPower);
       }
       if(distanceToTarget < 0)
       {
           //right is greater
           engine.setEngineToPower(maxPower, minPower);
       }

   }

}
