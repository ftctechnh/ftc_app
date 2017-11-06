package org.firstinspires.ftc.team9853.systems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.chathamrobotics.common.Robot;
import org.chathamrobotics.common.utils.robot.RobotLogger;

/*!
 * Created by carsonstorm on 11/3/2017.
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class GlyphGripper {
    private static final int RIGHT_INDEX = 0;
    private static final int LEFT_INDEX = 1;
    private static final double[] CLOSED_POSITIONS = {1, 0};
    private static final double[] OPEN_POSITIONS = {0.4, 0.6};
    private static final double[] GRIP_POSITIONS = {0.5, 0.5};

    private Servo leftServo;
    private Servo rightServo;
    private RobotLogger logger;

    public static GlyphGripper build(Robot robot) {
        return build(robot.getHardwareMap(), robot.log);
    }

    public static GlyphGripper build(HardwareMap hardwareMap, RobotLogger logger) {
        return new GlyphGripper(
                hardwareMap.servo.get("LeftGripper"),
                hardwareMap.servo.get("RightGripper"),
                logger
        );
    }

    public GlyphGripper(Servo leftServo, Servo rightServo, RobotLogger logger) {
        this.leftServo = leftServo;
        this.rightServo = rightServo;
        this.logger = logger;
    }

    public void close() {
        logger.info("Closing Glyph Gripper");

        leftServo.setPosition(CLOSED_POSITIONS[LEFT_INDEX]);
        rightServo.setPosition(CLOSED_POSITIONS[RIGHT_INDEX]);
    }

    public void open() {
        logger.info("Opening Glyph Gripper");

        leftServo.setPosition(OPEN_POSITIONS[LEFT_INDEX]);
        rightServo.setPosition(OPEN_POSITIONS[RIGHT_INDEX]);
    }

    public void grip() {
        logger.info("Gripping Glyph Gripper");

        leftServo.setPosition(GRIP_POSITIONS[LEFT_INDEX]);
        rightServo.setPosition(GRIP_POSITIONS[RIGHT_INDEX]);
    }
}
