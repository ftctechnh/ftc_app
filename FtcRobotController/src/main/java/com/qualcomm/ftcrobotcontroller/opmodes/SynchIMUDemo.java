package com.qualcomm.ftcrobotcontroller.opmodes;

import java.util.*;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;

/**
 * SynchIMUDemo gives a short demo on how to use the BNO055 Inertial Motion Unit (IMU) from AdaFruit.
 * http://www.adafruit.com/products/2472
 */
public class SynchIMUDemo extends SynchronousOpMode
    {
    IBNO055IMU imu;                     // our sensor
    IBNO055IMU.EulerAngles angles;      // used during dashboard updating
    
    @Override public void main() throws InterruptedException
        {
        // We are expecting the IMU to be attached to an I2C port on  a core device interface 
        // module and named "imu". Retrieve that raw I2cDevice and then wrap it in an object that
        // semantically understands this particular kind of sensor.
        IBNO055IMU.Parameters parameters = new IBNO055IMU.Parameters();
        imu = ClassFactory.createAdaFruitBNO055IMU(hardwareMap.i2cDevice.get("imu"), parameters);
        
        // Set up our dashboard computations
        composeDashboard();
        
        // Wait until we're told to go
        waitForStart();
        
        // Loop and update the dashboard
        while (opModeIsActive())
            {
            telemetry.dashboard.update();
            idle();
            }
        }
    
    void composeDashboard()
        {
        TelemetryDashboardAndLog.Dashboard dashboard = telemetry.dashboard;
        dashboard.action(new IAction()
        {
        @Override public void doAction()
            {
            angles = imu.getAngularOrientation();
            }
        });
        dashboard.line(
                dashboard.item("loop count: ", new IFunc<Object>() { @Override public Object value()
                    {
                    return getLoopCount();
                    }
                }),
                dashboard.item("hw cycle count: ", new IFunc<Object>() { @Override public Object value()
                    {
                    return ((II2cDeviceClientUser) imu).getI2cDeviceClient().getHardwareCycleCount();
                    }
                }));
        dashboard.line(
                dashboard.item("status: ", new IFunc<Object>() { @Override public Object value()
                    {
                    return String.format("0x%02x", imu.getSystemStatus());
                    }
                }),
                dashboard.item("calib: ", new IFunc<Object>() { @Override public Object value()
                    {
                    return String.format("0x%02x", imu.read8(IBNO055IMU.REGISTER.CALIB_STAT));
                    }
                })
            );
        dashboard.line(dashboard.item("heading: ", new IFunc<Object>() { @Override public Object value()
            {
            return formatAngle(angles.heading);
            }}));
        dashboard.line(dashboard.item("roll: ", new IFunc<Object>() { @Override public Object value()
            {
            return formatAngle(angles.roll);
            }}));
        dashboard.line(dashboard.item("pitch: ", new IFunc<Object>() { @Override public Object value()
            {
            return formatAngle(angles.pitch);
            }}));
        }
    
    String formatAngle(double angle)
        {
        return String.format("%.2f", angle * 180 / Math.PI);
        }
    }
