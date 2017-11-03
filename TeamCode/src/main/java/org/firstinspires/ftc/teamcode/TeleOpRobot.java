package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CompassSensor;

@TeleOp()
public class TeleOpRobot extends Robot
{
    boolean calibrationMode;
    boolean gripperPivotMode;

    boolean previousLeftBumper;
    boolean previousRightBumper;
    boolean previousAButton;
    boolean previousBButton;
    boolean previousXButton;
    boolean previousYButton;

    @Override
    public void loop()
    {
        // Set calibration mode using the x button
        if (gamepad1.a && !previousAButton) calibrationMode = !calibrationMode;

        if (calibrationMode) {
            // Turn on blue LED, which indicates calibration mode
            dim.setLED(0, true);

            // Calibrate compass when A button is down
            if (gamepad1.a) {
                // Calibrate compass
                targetCompassMode = CompassSensor.CompassMode.CALIBRATION_MODE;

                // Turn off red LED, which indicates calibration has previously failed
                dim.setLED(1, false);

                // Spin clockwise (rotation will not be updated during calibration mode)
                targetRotation = rotation + 5.0;
            }
            // Stop calibrating compass and set new rotation origin to behind the robot when A button is released
            else if (previousAButton) {
                // Compass calibration complete
                targetCompassMode = CompassSensor.CompassMode.MEASUREMENT_MODE;

                // Turn on red LED if calibration failed
                if (compass.calibrationFailed()) dim.setLED(1, false);

                // Set rotationOrigin to behind the robot
                rotationOrigin = compass.getDirection() + 180.0;

                // Reset rotation
                targetRotation = 180.0;
                rotation = 180.0;
            }

            // Adjust rotationOrigin when b button is down
            if (gamepad1.b) {
                // Set target rotation using triggers
                targetRotation -= gamepad1.left_trigger * deltaTime * 360.0;
                targetRotation += gamepad1.right_trigger * deltaTime * 360.0;
            }
            else if (previousBButton) {
                // Set rotationOrigin to behind the robot
                rotationOrigin = compass.getDirection() + 180.0;

                // Reset rotation
                targetRotation = 180.0;
                rotation = 180.0;
            }

            // Adjust arm rotation when y button is down
            if (gamepad1.y) {
                // Set targetArmRotation using right stick y while keeping armRotation in stored rotation
                targetArmRotation += (START_STORED_ARM_ROTATION - armRotation) + (gamepad1.right_stick_y * deltaTime * 30.0);
                armRotation = START_STORED_ARM_ROTATION;
            }
            // Reset arm rotation to stored rotation when b button is released
            else if (previousYButton) {
                targetArmRotation = START_STORED_ARM_ROTATION;
                armRotation = START_STORED_ARM_ROTATION;
            }
        }
        else {
            // Turn off blue LED, which indicates calibration mode
            dim.setLED(0, false);

            // Set target position using left stick
            targetXPosition += gamepad1.left_stick_x * deltaTime * 12.0;
            targetYPosition -= gamepad1.left_stick_y * deltaTime * 12.0;

            // Get gripper target position
            double gripperTargetXPosition = GetGripperTargetXPosition();
            double gripperTargetYPosition = GetGripperTargetYPosition();

            // Set target rotation using dpad or triggers
            if (gamepad1.dpad_up) targetRotation = 0.0;
            else if (gamepad1.dpad_right) targetRotation = 0.90;
            else if (gamepad1.dpad_down) targetRotation = 0.180;
            else if (gamepad1.dpad_left) targetRotation = 0.270;
            else {
                targetRotation -= gamepad1.left_trigger * deltaTime * 360.0;
                targetRotation += gamepad1.right_trigger * deltaTime * 360.0;
            }

            // Set gripper pivot mode using the b button
            if (gamepad1.b && !previousBButton) gripperPivotMode = !gripperPivotMode;

            if (gripperPivotMode) {
                // Set gripper position back to original position if arm is rotated to pick up blocks
                SetGripperTargetXPosition(gripperTargetXPosition);
                SetGripperTargetYPosition(gripperTargetYPosition);
            }

            // Update gripper target position
            gripperTargetXPosition = GetGripperTargetXPosition();
            gripperTargetYPosition = GetGripperTargetYPosition();

            // Set target arm rotation using left and right bumpers
            if (gamepad1.left_bumper && !previousLeftBumper) {
                if (targetArmRotation > RELIC_ARM_ROTATION) targetArmRotation = RELIC_ARM_ROTATION;
                else if (targetArmRotation > RELIC_WALL_CLEARANCE_ARM_ROTATION)
                    targetArmRotation = RELIC_WALL_CLEARANCE_ARM_ROTATION;
                else if (targetArmRotation > BLOCK_4_ARM_ROTATION)
                    targetArmRotation = BLOCK_4_ARM_ROTATION;
                else if (targetArmRotation > BLOCK_3_ARM_ROTATION)
                    targetArmRotation = BLOCK_3_ARM_ROTATION;
                else if (targetArmRotation > BLOCK_2_ARM_ROTATION)
                    targetArmRotation = BLOCK_2_ARM_ROTATION;
                else if (targetArmRotation > BLOCK_1_ARM_ROTATION)
                    targetArmRotation = BLOCK_1_ARM_ROTATION;
            } else if (gamepad1.right_bumper && !previousRightBumper) {
                if (targetArmRotation < BLOCK_1_ARM_ROTATION)
                    targetArmRotation = BLOCK_1_ARM_ROTATION;
                else if (targetArmRotation < BLOCK_2_ARM_ROTATION)
                    targetArmRotation = BLOCK_2_ARM_ROTATION;
                else if (targetArmRotation < BLOCK_3_ARM_ROTATION)
                    targetArmRotation = BLOCK_3_ARM_ROTATION;
                else if (targetArmRotation < BLOCK_4_ARM_ROTATION)
                    targetArmRotation = BLOCK_4_ARM_ROTATION;
                else if (targetArmRotation < RELIC_WALL_CLEARANCE_ARM_ROTATION)
                    targetArmRotation = RELIC_WALL_CLEARANCE_ARM_ROTATION;
                else if (targetArmRotation < RELIC_ARM_ROTATION)
                    targetArmRotation = RELIC_ARM_ROTATION;
                else targetArmRotation = FLIP_ARM_ROTATION;
            }

            // Set target arm rotation using right stick y
            if (gamepad1.right_stick_y < -0.1)
                targetArmRotation += (gamepad1.right_stick_y + 0.1) * deltaTime * 60.0;
            else if (gamepad1.right_stick_y > 0.1)
                targetArmRotation += (gamepad1.right_stick_y - 0.1) * deltaTime * 60.0;

            // Set gripper position back to original position if arm is rotated to pick up blocks
            SetGripperTargetXPosition(gripperTargetXPosition);
            SetGripperTargetYPosition(gripperTargetYPosition);

            // Set gripper rotation using right stick x
            gripperRotation += gamepad1.right_stick_x * deltaTime * 90.0;
        }

        previousLeftBumper = gamepad1.left_bumper;
        previousRightBumper = gamepad1.right_bumper;
        previousAButton = gamepad1.a;
        previousBButton = gamepad1.b;
        previousXButton = gamepad1.x;
        previousYButton = gamepad1.y;

        super.loop();
    }
}