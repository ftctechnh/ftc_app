package org.firstinspires.ftc.teamcode.ErikCode.DaquanOpModes;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

/*
-Name: Daquan Hardware Map
- Creator[s]: Erik
-Date Created: 10/7/17
-Objective: to create a class that sets up basic functions and variables for the holonomic robot
            with active intake, named "Daquan".
 */

public class Daquan_Hardware {

    public DcMotor fleft, fright, bleft, bright;
    public static final float DRIVE_POWER = 0.3f;
    public static final float INTAKE_POWER = 0.16f;
    public BNO055IMU gyro;
    public double heading;
    public double currentDrivePower = DRIVE_POWER;
    private HardwareMap hwMap;
    private Telemetry telemetry;

    public Daquan_Hardware(HardwareMap hwmap, Telemetry telem, boolean usesGyro) {
        hwMap = hwmap;
        telemetry = telem;

        telemetry.addData("Ready to begin", false);
        telemetry.update();

        fleft = hwMap.dcMotor.get("fleft");
        fright = hwMap.dcMotor.get("fright");
        bleft = hwMap.dcMotor.get("bleft");
        bright = hwMap.dcMotor.get("bright");

        fright.setDirection(DcMotor.Direction.REVERSE);
        bright.setDirection(DcMotor.Direction.REVERSE);

        if(usesGyro) {
            gyro = hwMap.get(BNO055IMU.class, "imu");
            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
            parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
            parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
            parameters.calibrationDataFile = "AdafruitIMUCalibration.json";
            parameters.loggingEnabled = true;
            parameters.loggingTag = "IMU";
            parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
            gyro.initialize(parameters);
        }

        telemetry.addData("Ready to begin", true);
        telemetry.update();
    }

    public void drive(double fl, double fr, double bl, double br) {
        fleft.setPower(clipValue(fl));
        fright.setPower(clipValue(fr));
        bleft.setPower(clipValue(bl));
        bright.setPower(clipValue(br));
    }

    public void updateGyro() {
        heading = gyro.getAngularOrientation().toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.ZYX).firstAngle + Math.PI / 2;
    }


    double clipValue(double value) {
        if(value > currentDrivePower || value < -currentDrivePower)
            return(value / Math.abs(value) * currentDrivePower);
        else
            return value;
    }
}
