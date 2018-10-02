package org.firstinspires.ftc.teamcode.chassis;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.misc.FtcUtils;

/**
 * Created by Marco on 4/13/18.
 */
public class Robot {
    static final DcMotor.ZeroPowerBehavior ZERO_POWER_BEHAVIOR = DcMotor.ZeroPowerBehavior.BRAKE;
    HardwareMap hwMap;
    ElapsedTime clock = new ElapsedTime();
    private BNO055IMU imu;
    private Rev2mDistanceSensor sensor1;
    BNO055IMU.Parameters parameters;
    private Orientation lastAngles;
    private Orientation currentAngles;
    private double globalAngle = 0;
    private int encoderPos = 0;
    private DcMotor FR = null;
    private DcMotor FL = null;
    private DcMotor BR = null;
    private DcMotor nom = null;
    private DcMotor extend = null;
    private DcMotor BL = null;
    private Servo nomServo = null;
    public void init(HardwareMap ahwMap, boolean initSensors) {
        hwMap = ahwMap;
        FR = hwMap.get(DcMotor.class, "FR");
        FL = hwMap.get(DcMotor.class, "FL");
        nom = hwMap.get(DcMotor.class, "nom");
        BR = hwMap.get(DcMotor.class, "BR");
        BL = hwMap.get(DcMotor.class, "BL");
        nomServo = hwMap.get(Servo.class, "nomServo");
        FR.setZeroPowerBehavior(ZERO_POWER_BEHAVIOR);
        FL.setZeroPowerBehavior(ZERO_POWER_BEHAVIOR);
        BR.setZeroPowerBehavior(ZERO_POWER_BEHAVIOR);
        BL.setZeroPowerBehavior(ZERO_POWER_BEHAVIOR);
        nom.setDirection(DcMotorSimple.Direction.REVERSE);
        extend.setDirection(DcMotorSimple.Direction.FORWARD);
        FL.setDirection(DcMotorSimple.Direction.FORWARD);
        BL.setDirection(DcMotorSimple.Direction.FORWARD);
        FR.setDirection(DcMotorSimple.Direction.REVERSE);
        BR.setDirection(DcMotorSimple.Direction.REVERSE);
       // sensor1 = (Rev2mDistanceSensor) hwMap.get(DistanceSensor.class, "sensor1");
        resetTicks();
        if (initSensors) {
            imu = hwMap.get(BNO055IMU.class, "imu");
            parameters = new BNO055IMU.Parameters();
            parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
            parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
            imu.initialize(parameters);
            resetAngle();
        }
    }
    public void init(HardwareMap ahwMap) {
        init(ahwMap, true);
    }

    public void drive(double fl, double bl, double fr, double br) {
        FL.setPower(fl);
        BL.setPower(bl);
        FR.setPower(fr);
        BR.setPower(br);
    }
    public void resetAngle() {
        lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        globalAngle = 0;
    }
    public double getAngle() {
        currentAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        globalAngle += AngleUnit.normalizeDegrees(currentAngles.firstAngle - lastAngles.firstAngle);
        lastAngles = currentAngles;
        return globalAngle;
    }

    public void resetTicks() {
        encoderPos = BL.getCurrentPosition();
    }
    public int getTicks() {
        return BL.getCurrentPosition() - encoderPos;
    }
    public void nom(double power) {
        nom.setPower(power);
    }
    public void extend(double power) {
        extend.setPower(power);
    }
    public void nomServo(double pos) {
        nomServo.setPosition(pos);
    }
    public double nomServoPos() {
        return FtcUtils.roundTwoDecimalPlaces(nomServo.getPosition());
    }
    public double sensorOneDist() {
        return sensor1.getDistance(DistanceUnit.INCH);
    }
    public void stop() {
        drive(0, 0, 0, 0);
    }
}