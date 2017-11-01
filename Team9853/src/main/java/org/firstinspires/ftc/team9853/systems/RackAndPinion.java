package org.firstinspires.ftc.team9853.systems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.chathamrobotics.common.Robot;
import org.chathamrobotics.common.hardware.LimitSwitch;
import org.chathamrobotics.common.hardware.modernrobotics.ModernRoboticsLimitSwitch;
import org.chathamrobotics.common.utils.HardwareListeners;
import org.chathamrobotics.common.utils.RobotLogger;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Timer;

/*!
 * Created by carsonstorm on 10/25/2017.
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public class RackAndPinion {
    private static final Timer TIMER = new Timer();
    private static final String TAG = "RackAndPinion";

    private final CRServo crServo;
    private final LimitSwitch upperLimit;
    private final LimitSwitch lowerLimit;
    private final HardwareListeners hardwareListener;
    private final RobotLogger logger;

    private boolean isBusy = false;

    /**
     * Builds a new rack and pinion system using CRServo as the name for the continuous servo
     * @param robot     the robot to get the hardware and logger from
     * @return          the built rack and pinion system
     */
    public static RackAndPinion build(Robot robot) {
        return build(robot.hardwareMap, robot, robot.log);
    }

    /**
     * Builds a new rack and pinion system using CRServo as the name for the continuous servo
     * @param hardwareMap   the robot's hardware mape
     * @param telemetry     the opmode's telemetry
     * @return              the built rack and pinion system
     */
    public static RackAndPinion build(HardwareMap hardwareMap, HardwareListeners hardwareListener, Telemetry telemetry) {
        return build(hardwareMap, hardwareListener, new RobotLogger("RackAndPinion", telemetry));
    }

    /**
     * Builds a new rack and pinion system using CRServo as the name for the continuous servo
     * @param hardwareMap   the robot's hardware mape
     * @param logger        the robot's logger
     * @return              the built rack and pinion system
     */
    public static RackAndPinion build(HardwareMap hardwareMap, HardwareListeners hardwareListener, RobotLogger logger) {
        return new RackAndPinion(
                hardwareMap.crservo.get("CRServo"),
                new ModernRoboticsLimitSwitch(hardwareMap.digitalChannel.get("UpperLimit")),
                new ModernRoboticsLimitSwitch(hardwareMap.digitalChannel.get("LowerLimit")),
                hardwareListener,
                logger
        );
    }

    /**
     * Creates a new instance of RackAndPinion
     * @param crServo   the continuous servo
     * @param logger    the logger for debugging
     */
    public RackAndPinion(
            CRServo crServo,
            LimitSwitch upperLimit,
            LimitSwitch lowerLimit,
            HardwareListeners hardwareListener,
            RobotLogger logger
    ) {
        this.crServo = crServo;
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
        this.hardwareListener = hardwareListener;
        this.logger = logger;
    }

    /**
     * Check whether or not the servo is currently moving
     * @return  whether or not the servo is currently moving
     */
    public boolean isBusy() {
        return this.isBusy;
    }

    public void moveToUpperSync() throws Exception {
        debug("Moving to upper limit");
        start(1);

        while (! upperLimit.isPressed()) Thread.sleep(10);

        stop();
        debug("Reached upper limit");
    }

    public void moveToUpper() throws Exception {
        debug("Moving to upper limit");
        start(1);

        hardwareListener.on(upperLimit, LimitSwitch::isPressed, () -> {
            stop();

            debug("Reached upper limit");
        });
    }

    public void moveToLowerSync() throws Exception {
        debug("Moving to lower limit");
        start(-1);

        while (! lowerLimit.isPressed()) Thread.sleep(10);

        stop();
        debug("Reached lower limit");
    }

    public void moveToLower() throws Exception {
        debug("Moving to lower limit");

        start(-1);

        hardwareListener.on(lowerLimit, LimitSwitch::isPressed, () -> {
            stop();

            debug("Reached lower limit");
        });
    }

    private void debug(String line) {
        synchronized (logger) {
            logger.debug(line);
        }
    }

    private void start(double power) throws Exception {
        if (isBusy) throw new Exception("RackAndPinion is busy");

        synchronized (crServo) {
            crServo.setPower(power);
        }
        isBusy = true;
    }

    private void stop() {
        synchronized (crServo) {
            crServo.setPower(0);
        }

        isBusy = false;
    }
}
