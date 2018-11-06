package org.firstinspires.ftc.teamcode.Salsa.Hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

/**
 * Created by adityamavalankar on 11/4/18.
 */

public class Hardware {

    public DcMotor leftFront = null;
    public DcMotor leftBack = null;
    public DcMotor rightFront = null;
    public DcMotor rightBack = null;
    public BNO055IMU imu = null;
    public ColorSensor leftLine = null;
    public ColorSensor rightLine = null;
    public WebcamName webcamFront = null;

}
