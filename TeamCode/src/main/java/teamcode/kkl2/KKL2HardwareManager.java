package teamcode.kkl2;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

class KKL2HardwareManager {

    private static final String DRIVE_LEFT_MOTOR_NAME = "DriveLMotor";
    private static final String DRIVE_RIGHT_MOTOR_NAME = "DriveRMotor";
    private static final String LIFT_BASE_MOTOR_NAME = "LiftBaseMotor";

    private static final String LIFT_LATCH_SERVO_NAME = "LiftLatchServo";
    private static final String LIFT_LOCK_SERVO_NAME = "LiftLockServo";
    private static final String INTAKE_BASE_SERVO_NAME = "IntakeBaseServo";
    private static final String INTAKE_WRIST_SERVO_NAME = "IntakeWristServo";
    private static final String INTAKE_SWALLOW_NAME = "IntakeSwallow";

    // drive
    public static DcMotor driveLMotor;
    public static DcMotor driveRMotor;

    // lift
    public static DcMotor liftBaseMotor;
    public static Servo liftLatchServo;

    public static Servo liftLockServo;

    // intake
    public static Servo intakeWristServo;

    public static void initialize(LinearOpMode mainClassInstance) {
        HardwareMap hardwareMap = mainClassInstance.hardwareMap;

        driveLMotor = hardwareMap.get(DcMotor.class, DRIVE_RIGHT_MOTOR_NAME);
        driveRMotor = hardwareMap.get(DcMotor.class, DRIVE_LEFT_MOTOR_NAME);

        liftBaseMotor = hardwareMap.get(DcMotor.class, LIFT_BASE_MOTOR_NAME);
        liftLatchServo = hardwareMap.get(Servo.class, LIFT_LATCH_SERVO_NAME);
        liftLockServo = hardwareMap.get(Servo.class, LIFT_LOCK_SERVO_NAME);
        //intakeWristServo = hardwareMap.get(Servo.class, INTAKE_WRIST_SERVO_NAME);

        driveLMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        liftBaseMotor = hardwareMap.get(DcMotor.class, LIFT_BASE_MOTOR_NAME);
        liftLatchServo = hardwareMap.get(Servo.class, LIFT_LATCH_SERVO_NAME);
    }

}
