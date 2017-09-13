package org.firstinspires.ftc.rick;

import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;

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
public class Hardware_8045_Rmux
{
    /* Public OpMode members. */
    public DcMotor  motorleft   = null;
    public DcMotor  motorright  = null;
    public CRServo  servoCR     = null;
    public LightSensor lightSensor = null;
    public ColorSensor HTcolorsensor = null;

    public MultiplexColorSensor muxColor;
    public int[] ports = {0,1,2,3};

    public static final double MID_SERVO       =  0.5 ;
    public static final double ARM_UP_POWER    =  0.45 ;
    public static final double ARM_DOWN_POWER  = -0.45 ;

    //Encoder Counts math
    public static final double     COUNTS_PER_MOTOR_REV    = 1120 ;    // eg: TETRIX Motor Encoder 1440
    public static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    public static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    public static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);

    // These constants define the desired driving/control characteristics
    // The can/should be tweaked to suite the specific robot drive train.
    public static final double     DRIVE_SPEED             = 0.6;     // Nominal speed for better accuracy.
    public static final double     TURN_SPEED              = 0.6;     // Nominal half speed for better accuracy.

    public static final double     HEADING_THRESHOLD       = 2.0 ;     // As tight as we can make it with an integer gyro
    public static final double     TURN_MIN_SPEED          = 0.07;     //Speed it should approach as it comes near to the target
    public static final double     PROPORTIONAL_TURN_COEFF = 0.7;      //fudge factor for turning Larger is more responsive, but also less stable an could take longer
    public static final double     PROPORTIONAL_DRIVE_COEFF= 0.03;     // Larger is more responsive, but also less stable

    //DIM Light ports
    public static final int    BLUE_LED    = 0;     // Blue LED channel on DIM
    public static final int    RED_LED     = 1;     // Red LED Channel on DIM

    //Menu Variables
    public int      nitems = 12;    // easier to just include 0 as the first element so there are actually 5
    public String[] menulabel = {"Blue Team","Mode", "Delay","Heading 1", "Distance 1","Heading 2","Distance 2","Heading 3","Distance 3","Heading 4","Distance 4","Heading 5","Distance 5"};

    public int[]    menuvalue = {1,1,0, -45,50, 30,-5, 0,13, 0,39 ,130,50};
    public int      programmode,secondstodelay,heading1,distance1,heading2,distance2,heading3,distance3,heading4,distance4,heading5,distance5;
    public boolean  TeamisBlue;
     /**
     *These are the Booleans for the button releases
     */
     boolean startisreleased = true;
     boolean backisreleased = true;

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    public BNO055IMU imu = null;
    public DeviceInterfaceModule   dim;


    /* Constructor */
    public Hardware_8045_Rmux(){

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

        // setup lego light sensor
        //LightSensor lightSensor;  // Hardware Device Object
        // get a reference to our Light Sensor object.
        lightSensor = hwMap.lightSensor.get("lightsensor");

        HTcolorsensor = hwMap.colorSensor.get("htcolor");   //Blue side Color Sensor

        // mux stuff
        int milliSeconds = 48;
        muxColor = new MultiplexColorSensor(hwMap, "mux", "ada",
                ports, milliSeconds,
                MultiplexColorSensor.GAIN_16X);

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

     /**
     * This method returns a 3x1 array of doubles with the yaw, pitch, and roll in that order.
     * The equations used in this method came from:
     * https://en.wikipedia.org/wiki/Conversion_between_quaternions_and_Euler_angles#Euler_Angles_from_Quaternion
     */
    //Get Angles method****************************************************************************************
    public double[] getAngles() {

        Quaternion quatAngles = imu.getQuaternionOrientation();

        double w = quatAngles.w;
        double x = quatAngles.x;
        double y = quatAngles.y;
        double z = quatAngles.z;

        // for the Adafruit IMU, yaw and roll are switched
        double roll = Math.atan2( 2*(w*x + y*z) , 1 - 2*(x*x + y*y) ) * 180.0 / Math.PI;
        double pitch = Math.asin( 2*(w*y - x*z) ) * 180.0 / Math.PI;
        double yaw = Math.atan2( 2*(w*z + x*y), 1 - 2*(y*y + z*z) ) * 180.0 / Math.PI;

        return new double[]{yaw, pitch, roll};
    }

}

