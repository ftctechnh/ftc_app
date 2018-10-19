package teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * A static-only class which provides hardware-related variables and methods
 */
public final class HardwareManager {

    private static final String DRIVE_MOTOR_L_NAME = "LeftDriveMotor";
    private static final String DRIVE_MOTOR_R_NAME = "RightDriveMotor";
    private static final String ARM_MOTOR_BASE_NAME = "ArmMotorBase";
    private static final String ARM_SERVO_BASE_NAME = "ArmServoBase";
    private static final String ARM_SERVO_MIDDLE_NAME = "ArmServoMiddle";
    private static final String ARM_SERVO_TOP_NAME = "ArmServoTop";
    private static final String ARM_SERVO_INTAKE_NAME = "ArmServoIntake";

    private static DcMotor driveMotorL;
    private static DcMotor driveMotorR;

    private static DcMotor armMotorBase;
    private static Servo armServoBase;
    private static Servo armServoMiddle;
    private static Servo armServoTop;

    private static CRServo armServoIntake;

    private static int armMotorBasePos;
    private static double armServoBasePos;
    private static double armServoMiddlePos;
    private static double armServoTopPos;


    static {
        initHardware();
    }

    private HardwareManager() {
        // not to be instantiated
    }

    private static void initHardware() {
        HardwareMap hardwareMap = ConnorRobot.instance.hardwareMap;
        /*driveMotorL = hardwareMap.get(DcMotor.class, DRIVE_MOTOR_L_NAME);
        driveMotorR = hardwareMap.get(DcMotor.class, DRIVE_MOTOR_R_NAME);
        //Sets correct directions for drive motors
        driveMotorL.setDirection(DcMotor.Direction.REVERSE);
        driveMotorR.setDirection(DcMotor.Direction.FORWARD);
    */

        armMotorBase = hardwareMap.get(DcMotor.class, ARM_MOTOR_BASE_NAME);
        armServoBase = hardwareMap.get(Servo.class, ARM_SERVO_BASE_NAME);
        armServoMiddle = hardwareMap.get(Servo.class, ARM_SERVO_MIDDLE_NAME);
        armServoTop = hardwareMap.get(Servo.class, ARM_SERVO_TOP_NAME);

        // armServoIntake = hardwareMap.get(CRServo.class, ARM_SERVO_INTAKE_NAME);

        armMotorBase.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotorBase.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armMotorBase.setDirection(DcMotor.Direction.FORWARD);

        armServoBase.setDirection(Servo.Direction.REVERSE);
        armServoMiddle.setDirection(Servo.Direction.FORWARD);
        armServoTop.setDirection(Servo.Direction.FORWARD);

        armMotorBasePos = 0;
        armServoBasePos = 0.0;
        armServoMiddlePos = 0.0;
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

    public static int setArmMotorBasePosition(int posDelta) {
        //armMotorBase.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //armMotorBase.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        int currentPos = armMotorBase.getCurrentPosition();
        int newTargetPos = currentPos + posDelta;
        ConnorRobot.instance.telemetry.addData("new target", newTargetPos);


        // clamp pos to something
        armMotorBase.setPower(.5);
        armMotorBase.setTargetPosition(newTargetPos);
        while (armMotorBase.isBusy())
        {
            /*
            int threshold = newTargetPos - armMotorBase.getCurrentPosition();
            if (Math.abs(threshold) > 5){
                break;
            }
            */
        }

        return armMotorBase.getCurrentPosition();
    }

    public static int getArmMotorBasePosition() {
        return armMotorBase.getTargetPosition();
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

    public static void setArmServoMiddlePosition(double posDelta) {
        armServoMiddlePos += posDelta;

        // clamp pos between 0 and 1
        if (armServoMiddlePos > 1.0) {
            armServoMiddlePos = 1.0;
        } else if (armServoMiddlePos < 0.0) {
            armServoMiddlePos = 0.0;
        }

        armServoMiddle.setPosition(armServoMiddlePos);
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

    public static void setArmServoIntakeSpeed(double speed) {
        armServoIntake.setPower(speed);
    }

}
