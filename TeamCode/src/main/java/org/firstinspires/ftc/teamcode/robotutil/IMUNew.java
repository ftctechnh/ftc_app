package org.firstinspires.ftc.teamcode.robotutil;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

import java.util.Locale;

enum Axis {
    PITCH,ROLL,HEADING;
}

public class IMUNew{
    private BNO055IMU imu;
    private Orientation angles;
    private OpMode opMode;
    boolean isInitialized = false;
    private BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
    
    public IMUNew(OpMode opMode){
        this.opMode = opMode;

        this.parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        this.parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        this.parameters.loggingEnabled      = true;
        this.parameters.loggingTag          = "IMU";
        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = this.opMode.hardwareMap.get(BNO055IMU.class, "imu");
    }

    public void init(){
        imu.initialize(parameters);
    }

    public double getAngle(Axis axis){
        Orientation orientation = imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
        if(axis==Axis.HEADING){
            return orientation.firstAngle;
        }else if(axis==Axis.ROLL){
            return orientation.secondAngle;
        }else{
            return orientation.thirdAngle;
        }
    }
}
