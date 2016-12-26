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

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.teamcode.HardwareProfiles.HardwareTestPlatform;
import org.firstinspires.ftc.teamcode.Libs.BeaconDetection;
import org.firstinspires.ftc.teamcode.Libs.DataLogger;
import org.firstinspires.ftc.teamcode.Libs.SensorGyro;
import org.firstinspires.ftc.teamcode.Libs.VuforiaLib;

import java.util.List;

/**
 * Name the opMode and put it in the appropriate group
 */
@Autonomous(name = "OpModeLib - Sensor Setup Template", group = "OpModeLib")
@Disabled
/**
 * This opMode attempts to acquire and trigger the rightRed beacons.
 *
 * Assumptions:
 *
 *  - The robot starts facing the positive Y axis (facing the the rightBlue wall
 *  with the rightBlue images, legos and tools)
 *  -  The robot starts with the left side wheels just to the left of the seam between tiles 2 and 3
 *  on the rightRed team wall with tile one being the left most tile on the wall, ~ -1500 X from origin
 */
public class AutoSetupSensorTemplate extends LinearOpMode {

    /**
     * Instantiate all objects needed in this class
     */
    public static final String TAG = "VuforiaLib Sample";
    private final static HardwareTestPlatform robot = new HardwareTestPlatform();
    private final static SensorGyro gyro = new SensorGyro(robot);
    private final static BeaconDetection beacon = new BeaconDetection();
    private ElapsedTime runtime = new ElapsedTime();
    // the the current vuforia image
    private double ods = 0;     //Value returned from the Optical Distance Sensor
    private double colorThreshold = 2;  //This is the threshold for detecting the beacon color
    private double rightRed = 0;
    private double rightBlue = 0;
    private double leftRed = 0;
    private double leftBlue = 0;
    private String beaconColor;
    private List<Double> vuforiaTracking;
    private VuforiaLib myVuforia = new VuforiaLib();
    private DataLogger Dl;
    private double zInt;            //Gyro Integrated Z
    private double robotX;          // The robot's X position from VuforiaLib
    private double robotY;  // The robot's Y position from VuforiaLib
    private double robotBearing;    //Bearing to, i.e. the bearing you need to stear toward
    private double touch;

    /**
     * Setup the init state of the robot.  The actions in the begin() method are run when you hit the
     * init button on the driver station app.
     */
    private void begin() {

        /** Setup the dataLogger
         * The dataLogger takes a set of fields defined here and sets up the file on the Android device
         * to save them to.  We then log data later throughout the class.
         */
        Dl = new DataLogger("Sensor_Check" + runtime.time());
        Dl.addField("touchSensor");
        Dl.addField("robotX");
        Dl.addField("robotY");
        Dl.addField("robotBearing");
        Dl.addField("zInt");
        Dl.addField("ODS");
        Dl.addField("rgb");
        Dl.addField("rightRed");
        Dl.addField("rightBlue");
        Dl.addField("leftRed");
        Dl.addField("leftBlue");
        Dl.addField("beacon color");
        Dl.addField("LRMotorPos");
        Dl.newLine();

        /**
         * Inititialize the robot's hardware.  The hardware configuration can be found in the
         * HardwareTestPlatform.java class.
         */
        robot.init(hardwareMap);

        robot.colorSensorRight.enableLed(true);
        robot.colorSensorLeft.enableLed(true);
        /**
         * Calibrate the gyro
         */
        gyro.calibrate();
        while (gyro.isCalibrating()) {
            telemetry.addData("Waiting on Gyro Calibration", "");
            telemetry.update();
        }

        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    public void runOpMode() {
        begin();
        List<VuforiaTrackable> myTrackables;
        myTrackables = myVuforia.vuforiaInit();

        /**
         * Start the opMode
         */
        telemetry.addData(">", "Press Play to start tracking");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            ods = robot.ods.getLightDetected();
            zInt = gyro.getIntegratedZ();

            //robot.colorSensorRight.enableLed(false);
            //robot.colorSensorLeft.enableLed(false);

            rightRed = robot.colorSensorRight.red();
            rightBlue = robot.colorSensorRight.blue();
            leftRed = robot.colorSensorLeft.red();
            leftBlue = robot.colorSensorLeft.blue();
            beaconColor = getColor();
            touch = robot.touchSensor.getValue();

            vuforiaTracking = myVuforia.getLocation(myTrackables);
            robotX = vuforiaTracking.get(0);
            robotY = vuforiaTracking.get(1);
            robotBearing = vuforiaTracking.get(2);

            logData();
            telemetry.update();

            idle();

        }
        //Stop the motors
        motorsHalt();

        //Stop the DataLogger
        dlStop();

        //Exit the OpMode
        requestOpModeStop();
    }

    public String getColor() {
        String color = "unk";

        rightRed = robot.colorSensorRight.red();
        rightBlue = robot.colorSensorRight.blue();

        if (rightRed >= colorThreshold) {
            color = "rightRed";
        }
        if (rightBlue >= colorThreshold) {
            color = "rightBlue";
        }
        return color;
    }

    private void logData() {
        telemetry.addData("LRMotorPos", String.valueOf(robot.motorLR.getCurrentPosition()));
        telemetry.addData("touchSensor", robot.touchSensor.getValue());
        telemetry.addData("robotX", String.valueOf(robotX));
        telemetry.addData("robotY", String.valueOf(robotY));
        telemetry.addData("robotBearing", String.valueOf(robotBearing));
        telemetry.addData("zInt", String.valueOf(zInt));
        telemetry.addData("ODS", String.valueOf(ods));
        telemetry.addData("rightRed", String.valueOf(rightRed));
        telemetry.addData("rightBlue", String.valueOf(rightBlue));
        telemetry.addData("leftRed", String.valueOf(leftRed));
        telemetry.addData("leftBlue", String.valueOf(leftBlue));
        telemetry.addData("beacon color", String.valueOf(beaconColor));
        telemetry.update();

        Dl.addField(String.valueOf(robot.touchSensor.getValue()));
        Dl.addField(String.valueOf(robotX));
        Dl.addField(String.valueOf(robotY));
        Dl.addField(String.valueOf(robotBearing));
        Dl.addField(String.valueOf(zInt));
        Dl.addField(String.valueOf(ods));
        Dl.addField(String.valueOf(rightRed));
        Dl.addField(String.valueOf(rightBlue));
        Dl.addField(String.valueOf(leftRed));
        Dl.addField(String.valueOf(leftBlue));
        Dl.addField(String.valueOf(beaconColor));
        Dl.addField(String.valueOf(robot.motorLR.getCurrentPosition()));
        Dl.newLine();
    }

    /**
     * Stop the DataLogger
     */
    private void dlStop() {
        Dl.closeDataLogger();

    }

    /**
     * Cut power to the motors.
     */
    private void motorsHalt() {
        robot.motorLF.setPower(0);
        robot.motorRF.setPower(0);
        robot.motorLR.setPower(0);
        robot.motorRR.setPower(0);
    }
}
