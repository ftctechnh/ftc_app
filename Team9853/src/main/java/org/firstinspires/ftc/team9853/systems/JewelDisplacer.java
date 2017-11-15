package org.firstinspires.ftc.team9853.systems;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.chathamrobotics.common.robot.Robot;
import org.chathamrobotics.common.systems.RackAndPinion;
import org.chathamrobotics.common.utils.RGBAColor;
import org.chathamrobotics.common.hardware.utils.HardwareListener;
import org.chathamrobotics.common.IsBusyException;
import org.chathamrobotics.common.robot.RobotLogger;

import java.util.Timer;

@SuppressWarnings({"WeakerAccess", "unused", "SameParameterValue"})
public class JewelDisplacer {
    public static final double ARM_UP_POSITION = 1.0;
    public static final double ARM_DOWN_POSITION = 0.3;
    public static final long TIME_SHIFT_LEFT = 1000;
    public static final long TIME_SHIFT_RIGHT = 1000;
    private static final Timer timer = new Timer();

    private final Servo jewelArmServo;
    private final RackAndPinion rackAndPinion;
    private final ColorSensor jewelColorSensor;
    private final RobotLogger logger;

    private boolean isArmUp;
    private boolean isShifting;

    /**
     * Builds a new JewelDisplacer using the standard hardware names.
     * jewelArmServo -> "JewelArm"
     * armShifterServo -> "ArmShifter"
     * jewelColorSensor -> "JewelSensor"
     * @param robot     the robot to get the logger and hardware from
     * @return          the built JewelDisplacer
     */
    /**
     * Builds a new JewelDisplacer using the standard hardware names.
     * jewelArmServo -> "JewelArm"
     * armShifterServo -> "ArmShifter"
     * jewelColorSensor -> "JewelColor"
     * @param robot     the robot to get the logger and hardware from
     * @return              the built JewelDisplacer
     */
    public static JewelDisplacer build(Robot robot) {
        return build(robot.getHardwareMap(), robot, robot.log);
    }

    /**
     * Builds a new JewelDisplacer using the standard hardware names.
     * jewelArmServo -> "JewelArm"
     * armShifterServo -> "ArmShifter"
     * jewelColorSensor -> "JewelColor"
     * @param hardwareMap   the robot's hardware map
     * @param logger        the robot's logger
     * @return              the built JewelDisplacer
     */
    public static JewelDisplacer build(HardwareMap hardwareMap, HardwareListener hardwareListener, RobotLogger logger) {
        return new JewelDisplacer(
                hardwareMap.servo.get("JewelArm"),
                RackAndPinion.build(hardwareMap, hardwareListener, logger),
                hardwareMap.colorSensor.get("JewelColor"),
                logger
        );
    }

    /**
     * Creates a new instance of JewelDisplacer
     * @param jewelArmServo     the arm the drops to knock over the jewel
     * @param rackAndPinion     the rack and pinion
     * @param jewelColorSensor  the color sensor used to identify the jewel on the right
     * @param logger            the logger to use for debugging
     */
    public JewelDisplacer(Servo jewelArmServo, RackAndPinion rackAndPinion, ColorSensor jewelColorSensor, RobotLogger logger) {
        this.jewelArmServo = jewelArmServo;
        this.rackAndPinion = rackAndPinion;
        this.jewelColorSensor = jewelColorSensor;

        this.logger = logger;

        this.isArmUp = Math.abs(jewelArmServo.getPosition() - ARM_UP_POSITION) <= 1e-10;
    }

    /**
     * Raises the jewel arm
     */
    public void raise() {
        logger.debug("Raising Jewel Arm");

        if (! isArmUp) {
            jewelArmServo.setPosition(ARM_UP_POSITION);
            isArmUp = true;
        }
    }

    /**
     * Drops the jewel arm
     */
    public void drop() {
        logger.debug("Dropping Jewel Arm");

        if (isArmUp) {
            jewelArmServo.setPosition(ARM_DOWN_POSITION);
            isArmUp = false;
        }
    }

    /**
     * Shifts the jewel arm to the left
     * @throws IsBusyException  throw if the jewel displacer is already busy
     */
    public void shiftLeft() throws IsBusyException {
        logger.debug("Shifting the Jewel Arm to the left");

        rackAndPinion.moveToLower();
    }

    /**
     * Shifts the jewel arm to the left synchronously
     * @throws IsBusyException  throw if the jewel displacer is already busy
     */
    public void shiftLeftSync() throws InterruptedException, IsBusyException {
        logger.debug("Shifting the Jewel Arm to the left");

        rackAndPinion.moveToLowerSync();
    }

    /**
     * Shifts the jewel arm to the right
     * @throws IsBusyException  throw if the jewel displacer is already busy
     */
    public void shiftRight() throws IsBusyException {
        logger.debug("Shifting the Jewel Arm to the right");

        rackAndPinion.moveToUpper();
    }

    /**
     * Shifts the jewel arm to the right synchronously
     * @throws IsBusyException  throw if the jewel displacer is already busy
     */
    public void shiftRightSync() throws InterruptedException, IsBusyException {
        logger.debug("Shifting the Jewel Arm to the left");

        rackAndPinion.moveToUpperSync();
    }

    public RGBAColor getColor() {
        return new RGBAColor(
                jewelColorSensor.red(),
                jewelColorSensor.green(),
                jewelColorSensor.blue(),
                jewelColorSensor.alpha()
        );
    }
}
