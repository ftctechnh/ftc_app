/*
 * Copyright (C) 2015 Powerstackers
 *
 * Basic configurations and capabilities of our robot.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.powerstackers.resq.common;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.Range;

import org.swerverobotics.library.ClassFactory;

/**
 * A general representation of a robot, with simple interaction methods.
 * @author Jonathan Thomas
 */
public class Robot {

    private DcMotor motorLeftA;
    private DcMotor motorLeftB;
    private DcMotor motorRightA;
    private DcMotor motorRightB;
    private DcMotor motorBrush;
    private DcMotor motorLift;

    private Servo servoTapeMeasure;
    private Servo servoBeacon;
    private Servo servoHopperRight;
    private Servo servoHopperLeft;
    private Servo servoClimberFlipper;

    private DeviceInterfaceModule dim;
    private ColorSensor sensorColor;
    private TouchSensor sensorTouch;
    private GyroSensor sensorGyro;
    public OpticalDistanceSensor opticalSensor;

    /**
     * Construct a Robot object.
     * @param mode The OpMode in which the robot is being used.
     */
    public Robot(OpMode mode) {
        motorLeftA  = mode.hardwareMap.dcMotor.get("motorFLeft");
        motorLeftB  = mode.hardwareMap.dcMotor.get("motorBLeft");
        motorRightA = mode.hardwareMap.dcMotor.get("motorFRight");
        motorRightB = mode.hardwareMap.dcMotor.get("motorBRight");
        motorBrush  = mode.hardwareMap.dcMotor.get("motorBrush");
        motorLift   = mode.hardwareMap.dcMotor.get("motorLift");

        motorLift.setDirection(DcMotor.Direction.REVERSE);
        motorRightA.setDirection(DcMotor.Direction.REVERSE);
        motorRightB.setDirection(DcMotor.Direction.REVERSE);

        servoTapeMeasure = mode.hardwareMap.servo.get("servoTapeMeasure");
        servoBeacon      = mode.hardwareMap.servo.get("servoBeacon");
        servoHopperRight = mode.hardwareMap.servo.get("servoHopperRight");
        servoHopperLeft = mode.hardwareMap.servo.get("servoHopperLeft");
        servoClimberFlipper = mode.hardwareMap.servo.get("servoClimbers");

        dim = mode.hardwareMap.deviceInterfaceModule.get("dim");
        sensorColor = ClassFactory.createSwerveColorSensor(mode,
                mode.hardwareMap.colorSensor.get("sensorColor"));
        sensorColor.enableLed(true);
        opticalSensor = mode.hardwareMap.opticalDistanceSensor.get("opticalDistance");
        sensorGyro = mode.hardwareMap.gyroSensor.get("sensorGyro");

    }

    /**
     * Initialize the robot's servos and sensors.
     */
    public void initializeRobot() throws InterruptedException {
        servoBeacon.setPosition(RobotConstants.BEACON_RESTING);

        sensorGyro.calibrate();
        // Give the gyroscope some time to calibrate
        while (sensorGyro.isCalibrating()) {
            Thread.sleep(50L);
        }
    }

    /**
     * Set the power for the right side drive motors.
     * @param power Double from 0 to 1.
     */
    public void setPowerRight(double power) {
        motorRightA.setPower(power);
        motorRightB.setPower(power);
    }

    /**
     * Set the power for the left side drive motors.
     * @param power Double from 0 to 1.
     */
    public void setPowerLeft(double power) {
        motorLeftA.setPower(power);
        motorLeftB.setPower(power);
    }

    /**
     * Set the movement of the tape measure motor.
     * @param setting MotorSetting enum value; FORWARD, STOP, or REVERSE.
     */
    public void setTapeMeasure(MotorSetting setting) {
        toggleCRServo(servoTapeMeasure, setting);
    }

    /**
     * Set the movement of the brush motor.
     * @param setting MotorSetting indicating the direction.
     */
    public void setBrush(MotorSetting setting) {
        toggleMotor(motorBrush, setting, RobotConstants.BRUSH_SPEED);
    }

    /**
     * Set the direction of the lift: REVERSE, STOP, or FORWARD.
     * @param setting MotorSetting enum indicating the direction.
     */
    public void setLift(MotorSetting setting) {
        toggleMotor(motorLift, setting, RobotConstants.LIFT_SPEED);
    }

    /**
     * Toggles a motor between three settings: FORWARD, STOP, and REVERSE.
     * @param toToggle Motor to change.
     * @param setting MotorSetting indicating the direction.
     * @param power Power value to use.
     */
    private void toggleMotor(DcMotor toToggle, MotorSetting setting, double power) {
        switch (setting) {
            case REVERSE:
                toToggle.setPower(-power);
                break;
            case STOP:
                toToggle.setPower(0);
                break;
            case FORWARD:
                toToggle.setPower(power);
                break;
            default:
                toToggle.setPower(0);
                break;
        }
    }

