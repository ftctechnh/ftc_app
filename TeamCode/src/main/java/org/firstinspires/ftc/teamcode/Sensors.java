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

import java.util.ArrayList;

class Sensors {
    HardwareMap hardwareMap;

    private TouchSensor touchTop;
    private TouchSensor touchBottom;
    private TouchSensor touchLander;
    private DistanceSensor dLow;
    private DistanceSensor dHigh;

    BNO055IMU imu = null;

    boolean usingImu = false;

    // State used for updating telemetry
    Orientation angles;
    Acceleration gravity;

    Bogg.Name name;


    Sensors(HardwareMap hardwareMap, Bogg.Name whichRobot)
    {
        this.hardwareMap = hardwareMap;

        this.name = whichRobot;

        switch (name)
        {
            case Bogg:
                touchTop = hardwareMap.get(TouchSensor.class, "touchTop");
                touchBottom = hardwareMap.get(TouchSensor.class, "touchBottom");
                touchLander = hardwareMap.get(TouchSensor.class, "touchLander");
                //dLow = hardwareMap.get(DistanceSensor.class, "dLow");
                //dHigh = hardwareMap.get(DistanceSensor.class, "dHigh");
            case MiniBogg:
                usingImu = true;
                if(imu == null) {
                    BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
                    parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
                    parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
                    parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
                    parameters.loggingEnabled = true;
                    parameters.loggingTag = "IMU";
                    parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

                    // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
                    // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
                    // and named "imu".
                    try {
                        imu = hardwareMap.get(BNO055IMU.class, "imu");
                        imu.initialize(parameters);
                    } catch (IllegalArgumentException i) {
                        usingImu = false;
                    }
                }
            case Fauxbot:
            case Fakebot:
        }
    }

    /**
     *
     * @return angle in radians
     */
    double getImuHeading()
    {
        if(usingImu)
            return MyMath.radians(imu.getAngularOrientation().firstAngle);
        return 0;
    }

    boolean touchTopIsPressed() {
        return name != Bogg.Name.Bogg || touchTop.isPressed();
    }

    boolean touchBottomIsPressed() {
        return touchBottom.isPressed();
    }

    boolean touchLanderIsPressed(){
        return touchLander.isPressed();
    }

    ArrayList<Double> lowDistances = new ArrayList<>();
    double getLowDistance()
    {
//        highDistances.add(dLow.getDistance(DistanceUnit.INCH));
//
//        if(lowDistances.size() > 3)
//            lowDistances.remove(0);
//
//        return MyMath.median(lowDistances);

        return 10;
    }

    double highAverage = 0;
    double highAlpha = .4;
    ArrayList<Double> highDistances = new ArrayList<>();
    double getHighDistance()
    {
        double d = 30;
//        double d = dHigh.getDistance(DistanceUnit.INCH);
//        return d * highAlpha + highAverage * (1 - highAlpha);
        highDistances.add(d);
        if(highDistances.size() > 5)
            highDistances.remove(0);

        highAverage = MyMath.median(highDistances) * highAlpha + highAverage * (1 - highAlpha);
        return highAverage;
    }

    MyColorSensor myColorSensor;
    void initializeColorSensor()
    {
        if(myColorSensor == null)
            myColorSensor = new MyColorSensor(hardwareMap);
    }

    boolean isGold()
    {
        return myColorSensor.isColor(MyColorSensor.Color.Gold);
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
