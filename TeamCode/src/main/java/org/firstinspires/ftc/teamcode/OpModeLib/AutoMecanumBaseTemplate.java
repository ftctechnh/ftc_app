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
package org.firstinspires.ftc.teamcode.OpModeLib;

/**
 * Import the classes we need to have local access to.
 */
//Foo

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.teamcode.HardwareProfiles.HardwareTestPlatform;
import org.firstinspires.ftc.teamcode.Libs.Beacon;
import org.firstinspires.ftc.teamcode.Libs.DataLogger;
import org.firstinspires.ftc.teamcode.Libs.DriveMecanum;
import org.firstinspires.ftc.teamcode.Libs.VuforiaLib;

import java.util.List;

/**
 * Name the opMode and put it in the appropriate group
 */
@Autonomous(name = " OpModeLib - AutoMecanumBaseTemplate", group = "OpModeLib")

public class AutoMecanumBaseTemplate extends LinearOpMode {

    /**
     * Instantiate all objects needed in this class
     */

    private final static HardwareTestPlatform robot = new HardwareTestPlatform();
    public List<Double> vuforiaTracking;   //List of Vuforia coordinates
    public List<VuforiaTrackable> myTrackables;    //List of Vuforia trackable objects
    public String alliance = "red";                //Your current alliance
    double radians = 0;                                     //Used in calculating wheel speeds
    private VuforiaLib myVuforia = new VuforiaLib();
    private ElapsedTime runtime = new ElapsedTime();
    /**
     * Define global variables
     */
    private double mm = 500;            //Distance for translateDistance method
    private double power = .6;          //Motor power for all methods
    private double heading = 90;        //Heading for all methods
    private double y = -200;            //Vuforia y stop coordinate
    private double x = -200;            //Vuforia x stop coordinate
    private double timeOut = 5;         //Timeout in seconds for translateTime method
    private double odsThreshold = .5;   //Threshold at which the ODS sensor acquires the whie line
    private boolean colorLedEnable = false; //Enable if in testing mode, disable for beacon
    private DataLogger Dl;                          //Datalogger object
    private double motorCorrectCoefficient = .05;    //Amount to divide the zInt drift by
    private String beaconColorRight;                //Color of the right side of the beacon
    private String beaconColorLeft;                 //Color of the left side of the beacon
    private String button;                          //Which button to push
    private String beaconState = "init";            //Has the beacon been triggered?
    private boolean log = true;                     //Set to true to enable logging
    private boolean tel = true;                     //Set to true to enable telemetry
    private DriveMecanum drive;
    private Beacon beacon;
    private State state = State.ACQUIRE_RED_BEACON_LEFT;               //Machine State

