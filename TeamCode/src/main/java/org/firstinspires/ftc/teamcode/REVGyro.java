package org.firstinspires.ftc.teamcode;

/**
 * Created by Kaden on 12/14/2017.
 */
import com.qualcomm.hardware.bosch.BNO055IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class REVGyro {
    BNO055IMU imu;
    BNO055IMU.Parameters parameters;
    Orientation angles;
    public double heading;
    public REVGyro (BNO055IMU imu) {
        this.parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        this.imu = imu;
    }
    public void calibrate() {
        imu.initialize(parameters);
        heading = getHeading();
    }

    public double getHeading() {
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        heading = angles.firstAngle;
        return heading;
    }
}