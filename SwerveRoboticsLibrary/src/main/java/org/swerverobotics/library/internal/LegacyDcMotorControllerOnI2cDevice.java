package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by bob on 9/1/15.
 */
public class LegacyDcMotorControllerOnI2cDevice implements DcMotorController
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    // HardwareDevice
    //----------------------------------------------------------------------------------------------

    @Override public String getDeviceName()
        {
        return "";
        }

    @Override public String getConnectionInfo()
        {
        return "";
        }

    @Override public int getVersion()
        {
        return 0;
        }

    @Override public void close()
        {
        
        }

    //----------------------------------------------------------------------------------------------
    // DcMotorController
    //----------------------------------------------------------------------------------------------

    @Override public void setMotorControllerDeviceMode(DcMotorController.DeviceMode port)
        {
        }

    @Override public DcMotorController.DeviceMode getMotorControllerDeviceMode()
        {
        return DeviceMode.READ_WRITE;
        }
    
    @Override public void setMotorChannelMode(int port, DcMotorController.RunMode var2)
        {
        }

    @Override public DcMotorController.RunMode getMotorChannelMode(int port)
        {
        return RunMode.RESET_ENCODERS;
        }

    @Override public void setMotorPower(int port, double var2)
        {
        }

    @Override public double getMotorPower(int port)
        {
        return 0;        
        }

    @Override public boolean isBusy(int port)
        {
        return false;
        }

    @Override public void setMotorPowerFloat(int port)
        {
        }

    @Override public boolean getMotorPowerFloat(int port)
        {
        return false;
        }

    @Override public void setMotorTargetPosition(int port, int var2)
        {
        }

    @Override public int getMotorTargetPosition(int port)
        {
        return 0;
        }

    @Override public int getMotorCurrentPosition(int port)
        {
        return 0;
        }
    }
