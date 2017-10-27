package org.firstinspires.ftc.team9853.systems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.chathamrobotics.common.Robot;
import org.chathamrobotics.common.utils.RobotLogger;

import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings({"WeakerAccess", "unused", "SameParameterValue"})
public class JewelDisplacer {
    public static final double ARM_UP_POSITION = 1.0;
    public static final double ARM_DOWN_POSITION = 0.3;
    public static final long TIME_SHIFT_LEFT = 1000;
    public static final long TIME_SHIFT_RIGHT = 1000;
    private static final Timer timer = new Timer();

    private final Servo jewelArmServo;
    private final CRServo armShifterServo;
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

    /**
     * Raises the jewel arm
     */
    public void raise() {
        if (! isArmUp) {
            jewelArmServo.setPosition(ARM_UP_POSITION);
            isArmUp = true;
        }
    }

    /**
     * Drops the jewel arm
     */
    public void drop() {
        if (isArmUp) {
            jewelArmServo.setPosition(ARM_DOWN_POSITION);
            isArmUp = false;
        }
    }

    public void shiftLeftSync() {
        logger.debug("Shifting jewel arm to the left");
        long end = System.currentTimeMillis() + TIME_SHIFT_LEFT;

        setShiftPower(-1);

        //noinspection StatementWithEmptyBody
        while (System.currentTimeMillis() < end) {
            // delay
        }

        setShiftPower(0);
        logger.debug("Finished shifting jewel arm to the left");
    }

    public void shiftLeft() {
        shiftLeft(null);
    }

    public void shiftLeft(Runnable cb) {
        logger.debug("Shifting jewel arm to the left");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                setShiftPower(0);
                logger.debug("Finished shifting jewel arm to the left");

                if (cb != null) cb.run();
            }
        }, TIME_SHIFT_LEFT);

        setShiftPower(-1);
    }

    public void shiftRightSync() {
        logger.debug("Shifting jewel arm to the right");
        long end = System.currentTimeMillis() + TIME_SHIFT_RIGHT;

        setShiftPower(1);

        //noinspection StatementWithEmptyBody
        while (System.currentTimeMillis() < end) {
            // delay
        }

        setShiftPower(0);
        logger.debug("Finished shifting jewel arm to the right");
    }

    public void shiftRight() {
        shiftRight(null);
    }

    public void shiftRight(Runnable cb) {
        logger.debug("Shifting jewel arm to the right");

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                setShiftPower(0);
                logger.debug("Finished shifting jewel arm to the right");

                if (cb != null) cb.run();
            }
        }, TIME_SHIFT_RIGHT);

        setShiftPower(1);
    }

    private void setShiftPower(double power) {
        synchronized ( armShifterServo ) {
            armShifterServo.setPower(1);
        }
    }
}
