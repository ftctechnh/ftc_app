package teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * A static-only class which provides hardware-related variables and methods
 */
public final class HardwareManager {

    private static DcMotor driveMotorL;
    private static DcMotor driveMotorR;

    private static DcMotor armMotorBase;
    private static Servo armServoBase;
    private static Servo armServoTop;
    private static Servo armServoIntake;

    private static int armMotorBasePos;
    private static double armServoBasePos;
    private static double armServoTopPos;


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

        armMotorBase = hardwareMap.get(DcMotor.class, "armMotorBase");
        armServoBase = hardwareMap.get(Servo.class, "armServoBase");
        armServoTop = hardwareMap.get(Servo.class, "armServoTop");
        armServoIntake = hardwareMap.get(Servo.class, "armServoIntake");

        armMotorBase.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armMotorBase.setDirection(DcMotor.Direction.FORWARD);

        armServoBase.setDirection(Servo.Direction.REVERSE);
        armServoTop.setDirection(Servo.Direction.FORWARD);

        armMotorBasePos = 0;
        armServoBasePos = 0.0;
        armServoTopPos = 0.0;
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

    public static void setArmMotorBasePosition(int posDelta) {
        armMotorBasePos += posDelta;

        // clamp pos to something

        armMotorBase.setTargetPosition(armMotorBasePos);
        armMotorBase.setPower(0.5);
    }

    public static void setArmServoBasePosition(double posDelta) {
        armServoBasePos += posDelta;

        // clamp pos between 0 and 1
        if (armServoBasePos > 1.0) {
            armServoBasePos = 1.0;
        } else if (armServoBasePos < 0.0) {
            armServoBasePos = 0.0;
        }

        armServoBase.setPosition(armServoBasePos);
    }

    public static void setArmServoTopPosition(double posDelta) {
        armServoTopPos += posDelta;

        // clamp pos between 0 and 1
        if (armServoTopPos > 1.0) {
            armServoTopPos = 1.0;
        } else if (armServoTopPos < 0.0) {
            armServoTopPos = 0.0;
        }

        armServoTop.setPosition(armServoTopPos);
    }

    public static void setArmServoIntakePos(double posDelta){

    }

}
