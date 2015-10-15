package org.swerverobotics.library.internal;

import com.qualcomm.ftccommon.*;
import com.qualcomm.robotcore.eventloop.*;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.robot.*;

/**
 * Skullduggery we wish we didn't have to do.
 */
public class MemberUtil
    {
    public static Robot robotOfFtcRobotControllerService(FtcRobotControllerService service)
        {
        return Util.<Robot>getPrivateObjectField(service, 2+7);
        }

    public static EventLoopManager eventLoopManagerOfRobot(Robot robot)
        {
        return Util.<EventLoopManager>getPrivateObjectField(robot, 0);
        }

    public static EventLoopManager.EventLoopMonitor monitorOfEventLoopManager(EventLoopManager manager)
        {
        return Util.<EventLoopManager.EventLoopMonitor>getPrivateObjectField(manager, 8);
        }

    public static FtcEventLoopHandler ftcEventLoopHandlerOfFtcEventLoop(FtcEventLoop ftcEventLoop)
        {
        return Util.<FtcEventLoopHandler>getPrivateObjectField(ftcEventLoop, 0);
        }

    static boolean isLegacyMotorController(DcMotorController controller)
        {
        return controller instanceof com.qualcomm.hardware.HiTechnicNxtDcMotorController;
        }

    static LegacyModule legacyModuleOfLegacyMotorController(DcMotorController controller)
        {
        return Util.<LegacyModule>getPrivateObjectField(controller, 0);
        }

    static I2cController.I2cPortReadyCallback[] callbacksOfLegacyModule(LegacyModule module)
        {
        return Util.<I2cController.I2cPortReadyCallback[]>getPrivateObjectField(module, 4);
        }

    static int portOfLegacyMotorController(DcMotorController controller)
        {
        return Util.getPrivateIntField(controller, 5);
        }

    public static I2cController i2cControllerOfI2cDevice(I2cDevice i2cDevice)
        {
        return Util.<I2cController>getPrivateObjectField(i2cDevice, 0);
        }

    public static int portOfI2cDevice(I2cDevice i2cDevice)
        {
        return Util.getPrivateIntField(i2cDevice, 1);
        }
    }
