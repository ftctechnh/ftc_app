package teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Provides hardware-related variables and methods.
 */
public final class HardwareManager {

    public static final boolean DRIVE_ENABLED = true;
    public static final boolean ARM_ENABLED = true;
    public static final boolean CLIMB_ENABLED = true;

    private static final String DRIVE_MOTOR_L_NAME = "LeftDriveMotor";
    private static final String DRIVE_MOTOR_R_NAME = "RightDriveMotor";
    private static final String ARM_SERVO_BASE_NAME = "ArmServoBase";
    private static final String ARM_SERVO_TOP_NAME = "ArmServoTop";
    private static final String ARM_SERVO_INTAKE_NAME = "ArmServoIntake";
    private static final String CLIMB_MOTOR_NAME = "ClimbMotor";

    private static HardwareManager instance;

    private DcMotor driveMotorL;
    private DcMotor driveMotorR;
    private Servo armServoBase;
    private Servo armServoTop;
    private Servo armServoIntake;
    private DcMotor climbMotor;

    public static void init() {
        instance = new HardwareManager();
    }

    private HardwareManager() {
        HardwareMap hardwareMap = ConnorRobot.instance.hardwareMap;
        if (DRIVE_ENABLED) {
            driveMotorL = hardwareMap.get(DcMotor.class, DRIVE_MOTOR_L_NAME);
            driveMotorL.setDirection(DcMotor.Direction.REVERSE);
            driveMotorR = hardwareMap.get(DcMotor.class, DRIVE_MOTOR_R_NAME);
            driveMotorR.setDirection(DcMotor.Direction.FORWARD);
        }
        if (ARM_ENABLED) {
            armServoBase = hardwareMap.get(Servo.class, ARM_SERVO_BASE_NAME);
            armServoTop = hardwareMap.get(Servo.class, ARM_SERVO_TOP_NAME);
            armServoIntake = hardwareMap.get(Servo.class, ARM_SERVO_INTAKE_NAME);
        }
        if (CLIMB_ENABLED) {
            climbMotor = hardwareMap.get(DcMotor.class, CLIMB_MOTOR_NAME);
        }
    }

    /**
     * Sets the power of the drive motors
     *
     * @param powerL the power of the left drive motor
     * @param powerR the power of the right drive motor
     */
    public static void setDrivePower(double powerL, double powerR) {
        instance.driveMotorL.setPower(powerL);
        instance.driveMotorR.setPower(powerR);
    }

    public static void setArmServoBasePosition(double pos) {
        if (ARM_ENABLED) {
            // clamp power between 0 and 1
            if (pos > 1.0) {
                pos = 1.0;
            } else if (pos < 0.0) {
                pos = 0.0;
            }
            instance.armServoBase.setPosition(pos);
        } else {
            throw new IllegalStateException("armServoBase is not enabled");
        }
    }

    public static void setArmServoTopPosition(double pos) {
        if (ARM_ENABLED) {
            // clamp pos between 0 and 1
            if (pos > 1.0) {
                pos = 1.0;
            } else if (pos < 0.0) {
                pos = 0.0;
            }
            instance.armServoTop.setPosition(pos);
        } else {
            throw new IllegalStateException("armServoTop is not enabled");
        }
    }

    public static void setArmServoIntakePosition(double position) {
        if (ARM_ENABLED) {
            // clamp pos between 0 and 1
            if (position > 1.0) {
                position = 1.0;
            } else if (position < 0.0) {
                position = 0.0;
            }
            instance.armServoIntake.setPosition(position);
        } else {
            throw new IllegalStateException("armServoIntake is not enabled");
        }
    }

    public static void setClimbMotorPower(double power) {
        if (ARM_ENABLED) {
            // clamp pos between 0 and 1
            if (power > 1.0) {
                power = 1.0;
            } else if (power < -1.0) {
                power = -1.0;
            }
            instance.climbMotor.setPower(power);
        } else {
            throw new IllegalStateException("climbMotor is not enabled");
        }
    }

}
