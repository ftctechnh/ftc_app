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
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import static com.powerstackers.resq.common.RobotConstants.BRUSH_SPEED;
import static com.powerstackers.resq.common.RobotConstants.CHURRO_LEFT_CLOSE;
import static com.powerstackers.resq.common.RobotConstants.CHURRO_LEFT_OPEN;
import static com.powerstackers.resq.common.RobotConstants.CHURRO_RIGHT_CLOSE;
import static com.powerstackers.resq.common.RobotConstants.CHURRO_RIGHT_OPEN;
import static com.powerstackers.resq.common.RobotConstants.CLIMBER_EXTEND;
import static com.powerstackers.resq.common.RobotConstants.CLIMBER_RETRACT;
import static com.powerstackers.resq.common.RobotConstants.CRS_FORWARD;
import static com.powerstackers.resq.common.RobotConstants.CRS_REVERSE;
import static com.powerstackers.resq.common.RobotConstants.CRS_STOP;
import static com.powerstackers.resq.common.RobotConstants.HOPPER_LEFT_CLOSE;
import static com.powerstackers.resq.common.RobotConstants.HOPPER_LEFT_OPEN;
import static com.powerstackers.resq.common.RobotConstants.HOPPER_RIGHT_CLOSE;
import static com.powerstackers.resq.common.RobotConstants.HOPPER_RIGHT_OPEN;
import static com.powerstackers.resq.common.RobotConstants.HOPPER_TILT_LEFT;
import static com.powerstackers.resq.common.RobotConstants.HOPPER_TILT_RESTING;
import static com.powerstackers.resq.common.RobotConstants.HOPPER_TILT_RIGHT;
import static com.powerstackers.resq.common.RobotConstants.LIFT_SPEED;
import static com.powerstackers.resq.common.RobotConstants.TRIMM_MOTOR;
import static com.powerstackers.resq.common.RobotConstants.WINCH_SPEED;
import static com.powerstackers.resq.common.RobotConstants.ZIPLINE_LEFT_CLOSE;
import static com.powerstackers.resq.common.RobotConstants.ZIPLINE_LEFT_OPEN;
import static com.powerstackers.resq.common.RobotConstants.ZIPLINE_RIGHT_CLOSE;
import static com.powerstackers.resq.common.RobotConstants.ZIPLINE_RIGHT_OPEN;
import static com.powerstackers.resq.common.enums.PublicEnums.DoorSetting;
import static com.powerstackers.resq.common.enums.PublicEnums.MotorSetting;
import static com.powerstackers.resq.common.enums.PublicEnums.TiltSetting;

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
    private DcMotor motorWinchLeft;
    private DcMotor motorWinchRight;

//    private Servo servoTapeMeasure;
//    private Servo servoTapeTilt;
//    private Servo servoBeacon;
    private Servo servoHopperRight;
    private Servo servoHopperLeft;
    private Servo servoHopperSlide;
    private Servo servoClimberFlipper;
    private Servo servoChurroLeft;
    private Servo servoChurroRight;
    private Servo servoLiftLeft;
    private Servo servoLiftRight;
    private Servo servoZippLineLeft;
    private Servo servoZippLineRight;
    private Servo servoHook;

//    private DeviceInterfaceModule dim;
//    private ColorSensor sensorColor;
//    private TouchSensor sensorTouch;
    private GyroSensor sensorGyro;
