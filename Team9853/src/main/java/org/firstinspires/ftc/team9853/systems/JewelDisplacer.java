package org.firstinspires.ftc.team9853.systems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.chathamrobotics.common.Robot;
import org.chathamrobotics.common.utils.RobotLogger;

/**
 * Created by carsonstorm on 10/24/2017.
 */

@SuppressWarnings("WeakerAccess")
public class JewelDisplacer {
    public static final double ARM_UP_POSITION = 0.0;
    public static final double ARM_DOWN_POSITION = 0.0;
    public static final long TIME_SHIFT_LEFT = 1000;
    public static final long TIME_SHIFT_RIGHT = 1000;

    private Servo jewelArmServo;
    private CRServo armShifterServo;
    private ColorSensor jewelColorSensor;

    private RobotLogger logger;

    private boolean isArmUp;

    /**
     * Builds a new JewelDisplacer using the standard hardware names.
     * jewelArmServo -> "JewelArm"
     * armShifterServo -> "ArmShifter"
     * jewelColorSensor -> "JewelSensor"
     * @param robot     the robot to get the logger and hardware from
     * @return          the built JewelDisplacer
     */
    public static JewelDisplacer build(Robot robot) {
        return build(robot.hardwareMap, robot.log);
    }

    /**
     * Builds a new JewelDisplacer using the standard hardware names.
     * jewelArmServo -> "JewelArm"
     * armShifterServo -> "ArmShifter"
     * jewelColorSensor -> "JewelSensor"
     * @param hardwareMap   the robot's hardware map
     * @param logger        the robot's logger
     * @return              the built JewelDisplacer
     */
    public static JewelDisplacer build(HardwareMap hardwareMap, RobotLogger logger) {
        return new JewelDisplacer(
                hardwareMap.servo.get("JewelArm"),
                hardwareMap.crservo.get("ArmShifter"),
                hardwareMap.colorSensor.get("JewelSensor"),
                logger
        );
    }

    /**
     * Creates a new instance of JewelDisplacer
     * @param jewelArmServo     the arm the drops to knock over the jewel
     * @param armShifterServo   the servo used to shift the arm right and left
     * @param jewelColorSensor  the color sensor used to identify the jewel on the right
     * @param logger            the logger to use for debugging
     */
    public JewelDisplacer(Servo jewelArmServo, CRServo armShifterServo, ColorSensor jewelColorSensor, RobotLogger logger) {
        this.jewelArmServo = jewelArmServo;
        this.armShifterServo = armShifterServo;
        this.jewelColorSensor = jewelColorSensor;

        this.logger = logger;

        this.isArmUp = Math.abs(jewelArmServo.getPosition() - ARM_UP_POSITION) <= 1e-10;
    }

    public void raise() {
        jewelArmServo.setPosition(ARM_UP_POSITION);
    }

    public void drop() {
        jewelArmServo.setPosition(ARM_DOWN_POSITION);
    }

    public void shiftRightSync() {
        long end = System.currentTimeMillis() + TIME_SHIFT_RIGHT;

        while (System.currentTimeMillis() < end) {
            armShifterServo.setPower(1);
        }

        armShifterServo.setPower(0);
    }

    public void shiftLeftSync() {
        long end = System.currentTimeMillis() + TIME_SHIFT_LEFT;

        while (System.currentTimeMillis() < end) {
            armShifterServo.setPower(-1);
        }

        armShifterServo.setPower(0);
    }
}
