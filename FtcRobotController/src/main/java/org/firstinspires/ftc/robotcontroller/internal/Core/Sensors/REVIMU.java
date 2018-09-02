package org.firstinspires.ftc.robotcontroller.internal.Core.Sensors;

import com.qualcomm.hardware.HardwareDeviceManager;
import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;
import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.HardwareMapper;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

/**
 * Created by pmkf2 on 9/1/2018.
 */

public class REVIMU
{
    private BNO055IMU imu = null;

    private Acceleration accel = null;

    private double angle1, angle2, angle3;

    private double angle1Offset, angle2Offset, angle3Offset;

    public void init(final RobotBase BASE, final String NAME, BNO055IMU.Parameters PARAMETERS)
    {
        HardwareMapper  mapHelper = new HardwareMapper(BASE);

        imu = mapHelper.mapREVIMU(NAME, PARAMETERS);


    }


    /*
    Begins the tracking of accel.
     */

    public void startAccel()
    {
        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);
    }

    /*Writes to IMU.
     */
    public void write8(final BNO055IMU.Register REGISTER, final int BYTE)
    {
        imu.write8(REGISTER, BYTE);
    }

    public void setAngle()
    {
        angle1 = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).firstAngle - angle1Offset;

        angle2 = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).firstAngle - angle2Offset;

        angle3 = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).firstAngle - angle3Offset;

        //Adjustments to have angle values between 0 and 360.

        if(angle1 < 0)
        {
            angle1 += 360;
            angle1 %= 360;
        }

        if(angle2 < 0)
        {
            angle2 += 360;
            angle2 %= 360;
        }


        if(angle3 < 0)
        {
            angle3 += 360;
            angle3 %= 360;
        }

        accel = imu.getLinearAcceleration();
    }

    //Each angle will read 0
    public void calibrateToZero()
    {
        calibrateTo(0);
    }

    //Calibrate to given offset
    public void calibrateTo(final int OFFSET)
    {
        angle1Offset = angle1 - OFFSET;
        angle2Offset = angle2 - OFFSET;
        angle3Offset = angle3 - OFFSET;
    }

    //Return x angle from accelerometer.

    public double xAngle()
    {
        return angle1;
    }

    //Return y angle from accelerometer.
    public double yAngle()
    {
        return angle2;
    }

    //Return z angle from accelerometer.
    public double zAngle()
    {
        return angle3;
    }

    //Return x accel from accelerometer.
    public double xAccel()
    {
        return accel.xAccel;
    }

    //Return x accel from accelerometer.
    public double yAccel()
    {
        return accel.yAccel;
    }

    //Return z accel from accelerometer.
    public double zAccel()
    {
        return accel.zAccel;
    }


}
