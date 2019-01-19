package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

class Sensors {
    HardwareMap hardwareMap;

    private TouchSensor touchTop;
    private TouchSensor touchBottom;
    private DistanceSensor dLow;
    private DistanceSensor dHigh;

    BNO055IMU imu = null;

    private boolean usingImu = true;

    // State used for updating telemetry
    Orientation angles;
    Acceleration gravity;


    Sensors(HardwareMap hardwareMap)
    {
        this.hardwareMap = hardwareMap;

        touchTop = hardwareMap.get(TouchSensor.class, "touchTop");
        touchBottom = hardwareMap.get(TouchSensor.class, "touchBottom");
        dLow = hardwareMap.get(DistanceSensor.class, "dLow");
        dHigh = hardwareMap.get(DistanceSensor.class, "dHigh");

        if(usingImu && imu == null)
        {
            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
            parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
            parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
            parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
            parameters.loggingEnabled      = true;
            parameters.loggingTag          = "IMU";
            parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

            // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
            // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
            // and named "imu".
            try
            {
                imu = hardwareMap.get(BNO055IMU.class, "imu");
                imu.initialize(parameters);
            }catch (IllegalArgumentException i)
            {
                usingImu = false;
            }
        }
    }

    /**
     * @param imu
     * @return angle in radians
     */
    static double getImuHeading(BNO055IMU imu)
    {
        return imu.getAngularOrientation().firstAngle * Math.PI / 180;
    }

    boolean touchTopIsPressed() {
        return touchTop.isPressed();
    }

    boolean touchBottomIsPressed() {
        return touchBottom.isPressed();
    }

    double getLowDistance()
    {
        return 36;
//        return dLow.getDistance(DistanceUnit.INCH);
    }

    double getHighDistance()
    {
        return 36;
//        return dHigh.getDistance(DistanceUnit.INCH);
    }

    boolean isTilted()
    {
        if(usingImu)
        {
            Orientation orientation = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            return Math.hypot(orientation.secondAngle, orientation.thirdAngle) > 10;
        }
        else
        {
            return Math.random() < .0001;
        }
    }
}
