package org.firstinspires.ftc.teamcode.core;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import static java.lang.Thread.sleep;


/**
 * The class that handles the initialization and calibration of all of the variables required for the Hardware
 */
public class Hardware
{
    public DcMotor frontRightDrive;
    public DcMotor frontLeftDrive;
    public DcMotor backRightDrive;
    public DcMotor backLeftDrive;

    public Servo markerServo;

    public BNO055IMU gyro;



    public Telemetry telemetry;
    public HardwareMap hardwareMap;

    /**
     * init EVERYTHING
     */
    public void initAllHardware(HardwareMap hardwareMap)
    {
        frontRightDrive = hardwareMap.dcMotor.get("front_right_drive");
        frontLeftDrive = hardwareMap.dcMotor.get("front_left_drive");
        backRightDrive = hardwareMap.dcMotor.get("back_right_drive");
        backLeftDrive =  hardwareMap.dcMotor.get("back_left_drive");

        markerServo = hardwareMap.servo.get("marker_servo");
        gyro = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode                = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = false;
        gyro.initialize(parameters);



        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);

    }
}