package org.chathamrobotics.common;

import com.qualcomm.robotcore.hardware.AccelerationSensor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.chathamrobotics.common.utils.RobotLogger;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 * @Last Modified by: storm
 * @Last Modified time: 9/17/2017
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class Robot {

    private HashMap<String, Double> servoRestPositions = new HashMap<>();

    /**
     * The robot's hardware map
     */
    protected HardwareMap hardwareMap;

    /**
     * The opmode's telemetry
     */
    protected Telemetry telemetry;

    /**
     * A logger for the robot that logs to telemetry and logcat
     */
    public RobotLogger log;

    /**
     * Creates an instance of Robot
     *
     * @param hardwareMap   The robot's hardware map
     * @param telemetry     The opmode's telemetry
     */
    public Robot(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;

        this.log = new RobotLogger(this.getClass().getSimpleName(), telemetry);
    }

    /**
     * Initializes the robot. Call when instantiated
     */
    public abstract void init();

    /**
     * Starts the robot
     */
    public abstract void start();

    /**
     * Stops the robot
     */
    public void stop() {
        log.info("Stopping robot");

        stopAllMotors();
        setServosToRestPosition();
    }

    /**
     * Debugs all hardware device values
     */
    public void debugHardware() {
        debugHardware(true, true);
    }

    /**
     * Debugs all hardware device values
     *
     * @param shouldUpdate  whether or not to update the telemetry
     * @param isLooping     whether or not the debug is contained in a loop
     */
    public void debugHardware(boolean shouldUpdate, boolean isLooping) {
        //  debug motor values
        for (Map.Entry<String, DcMotor> motorEntry : hardwareMap.dcMotor.entrySet()) {
            log.debug(
                    motorEntry.getKey() + " Motor Power",
                    motorEntry.getValue().getPower()
            );
        }

        // debug servo values
        for (Map.Entry<String, Servo> servoEntry : hardwareMap.servo.entrySet()) {
            log.debug(
                    servoEntry.getKey() + " Servo Position",
                    servoEntry.getValue().getPosition()
            );
        }

        // debug continuous rotation servo values
        for (Map.Entry<String, CRServo> crServoEntry : hardwareMap.crservo.entrySet()) {
            log.debug(
                    crServoEntry.getKey() + " CRServo Power",
                    crServoEntry.getValue().getPower()
            );
        }

        // debug touch sensor multiplexer values
        // TODO: debug this
//        for (Map.Entry<String, TouchSensorMultiplexer> tsmEntry : hardwareMap.touchSensorMultiplexer.entrySet()) {
//            log.debug(
//                    tsmEntry.getKey() + " CRServo Power",
//                    tsmEntry.getValue().,
//                    isLooping
//            );
//        }

        // debug optical distance sensor
        for (Map.Entry<String, OpticalDistanceSensor> odsEntry : hardwareMap.opticalDistanceSensor.entrySet()) {
            log.debug(
                    odsEntry.getKey() + " ODS Light Level",
                    odsEntry.getValue().getLightDetected()
            );
        }

        // debug touch sensor values
        for (Map.Entry<String, TouchSensor> touchSensorEntry : hardwareMap.touchSensor.entrySet()) {
            log.debug(
                    touchSensorEntry.getKey() + " Touch Sensor isPressed",
                    touchSensorEntry.getValue().isPressed()
            );
        }

        // debug color sensor values
        for (Map.Entry<String, ColorSensor> colorSensorEntry : hardwareMap.colorSensor.entrySet()) {
            log.debug(
                    colorSensorEntry.getKey() + " Color Sensor Color",
                    // format as <r, g, b>
                    String.format(Locale.US, "<%d, %d, %d>",
                            colorSensorEntry.getValue().red(),
                            colorSensorEntry.getValue().green(),
                            colorSensorEntry.getValue().blue()
                    )
            );
        }

        // debug acceleration sensor values
        for (Map.Entry<String, AccelerationSensor> asEntry : hardwareMap.accelerationSensor.entrySet()) {
            log.debug(
                    asEntry.getKey() + " Acceleration Sensor Acceleration",
                    asEntry.getValue().getAcceleration()
            );
        }

        // debug compass sensor values
        for (Map.Entry<String, CompassSensor> compassSensorEntry : hardwareMap.compassSensor.entrySet()) {
            log.debug(
                    compassSensorEntry.getKey() + " Compass Sensor Direction",
                    compassSensorEntry.getValue().getDirection()
            );
        }

        // debug ir seeker values
        for (Map.Entry<String, IrSeekerSensor> irssEntry : hardwareMap.irSeekerSensor.entrySet()) {
            log.debug(
                    irssEntry.getKey() + " IR Seeker Angle",
                    irssEntry.getValue().getAngle()
            );

            log.debug(
                    irssEntry.getKey() + " IR Seeker Strength",
                    irssEntry.getValue().getStrength()
            );
        }

        // debug light sensor values
        for (Map.Entry<String, LightSensor> lightSensorEntry : hardwareMap.lightSensor.entrySet()) {
            log.debug(
                    lightSensorEntry.getKey() + " Light Sensor Level",
                    lightSensorEntry.getValue().getLightDetected()
            );
        }

        // debug ultrasonic sensor values
        for (Map.Entry<String, UltrasonicSensor> usEntry : hardwareMap.ultrasonicSensor.entrySet()) {
            log.debug(
                    usEntry.getKey() + " Ultrasonic Sensor Level",
                    usEntry.getValue().getUltrasonicLevel()
            );
        }

        // debug voltage sensor values
        for (Map.Entry<String, VoltageSensor> vsEntry : hardwareMap.voltageSensor.entrySet()) {
            log.debug(
                    vsEntry.getKey() + " Voltage Sensor Voltage",
                    vsEntry.getValue().getVoltage()
            );
        }

        if (shouldUpdate) {
            telemetry.update();
        }
    }

    /**
     * Sets all motors powers to zero
     */
    public void stopAllMotors() {
        for (Map.Entry<String, DcMotor> motorEntry : hardwareMap.dcMotor.entrySet()) {
            motorEntry.getValue().setPower(0);
        }
    }

    /**
     * Sets the servo's rest position, so that when setServosToRestPosition is call, the servo will go to that position
     * @param servo         the servo whose rest position is being set
     * @param restPosition  the rest position for the servo
     */
    public void setServoRestPosition(Servo servo, double restPosition) {
        servoRestPositions.put(servo.getDeviceName(), restPosition);
    }

    /**
     * Sets all servos positions to their rest position
     */
    public void setServosToRestPosition() {
        for (Map.Entry<String, Double> servoRestPosition : servoRestPositions.entrySet()) {
            hardwareMap.servo.get(servoRestPosition.getKey())
                    .setPosition(servoRestPosition.getValue());
        }
    }
}
