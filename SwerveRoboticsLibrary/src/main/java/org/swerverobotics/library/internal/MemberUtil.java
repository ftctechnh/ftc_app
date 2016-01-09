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

    public static boolean isLegacyMotorController(DcMotorController controller)
        {
        return controller instanceof com.qualcomm.hardware.HiTechnicNxtDcMotorController;
        }

    public static boolean isLegacyServoController(ServoController controller)
        {
        return controller instanceof com.qualcomm.hardware.HiTechnicNxtServoController;
        }

    public static boolean isModernMotorController(DcMotorController controller)
        {
        return controller instanceof com.qualcomm.hardware.ModernRoboticsUsbDcMotorController;
        }

    public static boolean isModernServoController(ServoController controller)
        {
        return controller instanceof com.qualcomm.hardware.ModernRoboticsUsbServoController;
        }

    public static LegacyModule legacyModuleOfLegacyMotorController(DcMotorController controller)
        {
        return Util.<LegacyModule>getPrivateObjectField(controller, 0);
        }
    public static int portOfLegacyMotorController(DcMotorController controller)
        {
        return Util.getPrivateIntField(controller, 5);
        }

    public static LegacyModule legacyModuleOfLegacyServoController(ServoController controller)
        {
        return Util.<LegacyModule>getPrivateObjectField(controller, 0);
        }
    public static int portOfLegacyServoController(ServoController controller)
        {
        return Util.getPrivateIntField(controller, 3);
        }

    public static int i2cAddrOfLegacyMotorController(DcMotorController controller)
        {
        // From the spec from HiTechnic:
        //
        // "The first motor controller in the daisy chain will use an I2C address of 02/03. Subsequent
        // controllers will obtain addresses of 04/05, 06/07 and 08/09. Only four controllers may be
        // daisy chained."
        //
        // The legacy module appears not to support daisy chaining; it only supports the first
        // address. Note that these are clearly 8-bit addresses, not 7-bit.
        //
        return 0x02;
        }

    public static int i2cAddrOfLegacyServoController(ServoController controller)
        {
        return 0x02;
        }

    //----------------------------------------------------------------------------------------------
    // DCMotor
    //----------------------------------------------------------------------------------------------

    public static void setControllerOfMotor(DcMotor motor, DcMotorController controller)
        {
        Util.setPrivateObjectField(motor, 0, controller);
        }

    public static void setControllerOfServo(Servo servo, ServoController controller)
        {
        Util.setPrivateObjectField(servo, 0, controller);
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
        return Util.<DeviceInterfaceModule>getPrivateObjectField(sensor, 1);    // 0 is now i2c address
        }
    static int portOfHiTechnicColorSensor(ColorSensor sensor)
        {
        return Util.getPrivateIntField(sensor, 7);
        }
    static int portOfModernColorSensor(ColorSensor sensor)
        {
        return Util.getPrivateIntField(sensor, 8);
        }

    //----------------------------------------------------------------------------------------------
    // Legacy Module
    //----------------------------------------------------------------------------------------------

    static I2cController.I2cPortReadyCallback[] callbacksOfLegacyModule(LegacyModule imodule)
        {
        ModernRoboticsUsbLegacyModule module = (ModernRoboticsUsbLegacyModule)imodule;
        return Util.<I2cController.I2cPortReadyCallback[]>getPrivateObjectField(module, 4);
        }

    //----------------------------------------------------------------------------------------------
    // Device Interface Module
    //----------------------------------------------------------------------------------------------

    static I2cController.I2cPortReadyCallback[] callbacksOfDeviceInterfaceModule(DeviceInterfaceModule imodule)
        {
        ModernRoboticsUsbDeviceInterfaceModule module = (ModernRoboticsUsbDeviceInterfaceModule)imodule;
        return Util.<I2cController.I2cPortReadyCallback[]>getPrivateObjectField(module, 3);
        }

    //----------------------------------------------------------------------------------------------
    // I2cDevice
    //----------------------------------------------------------------------------------------------

    public static I2cController i2cControllerOfI2cDevice(I2cDevice i2cDevice)
        {
        return Util.<I2cController>getPrivateObjectField(i2cDevice, 0);
        }

    public static int portOfI2cDevice(I2cDevice i2cDevice)
        {
        return Util.getPrivateIntField(i2cDevice, 1);
        }
    }
