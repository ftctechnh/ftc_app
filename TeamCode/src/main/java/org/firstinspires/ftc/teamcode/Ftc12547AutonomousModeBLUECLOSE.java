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


import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;


import static org.firstinspires.ftc.teamcode.Ftc12547Config.*;
import org.firstinspires.ftc.teamcode.JewelDestroyer;

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

@Autonomous(name="Blue Close to Audience", group ="Autonomous")
public class Ftc12547AutonomousModeBLUECLOSE extends LinearOpMode {
    /**
     * Team 12547 robot variables
     */

    /* Declare OpMode members. */
    private HardwarePushbot robot   = new HardwarePushbot();   // Use a Pushbot's hardware

    private EncoderDriver encoderDriver = new EncoderDriver(this, robot, telemetry);
    private JewelDestroyer jewelDestroyer = new JewelDestroyer(this, robot, encoderDriver, telemetry);
    private VuMarkReader vuMarkreader = new VuMarkReader(this, telemetry);

    @Override
    public void runOpMode() {
        // Initialize the robot
        initRobot();

        // Initialize claw
        robot.leftClaw.setPosition(0.2);
        robot.rightClaw.setPosition(0.6);

        // Wait for the game to start (driver presses PLAY)
        telemetry.addData(">", "Press Play to start");
        telemetry.update();
        waitForStart();
        // step (-1)
        RelicRecoveryVuMark vuMark = vuMarkreader.vuMarkIdentificationTask();

        // Todo: debugging, keep all telemetry. This could be removed in competation.
        telemetry.setAutoClear(false);

        telemetry.addData("vuMark:", vuMark);
        telemetry.update();

        // (0) Calculate the distance based on the vuMark cypher.
        double rack_angle = vuMarkreader.calcFinalDistanceByVuMark(vuMark);

        jewelDestroyer.jewelDestroy();

        // (5) Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)
        // Note: IT IS DIFFERENT DEPENDING ON THE FOUR STARTING POSITIONS

        if (TEAM_COLOR == Color.BLUE) {
            if (TOWARDS_AUDIENCE == true) {
                encoderDriver.encoderDrive(ENCODER_RUN_SPEED, EIGHTEEN_INCHES, EIGHTEEN_INCHES, DESTINATION_TIMEOUT_SECONDS);
                encoderDriver.encoderDrive(ENCODER_RUN_SPEED, -HALF_FOOT, -HALF_FOOT, DESTINATION_TIMEOUT_SECONDS);
                encoderDriver.encoderDrive(ENCODER_RUN_SPEED, THREE_INCHES, THREE_INCHES, DESTINATION_TIMEOUT_SECONDS);
                telemetry.addData("Moving to the final destination", rack_angle);
                encoderDriver.encoderDrive(DESTINATION_TURN_SPEED,
                        -rack_angle,
                        rack_angle,
                        DESTINATION_TIMEOUT_SECONDS);

                encoderDriver.stopMotorsAndRestShortly();

                // (7) push the block into rack
                encoderDriver.encoderDrive(ENCODER_RUN_SPEED,
                        DISTANCE_TO_RACK_WITH_EXTRA_INSURANCE,
                        DISTANCE_TO_RACK_WITH_EXTRA_INSURANCE,
                        TO_RACK_TIMEOUT_SECONDS);
            }else{
                encoderDriver.encoderDrive(ENCODER_RUN_SPEED, EIGHTEEN_INCHES, EIGHTEEN_INCHES, DESTINATION_TIMEOUT_SECONDS);
                encoderDriver.encoderDrive(ENCODER_RUN_SPEED, -HALF_FOOT, -HALF_FOOT, DESTINATION_TIMEOUT_SECONDS);
                encoderDriver.encoderDrive(ENCODER_RUN_SPEED, THREE_INCHES, THREE_INCHES, DESTINATION_TIMEOUT_SECONDS);
                telemetry.addData("Moving to the final destination", rack_angle);
                encoderDriver.encoderDrive(DESTINATION_TURN_SPEED,
                        rack_angle,
                        -rack_angle,
                        DESTINATION_TIMEOUT_SECONDS);

                encoderDriver.stopMotorsAndRestShortly();

                // (7) push the block into rack
                encoderDriver.encoderDrive(ENCODER_RUN_SPEED,
                        DISTANCE_TO_RACK_WITH_EXTRA_INSURANCE,
                        DISTANCE_TO_RACK_WITH_EXTRA_INSURANCE,
                        TO_RACK_TIMEOUT_SECONDS);
            }
        }else{
            if (TOWARDS_AUDIENCE == true) {
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
            }else{
                encoderDriver.encoderDrive(ENCODER_RUN_SPEED, -EIGHTEEN_INCHES, -EIGHTEEN_INCHES, DESTINATION_TIMEOUT_SECONDS);
                encoderDriver.encoderDrive(ENCODER_RUN_SPEED, HALF_FOOT, HALF_FOOT, DESTINATION_TIMEOUT_SECONDS);
                encoderDriver.encoderDrive(ENCODER_RUN_SPEED, -THREE_INCHES, -THREE_INCHES, DESTINATION_TIMEOUT_SECONDS);

                telemetry.addData("Moving to the final destination", rack_angle);
                encoderDriver.encoderTurn(DESTINATION_TURN_SPEED,
                        rack_angle,
                        -rack_angle,
                        DESTINATION_TURN_TIMEOUT_SECONDS);

                encoderDriver.stopMotorsAndRestShortly();

                // (7) push the block into rack
                encoderDriver.encoderDrive(ENCODER_RUN_SPEED,
                        DISTANCE_TO_RACK_WITH_EXTRA_INSURANCE,
                        DISTANCE_TO_RACK_WITH_EXTRA_INSURANCE,
                        TO_RACK_TIMEOUT_SECONDS);
            }
        }
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
}
