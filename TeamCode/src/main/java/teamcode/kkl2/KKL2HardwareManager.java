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
    private static final String LIFT_LATCH_SERVO_NAME = "LiftLockServo";
    private static final String LIFT_SUPPORT_SERVO_NAME = "LiftSupportServo";

    // drive
    public static DcMotor driveLMotor;
    public static DcMotor driveRMotor;

    // lift
    public static DcMotor liftBaseMotor;
    public static Servo liftLatchServo;
    public static Servo liftSupportServo;

    public static void initialize(LinearOpMode mainClassInstance) {
        HardwareMap hardwareMap = mainClassInstance.hardwareMap;

        driveLMotor = hardwareMap.get(DcMotor.class, DRIVE_RIGHT_MOTOR_NAME);
        driveRMotor = hardwareMap.get(DcMotor.class, DRIVE_LEFT_MOTOR_NAME);

        driveLMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        liftBaseMotor = hardwareMap.get(DcMotor.class, LIFT_BASE_MOTOR_NAME);
        liftLatchServo = hardwareMap.get(Servo.class, LIFT_LATCH_SERVO_NAME);
        liftSupportServo = hardwareMap.get(Servo.class, LIFT_SUPPORT_SERVO_NAME);
    }

}