//    public OpticalDistanceSensor opticalSensor;

    /**
     * Construct a Robot object.
     * @param mode The OpMode in which the robot is being used.
     */
    public Robot(OpMode mode) {
        motorLeftA      = mode.hardwareMap.dcMotor.get("motorFLeft");
        motorLeftB      = mode.hardwareMap.dcMotor.get("motorBLeft");
        motorRightA     = mode.hardwareMap.dcMotor.get("motorFRight");
        motorRightB     = mode.hardwareMap.dcMotor.get("motorBRight");
        motorBrush      = mode.hardwareMap.dcMotor.get("motorBrush");
        motorLift       = mode.hardwareMap.dcMotor.get("motorLift");
        motorWinchLeft  = mode.hardwareMap.dcMotor.get("motorLHang");
        motorWinchRight = mode.hardwareMap.dcMotor.get("motorRHang");

        motorLift.setDirection(DcMotor.Direction.REVERSE);
        motorRightA.setDirection(DcMotor.Direction.REVERSE);
        motorRightB.setDirection(DcMotor.Direction.REVERSE);
        motorWinchRight.setDirection(DcMotor.Direction.REVERSE);

//        servoTapeMeasure    = mode.hardwareMap.servo.get("servoTapeMeasure");
//        servoTapeTilt       = mode.hardwareMap.servo.get("servoTapeTilt");
//        servoBeacon         = mode.hardwareMap.servo.get("servoBeacon");
        servoHopperRight    = mode.hardwareMap.servo.get("servoHopperRight");
        servoHopperLeft     = mode.hardwareMap.servo.get("servoHopperLeft");
        servoHopperSlide     = mode.hardwareMap.servo.get("servoHopperSlide");
        servoClimberFlipper = mode.hardwareMap.servo.get("servoClimbers");
        servoChurroLeft     = mode.hardwareMap.servo.get("servoChurroLeft");
        servoChurroRight    = mode.hardwareMap.servo.get("servoChurroRight");
        servoLiftLeft       = mode.hardwareMap.servo.get("servoLiftLeft");
        servoLiftRight      = mode.hardwareMap.servo.get("servoLiftRight");
        servoZippLineLeft   = mode.hardwareMap.servo.get("servoZipplineLeft");
        servoZippLineRight  = mode.hardwareMap.servo.get("servoZipplineRight");
        servoHook           = mode.hardwareMap.servo.get("servoHook");

        servoHopperLeft.setPosition(HOPPER_LEFT_CLOSE);
        servoHopperRight.setPosition(HOPPER_RIGHT_CLOSE);
        servoHopperSlide.setPosition(CRS_STOP);
        servoClimberFlipper.setPosition(CLIMBER_EXTEND);
        servoChurroRight.setPosition(CHURRO_RIGHT_OPEN);
        servoChurroLeft.setPosition(CHURRO_LEFT_OPEN);
        servoLiftLeft.setPosition(CRS_STOP);
        servoLiftRight.setPosition(CRS_STOP);
        servoHook.setPosition(CRS_STOP);
        servoZippLineLeft.setPosition(ZIPLINE_LEFT_CLOSE);
        servoZippLineRight.setPosition(ZIPLINE_RIGHT_CLOSE);
//        servoTapeTilt.setPosition(TAPE_FLAT);
//        servoTapeMeasure.setPosition(0.5);

//        dim = mode.hardwareMap.deviceInterfaceModule.get("dim");
//        sensorColor = ClassFactory.createSwerveColorSensor(mode,
//                mode.hardwareMap.colorSensor.get("sensorColor"));
//        sensorColor.enableLed(true);
//        opticalSensor = mode.hardwareMap.opticalDistanceSensor.get("opticalDistance");
        sensorGyro = mode.hardwareMap.gyroSensor.get("sensorGyro");

    }

    /**
     * Initialize the robot's servos and sensors.
     */
    public void initializeRobot() /*throws InterruptedException */{
//
//       double hopperTiltPosition = servoHopperTilt.getPosition();

//        servoBeacon.setPosition(RobotConstants.BEACON_RESTING);
        servoClimberFlipper.setPosition(CLIMBER_EXTEND);
        servoHopperLeft.setPosition(HOPPER_LEFT_CLOSE);
        servoHopperRight.setPosition(HOPPER_RIGHT_CLOSE);
        servoHopperSlide.setPosition(CRS_STOP);
        servoChurroRight.setPosition(CHURRO_RIGHT_OPEN);
        servoChurroLeft.setPosition(CHURRO_LEFT_OPEN);
        servoLiftLeft.setPosition(CRS_STOP);
        servoLiftRight.setPosition(CRS_STOP);
        servoHook.setPosition(CRS_STOP);
        servoZippLineLeft.setPosition(ZIPLINE_LEFT_CLOSE);
        servoZippLineRight.setPosition(ZIPLINE_RIGHT_CLOSE);
//        servoTapeTilt.setPosition(TAPE_FLAT);
        sensorGyro.calibrate();
         //Give the gyroscope some time to calibrate
//        while (sensorGyro.isCalibrating()) {
//            Thread.sleep(50L);
//        }
    }

    /**
     * Set the power for the right side drive motors.
     * @param power Double from 0 to 1.
     */
    public void setPowerRight(double power) {
        motorRightA.setPower(power*TRIMM_MOTOR);
        motorRightB.setPower(power);
    }

    /**
     * Set the power for the left side drive motors.
     * @param power Double from 0 to 1.
     */
    public void setPowerLeft(double power) {
        motorLeftA.setPower(power*TRIMM_MOTOR);
        motorLeftB.setPower(power);
    }

    public void setPowerAll(double power) {
        setPowerLeft(power);
        setPowerRight(power);
    }

    /**
     * Set the movement of the tape measure motor.
     * @param setting MotorSetting enum value; FORWARD, STOP, or REVERSE.
     */
