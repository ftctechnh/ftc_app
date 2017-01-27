/* Copyright (c) 2015 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * A simple example of a linear op mode that will approach an IR beacon
 */

@Autonomous(name="Autonomous - Red Beacons", group = "Autonomous Group")
public class AutonomousRedBeacons extends _AutonomousBase
{

    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO() throws InterruptedException
    {
        driveForTime(.6, 700); //Drive a little ways from the wall.

        setPrecisionFactor(25); //We can afford to be a bit inaccurate for this turn.
        turnToHeading(-45, TurnMode.LEFT);

        driveForTime(.8, 1500); //Dash over across the corner goal.

        //Turn until the range sensors detect that we are parallel with the wall.
        setRightPower(.2);
        while (frontRangeSensor.getDistance(DistanceUnit.CM) != backRangeSensor.getDistance(DistanceUnit.CM))
            updateMotorPowersBasedOnRangeSensors();

        stopDriving();

        for (int i = 0; i < 2; i++) //Two beacons.
        {
            setMovementPower(.3);
            while (bottomColorSensor.alpha() < 3)
                updateMotorPowersBasedOnRangeSensors();

            //We have reached the line and are parallel to the wall.
            outputNewLineToDrivers("Beacon reached, at " + frontRangeSensor.getDistance(DistanceUnit.CM) + " cm from the front and " + backRangeSensor.getDistance(DistanceUnit.CM) + " cm from the back.");

            double startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < 300)
                updateMotorPowersBasedOnRangeSensors();

            //Check first color.
            if (rightColorSensor.blue() > rightColorSensor.red()) //Looking at the blue one, so move forward a set distance.
            {
                startTime = System.currentTimeMillis();
                while (System.currentTimeMillis() - startTime < 700)
                    updateMotorPowersBasedOnRangeSensors();
            }

            //Run the continuous rotation servo out to press, then back in.
            leftButtonPusher.setPosition(.8);
            sleep(1000);
            leftButtonPusher.setPosition(.2);
            sleep(600);
            leftButtonPusher.setPosition(.5);

            if (i == 0)
            {
                setMovementPower(.7);
                startTime = System.currentTimeMillis();
                while (System.currentTimeMillis() - startTime < 3000) //Three seconds parallel to wall.
                    updateMotorPowersBasedOnRangeSensors();
            }
        }
    }
}