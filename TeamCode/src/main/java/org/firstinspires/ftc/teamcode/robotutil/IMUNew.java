package org.firstinspires.ftc.teamcode.robotutil;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

enum Axis {
    PITCH,ROLL,HEADING;
}

public class IMUNew{
    private BNO055IMU imu;
    private Orientation angles;
    private OpMode opMode;
    private Boolean initialized = false;
    private BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

    public IMUNew(String hwMapName, OpMode opMode){
        this.opMode = opMode;

        this.parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        this.parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        this.parameters.loggingEnabled      = true;
        this.parameters.loggingTag          = hwMapName;
        this.parameters.loggingEnabled      = true;
        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = this.opMode.hardwareMap.get(BNO055IMU.class, hwMapName);
    }

    public boolean isInitialized(){
        return this.initialized;
    }
    public void init(){
        imu.initialize(parameters);
        this.initialized = true;
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
