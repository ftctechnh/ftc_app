package org.firstinspires.ftc.teamcode.Code_2017_18_Season;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

//This class defines all the specific hardware for our robot.

public class MasterHardwareClass_2017_18 {
    /* Public OpMode members. */
    public DcMotor frontLeftMotor = null;
    public DcMotor frontRightMotor = null;
    public DcMotor backLeftMotor = null;
    public DcMotor backRightMotor = null;
    public DcMotor verticalArmMotor = null;
    public DcMotor clawMotor = null;
    public DcMotor slowMotor = null;
    public DcMotor fastMotor = null;
    public Servo   trayServo = null;
    public Servo gemServo;
    public BNO055IMU imu;

    /* Give place holder values for the motors and the grabber servo */
    double FrontLeftPower = 0;
    double FrontRightPower = 0;
    double BackRightPower = 0;
    double BackLeftPower = 0;
    double VerticalArmPower = 0;
    double ClawPower = 0;

    /*These values are used for the drive*/
    double verticalMax = 5900;
    double verticalMin = 300;
    double slowPower = 1;
    double fastPower = 1;
    double trayOut = 0;
    double trayIn = 1;
    boolean up;

    /* Define values for teleop bumper control */
    static double nobumper = 1.5;
    static double bumperSlowest = 3.2;
    static double bumperFastest = 1.0;

    /* Define values used in knocking the jewels */
    static double xPosUp = 0;
    static double xPosDown = .5;

    /* Define hardwaremap */
    HardwareMap hwMap = null;

    /* Constructor */
    public MasterHardwareClass_2017_18() {

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {

        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Hardware
        frontLeftMotor = hwMap.dcMotor.get("FL");
        frontRightMotor = hwMap.dcMotor.get("FR");
        backLeftMotor = hwMap.dcMotor.get("BL");
        backRightMotor = hwMap.dcMotor.get("BR");
        verticalArmMotor = hwMap.dcMotor.get("VAM");
        clawMotor = hwMap.dcMotor.get("CM");
        gemServo = hwMap.servo.get("gemservo");
        imu = hwMap.get(BNO055IMU.class, "imu");

        // Set all hardware to default position
        frontLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backLeftMotor.setPower(0);
        backRightMotor.setPower(0);
        verticalArmMotor.setPower(0);
        clawMotor.setPower(0);

        /* Reverse the direction of the front right and back right motors */
        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        backRightMotor.setDirection(DcMotor.Direction.REVERSE);

        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        verticalArmMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        // Set proper encoder state for all motor
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}
