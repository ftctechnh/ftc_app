package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by pston on 10/22/2017
 */

public class DriveTrainTest {

    DcMotor motor1;

    public DriveTrainTest (DcMotor testMotor) {
        motor1 = testMotor;
    }

    public void turnLeft(double testFloat) {
        if (testFloat == 5) {
            motor1.setPower(0.5);
        }
    }

}
