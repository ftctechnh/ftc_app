package teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Provides hardware-related variables and methods.
 */
public final class HardwareManager {

    public static final boolean DRIVE_ENABLED = false;
    public static final boolean ARM_ENABLED = true;

    private static final String DRIVE_MOTOR_L_NAME = "LeftDriveMotor";
    private static final String DRIVE_MOTOR_R_NAME = "RightDriveMotor";
    private static final String ARM_MOTOR_BASE_NAME = "ArmMotorBase";
    private static final String ARM_SERVO_BASE_NAME = "ArmServoBase";
    private static final String ARM_SERVO_MIDDLE_NAME = "ArmServoMiddle";
    private static final String ARM_SERVO_TOP_NAME = "ArmServoTop";
    private static final String ARM_SERVO_INTAKE_NAME = "ArmServoIntake";

    private static final double ARM_MOTOR_BASE_POWER = 1.0;
    private static final double ARM_MOTOR_TICKS_PER_REVOLUTION = 1200;

    private static HardwareManager instance;

    private DcMotor driveMotorL;
    private DcMotor driveMotorR;
    private DcMotor armMotorBase;
    private Servo armServoBase;
    private Servo armServoMiddle;
    private Servo armServoTop;
    private Servo armServoIntake;

    public static void init() {
        instance = new HardwareManager();
    }

    private HardwareManager() {
        HardwareMap hardwareMap = ConnorRobot.instance.hardwareMap;
        if (DRIVE_ENABLED) {
            driveMotorL = hardwareMap.get(DcMotor.class, DRIVE_MOTOR_L_NAME);
            driveMotorL.setDirection(DcMotor.Direction.REVERSE);
        }
        if (DRIVE_ENABLED) {
            driveMotorR = hardwareMap.get(DcMotor.class, DRIVE_MOTOR_R_NAME);
            driveMotorR.setDirection(DcMotor.Direction.FORWARD);
        }
        if (ARM_ENABLED) {
            armMotorBase = hardwareMap.get(DcMotor.class, ARM_MOTOR_BASE_NAME);
            armMotorBase.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            armMotorBase.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            armMotorBase.setDirection(DcMotor.Direction.FORWARD);
        }
        if (ARM_ENABLED) {
            armServoBase = hardwareMap.get(Servo.class, ARM_SERVO_BASE_NAME);
            armServoBase.setDirection(Servo.Direction.REVERSE);
            armMotorBase.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            armMotorBase.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        if (ARM_ENABLED) {
            armServoMiddle = hardwareMap.get(Servo.class, ARM_SERVO_MIDDLE_NAME);
            armServoMiddle.setDirection(Servo.Direction.FORWARD);
        }
        if (ARM_ENABLED) {
            armServoTop = hardwareMap.get(Servo.class, ARM_SERVO_TOP_NAME);
            armServoTop.setDirection(Servo.Direction.FORWARD);
        }
        if (ARM_ENABLED) {
            armServoIntake = hardwareMap.get(Servo.class, ARM_SERVO_INTAKE_NAME);
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

    /**
     * @param rot from 0.0 to 1.0, representing how far through a revolution the motor is
     */
    public static void setArmMotorBasePosition(double rot) {
        if (ARM_ENABLED) {
            if (rot < 0.0) {
                rot = 0.0;
            } else if (rot > 1.0) {
                rot = 1.0;
            }
            instance.armMotorBase.setPower(ARM_MOTOR_BASE_POWER);
            int ticks = (int) (rot * ARM_MOTOR_TICKS_PER_REVOLUTION);
            instance.armMotorBase.setTargetPosition(ticks);
        } else {
            throw new IllegalStateException("armMotorBase is not enabled");
        }
    }

    public static void setArmServoBasePosition(double pos) {
        if (ARM_ENABLED) {
            // clamp pos between 0 and 1
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

    public static void setArmServoMiddlePosition(double pos) {
        if (ARM_ENABLED) {
            // clamp pos between 0 and 1
            if (pos > 1.0) {
                pos = 1.0;
            } else if (pos < 0.0) {
                pos = 0.0;
            }
            instance.armServoMiddle.setPosition(pos);
        } else {
            throw new IllegalStateException("armServoMiddle is not enabled");
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

    public static void setArmServoIntakePosition(double pos) {
        if (ARM_ENABLED) {
            // clamp pos between 0 and 1
            if (pos > 1.0) {
                pos = 1.0;
            } else if (pos < 0.0) {
                pos = 0.0;
            }
            instance.armServoIntake.setPosition(pos);
        } else {
            throw new IllegalStateException("armServoIntake is not enabled");
        }
    }

}