    public void runOpMode() {
        /**
         * Setup the init state of the robot.  This configures all the hardware that is defined in
         * the HardwareTestPlatform class.
         */
        robot.init(hardwareMap);

        /**
         * Set the initial servo positions
         */
        robot.servoODS.setPosition(0);
        robot.servoPusher.setPosition(.5);

        /**
         *  Create the DataLogger object.
         */
        if (log) {
            createDl();
        }

        /**
         * Calibrate the gyro
         */
        robot.sensorGyro.calibrate();
        while (robot.sensorGyro.isCalibrating()) {
            telemetry.addData("Waiting on Gyro Calibration", "");
            telemetry.update();
        }

        /**
         * Initialize Vuforia and retrieve the list of trackable objects.
         */
        myTrackables = myVuforia.vuforiaInit();

        drive = new DriveMecanum(robot, this, myVuforia, myTrackables, Dl);
        beacon = new Beacon(robot, this);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        sleep(1000);

        telemetry.addData(">", "Press Play to start tracking");
        telemetry.update();

        /**
         * Start the opMode
         */
        waitForStart();

        while (opModeIsActive()) {
            /**
             * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
             * This is the section of code you should change for your robot.
             * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
             */

            switch (state) {
                case TEST:
                    heading = 90;
                    power = 1;
                    timeOut = 2;
                    x = -1450;

                    //drive.translateTime(timeOut, power, heading);
                    drive.followLineX(x, power, heading);

                    state = State.HALT;
                    break;
                case ACQUIRE_RED_BEACON_LEFT:
                    heading = 50;
                    power = 1;
                    timeOut = 2.5;

                    drive.translateTime(timeOut, power, heading);

                    heading = 90;
                    power = 1;
                    timeOut = .4;

                    drive.translateTime(timeOut, power, heading);

                    heading = 90;
                    power = .2;
                    drive.translateOdsStop(odsThreshold, power, heading);

                    sleep(250);

                    x = -1500;
                    heading = 0;
                    drive.acquireBeaconX(x, power, heading);

                    sleep(250);

                    //Exit the OpMode
                    state = State.PUSH_BUTTON_LEFT;
                    break;
                case PUSH_BUTTON_LEFT:
                    beaconColorRight = beacon.getRightColor(colorLedEnable);

                    button = beacon.getButtonPush(alliance, beaconColorRight);

                    beacon.pushButton(button);

                    sleep(500);
                    beacon.resetButton();

                    //Exit the OpMode
                    state = State.ACQUIRE_RED_BEACON_RIGHT;

                    break;

                case PUSH_BUTTON_RIGHT:
                    beaconColorRight = beacon.getRightColor(colorLedEnable);

                    button = beacon.getButtonPush(alliance, beaconColorRight);

                    beacon.pushButton(button);

                    sleep(500);
                    beacon.resetButton();

                    heading = 180;
                    power = 1;
                    timeOut = 1;

                    drive.translateTime(timeOut, power, heading);
                    //Exit the OpMode
                    state = State.END_GAME;

                    break;
                case END_GAME:
                    //Lift the ODS sensor away from the mat
                    robot.servoODS.setPosition(.5);
                    heading = 270;
                    timeOut = 3;
                    drive.translateTime(timeOut, power, heading);

                    heading = 30;
                    power = .5;
                    drive.pivotLeft(power, heading);

                    timeOut = 2;
                    heading = 0;
                    power = .7;
                    drive.translateTime(timeOut, power, heading);
                    state = State.HALT;
                    break;
                case ACQUIRE_RED_BEACON_RIGHT:

                    heading = 180;
                    power = 1;
                    timeOut = .5;

                    drive.translateTime(timeOut, power, heading);

                    heading = 90;
                    power = 1;
                    timeOut = 2;

                    drive.translateTime(timeOut, power, heading);

                    power = .2;
                    heading = 90;
                    drive.translateOdsStop(odsThreshold, power, heading);

                    sleep(250);

                    x = -1500;
                    heading = 0;
                    power = .3;
                    drive.acquireBeaconX(x, power, heading);

                    sleep(250);


                    state = State.PUSH_BUTTON_RIGHT;
                    break;
                case HALT:
                    //Lift the ODS sensor away from the mat
                    //Stop the motors
                    drive.motorsHalt();

                    if (log) {
                        //Stop the DataLogger
                        dlStop();
                    }

                    //Exit the OpMode
                    requestOpModeStop();
                    break;
            }
            /**
             * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
             * Don't change anything past this point.  Bad things could happen.
             * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
             */
        }
    }


    /**
     * Setup the dataLogger
     * The dataLogger takes a set of fields defined here and sets up the file on the Android device
     * to save them to.  We then log data later throughout the class.
     */
    private void createDl() {

        Dl = new DataLogger("AutoRedStratOne" + runtime.time());
        Dl.addField("runTime");
        Dl.addField("Alliance");
        Dl.addField("State");
        Dl.addField("Procedure");
        Dl.addField("courseCorrect");
        Dl.addField("heading");
        Dl.addField("robotX");
        Dl.addField("robotY");
        Dl.addField("X");
        Dl.addField("Y");
        Dl.addField("robotBearing");
        Dl.addField("initZ");
        Dl.addField("currentZ");
        Dl.addField("zCorrection");
        Dl.addField("touchSensor");
        Dl.addField("ODS");
        Dl.addField("colorRightRed");
        Dl.addField("colorRightBlue");
        Dl.addField("colorLeftRed");
        Dl.addField("colorLeftBlue");
        Dl.addField("LFTargetPos");
        Dl.addField("LFMotorPos");
        Dl.addField("LF");
        Dl.addField("RF");
        Dl.addField("LR");
        Dl.addField("RR");
        Dl.newLine();
    }

    /**
     * Stop the DataLogger
     */
    private void dlStop() {
        Dl.closeDataLogger();

    }

    /**
     * Enumerate the States of the machine.
     */
    enum State {
        ACQUIRE_RED_BEACON_LEFT, ACQUIRE_RED_BEACON_RIGHT, PUSH_BUTTON_LEFT, PUSH_BUTTON_RIGHT,
        TEST, HALT, END_GAME
    }

}
