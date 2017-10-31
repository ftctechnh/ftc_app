package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Hardware9330;

/**
 * Created by robot on 9/25/2017.
 */

public class Drive9330 {
    private Hardware9330 hwMap = null;
    Integer turnError = 2;
    Gyro9330 gyro;

    public Drive9330(Hardware9330 robotMap) {
        hwMap = robotMap;
        gyro = new Gyro9330(robotMap);
        if (gyro!=null) if (!gyro.isCalibrated()) gyro.init();
    }

    public void driveForward(int speed) { //Speed mush be between 0 and 100
        Hardware9330.leftMotor.setPower(-speed);
        Hardware9330.rightMotor.setPower(speed);
    }

    public void turnLeft(int speed) { //Speed mush be between 0 and 100
        Hardware9330.leftMotor.setPower(speed);
        Hardware9330.rightMotor.setPower(speed);
    }

    public void turnRight(int speed) { //Speed mush be between 0 and 100
        Hardware9330.leftMotor.setPower(speed);
        Hardware9330.rightMotor.setPower(-speed);
    }

    public void stopDrive() {
        Hardware9330.leftMotor.setPower(0);
        Hardware9330.rightMotor.setPower(0);
    }

    public void gyroTurn(float degrees, int speed) {
        Double initialAngle = gyro.getPitch();
        while (gyro.getPitch() - initialAngle < degrees - turnError || gyro.getPitch() - initialAngle > degrees + turnError) {
            if (gyro.getPitch() - initialAngle < degrees - turnError) {
                turnRight(speed);
            } else {
                turnLeft(speed);
            }
        }
    }
}
