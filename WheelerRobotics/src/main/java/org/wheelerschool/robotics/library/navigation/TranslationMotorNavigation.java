package org.wheelerschool.robotics.library.navigation;

import android.util.Log;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;

/**
 * A library to calculate the left and right motor speeds to correctly drive a specific translation.
 *
 * @author luciengaitskell
 * @version 1.0
 * @since 11/24/16
 */


public class TranslationMotorNavigation {
    // Configuration (with defaults):
    public int MAX_SPEED_CALCULATE_DISTANCE = 620; /* MAX distance that speed will be
        calculated (mm). If out of this range, speed will be the maximum */
    public int MIN_DRIVE_DISTANCE = 180; // MIN distance that motors will run (mm).
    public double DRIVE_SPEED_OFFSET = 0.08;
    public double MAX_DRIVE_SPEED = 0.25;
    public double DEFAULT_FORWARD_GAIN = 1.0; // GAIN for the FORWARD movement
    public double DEFAULT_ROTATION_GAIN = 2.5; // GAIN for the ROTATION amount
    public int ROTATION_IGNORE_DISTANCE = 300;
    public double IGNORED_ROTATION_GAIN = 0.5;
    // Other:
    public static String LOG_TAG = "Motor Nav Calculation";


    // A correct modulo function:
    private static double positiveModulo(double numb, double modulo) {
        double n = numb % modulo;
        if (n < 0) {
            n += modulo;
        }
        return n;
    }

    // Calculate the difference between two angles (rad):
    public static double angleDifference(double angle1, double angle2) {
        return positiveModulo(((angle1 - angle2) + Math.PI), (2 * Math.PI)) - Math.PI;
    }

    // Constructor (Creates target location):
    public TranslationMotorNavigation() {
    }

    public static VectorF calculateTranslation(VectorF robotLocation,  VectorF targetLocation) {
        return targetLocation.subtracted(robotLocation);
    }


    public class NavigationData {
        /**
         * A class containing data that is calculated by 'calculateNavigationData'.
         *
         * @see TranslationMotorNavigation#calculateNavigationData(double, double, double)
         */

        // Motor Side Powers:
        public double leftMotorPower;  // Left motor power
        public double rightMotorPower;  // Right motor power

        // Other helpful values:
        public boolean onTarget;

        // Debug values:
        public double translationAngle;
        public double translationDistance;
        public double rotationAmount;
        //      Speed calculation:
        public double forwardPower;
        public double forwardGain;
        public double rotationPower;
        public double rotationGain;
    }

    public NavigationData calculateNavigationData(double translationX, double translationY, double robotAngle) {
        /**
         * Calculate a left and right motor power for a specific translation and robot angle.
         */
        // Initiate object to hold data:
        NavigationData navigationData = new NavigationData();

        // Get the distance from robot to the target:
        double translationDistance = Math.sqrt((Math.pow(Math.abs(translationX), 2)
                + Math.pow(Math.abs(translationY), 2)));

        // Calculate angle to turn to for translation:
        double translationAngle = Math.atan2(translationY, translationX);

        // Calculate needed change in angle:
        double rotationAmount = angleDifference(translationAngle, robotAngle);

        // Default motor values:
        navigationData.leftMotorPower = 0;
        navigationData.rightMotorPower = 0;
        //      Debug:
        navigationData.forwardPower = 0;
        navigationData.forwardGain = 0;
        navigationData.rotationPower = 0;
        navigationData.rotationGain = 0;

        // Assume that robot is on the target:
        navigationData.onTarget = true;

        // Motor Speed Calculation:
        //      Only drive motors if not in minimum distance:
        if (translationDistance > MIN_DRIVE_DISTANCE) {
            // Robot is not on target:
            navigationData.onTarget = false;

            double leftPower;
            double rightPower;
            double forwardPower;
            double rotationPower;
            double forwardGain = DEFAULT_FORWARD_GAIN;
            double rotationGain = DEFAULT_ROTATION_GAIN;


            // If rotation is more that PI/2, drive backwards:
            if (Math.abs(rotationAmount) > Math.PI/2) {
                forwardGain = -1 * forwardGain;
                rotationAmount = rotationAmount - (Math.signum(rotationAmount) * Math.PI);
            }

            // POWER VALUE CALCULATION:
            //      Calculate forward power only when within a certain distance of the target:
            if (translationDistance < MAX_SPEED_CALCULATE_DISTANCE) {
                forwardPower = ((translationDistance / MAX_SPEED_CALCULATE_DISTANCE) *
                        (MAX_DRIVE_SPEED - DRIVE_SPEED_OFFSET) + DRIVE_SPEED_OFFSET);
            } else {
                forwardPower = MAX_DRIVE_SPEED;
            }

            //      Calculate rotation power as a value of (-1.0, 1):
            rotationPower = rotationAmount / Math.PI;


            // GAIN CALCULATION:
            //      Special GAIN calculation:
            //          Ignore ROTATION input at certain distance:
            if (translationDistance < ROTATION_IGNORE_DISTANCE) {
                Log.d(LOG_TAG, "Ignored rotation gain (" + IGNORED_ROTATION_GAIN + ")");
                rotationGain = IGNORED_ROTATION_GAIN;
            }
            // Save POWER and GAIN values for debug:
            navigationData.forwardPower = forwardPower;
            navigationData.forwardGain = forwardGain;
            navigationData.rotationPower = rotationPower;
            navigationData.rotationGain = rotationGain;


            // CALCULATE MOTOR SIDES DRIVE POWER:
            leftPower = (forwardPower * forwardGain) - (rotationPower * rotationGain);
            rightPower = (forwardPower * forwardGain) + (rotationPower * rotationGain);


            // MOTOR VALUE BANDING:
            /* If one is banded. Make sure to keep the ratio between the motors. */
            double newLeftPower = leftPower;
            double newRightPower = rightPower;
            if (Math.abs(leftPower) > 1) {
                newLeftPower = Math.signum(leftPower);
                newRightPower = newRightPower * (newLeftPower / leftPower);
            }
            if (Math.abs(rightPower) > 1) {
                newRightPower = Math.signum(rightPower);
                newLeftPower = newLeftPower * (newRightPower / rightPower);
            }
            leftPower = newLeftPower;
            rightPower = newRightPower;


            // Log the data as debug:
            Log.d(LOG_TAG, String.format("FwdPwr: %.3f, RotPwr: %.3f, RotAmt: %.3f," +
                    " TransDis: %.3f, TransAng: %.3f, LftPwr: %.3f, RgtPwr: %.3f, RbtAng: %.3f",
                    forwardPower, rotationPower, rotationAmount, translationDistance,
                    translationAngle, leftPower, rightPower, robotAngle));
            // Set motor power:
            navigationData.leftMotorPower = leftPower;
            navigationData.rightMotorPower = -1 * rightPower;
        }

        // Add debug values to data object:
        navigationData.translationDistance = translationDistance;
        navigationData.translationAngle = translationAngle;
        navigationData.rotationAmount = rotationAmount;

        return navigationData;
    }
}
