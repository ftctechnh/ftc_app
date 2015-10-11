package com.qualcomm.ftcrobotcontroller.codelib;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by JackV on 9/20/15.
 */
public class ArcadeDrive {
    DcMotor motorRightBack, motorLeftBack, motorRightFront, motorLeftFront;

    public ArcadeDrive(DcMotor motorRightBack, DcMotor motorLeftBack, DcMotor motorRightFront, DcMotor motorLeftFront) {
        this.motorRightBack = motorRightBack;
        this.motorLeftBack = motorLeftBack;
        this.motorRightFront = motorRightFront;
        this.motorLeftFront = motorLeftFront;
    }

    public void arcadeDrive(float moveValue, float rotateValue, boolean squaredInputs) {
        float leftMotorSpeed, rightMotorSpeed;

        moveValue = limit(moveValue);
        rotateValue = limit(rotateValue);

        if (squaredInputs) {
            // square the inputs (while preserving the sign) to increase fine control while permitting full power
            if (moveValue >= 0.0) {
                moveValue = (moveValue * moveValue);
            } else {
                moveValue = -(moveValue * moveValue);
            }
            if (rotateValue >= 0.0) {
                rotateValue = (rotateValue * rotateValue);
            } else {
                rotateValue = -(rotateValue * rotateValue);
            }
        }

        if (moveValue > 0.0) {
            if (rotateValue > 0.0) {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = Math.max(moveValue, rotateValue);
            } else {
                leftMotorSpeed = Math.max(moveValue, -rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            }
        } else {
            if (rotateValue > 0.0) {
                leftMotorSpeed = -Math.max(-moveValue, rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            } else {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = -Math.max(-moveValue, -rotateValue);
            }
        }

        motorLeftBack.setPower(leftMotorSpeed);
        motorLeftFront.setPower(leftMotorSpeed);
        motorRightBack.setPower(rightMotorSpeed);
        motorRightFront.setPower(rightMotorSpeed);
    }

    protected static float limit(float num) {
        if (num > 1.0) {
            return 1;
        }
        if (num < -1.0) {
            return -1;
        }
        return num;
    }
}
