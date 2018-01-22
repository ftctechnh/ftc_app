package org.chathamrobotics.common.robot;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 * @Last Modified by: storm
 * @Last Modified time: 9/17/2017
 */

import com.qualcomm.robotcore.hardware.AccelerationSensor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.chathamrobotics.common.hardware.utils.HardwareListener;
import org.chathamrobotics.common.systems.Driver;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Represents the physical robot in software
 */
@SuppressWarnings({"WeakerAccess", "unused", "SameParameterValue"})
public abstract class Robot extends HardwareListener {
    private static final int MAX_MOTOR_RPM = 160;

    private interface HardwareDebugFunc<E extends HardwareDevice> {
        void debug(String name, E device);
    }

    private final HardwareMap hardwareMap;
    private final Telemetry telemetry;

    public RobotLogger log;
    public Driver driver;

    // hardware debuggers
    private final HardwareDebugFunc<DcMotor> motorDebugger = (name, motor) -> {
        if (motor.getMode() == DcMotor.RunMode.RUN_USING_ENCODER)
            log.debug(String.format(Locale.US, "%s Motor Speed: %f",
                    name, MAX_MOTOR_RPM * motor.getPower()));
        else if (motor.getMode() == DcMotor.RunMode.RUN_TO_POSITION) {
            log.debug(name + " Motor Position", motor.getCurrentPosition());
            log.debug(name + " Motor Target Position", motor.getTargetPosition());
        } else
            log.debug(name + " Motor Power", motor.getPower());
    };

    private final HardwareDebugFunc<Servo> servoDebugger = (name, servo) ->
        log.debug(name + "Servo Position", servo.getPosition());

    private final HardwareDebugFunc<CRServo> crServoDebugger = (name, crServo) ->
            log.debug(name + "CRServo Power", crServo.getPower());

    private final HardwareDebugFunc<OpticalDistanceSensor> odsDebugger = (name, ods) ->
            log.debug(name + " ODS Light", ods.getLightDetected());

    private final  HardwareDebugFunc<TouchSensor> touchSensorDebugger = (name, touch) ->
            log.debug(name + "Touch Sensor isPressed", touch.isPressed());

    private final HardwareDebugFunc<ColorSensor> colorSensorDebugger = (name, color) ->
            log.debug(name + " Color Sensor Color", String.format(Locale.US, "<%d, %d, %d>",
                    color.red(),
                    color.green(),
                    color.blue()
            ));

    private final HardwareDebugFunc<AccelerationSensor> accelSensorDebugger = (name, as) ->
            log.debug(name + " Acceleration Sensor Value", as.getAcceleration());

    private final HardwareDebugFunc<CompassSensor> compassDebugger = (name, compass) ->
            log.debug(name + " Compass Sensor Direction", compass.getDirection());

    private final HardwareDebugFunc<IrSeekerSensor> irSensorDebugger = (name, ir) -> {
        log.debug(name + " IR Seeker Angle", ir.getAngle());
        log.debug(name + " IR Seeker Strength", ir.getStrength());
    };

    private final HardwareDebugFunc<LightSensor> lightSensorDebugger = (name, light) ->
            log.debug(name + " Light Sensor Light", light.getLightDetected());

    private final HardwareDebugFunc<UltrasonicSensor> ultrasonicDebugger = (name, u) ->
            log.debug(name + " Ultrasonic Sensor Level", u.getUltrasonicLevel());

    private final HardwareDebugFunc<VoltageSensor> voltageSensorDebugger = (name, v) ->
            log.debug(name + " Voltage Sensor Voltage", v.getVoltage());

    private HashMap<String, Double> servoRestPositions = new HashMap<>();


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
     * Returns the robot's hardware map
     * @return  the robot's hardware map
     */
    public HardwareMap getHardwareMap() {
        return this.hardwareMap;
    }

    /**
     * Debugs all hardware device values
     */
    public void debugHardware() {
        debugHardware(true);
    }

    /**
     * Debugs all hardware device values
     *  @param shouldUpdate  whether or not to update the telemetry
     *
     */
    public void debugHardware(boolean shouldUpdate) {
        debugHardwareDevice(hardwareMap.dcMotor, motorDebugger);
        debugHardwareDevice(hardwareMap.servo, servoDebugger);
        debugHardwareDevice(hardwareMap.crservo, crServoDebugger);
        debugHardwareDevice(hardwareMap.opticalDistanceSensor, odsDebugger);
        debugHardwareDevice(hardwareMap.touchSensor, touchSensorDebugger);
        debugHardwareDevice(hardwareMap.colorSensor, colorSensorDebugger);
        debugHardwareDevice(hardwareMap.accelerationSensor, accelSensorDebugger);
        debugHardwareDevice(hardwareMap.compassSensor, compassDebugger);
        debugHardwareDevice(hardwareMap.irSeekerSensor, irSensorDebugger);
        debugHardwareDevice(hardwareMap.lightSensor, lightSensorDebugger);
        debugHardwareDevice(hardwareMap.ultrasonicSensor, ultrasonicDebugger);
        debugHardwareDevice(hardwareMap.voltageSensor, voltageSensorDebugger);

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

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    private <E extends HardwareDevice> void debugHardwareDevice(HardwareMap.DeviceMapping<E> devices, HardwareDebugFunc<E> debugger) {
        for (Map.Entry<String, E> entry : devices.entrySet()) {
            if (entry.getValue() == null) continue;

            debugger.debug(entry.getKey(), entry.getValue());
        }
    }
}
