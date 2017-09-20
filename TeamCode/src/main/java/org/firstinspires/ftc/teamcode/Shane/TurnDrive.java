package org.firstinspires.ftc.teamcode.Shane;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Created by lsatt on 7/20/2017.
 */

public class TurnDrive implements DriveTrain{
    private double rightPower;
    private double leftPower;
    private double[] drivePower = new double[2];

    @Override
    public double[] drive(Gamepad gamepad1) {
        rightPower = gamepad1.left_stick_y;
        leftPower = gamepad1.left_stick_y;
        rightPower += gamepad1.right_stick_x;
        leftPower -= gamepad1.right_stick_x;
        drivePower[0] = rightPower;
        drivePower[1] = leftPower;
        return drivePower;
    }
}
