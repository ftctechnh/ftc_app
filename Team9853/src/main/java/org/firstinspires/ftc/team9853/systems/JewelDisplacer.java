package org.firstinspires.ftc.team9853.systems;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.chathamrobotics.common.RGBAColor;
import org.chathamrobotics.common.hardware.modernrobotics.ModernRoboticsLimitSwitch;
import org.chathamrobotics.common.hardware.utils.HardwareListener;
import org.chathamrobotics.common.robot.Robot;
import org.chathamrobotics.common.robot.RobotLogger;
import org.chathamrobotics.common.systems.IsBusyException;
import org.chathamrobotics.common.systems.RackAndPinion;

import java.util.Timer;

@SuppressWarnings({"WeakerAccess", "unused", "SameParameterValue"})
public class JewelDisplacer {
    public static final double ARM_UP_POSITION = 0;
    public static final double ARM_DOWN_POSITION = 0.65;
    public static final long TIME_SHIFT_LEFT = 1000;
    public static final long TIME_SHIFT_RIGHT = 1000;
    private static final Timer timer = new Timer();

    private final Servo jewelArmServo;
    private final RackAndPinion rackAndPinion;
    private final ModernRoboticsI2cColorSensor jewelColorSensor;
    private final RobotLogger logger;

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
                new RackAndPinion(
                        hardwareMap.crservo.get("JewelArmShifter"),
                        new ModernRoboticsLimitSwitch(hardwareMap.digitalChannel.get("UpperLimit")),
                        new ModernRoboticsLimitSwitch(hardwareMap.digitalChannel.get("LowerLimit")),
                        hardwareListener,
                        logger
                ),
                (ModernRoboticsI2cColorSensor) hardwareMap.colorSensor.get("JewelColor"),
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
    public JewelDisplacer(Servo jewelArmServo, RackAndPinion rackAndPinion, ModernRoboticsI2cColorSensor jewelColorSensor, RobotLogger logger) {
        this.jewelArmServo = jewelArmServo;
        this.rackAndPinion = rackAndPinion;
        this.jewelColorSensor = jewelColorSensor;
        jewelColorSensor.enableLed(true);
        logger.debug(Integer.toHexString(jewelColorSensor.getI2cAddress().get8Bit()));

        this.logger = logger;
    }

    /**
     * Raises the jewel arm
     */
    public void raise() {
        logger.debug("Raising Jewel Arm");

        jewelArmServo.setPosition(ARM_UP_POSITION);
    }

    /**
     * Drops the jewel arm
     */
    public void drop() {
        logger.debug("Dropping Jewel Arm");

        jewelArmServo.setPosition(ARM_DOWN_POSITION);
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

    public int getColor() {
        return jewelColorSensor.readUnsignedByte(ModernRoboticsI2cColorSensor.Register.COLOR_NUMBER);
    }
}
