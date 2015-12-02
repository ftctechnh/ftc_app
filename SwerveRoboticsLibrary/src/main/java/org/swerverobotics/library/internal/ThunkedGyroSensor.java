package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.*;
import org.swerverobotics.library.interfaces.*;

/**
 * A GyroSensor that can be called on a synchronous thread.
 */
public class ThunkedGyroSensor implements GyroSensor, IThunkWrapper<GyroSensor>
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private GyroSensor target;   // can only talk to him on the loop thread

    @Override public GyroSensor getWrappedTarget() { return this.target; }
    
    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private ThunkedGyroSensor(GyroSensor target)
        {
        if (target == null) throw new NullPointerException("null " + this.getClass().getSimpleName() + " target");
        this.target = target;
        }

    static public ThunkedGyroSensor create(GyroSensor target)
        {
        return target instanceof ThunkedGyroSensor ? (ThunkedGyroSensor)target : new ThunkedGyroSensor(target);
        }

    //----------------------------------------------------------------------------------------------
    // Device information: I2C registers and other info
    //----------------------------------------------------------------------------------------------
    /*
    #define HTIRS2_I2C_ADDR    0x10      /*!< IR Seeker I2C device address                           
    #define HTISR2_REG_MODE    0x41      /* register address of the IR reading mode                  
    #define HTIRS2_DIR_DC      0x42      /* DC direction: 0-9                                        
    #define HTIRS2_REG_DC      0x43      /* register address of first DC signal strength 0x43 - 0x47 
    #define HTIRS2_MEAN_DC     0x48      /* sensor DC mean                                           
    #define HTIRS2_DIR_AC      0x49      /* AC direction: 0-9                                        
    #define HTIRS2_REG_AC      0x4A      /* register address of first AC signal strength 0x4A - 0x4E 
    
    #define HTIRS2_READ_BASE_DC   0x42
    #define HTIRS2_READ_BASE_AC   0x49
        
    // "The Gyro Sensor connects to an NXT sensor port using a standard NXT wire and utilizes
    // the analog sensor interface.  The rotation rate can be read up to approximately 300
    // times per second."
    //
    // Note that the value coming out of the sensor is the rotation rate in the *clockwise*
    // direction.
    //
    // http://nxttime.wordpress.com/2010/11/03/gyro-offset-and-drift/
    // http://www.hitechnic.com/cgi-bin/commerce.cgi?preadd=action&key=NGY1044
    // http://proj.titanrobotics.net/hg/Frc/2011/code/file/ea68beef9fd9/trclib.nxt/gyro.h
    // http://mightor.wordpress.com/2009/11/17/you-spin-me-right-round-baby-right-round/    
    */
    
    //----------------------------------------------------------------------------------------------
    // HardwareDevice
    //----------------------------------------------------------------------------------------------

    @Override public void close()
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.close();
                }
            }).doUntrackedWriteOperation();
        }

    @Override public int getVersion()
        {
        return (new ThunkForReading<Integer>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getVersion();
                }
            }).doUntrackedReadOperation();
        }

    @Override public String getConnectionInfo()
        {
        return (new ThunkForReading<String>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getConnectionInfo();
                }
            }).doUntrackedReadOperation();
        }

    @Override public String getDeviceName()
        {
        return (new ThunkForReading<String>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getDeviceName();
                }
            }).doUntrackedReadOperation();
        }
    
    //----------------------------------------------------------------------------------------------
    // GyroSensor
    //----------------------------------------------------------------------------------------------

    @Override public double getRotation()
        {
        return (new ThunkForReading<Double>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getRotation();
                }
            }).doReadOperation();
        }

    @Override public String status()
        {
        return (new ThunkForReading<String>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.status();
                }
            }).doReadOperation();
        }

    @Override public void calibrate()
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.calibrate();
                }
            }).doWriteOperation();
        }

    @Override public boolean isCalibrating()
        {
        return (new ThunkForReading<Boolean>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.isCalibrating();
                }
            }).doReadOperation();
        }

    @Override public int getHeading()
        {
        return (new ThunkForReading<Integer>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getHeading();
                }
            }).doReadOperation();
        }

    @Override public int rawX()
        {
        return (new ThunkForReading<Integer>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.rawX();
                }
            }).doReadOperation();
        }

    @Override public int rawY()
        {
        return (new ThunkForReading<Integer>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.rawY();
                }
            }).doReadOperation();
        }

    @Override public int rawZ()
        {
        return (new ThunkForReading<Integer>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.rawZ();
                }
            }).doReadOperation();
        }

    @Override public void resetZAxisIntegrator()
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.resetZAxisIntegrator();
                }
            }).doWriteOperation();
        }

    }