//    public void setTapeMeasure(MotorSetting setting) {
//        toggleCRServo(servoTapeMeasure, setting);
//    }

    /**
     * Set the movement of the brush motor.
     * @param setting MotorSetting indicating the direction.
     */
    public void setBrush(MotorSetting setting) {
        toggleMotor(motorBrush, setting, BRUSH_SPEED);
    }

    /**
     * Set the direction of the lift: REVERSE, STOP, or FORWARD.
     * @param setting MotorSetting enum indicating the direction.
     */
    public void setLift(MotorSetting setting) {
        toggleMotor(motorLift, setting, LIFT_SPEED);
    }

    public void setLiftHeightLeft(MotorSetting setting) {
        toggleCRServo(servoLiftLeft, setting);
    }

    public void setLiftHeightRight(MotorSetting setting) {
        toggleCRServo(servoLiftRight, setting);
    }

    public void setHookPosition(MotorSetting setting) {
        toggleCRServo(servoHook, setting);
    }

    /**
     * Set the position of the tape tilt servo.
     * @param position Double position to set.
     */
//    public void setTapeTilt(double position) {
//        if (position >= 0.0 && position <= 1.0) {
//            servoTapeTilt.setPosition(position);
//        }
//    }

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
                toToggle.setPosition(CRS_REVERSE);
                break;
            case STOP:
                toToggle.setPosition(CRS_STOP);
                break;
            case FORWARD:
                toToggle.setPosition(CRS_FORWARD);
                break;
            default:
                toToggle.setPosition(CRS_STOP);
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
//    public void tapBeacon(AllianceColor allianceColor) {
//
//        AllianceColor dominantColor;
//        double positionBeaconServo;
//
//        // Detect the color shown on the beacon's left half, and record it.
//        if (sensorColor.red() > sensorColor.blue()) {
//            dominantColor = AllianceColor.RED;
//        } else {
//            dominantColor = AllianceColor.BLUE;
//        }
//
//        // Tap the correct side based on the dominant color.
//        if (dominantColor == allianceColor) {
//            positionBeaconServo = BEACON_TAP_LEFT;
//        } else {
//            positionBeaconServo = BEACON_TAP_RIGHT;
//        }

        // Trim the servo value and set the servo position.
//        positionBeaconServo = trimServoValue(positionBeaconServo);
//        servoBeacon.setPosition(positionBeaconServo);
//    }

    /**
     * Set the right hopper door to open or close.
     * @param doorRightSetting DoorSetting to set the door to.
     */
    public void setHopperRight(DoorSetting doorRightSetting) {
        if (doorRightSetting == DoorSetting.OPEN) {
            servoHopperRight.setPosition(HOPPER_RIGHT_OPEN);
        } else {
            servoHopperRight.setPosition(HOPPER_RIGHT_CLOSE);
        }
    }

    public void setHopperSlidePosition(MotorSetting slidesetting) {
            toggleCRServo(servoHopperSlide, slidesetting);
    }

    /**
     * Set the left hopper door to open or close.
     * @param doorLeftSetting DoorSetting to set the door to.
     */
    public void setHopperLeft(DoorSetting doorLeftSetting) {
        if (doorLeftSetting == DoorSetting.OPEN) {
            servoHopperLeft.setPosition(HOPPER_LEFT_OPEN);
//            servoHopperSlide.setPosition(CRS_FORWARD);
        } else {
            servoHopperLeft.setPosition(HOPPER_LEFT_CLOSE);
//            servoHopperSlide.setPosition(CRS_REVERSE);
        }
    }

