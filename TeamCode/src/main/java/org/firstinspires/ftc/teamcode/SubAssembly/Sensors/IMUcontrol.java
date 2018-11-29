package org.firstinspires.ftc.teamcode.SubAssembly.Sensors;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class IMUcontrol {
    /* Declare private class object */
    private LinearOpMode opmode = null;     /* local copy of opmode class */

    BNO055IMU imu;
    Orientation angles;

    public double startAngle;
    public double currentAngle;
    public double trueAngle;

    /* Subassembly constructor */
    public IMUcontrol() {
    }

    public void init(LinearOpMode opMode) {
        HardwareMap hwMap;

        /* Set local copies from opmode class */
        opmode = opMode;
        hwMap = opMode.hardwareMap;

        /* initialize IMU */
        // Send telemetry message to signify robot waiting;
        BNO055IMU.Parameters imu_parameters = new BNO055IMU.Parameters();
        imu_parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu_parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imu_parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        imu_parameters.loggingEnabled = true;
        imu_parameters.loggingTag = "IMU";
        imu_parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu = hwMap.get(BNO055IMU.class, "imu");
        imu.initialize(imu_parameters);

        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        setStarteAngle();
    }

    public void setStarteAngle() {
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        startAngle = angles.firstAngle;
        update();
    }

    public void update() {
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        currentAngle = angles.firstAngle;
        trueAngle = startAngle - currentAngle;

        //keeps the angle in a 360 degree range so there is only one number or each orientation
        if (trueAngle > 180) {
            trueAngle -= 360;
        }
        if (trueAngle < -180) {
            trueAngle += 360;
        }
    }
}
