package RicksCode;

//import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;

public class Hardware_8045Worlds
{
    /* Public OpMode members. */
//    public DcMotor  motorleft   = null;
//    public DcMotor  motorright  = null;
//    public DcMotor  motorforklift = null;
//    public DcMotor  motorsweeper = null;
//    public DcMotor  motorlauncher = null;
//    public DcMotor  lightsgreen = null;
//    public DcMotor  lightsblue = null;
//    public DcMotor  motorforkliftright = null;

//    public CRServo  servoCR     = null;
//    public Servo    servoloader = null;
//    public Servo    servoforklock = null;
//    public Servo    servoforklockleft = null;
//    public Servo    servoforkthumb = null;
//    public Servo    servodeflector = null;
//    public LightSensor lightSensor = null;
//    public ColorSensor HTcolorsensor = null;
//    public OpticalDistanceSensor odsSensor;  // Hardware Device Object
//    public AnalogInput maxbotixleft;
//    public AnalogInput maxbotixright;
//    public AnalogInput sharp;
//    public AnalogInput ballsensor;
//    public AnalogInput robotsensor;
//    public DigitalChannel ballsensor2isEmpty;                // Device Object

    //public MuxColorSensor muxColor;                        // Moved to Linear opmode
//   public int[] ports = {2,3,4,5};
//    public int[] red_ports = {2,3,4,5};
//    public int[] blue_ports = {0,1,4,5};

    public static final double loaderopen       =  0.70 ;  // positions for servo gate for loader
    public static final double loaderclosed    =  0.45 ;

    public static final double forkopen       =  1.0 ;  // positions for Forklift locking servo
    public static final double forkclosed    =  0.5 ;
    public static final double forkleftopen       =  0.0 ;  // positions for Left Forklift locking servo
    public static final double forkleftclosed    =  0.45 ;

    public static final double thumbopen       =  0.8 ;  // positions for Forklift locking servo
    public static final double thumbclosed    =  0.45;
    public static final double thumbinitialized    =  0.0;

    public static final double deflectorup       =  0.85 ;  // positions for Forklift locking servo
    public static final double deflectordown    =  0.3;
    boolean deflectoroff = true;

    public static final double loaderthreshold    =  0.5 ;  // voltage for sharpIR loader arm sensor
    public static final double ballthreshold    =    1.5 ;  // voltage for sharpIR to sense ball is loaded
    //public static final double supermodeballthreshold    =    0.4 ;  // voltage for sharpIR to sense ball is loaded

    public static final double endgamesignalstart   =  75.0 ;

    public static final double sweeperpower   =  0.75 ;  // default power for sweeper.
    public static final double sweeperpowerauto   =  0.3 ;  // default power for sweeper in autonomous
    public static final double sweeperpowerspit   =  -0.2 ;  // default power for sweeper when it spits out a ball
    public static final    int buttontime = 1250;

    //Encoder Counts math
    public static final double     COUNTS_PER_MOTOR_REV    = 1120 ;    // eg: TETRIX Motor Encoder 1440
    public static final double     DRIVE_GEAR_REDUCTION    = (16.0/24.0);     // drive sprocket / motor sprocket
    //public static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    public static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    public static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);

    // These constants define the desired driving/control characteristics
    // The can/should be tweaked to suite the specific robot drive train.
    public static final double     GYRO_DRIVE_SPEED             = 0.5;     // Nominal speed for better accuracy.
    public static final double     TURN_SPEED              = 0.3;     // Nominal half speed for better accuracy.

    public static final double     HEADING_THRESHOLD       = 3.0 ;     // As tight as we can make it with an integer gyro
    public static final double     TURN_MIN_SPEED          = 0.12;     //Speed it should approach as it comes near to the target
    public static final double     PROPORTIONAL_TURN_COEFF = 0.2;      //fudge factor for turning Larger is more responsive, but also less stable an could take longer
    public static final double     PROPORTIONAL_DRIVE_COEFF= 0.015;     // Larger is more responsive, but also less stable

    //Dim Light ports
    public static final int    BLUE_LED    = 0;     // Blue LED channel on DIM
    public static final int    RED_LED     = 1;     // Red LED Channel on DIM

    //Menu Variables
    //public int      nitems = 12;    // easier to just include 0 as the first element so there are actually 5
    public String[] menulabel = {"Team","Mode", "Particles","Delay","Heading 0", "Distance 0","Heading 1", "Distance 1","Heading 2","Distance 2","Heading 3","Distance 3","Heading 4","Distance 4","Heading 5","Distance 5","Right Color Threshold","Left Color Threshold","SuperMode Turn Blue","SuperMode Turn Red","Hit Cap Ball","Pick Up Partner Ball"};

