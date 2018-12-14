/* Copyright (c) 2018 FIRST. All rights reserved.
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

package org.firstinspires.ftc.teamcode.AutonomousComp;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;
import com.vuforia.CameraDevice;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.HardwareMap.BasicChasieHardware;

import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;

@Autonomous(name = "Depot Side Sample Auto", group = "Auto")
//@Disabled
public class DepotSideSampleAuto extends LinearOpMode {

    BasicChasieHardware robot = new BasicChasieHardware();
    private ElapsedTime runtime = new ElapsedTime();
    private ElapsedTime gametime = new ElapsedTime(); // Never reset, used to time events since Start

    // IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
    // 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
    // A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
    // web site at https://developer.vuforia.com/license-manager.
    //
    // Vuforia license keys are always 380 characters long, and look as if they contain mostly
    // random data. As an example, here is a example of a fragment of a valid key:
    // ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
    // Once you've obtained a license key, copy the string from the Vuforia web site
    // and paste it in to your code on the next line, between the double quotes.

    private static final String VUFORIA_KEY = "AUcoxf//////AAABmcMpe4Yi60OAn/zHVg0dTNYPj79vn8iRC7orx3abDd4T72PWJbSaMUO3w2/0RnvIBrjc7Ti42j2uJNh0Rk37IbQ4VjYz49C/nowtdGo+7+FL4TMUF9SQfz2I2C5exQu+rnOZ1ueGOppkD7eTcepeBm0ePez/ucss6O4ehkWjjtGuqvkak2rETObU+ZxeTdzNNgUzy+iMD/pdORuIiEcoC5agEX4ff8Ck4UqER9AlOQn5Lsst/LmnudVSV9SiS3LBat9H6RpS0aDjjHFhddoYBF9TFW6gASh5hsGCAgKWygkpHj1Kqp6D4QlvyBZp8HNBW8jPaQYLQEXksbKidoOeeq/PDEkxoT7t/TmpC7pw+dz6";


    // Since ImageTarget trackables use mm to specify their dimensions, we must use mm for all the physical dimension.
    // We will define some constants and conversions here
    private static final float mmPerInch = 25.4f;
    private static final float mmFTCFieldWidth = (12 * 6) * mmPerInch;       // the width of the FTC field (from the center point to the outer panels)
    private static final float mmTargetHeight = (6) * mmPerInch;          // the height of the center of the target image above the floor


    // Select which camera you want use.  The FRONT camera is the one on the same side as the screen.
    // Valid choices are:  BACK or FRONT
    private static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;

    private OpenGLMatrix lastLocation = null;
    private boolean targetVisible = false;
    Orientation rotation;

    // {@link #vuforia} is the variable we will use to store our instance of the Vuforia
    // localization engine.
    VuforiaLocalizer vuforia;
    VuforiaLocalizer.Parameters vu_parameters;
    final int CAMERA_FORWARD_DISPLACEMENT = 20;   // eg: Camera is 110 mm in front of robot center
    final int CAMERA_VERTICAL_DISPLACEMENT = 348;   // eg: Camera is 200 mm above ground
    final int CAMERA_LEFT_DISPLACEMENT = 72;     // eg: Camera is ON the robot's center line

    int cameraMonitorViewId;
    String imagename = "";



    // tfod is the variable we will use to store our instance of the Tensor Flow Object
    // Detection engine.
    private TFObjectDetector tfod;

    // Tensor Flow
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    double goldAngle; // estimated angle returned by tensor flow for gold mineral
    boolean goldMineralFound = false;
    int maxBackups = 3; // maximum number of times to backup to increase view width
    int backupCounter = 0; // number of backups performed
    double backupDistanceTF = -2.0; // negative number of inches to backup

    double rammingDistance = 33; // TODO: confirm this value by measurement


    // IMU Configuration
    private BNO055IMU imu;
    private BNO055IMU.Parameters imu_parameters;
    private Orientation imu_angles;
    private Orientation lastAngles = new Orientation();
    private double globalAngle = 0;

    /////////////////////////////////////////////////////////////////////////

    @Override
    public void runOpMode() throws InterruptedException {
        RobotLog.ii("DbgLog", "Starting");

        // Initialize the robot
        telemetry.addLine("Initializing robot config.");
        telemetry.update();
        robot.init(this.hardwareMap);
        telemetry.addLine("Robot config. Initialized.");
        telemetry.update();

        // Initialize the IMU
        telemetry.addLine("initializing imu");
        telemetry.update();
        initIMU();
        telemetry.addLine("imu initialized");
        telemetry.update();

        // Initialize Vuforia
        telemetry.addLine("initializing vuforia");
        telemetry.update();
        initVuforia();
        telemetry.addLine("vuforia initialized");
        telemetry.update();

        /////
        //
        // Initialize tensor flow
        //
        /////
        initTensorFlow();



        //////////////////////////////////////////////////////////////////////
        //
        // Wait for start button to be pressed
        //
        //////////////////////////////////////////////////////////////////////
        telemetry.addLine("Press Start to begin");
        telemetry.update();
        waitForStart();
        gametime.reset();
        runtime.reset();

        RobotLog.ii("DbgLog", "Start pressed (%f)", gametime.milliseconds()/1000);

        // Start the logging of measured acceleration from imu if needed
//        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);


        /////
        //TESTING
        /////
//        driveDistance(-4);
//        driveDistance(4);
//        turnByDegreesIMU(90, 0.4);
//        rotate(-90, 0.4);
//        rotate(-90, 1);
//        rotate(90, 1);
//        driveDistance(12);
//        sleep(1000);
//        driveDistance(-12);
//        sleep(1000);

        // Use this to stop the opmode early
//        requestOpModeStop();

        /////
        //
        // Drop from lander
        //
        /////

         telemetry.addLine("Ready to drop from lander");
         telemetry.update();
         RobotLog.ii("DbgLog", "Dropping from lander(%f)", gametime.milliseconds()/1000);

        // TODO: Uncomment next line for final version
        dropFromLander();
        RobotLog.ii("DbgLog", "Drop from lander complete(%f)", gametime.milliseconds()/1000);


        /////
        //
        // Turn to disengage from hook
        //
        /////

        // Original version
        // TODO: Uncomment next three lines for final version
//        drive(1,-1);
//        sleep(100);
//        driveStop();

        // Rotate to disengage
        RobotLog.ii("DbgLog", "Disengage from hook(%f)", gametime.milliseconds()/1000);
        rotate(-10, 0.6);
        RobotLog.ii("DbgLog", "Disengage from hook done(%f)", gametime.milliseconds()/1000);

        /////
        //
        // Retract the arm
        //
        /////
        // TODO: uncomment and test
        RobotLog.ii("DbgLog", "Retracting arm(%f)", gametime.milliseconds()/1000);
        retractArm();
        RobotLog.ii("DbgLog", "Retracting arm done(%f)", gametime.milliseconds()/1000);

        RobotLog.ii("DbgLog", "Turn toward minerals (%f)", gametime.milliseconds()/1000);
        rotate(10, 0.6);
        RobotLog.ii("DbgLog", "Turn toward minerals done (%f)", gametime.milliseconds()/1000);


        /////
        //
        // Determine position of the gold mineral using tensor flow
        //
        /////

        RobotLog.ii("DbgLog", "Scanning minerals(%f)", gametime.milliseconds()/1000);

        /////
        //
        // Activate Tensor Flow Object Detection
        //
        /////
        activateTensorFlowDetection();


        turnTurnHighbeamsOn();  // Turn on android camera flashlight for visibility

        if (tfod != null) {
            List<Recognition> updatedRecognitions;

            ElapsedTime tfTimer = new ElapsedTime();
            double tfTimeout = 5.0; // Timeout to give up on try to detect minerals

            tfTimer.reset();

            while (opModeIsActive() && (!goldMineralFound) && (tfTimer.seconds() < tfTimeout)) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    for (Recognition recognition : updatedRecognitions) {
                        if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                            goldMineralFound = true;
                            goldAngle = recognition.estimateAngleToObject(AngleUnit.DEGREES);
                            RobotLog.ii("DbgLog", "Gold mineral angle: %f (%f)", goldAngle, gametime.milliseconds()/1000);

                            telemetry.addData("DbgLog", "Gold mineral angle: %f (%f)", goldAngle, gametime.milliseconds()/1000);
                            telemetry.update();
                            wait(1000);

                            break;
                        }
                    }
                    telemetry.update();
                }
            }
        }
        RobotLog.ii("DbgLog", "Scanning minerals done (%f)", gametime.milliseconds()/1000);

        turnTurnHighbeamsOff();

        tfod.deactivate();

        /////
        //
        // Turn robot towards the gold mineral and bump it
        //
        /////
        if (goldMineralFound) {
            RobotLog.ii("DbgLog", "Turning to gold mineral(%f)", gametime.milliseconds() / 1000);

            rotate(-goldAngle, 0.6);

            /////
            //
            // Ram the mineral
            //
            /////
            RobotLog.ii("DbgLog", "Pushing mineral(%f)", gametime.milliseconds() / 1000);
            driveDistance(rammingDistance);

            // Back up to starting position after ramming the mineral
            driveDistance(-rammingDistance);

            // Rotate back to face minerals
            rotate(goldAngle, 0.6);

        }
        else {
            RobotLog.ii("DbgLog", "Cannot push gold mineral - not found(%f)", gametime.milliseconds()/1000);
        }


        // Turn to face wall
        RobotLog.ii("DbdLog", "Turn to face wall(%f)", gametime.milliseconds()/1000);
        rotate(95, 0.6);

        // Drive straight to wall
        RobotLog.ii("DbdLog", "Driving to wall(%f)", gametime.milliseconds()/1000);
        driveDistance(48);

        // Turn 90 degrees to face depot
        RobotLog.ii("DbdLog", "Turn to face depot(%f)", gametime.milliseconds()/1000);
        rotate(-90, 0.6);

        // Drive to depot
        RobotLog.ii("DbdLog", "Drive to depot(%f)", gametime.milliseconds()/1000);
        driveDistance(44);

        // Turn a bit to make sure marker stays in depot
        RobotLog.ii("DbdLog", "Turn to face depot(%f)", gametime.milliseconds()/1000);
        rotate(30, 0.6);

        RobotLog.ii("DbdLog", "Drop our marker(%f)", gametime.milliseconds()/1000);
        dropMarker();

        RobotLog.ii("DbdLog", "Reset marker dropper(%f)", gametime.milliseconds()/1000);
        resetMarkerServo();


        /////
        //
        // DONE!!!
        //
        /////
        RobotLog.ii("DbgLog", "Autonomous complete(%f)", gametime.milliseconds()/1000);
        allMotorStop();
    }

    /////////////////////////////////////////////////////////////////////////

    /////
    //
    // Activate Tensor Flow Object Detection.
    // Start it early so it is executing in background
    //
    /////
    private void activateTensorFlowDetection() {

        if (tfod != null) {
            telemetry.addLine("Activating tensor flow");
            tfod.activate();
        }
        else {
            telemetry.addLine("Could not activate tensor flow!");
        }
        telemetry.update();
    }


    /////
    //
    // Initialize Tensor Flow
    //
    /////
    private void initTensorFlow() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        telemetry.addLine("initializing tensor flow");
        telemetry.update();
        // Check to see if we can initialize tensor flow
        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
            tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
            tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
            telemetry.addLine("tensor flow ready");
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }
        telemetry.update();
    }

    /////
    //
    // initVuforia
    //
    // Initialize the Vuforia localization engine.
    //
    /////
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         * We can pass Vuforia the handle to a camera preview resource (on the RC phone);
         * If no camera monitor is desired, use the parameterless constructor instead (commented out below).
         */
        cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        vu_parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        // Fill the camera monitor view
        vu_parameters.fillCameraMonitorViewParent = true;

        vu_parameters.vuforiaLicenseKey = VUFORIA_KEY;
        vu_parameters.cameraDirection = CAMERA_CHOICE;

        // enable extended tracking even when an image is not visible.
        vu_parameters.useExtendedTracking = true;

        // Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(vu_parameters);
    }



    /////
    //
    // driveDistance
    //
    // Drive a specific distance in inches
    //
    /////
    private void driveDistance(double dist_in_inches) {
        double ticks = robot.ticksToMove(dist_in_inches);
        double drivePower = 0.8;
        double lowPower = 0.25; // low speed preparing to stop
        double lowPowerDistance = 4.0; // Go slower when close to target
        double lowPowerTicks = robot.ticksToMove(lowPowerDistance);

        if (dist_in_inches == 0) {
            return;
        }

        robot.rightDriveFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightDriveBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.leftDriveFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.leftDriveBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot.rightDriveFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightDriveFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightDriveFront.setTargetPosition((int) ticks);

        if (dist_in_inches < 0) {
            drivePower = -drivePower;
        }

        robot.rightDriveFront.setPower(drivePower);
        robot.leftDriveFront.setPower(drivePower);
        robot.rightDriveBack.setPower(drivePower);
        robot.leftDriveBack.setPower(drivePower);

        while (opModeIsActive() && robot.rightDriveFront.isBusy()) {
            if (Math.abs(ticks - robot.rightDriveFront.getCurrentPosition())  < Math.abs(lowPowerTicks)) {
                if (drivePower < 0) {
                    drivePower = -lowPower;
                }
                else {
                    drivePower = lowPower;
                }
                robot.rightDriveFront.setPower(drivePower);
                robot.leftDriveFront.setPower(drivePower);
                robot.rightDriveBack.setPower(drivePower);
                robot.leftDriveBack.setPower(drivePower);
            }
        }
        driveStop();
    }


    /////
    //
    // Initialize the IMU
    //
    /////
    private void initIMU() {
        // Set up the parameters with which we will use our IMU. Note that integration
        // algorithm here just reports accelerations to the logcat log; it doesn't actually
        // provide positional information.
        imu_parameters = new BNO055IMU.Parameters();
//        imu_parameters.mode = BNO055IMU.SensorMode.IMU;
        imu_parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu_parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imu_parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        imu_parameters.loggingEnabled = true;
        imu_parameters.loggingTag = "IMU";
        imu_parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(imu_parameters);

        // Make sure the imu was initialized before we begin moving
        while (!this.isStopRequested() && !imu.isGyroCalibrated()) {
            // if we don't put in idle, the opmode might crash due to watchdog
            sleep(10);
            idle();
        }
    }


    /////
    //
    // Reset angle (IMU)
    //
    /////
    private void resetAngle() {
        lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        globalAngle = 0;
    }

    private double getAngle() {
        Orientation localAngle = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double deltaAngle = localAngle.firstAngle - lastAngles.firstAngle;

        if (deltaAngle < -180) {
            deltaAngle += 360;
        }
        else if (deltaAngle > 180) {
            deltaAngle -= 360;
        }

        globalAngle += deltaAngle;

        lastAngles = localAngle;

        return globalAngle;
    }

    private void rotate(double degrees, double speed) {
        double minSpeed = 0.3; // slowest speed we can use to make accurate turns
        double midSpeed = 0.5;
        double maxSpeed = 0.8;
        double tmpSpeed = speed;

        // reset imu movement tracking
        resetAngle();

        if (Math.abs(degrees) < 30) {
            tmpSpeed = minSpeed;
        }
        else if (Math.abs(degrees) < 60) {
            tmpSpeed = Range.clip(tmpSpeed, minSpeed, midSpeed);
        }
        else {
            tmpSpeed = Range.clip(tmpSpeed, minSpeed, maxSpeed);
        }

        if (degrees < 0) {
            // turn to right
            drive(-tmpSpeed, tmpSpeed);
        }
        else if (degrees > 0) {
            // turn to left
            drive(tmpSpeed, -tmpSpeed);
        }
        else {
            return;
        }

        // keep going until done
        boolean angleReached = false;
        double currentAngle;
        double remainingAngle;
        if (degrees < 0) {
            while (opModeIsActive() && getAngle() == 0) {
                sleep(1);
            }
            while (opModeIsActive() && ! angleReached) {
                currentAngle = getAngle();
                remainingAngle = Math.abs(degrees - currentAngle);
                if (currentAngle <= degrees) {
                    angleReached = true;
                    break;
                } else if (remainingAngle < 30) {
                    RobotLog.ii("rotate()", "ang(%f), speed(%f)", currentAngle, tmpSpeed);
                    tmpSpeed = minSpeed;
                } else if (remainingAngle < 60) {
                    RobotLog.ii("rotate()", "ang(%f), speed(%f)", currentAngle, tmpSpeed);
                    tmpSpeed = Range.clip(tmpSpeed, minSpeed, midSpeed);
                }
                drive(-tmpSpeed, tmpSpeed);
            }
        }
        else {
            while (opModeIsActive() && ! angleReached) {
                currentAngle = getAngle();
                remainingAngle = degrees - currentAngle;
                if (currentAngle >= degrees) {
                    angleReached = true;
                    break;
                } else if (remainingAngle < 30){
                    RobotLog.ii("rotate()", "ang(%f), speed(%f)", currentAngle, tmpSpeed);
                    tmpSpeed = minSpeed;
                } else if (remainingAngle < 60) {
                    RobotLog.ii("rotate()", "ang(%f), speed(%f)", currentAngle, tmpSpeed);
                    tmpSpeed = Range.clip(tmpSpeed, minSpeed, midSpeed);
                }
                drive(tmpSpeed, -tmpSpeed);
            }
        }
        driveStop();

        // let motors stop fully
        sleep(1000);

        resetAngle();
    }


    /////
    //
    // Drop for the lander by extending the arm
    //
    /////
    private void dropFromLander() {
        // TODO: this needs to be updated for the new lift
        // Turn to disengage from the lander

        double timeout = 8;

        // Drop down from hanging position
        robot.arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.arm.setPower(0.8);
        robot.arm.setTargetPosition(2160);
        runtime.reset();

        // Wait for motor to stop moving or we timeout
        while (opModeIsActive() && robot.arm.isBusy() && runtime.seconds() < timeout) {
            sleep(10);
            idle();
        }
        robot.arm.setPower(0);
    }

    private void retractArm() {
        double timeout = 8;
        robot.arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.arm.setTargetPosition (-2160);
        robot.arm.setPower(0.8);
        runtime.reset();

        // Wait for the motor to stop moving or timeout
        while (opModeIsActive() && robot.arm.isBusy() && runtime.seconds() < timeout) {
            sleep(10);
            idle();
        }
        robot.arm.setPower(0);
    }




    /////
    //
    // Use the servo to drop the marker
    //
    /////
    private void dropMarker() {
        robot.markerDropper.setPosition(0);
    }

    /////
    //
    // Reset the marker servo back to its original position
    //
    /////
    private void resetMarkerServo() {
        robot.markerDropper.setPosition(1);
    }

    /////
    //
    // Stop all drive motors
    //
    /////
    private void driveStop () {
        robot.leftDriveFront.setPower(0);
        robot.rightDriveFront.setPower(0);
        robot.leftDriveBack.setPower(0);
        robot.rightDriveBack.setPower(0);
    }

    /////
    //
    // Set left, right power to drive motors
    //
    /////
    private void drive(double leftPower, double rightPower) {
        robot.leftDriveFront.setPower(leftPower);
        robot.leftDriveBack.setPower(leftPower);
        robot.rightDriveFront.setPower(rightPower);
        robot.rightDriveBack.setPower(rightPower);
    }

    /////
    //
    // Stop all motors configured on the robot (drive and arm)
    //
    /////
    private void allMotorStop() {
        driveStop();
        robot.arm.setPower(0);
    }

    /////
    //
    // Turn on the android phone camera flashlight mode
    //
    /////
    private void turnTurnHighbeamsOn() {
        CameraDevice.getInstance().setFlashTorchMode(true);
    }

    /////
    //
    // Turn off flashlight mode for internal android phone camera
    //
    /////
    private void turnTurnHighbeamsOff() {
        CameraDevice.getInstance().setFlashTorchMode(false);
    }
}
