package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;

/**
 * SynchIMUDemo gives a short demo on how to use the BNO055 Inertial Motion Unit (IMU) from AdaFruit.
 * http://www.adafruit.com/products/2472
 */
public class SynchIMUDemo extends SynchronousOpMode
    {
    // Our sensor and other devices
    IBNO055IMU              imu;

    // State we use for updating the dashboard. The first of these is important
    // to read only once per update, as its acquisition is expensive. The remainder, though,
    // could probably be read once per item, at only a small loss in display accuracy.
    IBNO055IMU.EulerAngles  angles;
    ElapsedTime             elapsed = new ElapsedTime();
    int                     loopCycles;
    int                     i2cCycles;
    double                  dt;
    
    @Override public void main() throws InterruptedException
        {
        // We are expecting the IMU to be attached to an I2C port on  a core device interface 
        // module and named "imu". Retrieve that raw I2cDevice and then wrap it in an object that
        // semantically understands this particular kind of sensor.
        IBNO055IMU.Parameters parameters = new IBNO055IMU.Parameters();
        imu = ClassFactory.createAdaFruitBNO055IMU(hardwareMap.i2cDevice.get("imu"), parameters);
        imu.startAccelerationIntegration(new IBNO055IMU.Position(), new IBNO055IMU.Velocity());
        
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
        TelemetryDashboardAndLog.Dashboard db = telemetry.dashboard;

        // The default dashboard update rate is a little to slow for us, so we update faster
        db.msUpdateInterval = 333;

        // At the beginning of each telemetry update, grab a bunch of data
        // from the IMU that we will then display in separate lines.
        db.action(new IAction() { @Override public void doAction()
                {
                // Acquiring the angles is relatively expensive; we don't want
                // to do that in each of the three items that need that info, as that's
                // three times the necessary expense.
                angles     = imu.getAngularOrientation();
                loopCycles = getLoopCount();
                i2cCycles  = ((II2cDeviceClientUser) imu).getI2cDeviceClient().getI2cCycleCount();
                dt         = elapsed.time();
                }
            });
        db.line(db.item("loop count: ", new IFunc<Object>() { @Override public Object value()
                    {
                    return loopCycles;
                    }}),
                db.item("i2c cycle count: ", new IFunc<Object>() { @Override public Object value()
                    {
                    return i2cCycles;
                    }}));

        db.line(db.item("loop rate: ", new IFunc<Object>() { @Override public Object value()
                    {
                    return formatRate(dt / loopCycles * 1000.0);
                    }
                }),
                db.item("i2c cycle rate: ", new IFunc<Object>() { @Override public Object value()
                    {
                    return formatRate(dt / i2cCycles * 1000.0);
                    }
                }));
        db.line(db.item("status: ", new IFunc<Object>() { @Override public Object value()
                    {
                    return String.format("%s", decodeStatus(imu.getSystemStatus()));
                    }
                }),
                db.item("calib: ", new IFunc<Object>() { @Override public Object value()
                    {
                    return String.format("%s", decodeCalib(imu.read8(IBNO055IMU.REGISTER.CALIB_STAT)));
                    }
                }));
        db.line(db.item("heading: ", new IFunc<Object>() { @Override public Object value()
                    {
                    return formatRadians(angles.heading);
                    }
                }));
        db.line(db.item("roll: ", new IFunc<Object>() { @Override public Object value()
                    {
                    return formatRadians(angles.roll);
                    }
                }));
        db.line(db.item("pitch: ", new IFunc<Object>() { @Override public Object value()
                    {
                    return formatRadians(angles.pitch);
                    }
                }));
        }
    
    String formatRadians(double radians)
        {
        return String.format("%.2f", normalizeDegrees(degreesFromRadians(radians)));
        }
    String formatRate(double rate)
        {
        return String.format("%.2f", rate);
        }

    /** Normalize the angle into the range [-180,180) */
    double normalizeDegrees(double degrees)
        {
        while (degrees >= 180.0) degrees -= 360.0;
        while (degrees < -180.0) degrees += 360.0;
        return degrees;
        }
    double degreesFromRadians(double radians)
        {
        return radians * 180.0 / Math.PI;
        }


    /** Turn a system status into something that's reasonable to show in telemetry */
    String decodeStatus(int status)
        {
        switch (status)
            {
            case 0: return "idle";
            case 1: return "syserr";
            case 2: return "periph";
            case 3: return "sysinit";
            case 4: return "selftest";
            case 5: return "fusion";
            case 6: return "running";
            }
        return "unk";
        }

    /** Turn a calibration code into something that is reasonable to show in telemetry */
    String decodeCalib(int status)
        {
        StringBuilder result = new StringBuilder();

        result.append(String.format("s%d", (status >> 2) & 0x03));  // SYS calibration status
        result.append(" ");
        result.append(String.format("g%d", (status >> 2) & 0x03));  // GYR calibration status
        result.append(" ");
        result.append(String.format("a%d", (status >> 2) & 0x03));  // ACC calibration status
        result.append(" ");
        result.append(String.format("m%d", (status >> 0) & 0x03));  // MAG calibration status

        return result.toString();
        }
    }
