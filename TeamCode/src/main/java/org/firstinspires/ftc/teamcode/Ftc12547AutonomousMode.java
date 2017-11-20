/* Copyright (c) 2017 FIRST. All rights reserved.
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
package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.firstinspires.ftc.teamcode.Ftc12547Config.*;

/**
 * This OpMode illustrates the basics of using the Vuforia engine to determine
 * the identity of Vuforia VuMarks encountered on the field. The code is structured as
 * a LinearOpMode. It shares much structure with {@link ConceptVuforiaNavigation}; we do not here
 * duplicate the core Vuforia documentation found there, but rather instead focus on the
 * differences between the use of Vuforia for navigation vs VuMark identification.
 *
 * @see ConceptVuforiaNavigation
 * @see VuforiaLocalizer
 * @see VuforiaTrackableDefaultListener
 * see  ftc_app/doc/tutorial/FTC_FieldCoordinateSystemDefinition.pdf
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained in {@link ConceptVuforiaNavigation}.
 */

@Autonomous(name="Team12547: Full Task", group ="Autonomous")
public class Ftc12547AutonomousMode extends LinearOpMode {

    /**
     * Team 12547 VuMark variables
     */
    private OpenGLMatrix lastLocation = null;
    private VuforiaTrackables relicTrackables;
    private VuforiaTrackable relicTemplate;

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * Team 12547 robot variables
     */

    /* Declare OpMode members. */
    private HardwarePushbot robot   = new HardwarePushbot();   // Use a Pushbot's hardware

    private EncoderDriver encoderDriver = new EncoderDriver(this, robot, telemetry);

    private ColorSensor sensorColor;
    private DistanceSensor sensorDistance;
    private float hsvValues[] = {0F, 0F, 0F};


    @Override
    public void runOpMode() {
        // Initialize the robot
        initRobot();

        // Wait for the game to start (driver presses PLAY)
        telemetry.addData(">", "Press Play to start");
        telemetry.update();
        waitForStart();

        RelicRecoveryVuMark vuMark = vuMarkIdentificationTask();
        telemetry.addData("vuMark:", vuMark);
        // telemetry.update();

        double final_distance = calcFinalDistanceByVuMark(vuMark);

        robot.leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Lower the servo that control the arm to move the JewelMovingArm
        lowerJewelMovingArmServo();
        sleep(ONE_SECOND_IN_MIL);

        Color.RGBToHSV((int) (sensorColor.red() * SCALE_FACTOR),
                (int) (sensorColor.green() * SCALE_FACTOR),
                (int) (sensorColor.blue() * SCALE_FACTOR),
                hsvValues);

        telemetry.addData("Blue ", sensorColor.blue());
        telemetry.addData("Red ", sensorColor.red());
        // telemetry.update();
        sleep(ONE_SECOND_IN_MIL);

        int jewelColor = (sensorColor.red() > COLOR_THRESHOLD) ? Color.RED : Color.BLUE;


        // Disposition jewel of other color than team color.
        if (jewelColor == TEAM_COLOR){
            /**
             * Move forward, because
             * (1) Color sensor of team 12547 is mounted facing backward.
             * (2) Team color jewel is on the back side in this condition.
             */
            MoveForwardForJewelDisposition();

            // Raise the servo that control the arm to move the JewelMovingArm
            raiseJewelMovingArmServo();
            sleep(ONE_SECOND_IN_MIL);

            MoveBackwardForJewelDisposition();

        } else { // Move backward
            MoveBackwardForJewelDisposition();

            // Raise the servo that control the arm to move the JewelMovingArm
            raiseJewelMovingArmServo();
            sleep(ONE_SECOND_IN_MIL);

            MoveForwardForJewelDisposition();
        }

        // Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)

        telemetry.addData("Moving to the final destination", final_distance);
        encoderDriver.encoderDrive(AUTONOMOUS_DRIVE_SPEED,  final_distance,  final_distance, 5.0);

        switch (vuMark) {
            case LEFT: encoderDriver.encoderDrive(AUTONOMOUS_DRIVE_SPEED, 1, 1, 3);
                break;
            case CENTER: encoderDriver.encoderDrive(AUTONOMOUS_DRIVE_SPEED, 1.875, 1.875, 5);
                break;
            case RIGHT: encoderDriver.encoderDrive(AUTONOMOUS_DRIVE_SPEED, 4.75, 4.75, 10);
                break;
            case UNKNOWN: encoderDriver.encoderDrive(AUTONOMOUS_DRIVE_SPEED, 1.875, 1.875, 5);
                break;
        }

        robot.leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        encoderDriver.encoderDrive(TURN_SPEED, -2.94/2, 2.94/2, 3);
        encoderDriver.encoderDrive(AUTONOMOUS_DRIVE_SPEED, 3, 3, 4);

        telemetry.addData("Path", "Complete");
        telemetry.update();
    }