//    public void setAllianceHopper(DoorSetting doorSetting, AllianceColor allianceColor) {
//        // If we are red, we should dump to the left. Blue, to the right.
//        if (allianceColor == AllianceColor.RED) {
//            servoHopperTilt.setPosition(RobotConstants.HOPPER_TILT_LEFT);
//            servoHopperLeft.setPosition((doorSetting==DoorSetting.OPEN)?
//                    RobotConstants.HOPPER_LEFT_OPEN : RobotConstants.HOPPER_LEFT_CLOSE);
//        } else {
//            servoHopperTilt.setPosition(RobotConstants.HOPPER_TILT_RESTING);
//            servoHopperLeft.setPosition((doorSetting==DoorSetting.OPEN)?
//                    RobotConstants.HOPPER_LEFT_OPEN : RobotConstants.HOPPER_LEFT_CLOSE);
//        }
//
//        if (allianceColor == AllianceColor.BLUE) {
//            servoHopperTilt.setPosition(RobotConstants.HOPPER_TILT_RIGHT);
//            servoHopperRight.setPosition((doorSetting==DoorSetting.OPEN)?
//                    RobotConstants.HOPPER_RIGHT_OPEN : RobotConstants.HOPPER_RIGHT_CLOSE);
//        } else {
//            servoHopperTilt.setPosition(RobotConstants.HOPPER_TILT_RESTING);
//            servoHopperRight.setPosition((doorSetting==DoorSetting.OPEN)?
//                    RobotConstants.HOPPER_RIGHT_OPEN : RobotConstants.HOPPER_RIGHT_CLOSE);
//        }
//    }

    /**
     * Set the climber flipping device to either extended or retracted position.
     * In this case, the OPEN setting indicates extended, and the CLOSED setting is its resting
     * position.
     * @param doorSetting DoorSetting indicating the position.
     */
    public void setClimberFlipper(DoorSetting doorSetting) {
        if (doorSetting == DoorSetting.OPEN) {
            servoClimberFlipper.setPosition(CLIMBER_RETRACT);
        } else {
            servoClimberFlipper.setPosition(CLIMBER_EXTEND);
        }
    }

    /**
     * Set the position of the churro grabber servos.
     * In this case, the OPEN position is the retracted, <b>not grabbing</b> position, and close is
     * the opposite.
     * @param doorSetting DoorSetting indicating the position.
     */
    public void setChurroGrabbers(DoorSetting doorSetting) {
        if (doorSetting == DoorSetting.OPEN) {
            servoChurroLeft.setPosition(CHURRO_LEFT_OPEN);
            servoChurroRight.setPosition(CHURRO_RIGHT_OPEN);
        } else {
            servoChurroLeft.setPosition(CHURRO_LEFT_CLOSE);
            servoChurroRight.setPosition(CHURRO_RIGHT_CLOSE);
        }
    }

    public void setZiplineLeft(DoorSetting doorSetting) {
        if(doorSetting == DoorSetting.OPEN) {
            servoZippLineLeft.setPosition(ZIPLINE_LEFT_OPEN);
        } else {
            servoZippLineLeft.setPosition(ZIPLINE_LEFT_CLOSE);
        }
    }

    public void setZiplineRight(DoorSetting doorSetting) {
        if(doorSetting == DoorSetting.OPEN) {
            servoZippLineRight.setPosition(ZIPLINE_RIGHT_OPEN);
        } else {
            servoZippLineRight.setPosition(ZIPLINE_RIGHT_CLOSE);
        }
    }

    /**
     * Set the winch motors.
     * @param motorSetting MotorSetting indicating the direction.
     */
    public void setWinch(MotorSetting motorSetting) {
        toggleMotor(motorWinchLeft, motorSetting, WINCH_SPEED);
        toggleMotor(motorWinchRight, motorSetting, WINCH_SPEED);
    }

    public void calibrateGyro(){
        sensorGyro.calibrate();
    }

    public  boolean isGyrocalibrate() {
        return sensorGyro.isCalibrating();
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

    public long getLeftEncoder() {
        return motorLeftA.getCurrentPosition();
    }

    public long getRightEncoder() {
        return motorRightA.getCurrentPosition();
    }

//    public double getGyroRotation(){
//        return sensorGyro.getRotation();
//    }

    public long getGyroHeading() {
        return sensorGyro.getHeading();
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
        // TODO Actually make this method work //Put in JonsAlgo
    }
}
