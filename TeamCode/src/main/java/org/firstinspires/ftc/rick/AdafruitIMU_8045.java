package org.firstinspires.ftc.rick;

/**
 * Created by ftc8045 on 10/31/2016.
 */


import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;
import java.util.Locale;

/**
 * This is an Adafruit Bosch BNO055 Inertial Measurement Unit.
 * It gets data in quaternions from the fused sensor data, and returns yaw, pitch, and roll.
 * Created by Varun Singh, Lead Programmer of FTC Team 4997 Masquerade.
 */

public class AdafruitIMU_8045 {

    private final BNO055IMU imu;
    private final String name;


    public AdafruitIMU_8045(String name, HardwareMap hwmap) {
        this.name = name;
        imu = hwmap.get(BNO055IMU.class, name);
        setParameters();
    }


    private void setParameters() {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.useExternalCrystal = true;
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        parameters.pitchMode = BNO055IMU.PitchMode.WINDOWS;
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        imu.initialize(parameters);
    }


    /**
     * This method returns a 3x1 array of doubles with the yaw, pitch, and roll in that order.
     * The equations used in this method came from:
     * https://en.wikipedia.org/wiki/Conversion_between_quaternions_and_Euler_angles#Euler_Angles_from_Quaternion
     */
    public double[] getAngles() {
        Quaternion quatAngles = imu.getQuaternionOrientation();

        double w = quatAngles.w;
        double x = quatAngles.x;
        double y = quatAngles.y;
        double z = quatAngles.z;

        // for the Adafruit IMU, yaw and roll are switched
        double roll = Math.atan2( 2*(w*x + y*z) , 1 - 2*(x*x + y*y) ) * 180.0 / Math.PI;
        double pitch = Math.asin( 2*(w*y - x*z) ) * 180.0 / Math.PI;
        double yaw = Math.atan2( 2*(w*z + x*y), 1 - 2*(y*y + z*z) ) * 180.0 / Math.PI;

        return new double[]{yaw, pitch, roll};
    }

    public String getName() {return name;}

    // This method returns a string that can be used to output telemetry data easily in other classes.
    public String telemetrize() {
        double[] angles = getAngles();
        return String.format(Locale.US, "Yaw: %.3f  Pitch: %.3f  Roll: %.3f", angles[0], angles[1], angles[2]);
    }


}