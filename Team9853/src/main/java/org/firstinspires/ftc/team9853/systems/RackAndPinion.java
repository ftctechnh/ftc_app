package org.firstinspires.ftc.team9853.systems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.chathamrobotics.common.Robot;
import org.chathamrobotics.common.utils.RobotLogger;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Timer;

/**
 * Created by carsonstorm on 10/25/2017.
 */

public class RackAndPinion {
    private static final Timer TIMER = new Timer();
    private static final String TAG = "RackAndPinion";

    private final CRServo crServo;
    private final RobotLogger logger;

    private boolean isBusy = false;

    /**
     * Builds a new rack and pinion system using CRServo as the name for the continuous servo
     * @param robot     the robot to get the hardware and logger from
     * @return          the built rack and pinion system
     */
    public static RackAndPinion build(Robot robot) {
        return build(robot.hardwareMap, robot.log);
    }

    /**
     * Builds a new rack and pinion system using CRServo as the name for the continuous servo
     * @param hardwareMap   the robot's hardware mape
     * @param telemetry     the opmode's telemetry
     * @return              the built rack and pinion system
     */
    public static RackAndPinion build(HardwareMap hardwareMap, Telemetry telemetry) {
        return build(hardwareMap, new RobotLogger("RackAndPinion", telemetry));
    }

    /**
     * Builds a new rack and pinion system using CRServo as the name for the continuous servo
     * @param hardwareMap   the robot's hardware mape
     * @param logger        the robot's logger
     * @return              the built rack and pinion system
     */
    public static RackAndPinion build(HardwareMap hardwareMap, RobotLogger logger) {
        return new RackAndPinion(hardwareMap.crservo.get("CRServo"), logger);
    }

    /**
     * Creates a new instance of RackAndPinion
     * @param crServo   the continuous servo
     * @param logger    the logger for debugging
     */
    public RackAndPinion(CRServo crServo, RobotLogger logger) {
        this.crServo = crServo;
        this.logger = logger;
    }

    /**
     * Check whether or not the servo is currently moving
     * @return  whether or not the servo is currently moving
     */
    public boolean isBusy() {
        return this.isBusy;
    }


}
