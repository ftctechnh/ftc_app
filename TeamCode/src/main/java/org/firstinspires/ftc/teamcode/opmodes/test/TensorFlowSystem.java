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

package org.firstinspires.ftc.teamcode.opmodes.test;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.systems.MecanumDriveSystem;
import org.firstinspires.ftc.teamcode.systems.tensorflow.TensorFlow;

import java.util.List;

/**
 * This 2018-2019 OpMode illustrates the basics of using the TensorFlow Object Detection API to
 * determine the position of the gold and silver minerals.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */



@Autonomous(name = "tensor_flow")
public class TensorFlowSystem extends LinearOpMode {
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    private static final String TAG = "TensorFlowTelemetry";

    private static final int SCREEN_WIDTH = 1280;
    private static final int SCREEN_CENTER = 1280 / 2;
    private static final int OFFSET = 20;

    private TensorFlow tensorFlow;
    private MecanumDriveSystem driveSystem;
    private boolean hasDriven;

    @Override
    public void runOpMode() {
        initializeOpMode();
        waitForStart();
        if (opModeIsActive()) {
            tensorFlow.activate();
            lookForGoldMineral();
        }
        tensorFlow.shutDown();
    }

    private void initializeOpMode() {
        driveSystem = new MecanumDriveSystem(this);
        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            tensorFlow = new TensorFlow(this);
            tensorFlow.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL);
            tensorFlow.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_SILVER_MINERAL);
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }
    }

    /**
     * Initialize the Vuforia localization engine.
     */

    private void lookForGoldMineral() {
        while (shouldLookForGoldMineral()) {
            List<Recognition> updatedRecognitions = tensorFlow.getUpdatedRecognitions();
            if (shouldHandleUpdatedRecognitions(updatedRecognitions)) {
                handleUpdatedRecognitions(updatedRecognitions);
            }
        }
    }

    private boolean shouldLookForGoldMineral() {
        return !hasDriven;
    }

    private boolean shouldHandleUpdatedRecognitions(List<Recognition> updatedRecognitions) {
        return updatedRecognitions.size() > 0;
    }

    private void handleUpdatedRecognitions(List<Recognition> updatedRecognitions) {
        int goldMineralX = -1;
        int silverMineral1X = -1;
        int silverMineral2X = -1;

        telemetry.addData("# Object Detected", updatedRecognitions.size());

        for (Recognition recognition : updatedRecognitions) {
            if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                goldMineralX = (int) recognition.getBottom();
            } else if (silverMineral1X == -1) {
                silverMineral1X = (int) recognition.getBottom();
            } else {
                silverMineral2X = (int) recognition.getBottom();
            }
        }

        if (hasFoundGoldMineral(goldMineralX)) {
            handleSilverMineralWhenFound(silverMineral1X, silverMineral2X);
        } else {
            handleGoldMineralWhenFound(goldMineralX);
        }
        telemetry.update();
    }

    private void handleSilverMineralWhenFound(int silverMineral1X, int silverMineral2X) {
        // make silver1 on left and silver2 on right
        if (silverMineral1X > silverMineral2X) {
            int temp = silverMineral1X;
            silverMineral1X = silverMineral2X;
            silverMineral2X = temp;
        }

        // find the gold block
        if (shouldStrafeRight(silverMineral2X)) {
            Log.i(TAG, "can't find gold -- strafing right");
        } else {
            Log.i(TAG, "can't find gold -- strafing left");
        }
    }

    private boolean shouldStrafeRight(int silverMineral2X) {
        return silverMineral2X < SCREEN_WIDTH * 2 / 3;
    }

    private void handleGoldMineralWhenFound(int goldMineralX) {
        if (goldMineralX < SCREEN_CENTER - OFFSET) {
            // strafe right to center gold
            Log.i(TAG, "strafing right to center gold");
            drive(0, -0.2, 75);
        } else if (goldMineralX > SCREEN_CENTER + OFFSET) {
            // strafe left to center gold
            Log.i(TAG, "strafing left to center gold");
            drive(0, 0.2, 75);
        } else {
            Log.i(TAG, "driving forward to hit gold -- gold seen");
            driveSystem.turn(-90, 0.5);
            drive(0, 0.5, 1500);
            hasDriven = true;
        }
    }

    private void drive(double x, double y, int miliseconds) {
        driveSystem.mecanumDriveXY(x, y);
        sleep(miliseconds);
        driveSystem.mecanumDriveXY(0, 0);
    }

    private boolean hasFoundGoldMineral(int goldMineralX) {
        return  goldMineralX != -1;
    }
}
