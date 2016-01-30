package org.swerverobotics.library.internal;

import com.qualcomm.ftccommon.*;
import com.qualcomm.hardware.modernrobotics.*;
import com.qualcomm.hardware.hitechnic.*;
import com.qualcomm.robotcore.hardware.*;
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
    // Legacy Motor Controller
    //----------------------------------------------------------------------------------------------

    public static boolean isLegacyMotorController(DcMotorController controller)
        {
        return controller instanceof HiTechnicNxtDcMotorController;
        }

    public static boolean isLegacyServoController(ServoController controller)
        {
        return controller instanceof HiTechnicNxtServoController;
        }

    public static boolean isModernMotorController(DcMotorController controller)
        {
        return controller instanceof ModernRoboticsUsbDcMotorController;
        }

    public static boolean isModernServoController(ServoController controller)
        {
        return controller instanceof ModernRoboticsUsbServoController;
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
    // DCMotor and Servo
    //----------------------------------------------------------------------------------------------

    public static void setControllerOfMotor(DcMotor motor, DcMotorController controller)
        {
        Util.setPrivateObjectField(motor, 0, controller);
        }

    public static void setControllerOfServo(Servo servo, ServoController controller)
        {
        Util.setPrivateObjectField(servo, 0, controller);
        }
    }
