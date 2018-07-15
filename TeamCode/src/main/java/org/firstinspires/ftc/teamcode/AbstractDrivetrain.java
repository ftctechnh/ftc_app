package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public abstract class AbstractDrivetrain {

    protected DcMotor lfDriveM, rfDriveM, lbDriveM, rbDriveM;
    protected HardwareMap hwMap;
    protected BNO055IMU imu;               // IMU Gyro sensor inside of REV Hub
    protected Orientation angles;

    public AbstractDrivetrain(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        this.lfDriveM  = hwMap.get(DcMotor.class, "FL");
        this.rfDriveM = hwMap.get(DcMotor.class, "FR");
        this.lbDriveM  = hwMap.get(DcMotor.class, "BL");
        this.rbDriveM = hwMap.get(DcMotor.class, "BR");
        lfDriveM.setDirection(DcMotor.Direction.FORWARD);
        lbDriveM.setDirection(DcMotor.Direction.FORWARD);
        rfDriveM.setDirection(DcMotor.Direction.REVERSE);
        rbDriveM.setDirection(DcMotor.Direction.REVERSE);

        // Set all motors to zero power
        stop();

        // Set all motors to run without encoders.
        setMotorMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setMotorMode(DcMotor.RunMode.RUN_USING_ENCODER);

        lfDriveM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lbDriveM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rfDriveM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rbDriveM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        BNO055IMU.Parameters parameters_IMU = new BNO055IMU.Parameters();
        parameters_IMU.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters_IMU.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters_IMU.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters_IMU.loggingEnabled = true;
        parameters_IMU.loggingTag = "IMU";
        parameters_IMU.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu = ahwMap.get(BNO055IMU.class, "imu3");
        imu.initialize(parameters_IMU);

    }

    public abstract void drive(double xVelocity, double yVelocity, double wVelocity);

    public abstract void encoderDrive(double yDist, double maxSpeed);

    public abstract void gyroTurn(int wDist, double maxSpeed);

    public void stop(){
        lfDriveM.setPower(0);
        rfDriveM.setPower(0);
        lbDriveM.setPower(0);
        rbDriveM.setPower(0);
    }

    public void setMotorMode(DcMotor.RunMode mode) {
        rfDriveM.setMode(mode);
        rbDriveM.setMode(mode);
        lfDriveM.setMode(mode);
        lbDriveM.setMode(mode);
    }


}