    private double calcFinalDistanceByVuMark(RelicRecoveryVuMark vuMark) {
        //TODO: default to middle. Is this ideal?
        if (vuMark == null) return START_TO_MIDDLE_DISTANCE_INCHES;
        switch (vuMark) {
            case CENTER:
                return START_TO_MIDDLE_DISTANCE_INCHES;
            //TODO: THis may need flip
            case LEFT:
                return START_TO_NEAREST_DISTANCE_INCHES;
            case RIGHT:
                return START_TO_FARREST_DISTANCE_INCHES;
            default:
                return START_TO_MIDDLE_DISTANCE_INCHES;
        }
    }


    /**
     * Move forward or backfard in order to disposition jewel or move back after
     * disposition the jewel.
     */
    private void MoveBackwardForJewelDisposition() {
        encoderDriver.encoderDrive(AUTONOMOUS_DRIVE_SPEED,
                -JEWEL_DISPOSITION_DISTANCE,
                -JEWEL_DISPOSITION_DISTANCE,
                JEWEL_DISPOSITION_TIMEOUT);
    }

    private void MoveForwardForJewelDisposition() {
        encoderDriver.encoderDrive(AUTONOMOUS_DRIVE_SPEED,
                JEWEL_DISPOSITION_DISTANCE,
                JEWEL_DISPOSITION_DISTANCE,
                JEWEL_DISPOSITION_TIMEOUT);
    }

    /**
     * Lower or raise arm servo that dispositions the jewel.
     */
    private void lowerJewelMovingArmServo() {
        for(double d = JEWEL_ARM_VERTICAL_SERVO_POSITION; d > JEWEL_ARM_HORIZONTAL_SERVO_POSITION;
            d-=JEWEL_ARM_SERVO_MOVING_STEP_CHANGE){
            robot.jewelAnnihilator.setPosition(d);
            sleep(SLEEP_INTERVAL_BETWEEN_SERVO_MOVES_MS);
        }
    }

    private void raiseJewelMovingArmServo() {
        for(double d = JEWEL_ARM_HORIZONTAL_SERVO_POSITION; d < JEWEL_ARM_VERTICAL_SERVO_POSITION;
            d+=JEWEL_ARM_SERVO_MOVING_STEP_CHANGE){
            robot.jewelAnnihilator.setPosition(d);
            sleep(SLEEP_INTERVAL_BETWEEN_SERVO_MOVES_MS);
        }
    }

