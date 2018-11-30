package teamcode.ttl2;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.Queue;

class TTL2HardwareManager {

    private static final String FRONT_LEFT_DRIVE_NAME = "FrontLeftDrive";
    private static final String FRONT_RIGHT_DRIVE_NAME = "FrontRightDrive";
    private static final String BACK_LEFT_DRIVE_NAME = "BackLeftDrive";
    private static final String BACK_RIGHT_DRIVE_NAME = "BackRightDrive";

    private static final String ARM_BASE_SERVO_NAME = "ArmServoBase";
    private static final String A = "";
    private static final String B = "";
    private static final String C = "";

    private static final String LIFT_MOTOR_LEFT_NAME = "LiftMotorL";
    private static final String LIFT_MOTOR_RIGHT_NAME = "LiftMotorR";

    // drive hardware
    public static DcMotor frontLeftDrive;
    public static DcMotor frontRightDrive;
    public static DcMotor backLeftDrive;
    public static DcMotor backRightDrive;


    // lift hardware
    public static DcMotor liftMotorL;
    public static DcMotor liftMotorR;

    public static void initialize(LinearOpMode mainClassInstance) {
        HardwareMap hardwareMap = mainClassInstance.hardwareMap;

        frontLeftDrive = hardwareMap.get(DcMotor.class, FRONT_LEFT_DRIVE_NAME);
        frontRightDrive = hardwareMap.get(DcMotor.class, FRONT_RIGHT_DRIVE_NAME);
        backLeftDrive = hardwareMap.get(DcMotor.class, BACK_LEFT_DRIVE_NAME);
        backRightDrive = hardwareMap.get(DcMotor.class, BACK_RIGHT_DRIVE_NAME);

        frontLeftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        backRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        liftMotorL = hardwareMap.get(DcMotor.class, LIFT_MOTOR_LEFT_NAME);
        liftMotorR = hardwareMap.get(DcMotor.class, LIFT_MOTOR_RIGHT_NAME);
    }

}
