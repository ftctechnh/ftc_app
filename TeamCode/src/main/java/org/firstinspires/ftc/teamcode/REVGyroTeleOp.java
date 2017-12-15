package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Kaden on 12/14/2017.
 */

public class REVGyroTeleOp extends OpMode {
    REVGyro gyro;
    public void init() {
        gyro = new REVGyro(hardwareMap.get(BNO055IMU.class, "imu"));
        gyro.calibrate();
    }
    public void loop() {
        telemetry.addData("Current heading: ", gyro.getHeading());
        telemetry.update();
    }
}