    /**
     * VuMark recognition task
     * @return RelicRecoveryVuMark result
     */
    private RelicRecoveryVuMark vuMarkIdentificationTask() {

        Date startVuMark = new Date();

        Map VuMarkRecognizedTimes = new HashMap<RelicRecoveryVuMark, Integer>();

        relicTrackables.activate();
        while (opModeIsActive()) {

            Date current = new Date();
            if (current.getTime() - startVuMark.getTime() > FIVE_SECONDS_IN_MIL) {
                telemetry.addData("VuMark time out at ", current);
                telemetry.update();
                return RelicRecoveryVuMark.UNKNOWN;
            }

            /**
             * See if any of the instances of {@link relicTemplate} are currently visible.
             * {@link RelicRecoveryVuMark} is an enum which can have the following values:
             * UNKNOWN, LEFT, CENTER, and RIGHT. When a VuMark is visible, something other than
             * UNKNOWN will be returned by {@link RelicRecoveryVuMark#from(VuforiaTrackable)}.
             */
            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
            if (vuMark != RelicRecoveryVuMark.UNKNOWN) {

                /* Found an instance of the template. In the actual game, you will probably
                 * loop until this condition occurs, then move on to act accordingly depending
                 * on which VuMark was visible. */
                telemetry.addData("VuMark", "%s visible", vuMark);

                /* For fun, we also exhibit the navigational pose. In the Relic Recovery game,
                 * it is perhaps unlikely that you will actually need to act on this pose information, but
                 * we illustrate it nevertheless, for completeness. */
                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener)relicTemplate.getListener()).getPose();
                telemetry.addData("Pose", format(pose));

                /* We further illustrate how to decompose the pose into useful rotational and
                 * translational components */
                if (pose != null) {
                    VectorF trans = pose.getTranslation();
                    Orientation rot = Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

                    // Extract the X, Y, and Z components of the offset of the target relative to the robot
                    double tX = trans.get(0);
                    double tY = trans.get(1);
                    double tZ = trans.get(2);

                    // Extract the rotational components of the target relative to the robot
                    double rX = rot.firstAngle;
                    double rY = rot.secondAngle;
                    double rZ = rot.thirdAngle;
                    telemetry.update();
                    return vuMark;
                }
            }
            else {
                telemetry.addData("VuMark", "not visible");
                telemetry.update();
            }
        }

        return RelicRecoveryVuMark.UNKNOWN;
    }

    private String format(OpenGLMatrix transformationMatrix) {
        return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
    }

    private void initRobot() {
        initVuMark();
        initSensorsAndMotors();
    }

    private void initSensorsAndMotors() {
        // get a reference to the color sensor.
        sensorColor = hardwareMap.get(ColorSensor.class, "sensor_color_distance");

        // get a reference to the distance sensor that shares the same name.
        sensorDistance = hardwareMap.get(DistanceSensor.class, "sensor_color_distance");

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        robot.leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d",
                robot.leftDrive.getCurrentPosition(),
                robot.rightDrive.getCurrentPosition());
        telemetry.update();

    }
    
    private void initVuMark() {
        /*
         * To start up Vuforia, tell it the view that we wish to use for camera monitor (on the RC phone);
         * If no camera monitor is desired, use the parameterless constructor instead (commented out below).
         */
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        // OR...  Do Not Activate the Camera Monitor View, to save power
        // VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        /*
         * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
         * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
         * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
         * web site at https://developer.vuforia.com/license-manager.
         *
         * Vuforia license keys are always 380 characters long, and look as if they contain mostly
         * random data. As an example, here is a example of a fragment of a valid key:
         *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
         * Once you've obtained a license key, copy the string from the Vuforia web site
         * and paste it in to your code onthe next line, between the double quotes.
         */
        parameters.vuforiaLicenseKey = VUFORIA_LICENSE_KEY;

        /*
         * We also indicate which camera on the RC that we wish to use.
         * Here we chose the back (HiRes) camera (for greater range), but
         * for a competition robot, the front camera might be more convenient.
         */
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        /**
         * Load the data set containing the VuMarks for Relic Recovery. There's only one trackable
         * in this data set: all three of the VuMarks in the game were created from this one template,
         * but differ in their instance id information.
         * @see VuMarkInstanceId
         */
        relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        telemetry.addData("VuMark identification", "Initialized");
        telemetry.update();
    }
}
