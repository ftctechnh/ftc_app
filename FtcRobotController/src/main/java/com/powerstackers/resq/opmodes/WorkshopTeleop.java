package com.powerstackers.resq.opmodes;

import com.powerstackers.resq.opmodes.common.Robot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Teleop program made at the workshop.
 */
public class WorkshopTeleop extends Robot{

    /**
     * Run the OpMode.
     * @throws InterruptedException
     *      Throws this if the thread gets interrupted.
     */
    @Override
    public void runOpMode() throws InterruptedException {
        motorLeft = hardwareMap.dcMotor.get("motor_1");
        motorRight = hardwareMap.dcMotor.get("motor_2");

        servoArm = hardwareMap.servo.get("servo_1");
        servoClaw = hardwareMap.servo.get("servo_2");

        touchSensor = hardwareMap.touchSensor.get("sensor_touch");
        opticalSensor = hardwareMap.opticalDistanceSensor.get("sensor_optical");

        // This is where the program goes.
    }
}
