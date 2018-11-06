package org.firstinspires.ftc.teamcode.Salsa.Hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Salsa.Constants;

/**
 * Created by adityamavalankar on 11/4/18.
 */

public class Robot {

    public HardwareMap hwmap = null;
    public Constants constants = new Constants();
    public Hardware hardware = new Hardware();

    public void initDrivetrain(HardwareMap inputMap) {

        hwmap = inputMap;

        //Drivetrain
        hardware.leftFront = hwmap.get(DcMotor.class, constants.LEFT_FRONT_NAME);
        hardware.leftBack = hwmap.get(DcMotor.class, constants.LEFT_BACK_NAME);
        hardware.rightFront = hwmap.get(DcMotor.class, constants.RIGHT_FRONT_NAME);
        hardware.rightBack = hwmap.get(DcMotor.class, constants.RIGHT_BACK_NAME);
    }

    public void initSensors(HardwareMap inputMap) {

        hwmap = inputMap;

        //Sensors
        hardware.imu = hwmap.get(BNO055IMU.class, constants.GYRO_NAME);

    }

    public void initWebcam(HardwareMap inputMap) {

        hwmap = inputMap;

        //Webcam
        hardware.webcamFront = hwmap.get(WebcamName.class, constants.WEBCAM_FRONT_NAME);

    }
}
