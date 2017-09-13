package org.firstinspires.ftc.rick;

import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a Pushbot.
 * See PushbotTeleopTank_Iterative and others classes starting with "Pushbot" for usage examples.
 *
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 *
 * Motor channel:  Left  drive motor:        "left_drive"
 * Motor channel:  Right drive motor:        "right_drive"
 * Motor channel:  Manipulator drive motor:  "left_arm"
 * Servo channel:  Servo to open left claw:  "left_hand"
 * Servo channel:  Servo to open right claw: "right_hand"
 */
public class HardwareGromit
{
    /* Public OpMode members. */
    public DcMotor  motorleft   = null;
    public DcMotor  motorright  = null;
    public CRServo  servoCR     = null;


    public static final double MID_SERVO       =  0.5 ;
    public static final double ARM_UP_POWER    =  0.45 ;
    public static final double ARM_DOWN_POWER  = -0.45 ;

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    public BNO055IMU imu = null;
    public DeviceInterfaceModule   dim;

    /* Constructor */
    public HardwareGromit(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;
        // Define and Initialize Motors from the hardwaremap
        motorleft   = hwMap.dcMotor.get("motorleft");
        motorright  = hwMap.dcMotor.get("motorright");

        motorleft.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        motorright.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
        // Set all motors to zero power
        motorleft.setPower(0);
        motorright.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        motorleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        // Define and initialize ALL installed servos.
        servoCR = hwMap.crservo.get("servo_cr");
        servoCR.setPower(0.0);
        //leftClaw = hwMap.servo.get("left_hand");
        //rightClaw = hwMap.servo.get("right_hand");
        //leftClaw.setPosition(MID_SERVO);
        //rightClaw.setPosition(MID_SERVO);

        //Define and initialize the imu
        imu = hwMap.get(BNO055IMU.class, "imu");
        setImuParameters();                             //Setup other parameters on the imu

        // setup the DeviceInterface Module
        //dim = hwMap.get(DeviceInterfaceModule) ;
        dim = hwMap.deviceInterfaceModule.get("dim");
    }

    public void setImuParameters() {
        BNO055IMU.Parameters  parameters = new BNO055IMU.Parameters();
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.useExternalCrystal = true;
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        parameters.pitchMode = BNO055IMU.PitchMode.WINDOWS;
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        imu.initialize(parameters);

    }
}

