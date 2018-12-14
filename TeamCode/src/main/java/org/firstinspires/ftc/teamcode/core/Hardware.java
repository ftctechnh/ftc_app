package org.firstinspires.ftc.teamcode.core;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.Telemetry;

    //This class needs to be changed to be object oriented, not a combination of object and static
/**
 * The class that handles the initialization and calibration of all of the variables required for the Hardware
 */
public class Hardware
{
    public static DcMotor frontRightDrive;
    public static DcMotor frontLeftDrive;
    public static DcMotor backRightDrive;
    public static DcMotor backLeftDrive;

    public static DcMotor armLiftMotorTop;
    public static DcMotor armLiftMotorBottom;
    public static DcMotor armTiltMotor;
    public static DcMotor wrist;

    public static CRServo armServo;
    public static Servo markerServo;
    public static DigitalChannel magLimitSwitchTilt;
    public static CRServo finger1;
    public static CRServo finger2;

    public static Telemetry telemetry;
    public static HardwareMap hardwareMap;

    /**
     * init EVERYTHING
     */
    public static void initAllHardware(HardwareMap hardwareMap)
    {
        frontRightDrive = hardwareMap.dcMotor.get("front_right_drive");
        frontLeftDrive = hardwareMap.dcMotor.get("front_left_drive");
        backRightDrive = hardwareMap.dcMotor.get("back_right_drive");
        backLeftDrive =  hardwareMap.dcMotor.get("back_left_drive");

        armLiftMotorTop = hardwareMap.dcMotor.get("arm_lift_motor_top");
        armLiftMotorBottom = hardwareMap.dcMotor.get("arm_lift_motor_bottom");
        armTiltMotor = hardwareMap.dcMotor.get("arm_tilt_motor");
        wrist = hardwareMap.dcMotor.get("wrist");

        markerServo = hardwareMap.servo.get("marker_servo");
        armServo = hardwareMap.crservo.get("arm_servo");
        finger1 = hardwareMap.crservo.get("finger1");
        finger2 = hardwareMap.crservo.get("finger2");

        magLimitSwitchTilt = hardwareMap.digitalChannel.get("mag_limit_switch_tilt");

        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);
        armLiftMotorBottom.setDirection(DcMotor.Direction.REVERSE);

    }
}