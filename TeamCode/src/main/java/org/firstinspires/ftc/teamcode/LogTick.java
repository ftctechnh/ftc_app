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
    Double frontLeftPower;
    Double frontRightPower;
    Double backLeftPower;
    Double backRightPower;

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
                backLeftPower, backLeftPos, backRightPower, backRightPos};

        for (int i = 0; i < order.length; i++) {
            builder.append(order[i].toString());
            builder.append(",");
        }

        return builder.toString();
    }

}
