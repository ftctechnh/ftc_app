/* Copyright (c) 2014 Qualcomm Technologies Inc
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

import android.media.MediaPlayer;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareDevice;

import java.util.ArrayList;

//Edited in order to have all of the important constants as final, so that no unintentional modifications are made.
//This class should be used so that any changes made to the robot configuration propagates through all parts of the code that has been written.

public abstract class _RobotBase extends LinearOpMode
{
    /*** CONFIGURE ALL ROBOT ELEMENTS HERE ***/
    //Drive motors (they are lists because it helps when we add on new motors.
    protected ArrayList <DcMotor> leftDriveMotors = new ArrayList <>(), rightDriveMotors = new ArrayList<>();
    //Other motors
    protected DcMotor harvester, pusher;

    //This took a LONG TIME TO WRITE
    protected <T extends HardwareDevice> T Initialize  (Class <T> hardwareDevice, String name)
    {
        try
        {
            //Returns the last subclass (if this were a DcMotor it would pass back a Dc Motor.
            return hardwareDevice.cast(hardwareMap.get(name));
        }
        catch (Exception e)
        {
            OutputToDriverStation("Could not find " + name + " in hardware map.");
            return null;
        }
    }

    // Called on initialization (once)
    @Override
    public void runOpMode()
    {
        //Make sure that the robot components are found and initialized correctly.
        //This all happens during init()
        /*************************** DRIVING MOTORS ***************************/
        rightDriveMotors.add(Initialize(DcMotor.class, "right"));

        leftDriveMotors.add(Initialize(DcMotor.class, "left"));
        for(DcMotor motor : leftDriveMotors)
                motor.setDirection(DcMotor.Direction.REVERSE);

        /*************************** OTHER MOTORS ***************************/
        pusher = Initialize(DcMotor.class, "pusher");
        harvester = Initialize(DcMotor.class, "harvester");

        //NOTE: Actually attempting to use null motors will cause the program to terminate.
        //This advanced system is designed for when only specific hardware is required.
        //This code should tell you which motors and sensors are not configured before the program starts running.
        //Kudos Makiah

        //Actual program thread
        //Custom Initialization steps.
        driverStationSaysINITIALIZE();

        //Wait for the start button to be pressed.
        waitForStart();

        driverStationSaysGO(); //This is where the child classes differ.
    }

    //Optional overload.
    protected void driverStationSaysINITIALIZE() {}
    //Has to be implemented.
    protected abstract void driverStationSaysGO();

    /*** USE TO OUTPUT DATA IN A SLIGHTLY BETTER WAY THAT LINEAR OP MODES HAVE TO ***/
    ArrayList<String> linesAccessible = new ArrayList<>();
    private int maxLines = 7;
    protected void OutputToDriverStation(String newLine)
    {
        //Add new line at beginning of the lines.
        linesAccessible.add(0, newLine);
        //If there is more than 5 lines there, remove one.
        if (linesAccessible.size() > maxLines)
            linesAccessible.remove(maxLines);

        //Output every line in order.
        telemetry.update(); //Empty the output
        for (String s : linesAccessible)
            telemetry.addLine(s); //add all lines
        telemetry.update(); //update the output with the added lines.
    }

    //Allows for more robust output of actual data instead of line by line without wrapping.  Used for driving and turning.
    protected void OutputRealTimeData (String[] data)
    {
        telemetry.update();
        for (String s : data)
            telemetry.addLine(s);
        telemetry.update();
    }
}