package org.firstinspires.ftc.team9853.systems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.chathamrobotics.common.Robot;
import org.chathamrobotics.common.utils.RobotLogger;

/**
 * Created by carsonstorm on 11/3/2017.
 */

public class GlyphGripper {
    public static final int TOP_LEFT_BOTTOM_RIGHT_INDEX = 0;
    public static final int TOP_RIGHT_BOTTOM_LEFT_INDEX = 1;

    public static final double[] CLOSED_POSITIONS = {0, 1};
    public static final double[] OPEN_POSITIONS = {0.6, 0.4};
    public static final double[] GRIP_POSITIONS = {0.5, 0,5};

    private Servo topLeftServo;
    private Servo topRightServo;
    private RobotLogger logger;

    public static GlyphGripper build(Robot robot) {
        return build(robot.hardwareMap, robot.log);
    }

    public static GlyphGripper build(HardwareMap hardwareMap, RobotLogger logger) {
        return new GlyphGripper(
                hardwareMap.servo.get("TopLeftGripper"),
                hardwareMap.servo.get("TopRightGripper"),
                logger
        );
    }

    public GlyphGripper(Servo topLeftBottomRight, Servo topRightBottomLeft, RobotLogger logger) {
        this.topLeftServo = topLeftBottomRight;
        this.topRightServo = topRightBottomLeft;
        this.logger = logger;
    }

    public void close() {
        topLeftServo.setPosition(CLOSED_POSITIONS[TOP_LEFT_BOTTOM_RIGHT_INDEX]);
        topRightServo.setPosition(CLOSED_POSITIONS[TOP_RIGHT_BOTTOM_LEFT_INDEX]);
    }

    public void open() {
        topLeftServo.setPosition(OPEN_POSITIONS[TOP_LEFT_BOTTOM_RIGHT_INDEX]);
        topRightServo.setPosition(OPEN_POSITIONS[TOP_RIGHT_BOTTOM_LEFT_INDEX]);
    }

    public void grip() {
        topLeftServo.setPosition(GRIP_POSITIONS[TOP_LEFT_BOTTOM_RIGHT_INDEX]);
        topRightServo.setPosition(GRIP_POSITIONS[TOP_RIGHT_BOTTOM_LEFT_INDEX]);
    }
}
