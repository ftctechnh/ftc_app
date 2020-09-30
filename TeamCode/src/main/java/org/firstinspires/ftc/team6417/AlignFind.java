/* Copyright (c) 2019 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.team6417;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.internal.vuforia.VuforiaException;
import org.firstinspires.ftc.team6417.Hardware6417;

import detectors.ClosableVuforiaLocalizer;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.RobotLog;

import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;


@Autonomous(name="VueBoi", group ="Linear Opmode")

public class AlignFind extends LinearOpMode {

    private ElapsedTime searchTime = new ElapsedTime(0);

    /* vuforia key */
    private static final String VUFORIA_KEY =
            "ARuvZIj/////AAABmVkUSkLsEkjjhzixTaJVlaFNlxHEjVM47c6y7GtSCjJmsGR3bNdALVunnoGjmIog/AUoVdmCsQmzWc2WS0R1Qh+EVA5/H+39wEZ1sohFZ9UGpyWT/1da4Mm0sUbB4/atlnNbCs9PzFCnd0cFDpbzWRSmQjkaxdBlDY9rFnOGrqdEzdT9hWK2NznebhyCFWITMTu94TYY1vbuiyD+08KBIgsMPypNZ8tVSrB405WNTN3ZEqU1a+tQd7ppLUOIYfcYLKuXHRIsmiybps87PkhD84d+/NiE1pinNwBhtxhMPYgqGf1HrE7H+AzKYJpgla1DyG8AMLL1pwJKsA3penrUGwGENWr6EkVHkMLfsQoe6Jym";

    private static final int skystoneMid = -100; //X positions of skystone positions
    private static final int skystoneCenter = 100;

    // Class Members
    private OpenGLMatrix skystonePositionCoords = null;
    public VuforiaLocalizer vuforia = null;

    private static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;
    private static final boolean PHONE_IS_PORTRAIT = true;

    /**
     * This is the webcam we are to use. As with other hardware devices such as motors and
     * servos, this device is identified using the robot configuration tool in the FTC application.
     */

    private boolean targetVisible = false;

    private int skystonePosition = 4;

    Hardware6417 robot = new Hardware6417();
    BNO055IMU imu;
    Orientation angles;
    Acceleration gravity;

    //private WebcamName webcamName = null;


    /**
     * This method initiates vuforia. Make sure to pass a webcam object
     * <p>
     * // * @param awebcamName The webcam object
     */

    public void runOpMode() {

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        telemetry.addLine("initialized camera");
        telemetry.update();

        //robot.drivetoPosition(20, 0.5);

        blueInit();
        telemetry.addLine("blueInit");
        telemetry.update();

        try {
            skystonePosition = visionTest();
            telemetry.addData("SkyStone position:", skystonePosition);
            telemetry.update();

        } catch (Exception e) {
            telemetry.addLine(e.getMessage());
            telemetry.update();
        }


        /***

         if(skystonePosition == 0){
         robot.strafeToPosition(-8, 0.5);

         } else if(skystonePosition == 2){
         robot.strafeToPosition(8, 0.5);
         }
         ***/


    }

    public void blueInit() {

        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         * We can pass Vuforia the handle to a camera preview resource (on the RC phone);
         * If no camera monitor is desired, use the parameter-less constructor instead (commented out below).
         */

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CAMERA_CHOICE;

        /*
         * We also indicate which camera on the RC we wish to use.
         */

        //  Instantiate the Vuforia engine
        vuforia = new ClosableVuforiaLocalizer(parameters);

        float mmPerInch = 25.4f;
        float mmBotWidth = 18 * mmPerInch;
        float mmFTCFieldWidth = (12 * 12 - 2) * mmPerInch;

        OpenGLMatrix phoneLocationOnRobot = OpenGLMatrix
                .translation(mmBotWidth / 2, 0, 0)
                .multiplied(Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC, AxesOrder.YZY,
                        AngleUnit.DEGREES, -90, 0, 0));

    }

    public int visionTest() {

        VuforiaTrackables targetsSkyStone = this.vuforia.loadTrackablesFromAsset("Skystone");
        VuforiaTrackable stoneTarget = targetsSkyStone.get(0);
        stoneTarget.setName("Skystone");

        targetsSkyStone.activate();

        boolean noFoundSkystone = true;

        searchTime.reset();
        while (!isStopRequested()) {
            //Just  a refresh timer, don't actually need this
            //if(timer.milliseconds() == 3) continue;
            boolean stoneVisible = false;

            // check to see if the skystone is visible.
            if (((VuforiaTrackableDefaultListener) stoneTarget.getListener()).isVisible()) {
                telemetry.addData("Visible Target", stoneTarget.getName()); //just returns "Stone Target"
                /*In these coordinates, the X axis goes from the left (negative) to the right (positive).
                    The Y axis goes up and down on the middle of the screen, and the Z axis goes from the camera outward. */

                //command to get the relative position as provided by vuforia
                OpenGLMatrix location = ((VuforiaTrackableDefaultListener) stoneTarget.getListener()).getVuforiaCameraFromTarget();
                if (location != null) {
                    // Get the positional part of the coordinates
                    VectorF translation = location.getTranslation();
                    //clip the actual X to see if it is closer to the left or right
                    float closestX = Range.clip(translation.get(0), -20f, 20f);
                    /*"center" because we (my team) only looks at the right two in the farthest set of three in the quarry,
                    so the leftmost image would be the center of the three stones concerned */
                    if (closestX == -20) telemetry.addData("Skystone Target:", "Center");
                    //Right most stone of the two
                    if (closestX == 20) telemetry.addData("Skystone Target:", "Right");
                    //Also express the relative pose (for info purposes)
                    telemetry.addData("Pos (in)", "{X, Y, Z} = %.1f, %.1f, %.1f",
                            translation.get(0), translation.get(1), translation.get(2));
                }
            } else {
                telemetry.addData( "Visible Target", "none");
            }
            telemetry.update();
            //timer.reset();

            if (skystonePosition != 1 && skystonePosition != 2) skystonePosition = 0;

            // Disable Tracking when we are done;
            targetsSkyStone.deactivate();

        }
        return skystonePosition;
    }
}