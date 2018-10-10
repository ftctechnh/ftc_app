package teamcode.connor;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * A static-only class which provides hardware-related variables and methods
 */
public final class HardwareManager {

    private static DcMotor driveMotorL;
    private static DcMotor driveMotorR;
    private static DcMotor armMotor;

    private static Servo armServo0;
    private static Servo armServo1;
    private static Servo armServo2;
    private static Servo armServo3;


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
        /*
        armMotor = hardwareMap.get(DcMotor.class, "ArmMotor");
        armMotor.setDirection(DcMotor.Direction.FORWARD);

        armServo1 = hardwareMap.get(Servo.class, "ArmServoTop");
        armServo0 = hardwareMap.get(Servo.class, "ArmServoBottom");
        armServo2 = hardwareMap.get(Servo.class, "GrabberServoLeft");
        armServo3 = hardwareMap.get(Servo.class, "GrabberServoRight");

        armServo0.setDirection(Servo.Direction.FORWARD);
        armServo1.setDirection(Servo.Direction.FORWARD);

        //RotateArmMotor.setDirection(Servo.Direction.FORWARD);
        armServo2.setDirection(Servo.Direction.FORWARD);
        armServo3.setDirection(Servo.Direction.REVERSE);

        //Sets arm motors to work with position
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armServo2.setPosition(0);
        armServo3.setPosition(0);
        */
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

    public static void setArmState(double motorPos, double servo0Pos, double servo1Pos, double servo2Pos, double servo3Pos){
        armMotor.setPower(motorPos);
        armServo0.setPosition(servo0Pos);
        armServo1.setPosition(servo1Pos);
        armServo2.setPosition(servo2Pos);
        armServo3.setPosition(servo3Pos);
    }

    public static void extendArm(double amount){
        double motorPos = armMotor.getPower();
        double servo0Pos = armServo0.getPosition();
        double servo1Pos = armServo0.getPosition();
        double servo2Pos = armServo0.getPosition();
        double servo3Pos = armServo0.getPosition();
        setArmState(motorPos+amount,servo0Pos+amount,servo1Pos+amount,servo2Pos+amount,servo3Pos+amount);
    }

}
