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

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.teamcode.HardwareProfiles.HardwareTestPlatform;
import org.firstinspires.ftc.teamcode.Libs.VuforiaLib;

import java.util.List;

/**
 * Name the opMode and put it in the appropriate group
 */
@Autonomous(name = "OpModeLib - Check Vuforia Template", group = "OpModeLib")
@Disabled
/**
 * This opMode attempts to acquire and trigger the red beacons.
 *
 * Assumptions:
 *
 *  - The robot starts facing the positive Y axis (facing the the blue wall
 *  with the blue images, legos and tools)
 *  -  The robot starts with the left side wheels just to the left of the seam between tiles 2 and 3
 *  on the red team wall with tile one being the left most tile on the wall, ~ -1500 X from origin
 */
public class AutoSetupVuforiaTemplate extends LinearOpMode {

    /**
     * Instantiate all objects needed in this class
     */
    public static final String TAG = "VuforiaLib Sample";
    private final static HardwareTestPlatform robot = new HardwareTestPlatform();
    private List<Double> vuforiaTracking;
    private VuforiaLib myVuforia = new VuforiaLib();
    private double robotX;          // The robot's X position from VuforiaLib
    private double robotY;  // The robot's Y position from VuforiaLib
    private double robotBearing;    //Bearing to, i.e. the bearing you need to stear toward

    /**
     * Setup the init state of the robot.  The actions in the begin() method are run when you hit the
     * init button on the driver station app.
     */
    private void begin() {

        /**
         * Inititialize the robot's hardware.  The hardware configuration can be found in the
         * HardwareTestPlatform.java class.
         */
        robot.init(hardwareMap);

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
            vuforiaTracking = myVuforia.getLocation(myTrackables);
            robotX = vuforiaTracking.get(0);
            robotY = vuforiaTracking.get(1);
            robotBearing = vuforiaTracking.get(2);

            logData();
            telemetry.update();

            idle();

        }
    }

    private void logData() {
        telemetry.addData("LRMotorPos", String.valueOf(robot.motorLR.getCurrentPosition()));
        telemetry.addData("robotX", String.valueOf(robotX));
        telemetry.addData("robotY", String.valueOf(robotY));
        telemetry.addData("robotBearing", String.valueOf(robotBearing));
        telemetry.update();

    }


}
