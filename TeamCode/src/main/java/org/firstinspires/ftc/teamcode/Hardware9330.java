package org.firstinspires.ftc.teamcode;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;


/**
 * Created by robot on 9/25/2017.
 */

public class Hardware9330 {
    /* Public OpMode members. */

    public static Servo lowGlyphClamp;
    public static Servo highGlyphClamp;
    public static Servo relicHandServo;
    public static Servo relicWristServo;
    public static Servo crystalArm;
    public static DcMotor leftMotor;
    public static DcMotor glyphLiftMotor;
    public static ColorSensor cs;
    public static DistanceSensor ds;
    public static DcMotor rightMotor;
    public static BNO055IMU gyro;
    public static DigitalChannel touch;                // Device Object
    public static DigitalChannel ultrasonicTrigger;
    public static DigitalChannel ultrasonicEcho;



    HardwareMap hwMap                 = null;

    private ElapsedTime period  = new ElapsedTime();

    public Hardware9330() {
    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors

        lowGlyphClamp = hwMap.servo.get("lowGlyphClamp");
        lowGlyphClamp.setDirection(Servo.Direction.REVERSE);
        highGlyphClamp = hwMap.servo.get("highGlyphClamp");
        highGlyphClamp.setDirection(Servo.Direction.REVERSE);
        relicHandServo = hwMap.servo.get("relicHandServo");
        relicHandServo.setDirection(Servo.Direction.REVERSE);
        relicWristServo = hwMap.servo.get("relicWristServo");
        relicWristServo.setDirection(Servo.Direction.REVERSE);
        crystalArm = hwMap.servo.get("crystalArm");
        crystalArm.setDirection(Servo.Direction.REVERSE);
        leftMotor = hwMap.dcMotor.get("leftMotor");
        glyphLiftMotor = hwMap.dcMotor.get("liftMotor");
        cs = hwMap.get(ColorSensor.class, "cs");
        ds = hwMap.get(DistanceSensor.class, "cs");
        rightMotor = hwMap.dcMotor.get("rightMotor");
        gyro = hwMap.get(BNO055IMU.class, "imu");
        touch  = hwMap.get(DigitalChannel.class, "touch");
        ultrasonicTrigger = hwMap.get(DigitalChannel.class, "ultrasonicTrigger");
        ultrasonicEcho = hwMap.get(DigitalChannel.class, "ultrasonicEcho");

        ultrasonicTrigger.setMode(DigitalChannel.Mode.OUTPUT);
        ultrasonicEcho.setMode(DigitalChannel.Mode.INPUT);

        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
}