    public String[] teamname = {"Red", "Blue"};
    public String[] modename = {"Beacon Route","Judging Mode","Beacon Route SUPER Mode"," Simple Capball","Simple Ramp", "Defense Mode","Super Defense Mode"};
    //                                           -52,58    supermodeturnred 4

    public int[]    menuvalue = {1,0,2,0, 0,8,  -45,55,   20,-14,   0,13,   0,30   ,130,40, 121,121, -6,-7,1,0};
    public int      programmode,secondstodelay,particles,heading0,distance0,heading1,distance1,heading2,distance2,heading3,distance3,heading4,distance4,heading5,distance5,rightaveragecolor,leftaveragecolor, supermodeturnblue,supermodeturnred;
    public boolean  TeamisBlue,PickUpPartnerBall, hitcapball;



    int[] rightballcrgb;
    double redblueright;

    int[] leftballcrgb;
    double redblueleft;

    public double   rightbluecutoff;
    public double   rightredcutoff ;

    public double   leftbluecutoff;
    public double   leftredcutoff;

    static boolean sweeperforward;
    boolean smartsweeping = false;
    /*public double   rightbluecutoff = 1.18;
    public double   rightredcutoff =1.26;

    public double   leftbluecutoff = 1.18;
    public double   leftredcutoff = 1.26;*/

    boolean detectballs;
    boolean rapidfire = false;
    boolean ballloaded = false;
    boolean firingaball = false;
    public double timesincesensor = 0.0;

    //boolean rapidfire = false;
     /**
     *These are the Booleans for the button releases
     */
//
//    boolean start1isreleased = true;
//    boolean start2isreleased = true;
    boolean back1isreleased = true;
    boolean back2isreleased = true;
    boolean rightbumper2isreleased = true;
    boolean righttrigger2isreleased = true;
    boolean leftbumper2isreleased = true;
    boolean x1isreleased= true;
    boolean x2isreleased = true;
    boolean y2isreleased = true;
    boolean y1isreleased = true;
    boolean a1isreleased = true;
    //boolean isreleased = true;

    boolean rightbumperisreleased = true;
    boolean righttriggerisreleased = true;
    boolean dpadupisreleased = true;

    //Other Variables
    boolean forkliftlock = false;
    boolean forklocked = true;
    boolean thumbdown = true;
    boolean forkliftdropping = false;
    boolean sweeperon = false;
    boolean launcherzeroing = false;    //Used to determine when to turn off the motor when zeroing the launcher
    boolean loadingballs = true;        //Used to determine when to close the Launcher Servo
    boolean launcherfiring = false;     //Used to determine when a full cycle has been rotated on the launcher
    float firingposition;               //Used to make sure the launcher goes atleast a half revolution before the sensor turns it off


    int launcherzeroposition;
   // boolean imuenabled;
    /**
     * Variables for Tele-Op
     */
    double drivespeed= 0.8;

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
//    private ElapsedTime period  = new ElapsedTime();

//    public BNO055IMU imu = null;
//    public DeviceInterfaceModule   dim;
//    public DeviceInterfaceModule   dim2;


