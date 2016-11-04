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
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;

/**
 * Linear Autonomous Op Mode for iRads Robot.
 * Currently, only vision is implemented. Efforts have been made to move Vuforia code to
 * the VisualNavigation class, which is instantiated as 'visualNav' in this OpMode.
 *
 * Hardware has been instantiated using the Hardware_iRads class, but has not been initialized.
 * To initialize, simply uncomment 'robot.init(hardwareMap)' in the initialization block below.
 *
 */

@Autonomous(name="iRads: AutoOp Vision Linear", group="iRads")  // @Autonomous(...) is the other common choice
//@Disabled
public class iRadsAutoOpMode_Linear extends LinearOpMode {

    /* Declare OpMode members. */
    private VisualNavigation visualNav = new VisualNavigation();
    private ElapsedTime runtime = new ElapsedTime();
    Hardware_iRads robot = new Hardware_iRads();   // use the class created to define iRads hardware

    @Override
    public void runOpMode() {

        // Initialization
//        robot.init(hardwareMap); // Initialize Hardware (5 motors/encoders, 1 servo)
        this.visualNav.telemetry = this.telemetry;  // Helps output navigation messages.
        this.visualNav.runtime = this.runtime;      // Helps create navigation timestamps.
        this.visualNav.init(); // Initialize Visual Navigation
        telemetry.addData("Status", "Initialized");
        telemetry.update();




        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        /** Start tracking the data sets we care about. */
        this.visualNav.visualTargets.activate();
        telemetry.addData("Status", "visualTargets Activate");

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status", "Run Time: " + runtime.toString());
//            telemetry.update();


            // **************** Vuforia Test *********************

            for (VuforiaTrackable trackable : this.visualNav.allTrackables) {
                /**
                 * getUpdatedRobotLocation() will return null if no new information is available since
                 * the last time that call was made, or if the trackable is not currently visible.
                 * getRobotLocation() will return null if the trackable is not currently visible.
                 */
                telemetry.addData(trackable.getName(), ((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible() ? "Visible" : "Not Visible");    //

                OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener)trackable.getListener()).getUpdatedRobotLocation();
                if (robotLocationTransform != null) {
                    this.visualNav.lastLocation = robotLocationTransform;
                    this.visualNav.lastLocationUpdateTime = this.visualNav.runtime.time(); // update timestamp
                }
            } // for each trackable
            /**
             * Provide feedback as to where the robot was last located (if we know).
             */
            if (this.visualNav.lastLocation != null) {
                //  RobotLog.vv(TAG, "robot=%s", format(lastLocation));
                telemetry.addData("Pos", format(this.visualNav.lastLocation));
            } else {
                telemetry.addData("Pos", "Unknown");
            }
                // output time since last location update.
                telemetry.addData("trackAge",this.visualNav.getTrackAge());
            telemetry.update();

            // ***************** END Vuforia Testing *********************



        } // while(opModeIsActive())
    } // runOpMode()

    String format(OpenGLMatrix transformationMatrix) {
        return transformationMatrix.formatAsTransform();
    } // format(transformationMatrix)


} // Class
