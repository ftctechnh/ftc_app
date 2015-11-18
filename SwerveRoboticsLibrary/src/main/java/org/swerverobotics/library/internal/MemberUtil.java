package org.swerverobotics.library.internal;

import com.qualcomm.ftccommon.*;
import com.qualcomm.hardware.*;
import com.qualcomm.modernrobotics.*;
import com.qualcomm.robotcore.eventloop.*;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.hardware.usb.*;
import com.qualcomm.robotcore.robocol.*;
import com.qualcomm.robotcore.robot.*;
import java.util.concurrent.*;

/**
 * Skullduggery we wish we didn't have to do.
 */
public class MemberUtil
    {
    //----------------------------------------------------------------------------------------------
    // ReadWriteRunnableStandard
    //----------------------------------------------------------------------------------------------

    public static ReadWriteRunnableUsbHandler getHandlerOfReadWriteRunnableStandard(ReadWriteRunnableStandard readWriteRunnableStandard)
        {
        return Util.<ReadWriteRunnableUsbHandler>getPrivateObjectField(readWriteRunnableStandard, 13);
        }
    public static void setHandlerOfReadWriteRunnableStandard(ReadWriteRunnableStandard readWriteRunnableStandard, ReadWriteRunnableUsbHandler handler)
        {
        Util.setPrivateObjectField(readWriteRunnableStandard, 13, handler);
        }

    public static void setRunningReadWriteRunnableStandard(ReadWriteRunnableStandard readWriteRunnableStandard, boolean isRunning)
        {
        Util.setPrivateBooleanField(readWriteRunnableStandard, 6, isRunning);
        }

    //----------------------------------------------------------------------------------------------
    // ReadWriteRunnableUsbHandler
    //----------------------------------------------------------------------------------------------

    public static RobotUsbDevice getRobotUsbDeviceOfReadWriteRunnableUsbHandler(ReadWriteRunnableUsbHandler handler)
        {
        return Util.<RobotUsbDevice>getPrivateObjectField(handler, 2);
        }

    //----------------------------------------------------------------------------------------------
    // ModernRoboticsUsbDevice
    //----------------------------------------------------------------------------------------------

    public static ReadWriteRunnableStandard getReadWriteRunnableModernRoboticsUsbDevice(ModernRoboticsUsbDevice device)
    // Here we rely on the fact that ReadWriteRunnableBlocking inherits from ReadWriteRunnableStandard
        {
        return Util.<ReadWriteRunnableStandard>getPrivateObjectField(device, 0);
        }
    public static void setReadWriteRunnableModernRoboticsUsbDevice(ModernRoboticsUsbDevice device, ReadWriteRunnableStandard readWriteRunnableStandard)
        {
        Util.setPrivateObjectField(device, 0, readWriteRunnableStandard);
        }

    public static void setExecutorServiceModernRoboticsUsbDevice(ModernRoboticsUsbDevice device, ExecutorService service)
        {
        Util.setPrivateObjectField(device, 1, service);
        }
    public static ExecutorService getExecutorServiceModernRoboticsUsbDevice(ModernRoboticsUsbDevice device)
        {
        return Util.<ExecutorService>getPrivateObjectField(device, 1);
        }


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

    static boolean isModernMotorController(DcMotorController controller)
        {
        return controller instanceof com.qualcomm.hardware.ModernRoboticsUsbDcMotorController;
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
    // DCMotor
    //----------------------------------------------------------------------------------------------

    static void setControllerOfMotor(DcMotor motor, DcMotorController controller)
        {
        Util.setPrivateObjectField(motor, 0, controller);
        }

    //----------------------------------------------------------------------------------------------
    // Color Sensors
    //----------------------------------------------------------------------------------------------

    static DeviceInterfaceModule deviceInterfaceModuleOfAdaFruitColorSensor(ColorSensor sensor)
        {
        return Util.<DeviceInterfaceModule>getPrivateObjectField(sensor, 0);
        }
    static int portOfAdaFruitColorSensor(ColorSensor sensor)
        {
        return Util.getPrivateIntField(sensor, 5);
        }

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