    /* Constructor */
    public Hardware_8045Worlds(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap, boolean imuenabled) {
        // Save reference to Hardware map
        hwMap = ahwMap;
//        // Define and Initialize Motors from the hardwaremap
//        motorleft   = hwMap.dcMotor.get("motorleft");
//        motorright  = hwMap.dcMotor.get("motorright");
//        motorforklift = hwMap.dcMotor.get("motorforklift");
//        motorforkliftright = hwMap.dcMotor.get("motorforkliftright");
//        motorsweeper = hwMap.dcMotor.get("motorsweeper");
//        motorlauncher = hwMap.dcMotor.get("motorlauncher");
//        lightsgreen = hwMap.dcMotor.get("lightsgreen");
//        lightsblue = hwMap.dcMotor.get("lightsblue");
//
//        motorleft.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
//        motorright.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
//        motorforklift.setDirection(DcMotor.Direction.REVERSE);
//        motorforkliftright.setDirection(DcMotor.Direction.FORWARD);
//        motorsweeper.setDirection(DcMotor.Direction.FORWARD);
//        lightsgreen.setDirection(DcMotor.Direction.FORWARD);
//        lightsblue.setDirection(DcMotor.Direction.FORWARD);
//
//
//        // Set all motors to zero power
//        motorleft.setPower(0.0);
//        motorright.setPower(0.0);
//        motorforklift.setPower(0.0);
//        motorforkliftright.setPower(0.0);
//        motorsweeper.setPower(0.0);
//        motorlauncher.setPower(0.0);
//
//        //Turn Off lights
//        lightsgreen.setPower(0.0);
//        lightsblue.setPower(0.0);
//
//
//        // Set all motors to reset encoders.
//        motorleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        motorright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        motorforklift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        motorforkliftright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        motorlauncher.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//
//        //Pid Control for motors
//        motorleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        motorright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        motorforklift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        motorforkliftright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        motorlauncher.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        motorsweeper.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        lightsgreen.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        lightsblue.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//
//        // zero power mode  // default is coast?
//        motorleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        motorright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        motorforklift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        motorforkliftright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        motorsweeper.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
//        motorlauncher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        lightsgreen.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
//        lightsblue.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
//
//        // Define and initialize ALL installed servos.
//        servoCR = hwMap.crservo.get("servobutton");
//        servoCR.setPower(0.0);
//        //Ball Loading Servo
//        servoloader = hwMap.servo.get("servoloader");
//        servoloader.setPosition(loaderopen);
//        //Forklift locking servo
//        servoforklock = hwMap.servo.get("servoforklock");
//        servoforklock.setPosition(forkclosed);
//        //Left Fork Lock servo
//        servoforklockleft = hwMap.servo.get("servoforklockleft");
//        servoforklockleft.setPosition(forkleftclosed);
//        //Thumb on forklift
//        servoforkthumb = hwMap.servo.get("servoforkthumb");
//        servoforkthumb.setPosition(thumbinitialized);
//        //Ball deflector in tele-op
//        servodeflector = hwMap.servo.get("servodeflector");
////        servodeflector.setPosition(deflectordown);


//        //Define and initialize the imu
//        if(imuenabled){
//            //Doesn't initialize in teleop
//            imu = hwMap.get(BNO055IMU.class, "imu");
//            setImuParameters();//Setup other parameters on the imu
//            servodeflector.setPosition(deflectordown);
//        }
//        else{
//            servodeflector.setPosition(deflectorup);
//        }

        // setup the DeviceInterface Module
//        dim = hwMap.deviceInterfaceModule.get("dim");
//        dim2 = hwMap.deviceInterfaceModule.get("dim2");
        // setup lego light sensor
        //LightSensor lightSensor;  // Hardware Device Object
        // get a reference to our Light Sensor object.
        //lightSensor = hwMap.lightSensor.get("lightsensor");

        //HTcolorsensor = hwMap.colorSensor.get("htcolor");   //Blue side Color Sensor

        // mux setup
        /*int milliSeconds = 10;
        muxColor = new MuxColorSensor(hwMap, "mux", "ada",
                ports, milliSeconds,
                MuxColorSensor.GAIN_60X);//Change this for potential different Gain*/

//        //Maxbotix Sensor
//        maxbotixleft = hwMap.analogInput.get("maxsonarleft");
//        maxbotixright = hwMap.analogInput.get("maxsonarright");
//        //Sharp IR Sensors
//        sharp = hwMap.analogInput.get("sharpir");
//        ballsensor = hwMap.analogInput.get("ballsensor");
//        robotsensor = hwMap.analogInput.get("robotsensor");
//        //Digital Sharp sensor
//        ballsensor2isEmpty = hwMap.digitalChannel.get("ballsensor2");
//        ballsensor2isEmpty.setMode(DigitalChannelController.Mode.INPUT);          // Set the direction of each channel
//
//        // Optical Distance Sensor
//        odsSensor = hwMap.opticalDistanceSensor.get("ods");
    }

//    public void setImuParameters() {
//        BNO055IMU.Parameters  parameters = new BNO055IMU.Parameters();
//        parameters.mode = BNO055IMU.SensorMode.IMU;
//        parameters.useExternalCrystal = true;
//        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
//        parameters.pitchMode = BNO055IMU.PitchMode.WINDOWS;
//        parameters.loggingEnabled = true;
//        parameters.loggingTag = "IMU";
//        imu.initialize(parameters);
//
//    }

     /**
     * This method returns a 3x1 array of doubles with the yaw, pitch, and roll in that order.
     * The equations used in this method came from:
     * https://en.wikipedia.org/wiki/Conversion_between_quaternions_and_Euler_angles#Euler_Angles_from_Quaternion
     */
    //Get Angles method****************************************************************************************
//    public double[] getAngles() {
//
//        Quaternion quatAngles = imu.getQuaternionOrientation();
//
//        double w = quatAngles.w;
//        double x = quatAngles.x;
//        double y = quatAngles.y;
//        double z = quatAngles.z;
//
//        // for the Adafruit IMU, yaw and roll are switched
//        double roll = Math.atan2( 2*(w*x + y*z) , 1 - 2*(x*x + y*y) ) * 180.0 / Math.PI;
//        double pitch = Math.asin( 2*(w*y - x*z) ) * 180.0 / Math.PI;
//        double yaw = Math.atan2( 2*(w*z + x*y), 1 - 2*(y*y + z*z) ) * 180.0 / Math.PI;
//
//        return new double[]{yaw, pitch, roll};
//    }

}

