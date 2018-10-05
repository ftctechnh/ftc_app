package teamcode.connor;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * A static-only class which provides hardware-related references and methods
 */
public final class HardwareManager {

    private static DcMotor driveMotorL;
    private static DcMotor driveMotorR;

    static {
        initHardware();
    }

    private HardwareManager() {
        // not to be instantiated
    }


    private static void initHardware() {
        HardwareMap hardwareMap = ConnorRobot.instance.hardwareMap;
        driveMotorL = hardwareMap.get(DcMotor.class, "LeftDriveMotor");
        driveMotorR = hardwareMap.get(DcMotor.class, "RightDriveMotor");

        //Sets correct directions for drive motors
        driveMotorL.setDirection(DcMotor.Direction.REVERSE);
        driveMotorR.setDirection(DcMotor.Direction.FORWARD);
    }

    /**
     * Sets the power of the drive motors
     *
     * @param powerL the power of the left drive motor
     * @param powerR the power of the right drive motor
     */
    public static void setDrivePower(double powerL, double powerR) {
        driveMotorL.setPower(powerL);
        driveMotorR.setPower(powerR);
    }

}
