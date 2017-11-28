package org.firstinspires.ftc.team9853;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 *
 * @Last Modified by: storm
 * @Last Modified time: 9/17/2017
 */


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.chathamrobotics.common.Controller;
import org.chathamrobotics.common.robot.Robot;
import org.chathamrobotics.common.robot.RobotFace;
import org.chathamrobotics.common.systems.GyroHandler;
import org.chathamrobotics.common.systems.HolonomicDriver;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.team9853.systems.GlyphGripper;
import org.firstinspires.ftc.team9853.systems.JewelDisplacer;

/**
 * Team 9853's robot
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Robot9853 extends Robot {

    private static final int GYRO_TOERANCE = 15;
    private static final double GYRO_TOERANCE_RAD = Math.toRadians(25);
    private static final int GYRO_SCALE_FACTOR = 2;

    public HolonomicDriver driver;
    public GlyphGripper glyphGripper;
    public DcMotor leftLift;
    public DcMotor rightLift;
    public JewelDisplacer jewelDisplacer;
    private GyroHandler gyroHandler;

    public static Robot9853 build(OpMode opMode) {
        return new Robot9853(opMode.hardwareMap, opMode.telemetry);
    }

    public Robot9853(HardwareMap hardwareMap, Telemetry telemetry) {
        super(hardwareMap, telemetry);
    }

    @Override
    public void init() {
        driver = HolonomicDriver.build(this);
        glyphGripper = GlyphGripper.build(this);
        leftLift = getHardwareMap().dcMotor.get("LeftLift");
        rightLift = getHardwareMap().dcMotor.get("RightLift");
        jewelDisplacer = JewelDisplacer.build(this);
        gyroHandler = GyroHandler.build(this);

        leftLift.setDirection(DcMotorSimple.Direction.REVERSE);
        jewelDisplacer.raise();
        glyphGripper.close();
        gyroHandler.init();
    }

    @Override
    public void start() {
        glyphGripper.open();

        while (! gyroHandler.isInitialized());
    }

    @Override
    public void stop() {
        jewelDisplacer.raise();

        super.stop();
    }

    public void setLiftPower(double power) {
        leftLift.setPower(power);
        rightLift.setPower(power);
    }

    public void rotate(double target) {
        gyroHandler.untilAtTarget(-target, GYRO_TOERANCE_RAD, (double diff) ->
                        driver.rotate(Range.clip(diff * GYRO_SCALE_FACTOR / Math.PI, 1, -1)),
                () ->
                        driver.stop()
        );
    }

    public void rotate(double target, AngleUnit angleUnit) {
        gyroHandler.untilAtTarget(-target, GYRO_TOERANCE, angleUnit, (double diff) -> {
                    driver.rotate(Range.clip(diff * GYRO_SCALE_FACTOR / angleUnit.fromDegrees(180), 1, -1));
                },
                () ->
                        driver.stop()
        );
    }

    public void rotateSync(double target) throws InterruptedException {
        gyroHandler.untilAtTargetSync(-target, (double diff) ->
                driver.rotate(Range.clip(diff * GYRO_SCALE_FACTOR / Math.PI, 1, -1)));
        driver.stop();
    }

    public void rotateSync(double target, AngleUnit angleUnit) throws InterruptedException {
        gyroHandler.untilAtTargetSync(-target, angleUnit, (double diff) -> driver.rotate(Range.clip(diff * GYRO_SCALE_FACTOR / angleUnit.fromDegrees(180), 1, -1)));
        driver.stop();
    }

    public void driveWithControls(Gamepad gp) {
        double magnitude = Math.hypot(gp.left_stick_x, -gp.left_stick_y);
        double direction = Math.atan2(-gp.left_stick_y, gp.left_stick_x);

        if (gp.dpad_up) driver.setFront(RobotFace.FRONT);
        if (gp.dpad_down) driver.setFront(RobotFace.BACK);
        if (gp.dpad_left) driver.setFront(RobotFace.LEFT);
        if (gp.dpad_right) driver.setFront(RobotFace.RIGHT);

        driver.setDrivePower(direction, magnitude, gp.right_stick_x);
    }

    public void driveWithControls(Controller controller) {
        float x = controller.left_stick_x, y = -controller.left_stick_y, rotation = controller.right_stick_x;
        double magnitude = Math.hypot(x, y);
        double direction = Math.atan2(y, x);

        if (controller.padUpState == Controller.ButtonState.TAPPED);
            driver.setFront(RobotFace.FRONT);
        if (controller.padDownState == Controller.ButtonState.TAPPED);
            driver.setFront(RobotFace.BACK);
        if (controller.padLeftState == Controller.ButtonState.TAPPED);
            driver.setFront(RobotFace.LEFT);
        if (controller.padRightState == Controller.ButtonState.TAPPED);
        driver.setFront(RobotFace.RIGHT);
    }
}