    /**
     * Toggle a continuous rotation servo in one of three directions: FORWARD, STOP, and REVERSE.
     * @param toToggle Servo to toggle.
     * @param setting MotorSetting indicating the direction.
     */
    private void toggleCRServo(Servo toToggle, MotorSetting setting) {
        switch (setting) {
            case REVERSE:
                toToggle.setPosition(RobotConstants.CRS_REVERSE);
                break;
            case STOP:
                toToggle.setPosition(RobotConstants.CRS_STOP);
                break;
            case FORWARD:
                toToggle.setPosition(RobotConstants.CRS_FORWARD);
                break;
            default:
                toToggle.setPosition(RobotConstants.CRS_STOP);
        }
    }

    /**
     * Trim a servo value between the minimum and maximum ranges.
     * @param servoValue Value to trim.
     * @return A raw double with the trimmed value.
     */
    private double trimServoValue(double servoValue) {
        return Range.clip(servoValue, 0.0, 1.0);
    }

    /**
     * Tap the beacon on the correct side.
     * @param allianceColor The color that we are currently playing as.
     */
    public void tapBeacon(AllianceColor allianceColor) {

        AllianceColor dominantColor;
        double positionBeaconServo;

        // Detect the color shown on the beacon's left half, and record it.
        if (sensorColor.red() > sensorColor.blue()) {
            dominantColor = AllianceColor.RED;
        } else {
            dominantColor = AllianceColor.BLUE;
        }

        // Tap the correct side based on the dominant color.
        if (dominantColor == allianceColor) {
            positionBeaconServo = RobotConstants.BEACON_TAP_LEFT;
        } else {
            positionBeaconServo = RobotConstants.BEACON_TAP_RIGHT;
        }

        // Trim the servo value and set the servo position.
        positionBeaconServo = trimServoValue(positionBeaconServo);
        servoBeacon.setPosition(positionBeaconServo);
    }

    /**
     * Set the right hopper door to open or close.
     * @param doorSetting DoorSetting to set the door to.
     */
    public void setHopperRight(DoorSetting doorSetting) {
        if (doorSetting == DoorSetting.OPEN) {
            servoHopperRight.setPosition(RobotConstants.HOPPER_RIGHT_OPEN);
        } else {
            servoHopperRight.setPosition(RobotConstants.HOPPER_RIGHT_CLOSE);
        }
    }

    /**
     * Set the left hopper door to open or close.
     * @param doorSetting DoorSetting to set the door to.
     */
    public void setHopperLeft(DoorSetting doorSetting) {
        if (doorSetting == DoorSetting.OPEN) {
            servoHopperLeft.setPosition(RobotConstants.HOPPER_LEFT_OPEN);
        } else {
            servoHopperLeft.setPosition(RobotConstants.HOPPER_LEFT_CLOSE);
        }
    }

    /**
     * Set the climber flipping device to either extended or retracted position.
     * In this case, the OPEN setting indicates extended, and the CLOSED setting is its resting
     * position.
     * @param doorSetting DoorSetting indicating the position.
     */
    public void setClimberFlipper(DoorSetting doorSetting) {
        if (doorSetting == DoorSetting.OPEN) {
            servoClimberFlipper.setPosition(RobotConstants.CLIMBER_RETRACT);
        } else {
            servoClimberFlipper.setPosition(RobotConstants.CLIMBER_EXTEND);
        }
    }

    /**
     * Move the robot a specific distance forwards or backwards.
     * To specify the distance, pass a double representing the number of <b>inches</b> that you would
     * like the robot to move. To move backwards, simply pass a negative number.
     * @param distance An integer representing the distance to move.
     */
    public void moveDistance(int distance) {
        motorLeftA.setTargetPosition(motorLeftA.getCurrentPosition() + distance);
        motorRightA.setTargetPosition(motorRightA.getCurrentPosition() + distance);
    }

    /**
     * Turn the robot a specific number of degrees clockwise or counter-clockwise.
     * To specify the number of degrees to turn, pass a double representing the number of
     * <b>degrees</b> to turn. It should be noted that the degrees you turn assume standard position
     * when looking at the robot from above. In other words, passing a negative number will turn
     * clockwise, and a positive number will turn counter-clockwise.
     * @param degrees A double representing the distance to turn.
     */
    public void turnDegrees(double degrees) {
        // TODO Actually make this method work
    }
}
