package org.swerverobotics.library.internal;

import com.qualcomm.ftccommon.*;
import com.qualcomm.robotcore.eventloop.*;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.robocol.*;
import com.qualcomm.robotcore.robot.*;

/**
 * Skullduggery we wish we didn't have to do.
 */
public class MemberUtil
    {
    //----------------------------------------------------------------------------------------------
    // FtcRobotControllerService
    //----------------------------------------------------------------------------------------------

    public static Robot robotOfFtcRobotControllerService(FtcRobotControllerService service)
        {
        return Util.<Robot>getPrivateObjectField(service, 2+7);
        }

    //----------------------------------------------------------------------------------------------
    // FTCEventLoop
    //----------------------------------------------------------------------------------------------

    public static FtcEventLoopHandler handlerOfFtcEventLoop(FtcEventLoop ftcEventLoop)
        {
        return Util.<FtcEventLoopHandler>getPrivateObjectField(ftcEventLoop, 0);
        }

    //----------------------------------------------------------------------------------------------
    // FtcEventLoopHandler
    //----------------------------------------------------------------------------------------------

    public static EventLoopManager eventLoopManagerOfFtcEventLoopHandler(FtcEventLoopHandler ftcEventLoopHandler)
        {
        return Util.<EventLoopManager>getPrivateObjectField(ftcEventLoopHandler, 0);
        }

    //----------------------------------------------------------------------------------------------
    // EventLoopManager
    //----------------------------------------------------------------------------------------------

    public static RobocolDatagramSocket socketOfEventLoopManager(EventLoopManager manager)
        {
        return Util.<RobocolDatagramSocket>getPrivateObjectField(manager, 2);
        }

    public static void setSocketOfEventLoopManager(EventLoopManager manager, RobocolDatagramSocket socket)
        {
        Util.setPrivateObjectField(manager, 2, socket);
        }

    public static EventLoopManager.EventLoopMonitor monitorOfEventLoopManager(EventLoopManager manager)
        {
        return Util.<EventLoopManager.EventLoopMonitor>getPrivateObjectField(manager, 8);
        }

    //----------------------------------------------------------------------------------------------
    // Legacy Motor Controller
    //----------------------------------------------------------------------------------------------

    static boolean isLegacyMotorController(DcMotorController controller)
        {
        return controller instanceof com.qualcomm.hardware.HiTechnicNxtDcMotorController;
        }

    static LegacyModule legacyModuleOfLegacyMotorController(DcMotorController controller)
        {
        return Util.<LegacyModule>getPrivateObjectField(controller, 0);
        }

    static int portOfLegacyMotorController(DcMotorController controller)
        {
        return Util.getPrivateIntField(controller, 5);
        }

    //----------------------------------------------------------------------------------------------
    // Color Sensors
    //----------------------------------------------------------------------------------------------

    static LegacyModule legacyModuleOfHiTechnicColorSensor(ColorSensor sensor)
        {
        return Util.<LegacyModule>getPrivateObjectField(sensor, 0);
        }
    static DeviceInterfaceModule deviceModuleOfModernColorSensor(ColorSensor sensor)
        {
        return Util.<DeviceInterfaceModule>getPrivateObjectField(sensor, 0);
        }
    static int portOfHiTechnicColorSensor(ColorSensor sensor)
        {
        return Util.getPrivateIntField(sensor, 7);
        }
    static int portOfModernColorSensor(ColorSensor sensor)
        {
        return Util.getPrivateIntField(sensor, 7);
        }

    //----------------------------------------------------------------------------------------------
    // Legacy Module
    //----------------------------------------------------------------------------------------------

    static I2cController.I2cPortReadyCallback[] callbacksOfLegacyModule(LegacyModule module)
        {
        return Util.<I2cController.I2cPortReadyCallback[]>getPrivateObjectField(module, 4);
        }

    //----------------------------------------------------------------------------------------------
    // Device Interface Module
    //----------------------------------------------------------------------------------------------

    static I2cController.I2cPortReadyCallback[] callbacksOfDeviceInterfaceModule(DeviceInterfaceModule module)
        {
        return Util.<I2cController.I2cPortReadyCallback[]>getPrivateObjectField(module, 0);
        }

    public static I2cController i2cControllerOfI2cDevice(I2cDevice i2cDevice)
        {
        return Util.<I2cController>getPrivateObjectField(i2cDevice, 0);
        }

    //----------------------------------------------------------------------------------------------
    // I2cDevice
    //----------------------------------------------------------------------------------------------

    public static int portOfI2cDevice(I2cDevice i2cDevice)
        {
        return Util.getPrivateIntField(i2cDevice, 1);
        }
    }
