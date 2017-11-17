package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class TankDriveTrain {

    private DcMotor motorLeft, motorRight;

    private final int MOTOR_0_DIRECTION = -1;
    private final int MOTOR_1_DIRECTION = 1;

    public TankDriveTrain(DcMotor motor0, DcMotor motor1) {

        this.motorLeft = motor0;
        this.motorRight = motor1;

    }

    public void move(double motor0Power, double motor1Power) {

        motorLeft.setPower(MOTOR_0_DIRECTION * motor0Power);
        motorRight.setPower(MOTOR_1_DIRECTION * motor1Power);

    }

}
