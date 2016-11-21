package org.firstinspires.ftc.team9374;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by lego7_000 on 11/21/2016.
 */

public class MotorMatr {

    public DcMotor[][] Motors = new DcMotor[10][4];

    public void runMotors(int column, float power) {
        for (int i = 0; i < this.Motors.length; i++) {
            this.Motors[i][column-1].setPower(power);
        }
    }
}
