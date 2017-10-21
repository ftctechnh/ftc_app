package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.Hardware9330;

/**
 * Created by robot on 9/25/2017.
 */

public class Drive9330 {
    Hardware9330 robotMap = new Hardware9330();

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
}
