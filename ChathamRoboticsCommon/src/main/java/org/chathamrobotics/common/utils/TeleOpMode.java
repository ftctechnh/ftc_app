package org.chathamrobotics.common.utils;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.chathamrobotics.common.Robot;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.lang.reflect.ParameterizedType;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 *
 * @Last Modified by: storm
 * @Last Modified time: 9/23/2017
 */
@SuppressWarnings("unused")
public abstract class TeleOpMode<R extends Robot> extends OpMode {
    /**
     * The robot object
     */
    public R robot;

    /**
     * Creates an instance of TeleOpMode
     */
    public TeleOpMode() {
        try {
            //noinspection unchecked
            robot = (R)(
                    // Get generic type class
                    ((Class)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0])
                            // Get robot class
                            .getConstructor(HardwareMap.class, Telemetry.class)
                            // Instantiate robot class
                            .newInstance(hardwareMap, telemetry)
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate the robot class", e);
        }
    }

    @Override
    public void start() {
        super.start();
        robot.start();
    }

    @Override
    public void stop() {
        super.stop();
        robot.stop();
    }
}
