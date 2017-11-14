package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Created by guberti on 6/6/2017.
 */

public class LogTick {
    Float gamepad1X1;
    Float gamepad1Y1;
    Float gamepad1X2;
    Float gamepad1Y2;
    Double heading;

    Integer frontLeftPos;
    Integer frontRightPos;
    Integer backLeftPos;
    Integer backRightPos;
    Integer zTypePos;
    Integer liftPos;
    Double frontLeftPower;
    Double frontRightPower;
    Double backLeftPower;
    Double backRightPower;
    Double zTypePower;
    Double liftPower;

    Double leftBlockClawPos;
    Double rightBlockClawPos;
    Double leftWhipSnakePos;
    Double rightWhipSnakePos;
    Double relicClawPos;
    Double relicClawFlipperPos;

    Long tick;

    public LogTick(Gamepad g1, NullbotHardware hardware) {
        frontLeftPos = hardware.motorArr[0].getCurrentPosition();
        frontLeftPower = hardware.motorArr[0].getPower();
        frontRightPos = hardware.motorArr[1].getCurrentPosition();
        frontRightPower = hardware.motorArr[1].getPower();
        backLeftPos = hardware.motorArr[2].getCurrentPosition();
        backLeftPower = hardware.motorArr[2].getPower();
        backRightPos = hardware.motorArr[3].getCurrentPosition();
        backRightPower = hardware.motorArr[3].getPower();

        zTypePos = hardware.zType.getCurrentPosition();
        zTypePower = hardware.zType.getPower();
        liftPos = hardware.lift.getCurrentPosition();
        liftPower = hardware.lift.getPower();

        leftBlockClawPos = hardware.leftBlockClaw.getPosition();
        rightBlockClawPos = hardware.rightBlockClaw.getPosition();
        leftWhipSnakePos = hardware.leftWhipSnake.getPosition();
        rightWhipSnakePos = 0.0;
        relicClawPos = hardware.relicClaw.getPosition();
        relicClawFlipperPos = hardware.relicClawFlipper.getPosition();

        heading = hardware.getGyroHeading();
        gamepad1X1 = g1.left_stick_x;
        gamepad1Y1 = g1.left_stick_y;
        gamepad1X2 = g1.right_stick_x;
        gamepad1Y2 = g1.right_stick_y;



        tick = hardware.currentTick;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();

        final Number[] order = new Number[]{tick, heading, gamepad1X1, gamepad1Y1, gamepad1X2, gamepad1Y2,
                frontLeftPower, frontLeftPos, frontRightPower, frontRightPos,
                backLeftPower, backLeftPos, backRightPower, backRightPos,
                zTypePower, zTypePos, liftPower, liftPos,
                leftBlockClawPos, rightBlockClawPos, leftWhipSnakePos, rightWhipSnakePos,
                relicClawPos, relicClawFlipperPos};

        for (int i = 0; i < order.length; i++) {
            builder.append(order[i].toString());
            builder.append(",");
        }

        return builder.toString();
    }

}
