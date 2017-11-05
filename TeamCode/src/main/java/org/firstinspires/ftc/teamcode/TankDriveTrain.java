package org.firstinspires.ftc.teamcode;

/**
 * Created by DiegoGutiDev on 11/5/17.
 */

import com.qualcomm.robotcore.hardware.DcMotor;

public class TankDriveTrain {

    private DcMotor motor0, motor1;
    private final int MOTOR_0_DIRECTION = -1;
    private final int MOTOR_1_DIRECTION = 1;

    public TankDriveTrain(DcMotor motor0, DcMotor motor1) {

        this.motor0 = motor0;
        this.motor1 = motor1;

    }

    public void move(double motor0Power, double motor1Power) {

        motor0.setPower(MOTOR_0_DIRECTION * motor0Power);
        motor1.setPower(MOTOR_1_DIRECTION * motor1Power);

    }

}
