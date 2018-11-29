package teamcode.kkl1;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Provides hardware-related variables and methods.
 */
final class KKL1HardwareManager {

    public static final boolean DRIVE_ENABLED = true;
    public static final boolean ARM_ENABLED = true;
    public static final boolean INTAKE_ENABLED = true;
    public static final boolean LIFT_ENABLED = true;
    public static final boolean SENSOR_ENABLED = true;

    private static final String L_DRIVE_MOTOR_NAME = "LeftDrive";
    private static final String R_DRIVE_MOTOR_NAME = "RightDrive";
    private static final String ARM_BASE_SERVO_NAME = "ArmBase";
    private static final String ARM_WRIST_SERVO_NAME = "ArmWrist";
    private static final String INTAKE_SERVO_NAME = "Intake";
    private static final String LIFT_MOTOR_NAME = "Lift";
    private static final String SENSOR_SERVO_NAME = "SensorServo";
    private static final String INNER_COLOR_SENSOR_NAME = "InnerColorSensor";
    private static final String OUTER_COLOR_SENSOR_NAME = "OuterColorSensor";

    private static KKL1HardwareManager instance;

    public static DcMotor lDriveMotor;
    public static DcMotor rDriveMotor;
    public static Servo armBaseServo;
    public static Servo armWristServo;
    public static Servo intakeServo;
    public static DcMotor liftMotor;
    public static Servo sensorServo;
    public static ColorSensor innerColorSensor;
    public static ColorSensor outerColorSensor;

    public static void init(LinearOpMode opMode) {
        instance = new KKL1HardwareManager(opMode);
    }

    private KKL1HardwareManager(LinearOpMode opMode) {
        HardwareMap hardwareMap = opMode.hardwareMap;
        if (DRIVE_ENABLED) {
            lDriveMotor = hardwareMap.get(DcMotor.class, L_DRIVE_MOTOR_NAME);
            lDriveMotor.setDirection(DcMotor.Direction.REVERSE);
            rDriveMotor = hardwareMap.get(DcMotor.class, R_DRIVE_MOTOR_NAME);
            rDriveMotor.setDirection(DcMotor.Direction.FORWARD);
        }
        if (ARM_ENABLED) {
            armBaseServo = hardwareMap.get(Servo.class, ARM_BASE_SERVO_NAME);
            armWristServo = hardwareMap.get(Servo.class, ARM_WRIST_SERVO_NAME);
            intakeServo = hardwareMap.get(Servo.class, INTAKE_SERVO_NAME);
        }
        if (LIFT_ENABLED) {
            liftMotor = hardwareMap.get(DcMotor.class, LIFT_MOTOR_NAME);
        }
        if (SENSOR_ENABLED) {
            sensorServo = hardwareMap.get(Servo.class, SENSOR_SERVO_NAME);
            innerColorSensor = hardwareMap.get(ColorSensor.class, INNER_COLOR_SENSOR_NAME);
            outerColorSensor = hardwareMap.get(ColorSensor.class, OUTER_COLOR_SENSOR_NAME);
        }
    }

    /**
     * Sets the power of the drive motors
     *
     * @param powerL the power of the left drive motor
     * @param powerR the power of the right drive motor
     */
    public static void setDrivePower(double powerL, double powerR) {
        if (DRIVE_ENABLED) {
            instance.lDriveMotor.setPower(powerL);
            instance.rDriveMotor.setPower(powerR);
        } else
            throw new IllegalStateException("Drive not enabled");
    }

    public static void setArmBaseServoPower(double power) {
        if (ARM_ENABLED) {
            // clamp power between 0 and 1
            if (power > 1.0) {
                power = 1.0;
            } else if (power < 0.0) {
                power = 0.0;
            }
            instance.armBaseServo.setPosition(power);
        } else {
            throw new IllegalStateException("Arm not enabled");
        }
    }

    public static void setArmWristServoPosition(double pos) {
        if (ARM_ENABLED) {
            // clamp pos between 0 and 1
            if (pos > 1.0) {
                pos = 1.0;
            } else if (pos < 0.0) {
                pos = 0.0;
            }
            instance.armWristServo.setPosition(pos);
        } else {
            throw new IllegalStateException("Arm not enabled");
        }
    }

    public static void setIntakeServoPosition(double position) {
        if (INTAKE_ENABLED) {
            // clamp pos between 0 and 1
            if (position > 1.0) {
                position = 1.0;
            } else if (position < 0.0) {
                position = 0.0;
            }
            instance.intakeServo.setPosition(position);
        } else {
            throw new IllegalStateException("Intake not enabled");
        }
    }

    public static void setLiftMotorPower(double power) {
        if (LIFT_ENABLED) {
            // clamp pos between 0 and 1
            if (power > 1.0) {
                power = 1.0;
            } else if (power < -1.0) {
                power = -1.0;
            }
            instance.liftMotor.setPower(power);
        } else {
            throw new IllegalStateException("Lift not enabled");
        }
    }

}
