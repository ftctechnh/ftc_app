package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;


import static org.firstinspires.ftc.teamcode.Ftc12547Config.*;

/**
 * This OpMode illustrates the basics of using the Vuforia engine to determine
 * the identity of Vuforia VuMarks encountered on the field. The code is structured as
 * a LinearOpMode. It shares much structure with {ConceptVuforiaNavigation}; we do not here
 * duplicate the core Vuforia documentation found there, but rather instead focus on the
 * differences between the use of Vuforia for navigation vs VuMark identification.
 *
 * ConceptVuforiaNavigation
 * @see VuforiaLocalizer
 * @see VuforiaTrackableDefaultListener
 * see  ftc_app/doc/tutorial/FTC_FieldCoordinateSystemDefinition.pdf
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained in {ConceptVuforiaNavigation}.
 */

@Autonomous(name="Red Close to Audience", group ="Autonomous")
public class Ftc12547AutonomousModeREDCLOSE extends LinearOpMode {
    /**
     * Team 12547 robot variables
     */

    /* Declare OpMode members. */
    private HardwarePushbot robot = new HardwarePushbot();   // Use a Pushbot's hardware

    private EncoderDriver encoderDriver = new EncoderDriver(this, robot, telemetry);
    private JewelDestroyer jewelDestroyer = new JewelDestroyer(this, robot, encoderDriver, telemetry);
    private VuMarkReader vuMarkreader = new VuMarkReader(this, telemetry);

    @Override
    public void runOpMode() {
        // Initialize the robot
        initRobot();

        robot.leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        TEAM_COLOR = Color.RED;

        // Initialize claw
        robot.leftClaw.setPosition(0.2);
        robot.rightClaw.setPosition(0.6);

        // Wait for the game to start (driver presses PLAY)
        telemetry.addData(">", "Press Play to start");
        telemetry.update();
        waitForStart();
        // step (-1)
        RelicRecoveryVuMark vuMark = vuMarkreader.vuMarkIdentificationTask();
        double rack_angle = calcFinalDistanceByVuMark(vuMark);

        // Todo: debugging, keep all telemetry. This could be removed in competation.
        telemetry.setAutoClear(false);

        telemetry.addData("vuMark:", vuMark);
        telemetry.update();

        // (0) Calculate the distance based on the vuMark cypher.

        jewelDestroyer.jewelDestroy();

        // (5) Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)
        // Note: IT IS DIFFERENT DEPENDING ON THE FOUR STARTING POSITIONS

                encoderDriver.encoderDrive(ENCODER_RUN_SPEED, -EIGHTEEN_INCHES, -EIGHTEEN_INCHES, DESTINATION_TIMEOUT_SECONDS);
                encoderDriver.encoderDrive(ENCODER_RUN_SPEED, HALF_FOOT, HALF_FOOT, DESTINATION_TIMEOUT_SECONDS);
                encoderDriver.encoderDrive(ENCODER_RUN_SPEED, -THREE_INCHES, -THREE_INCHES, DESTINATION_TIMEOUT_SECONDS);

                telemetry.addData("Moving to the final destination", rack_angle);
                encoderDriver.encoderTurn(DESTINATION_TURN_SPEED,
                        -rack_angle,
                        rack_angle,
                        DESTINATION_TURN_TIMEOUT_SECONDS);

                encoderDriver.stopMotorsAndRestShortly();

                // (7) push the block into rack
                encoderDriver.encoderDrive(ENCODER_RUN_SPEED,
                        DISTANCE_TO_RACK_WITH_EXTRA_INSURANCE,
                        DISTANCE_TO_RACK_WITH_EXTRA_INSURANCE,
                        TO_RACK_TIMEOUT_SECONDS);
        sleep(250);
        robot.leftClaw.setPosition(0.8);
        robot.rightClaw.setPosition(0.2);
        sleep(250);
        encoderDriver.encoderDrive(ENCODER_RUN_SPEED, -4, -4, TO_RACK_TIMEOUT_SECONDS);
        telemetry.addData("Mission ", "Complete");
        telemetry.update();
    }

    private void initRobot() {
        vuMarkreader.init();
        jewelDestroyer.init();
        encoderDriver.init();
    }
    private double calcFinalDistanceByVuMark(RelicRecoveryVuMark vuMark) {
        switch (vuMark) {
            case CENTER:
                return START_TO_MIDDLE_ANGLE;
            case LEFT:
                return START_TO_NEAREST_ANGLE;
            case RIGHT:
                return START_TO_FURTHEST_ANGLE;
            default:
                return START_TO_MIDDLE_ANGLE;
        }
    }
}