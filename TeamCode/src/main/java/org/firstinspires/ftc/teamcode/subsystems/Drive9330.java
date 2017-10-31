package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Hardware9330;

/**
 * Created by robot on 9/25/2017.
 */

public class Drive9330 {
    private Hardware9330 hwMap = null;
    Integer turnError = 1;
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

    public void driveDistance(int distance) {
        hwMap.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hwMap.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hwMap.leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        hwMap.rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Integer encoderDistance = distance; //not quite sure what numbers encoder needs, so calculate here
        Hardware9330.rightMotor.setTargetPosition(encoderDistance);
        Hardware9330.leftMotor.setTargetPosition(encoderDistance);
        hwMap.leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        hwMap.rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
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
