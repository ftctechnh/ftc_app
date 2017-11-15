/**
 * Created by spmce on 12/1/2015.
 */
package org.firstinspires.ftc.teamcode.ResQ;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AccelerationSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.TouchSensorMultiplexer;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.Range;
import java.text.DecimalFormat;
import java.text.NumberFormat;
/**
 * Provides a single hardware access point between custom op-modes and the OpMode class for BigBertha.
 * @author SSI Robotics and revised by Shane McEnaney
 * @version 2015-08-13-20-04-----2015-12-01
 */
public class BigBerthaHardware extends OpMode {//my bad
    //------------Private Variables------------
    private boolean warningGenerated = false;
    private String warningMessage;
    private boolean driveWarningGenerated = false;
    private String driveWarningMessage;
    private boolean servoWarningGenerated = false;
    private String servoWarningMessage;
    private boolean armWarningGenerated = false;
    private String armWarningMessage;
    private boolean liftWarningGenerated = false;
    private String liftWarningMessage;
    private boolean chainWarningGenerated = false;
    private String chainWarningMessage;
    private boolean sweeperWarningGenerated = false;
    private String bucketWarningMessage;
    private boolean spinnerWarningGenerated = false;
    private String spinnerWarningMessage;

    protected int game1config;
    protected int game2config;
    protected static boolean sweeperOff = false;
    protected static boolean aux1ScaleOff = false;
    protected boolean leftDriveOff = false;
    protected boolean rightDriveOff = false;
    protected boolean backLeftOff = false;
    protected boolean backRightOff = false;
    protected boolean fullDriveOff = false;
    protected boolean driveOff = false;
    protected boolean backDriveOff = false;
    protected boolean leftFastDriveOff = false;
    protected boolean rightFastDriveOff = false;
    protected boolean fastDriveOff = false;
    protected boolean leftSlowDriveOff = false;
    protected boolean rightSlowDriveOff = false;
    protected boolean slowDriveOff = false;
    protected boolean chainHooksOff = false;
    protected boolean leftArmOff = false;
    protected boolean rightArmOff = false;
    protected boolean leftLiftOff = false;
    protected boolean rightLiftOff = false;

    protected boolean rightClimberOff = false;
    protected boolean leftClimberOff = false;
    protected boolean climberOff = false;

    protected boolean leftClimberIsPressed = false;
    protected boolean rightClimberIsPressed = false;

    private DcMotor motorLeftDrive, motorRightDrive, motorBackLeft, motorBackRight;
    private DcMotor motorLiftArm, motorLeftArm, motorRightArm;
    private DcMotor motorLift, motorLeftLift, motorRightLift;
    private DcMotor motorChainHooks, motorLeftChain, motorRightChain;
    private DcMotor motorSweeper;
    private DcMotor motorBucket;
    private DcMotor motorSpinner, motorLeftSpinner,motorRightSpinner;

    private Servo servoBucketDoor;
    private Servo servoHook;
    private Servo servoMan;
    private Servo servoFlag, servoRightFlag, servoLeftFlag;
    private Servo servoClimber, servoLeftClimber, servoRightClimber;
    private Servo servoChainHooks, servoLeftChain, servoRightChain;
    private Servo servoSweeper;
    private Servo servoBucket;
    private Servo servoSpinner, servoLeftSpinner, servoRightSpinner;

    protected AccelerationSensor acceleration;
    protected ColorSensor color;
    //protected ColorSensor color2;
    protected CompassSensor compass;
    protected DcMotorController motorController;
    protected GyroSensor gyro;
    protected IrSeekerSensor ir;
    protected LightSensor light;
    protected ServoController servoController;
    protected TouchSensor touch;
    protected TouchSensorMultiplexer multi;
    protected UltrasonicSensor sonar;

    protected LED led;

    //------------Virtual Values of Motors and Servos for Testing Code Without Robot------------
    protected float leftDrivePower;
    protected float rightDrivePower;
    protected float backLeftPower;
    protected float backRightPower;
    protected double chainHooksPower;
    protected double liftPower;
    protected float sweeperPower;
    protected float backSweeperPower;
    protected float sweeperPosition;
    protected float backSweeperPosition;

    protected float liftUpScale;
    protected float liftDownScale;
    protected float chainHooksUpScale;
    protected float chainHooksDownScale;

    private static double leftDriveValue, rightDriveValue, backLeftValue, backRightValue;
    private static double liftArmValue, leftArmValue, rightArmValue;
    private static double liftValue, leftLiftValue, rightLiftValue;
    private static double chainHooksValue, leftChainValue, rightChainValue;
    private static double sweeperValue;
    private static double bucketValue;
    private static double spinnerValue,leftSpinnerValue,rightSpinnerValue;

    private static double sBucketDoorValue;
    private static double sHookValue;
    private static double sManValue;
    private static double sFlagValue, sLeftFlagValue, sRightFlagValue;
    private static double sClimberValue, sLeftClimberValue, sRightClimberValue;
    private static double sChainHooksValue, sLeftChainValue, sRightChainValue;
    private static double sSweeperValue;
    private static double sBucketValue;
    private static double sSpinnerValue, sLeftSpinnerValue, sRightSpinnerValue;//like no u

    double motorValue;

    protected int xVal, yVal, zVal = 0;
    protected int heading = 0;

    //------------initial positions------------
    double initLeftDrivePower;
    double initRightDrivePower;
    double initBackLeftPower;
    double initBackRightPower;
    double initLiftArmPower;
    double initLeftArmPower;
    double initRightArmPower;
    double initLiftPower;
    double initLeftLiftPower = 0.0f;
    double initRightLiftPower;
    double initChainHooksPower;
    double initLeftChainPower;
    double initRightChainPower;
    double initSweeperPower;
    double initBucketPower;
    double initSpinnerPower;
    //0.5 is off, 1 is forwards, and 0 is  for continuous servos
    private double initBucketDoorPosition = 0.5;
    private double initHookPosition = 0.125;
    private double initManPosition = 0.5;
    private double initFlagPosition = 0.95;
    private double initLeftFlagPosition = 1.0;
    private double initRightFlagPosition = 0.0;
    private double initClimberPosition = 0.0;
    private double initLeftClimberPosition = 0.75;
    private double initRightClimberPosition = 0.375;
    private double initChainHooksPosition = 0.5;
    private double initLeftChainPosition = 0.5;
    private double initRightChainPosition = 0.5;
    private double initSweeperPosition = 0.5;
    private double initBucketPosition = 0.5;
    private double initSpinnerPosition = 0.5;
    double initPower = 0.0;

    public BigBerthaHardware () {
    }

    public void mapDevice (HardwareDevice device) {
        try {
            device = hardwareMap.dcMotor.get (String.valueOf(device));
        } catch (Exception opModeException) {
            setDriveWarningMessage(String.valueOf(device));
        }
    }

    @Override public void init () {//The system calls this member once when the OpMode is enabled.
        // Use the hardwareMap to associate class members to hardware ports.
        warningGenerated = false; // Provide telemetry data to a class user
        warningMessage = "Can't map: ";
        driveWarningGenerated = false; // Provide telemetry data to a class user
        driveWarningMessage = "DriveMotors ";
        warningGenerated = false; // Provide telemetry data to a class user
        servoWarningMessage = "Servos: ; ";
        servoWarningGenerated = false; // Provide telemetry data to a class user
        warningMessage = "Can't map; ";
        warningGenerated = false; // Provide telemetry data to a class user
        warningMessage = "Can't map; ";
        warningGenerated = false; // Provide telemetry data to a class user
        warningMessage = "Can't map; ";
        warningGenerated = false; // Provide telemetry data to a class user
        warningMessage = "Can't map; ";
        warningGenerated = false; // Provide telemetry data to a class user
        warningMessage = "Can't map; ";
        warningGenerated = false; // Provide telemetry data to a class user
        warningMessage = "Can't map; ";
        warningGenerated = false; // Provide telemetry data to a class user
        warningMessage = "Can't map; ";
        warningGenerated = false; // Provide telemetry data to a class user
        warningMessage = "Can't map; ";
        // This class prevents the custom op-mode from throwing an exception at runtime.
        //------------DcMotors------------
        try {
            motorLeftDrive = hardwareMap.dcMotor.get ("left");
            motorLeftDrive.setPower(initLeftDrivePower);
        } catch (Exception opModeException) {
            setDriveWarningMessage("leftDrive");
            leftDriveValue = initLeftDrivePower;
        }
        try {
            motorRightDrive = hardwareMap.dcMotor.get ("right");
            motorRightDrive.setPower(initRightDrivePower);
            motorRightDrive.setDirection(DcMotor.Direction.REVERSE);
        } catch (Exception opModeException) {
            setDriveWarningMessage("rightDrive");
            rightDriveValue = initRightDrivePower;
        }
        try {
            motorBackLeft = hardwareMap.dcMotor.get ("backLeft");
            motorBackLeft.setPower(initBackLeftPower);
        } catch (Exception opModeException) {
            setDriveWarningMessage("backLeftDrive");
            
            backLeftValue = initBackLeftPower;
        }
        try {
            motorBackRight = hardwareMap.dcMotor.get ("backRight");
            motorBackRight.setPower(initBackRightPower);
            motorBackRight.setDirection(DcMotor.Direction.REVERSE);
        } catch (Exception opModeException) {
            setDriveWarningMessage("backRightDrive");
            
            backRightValue = initBackRightPower;
        }
        try {
            motorLiftArm = hardwareMap.dcMotor.get ("liftArm");
            motorLiftArm.setPower(initLiftArmPower);
            motorLiftArm.setDirection(DcMotor.Direction.REVERSE);
        } catch (Exception opModeException) {
            setWarningMessage("liftArm");
            
            liftArmValue = initLiftArmPower;
        }
        try {
            motorLeftArm = hardwareMap.dcMotor.get ("leftArm");
            motorLeftArm.setPower(initLeftArmPower);
            //motorLeftArm.setDirection(DcMotor.Direction.REVERSE);
        } catch (Exception opModeException) {
            setWarningMessage("leftArm");
            
            leftArmValue = initLeftArmPower;
        }
        try {
            motorRightArm = hardwareMap.dcMotor.get ("rightArm");
            motorRightArm.setPower(initRightArmPower);
            //motorRightArm.setDirection(DcMotor.Direction.REVERSE);
        } catch (Exception opModeException) {
            setWarningMessage("rightArm");
            
            rightArmValue = initRightArmPower;
        }
        try {
            motorLift = hardwareMap.dcMotor.get ("lift");
            motorLift.setPower(initLiftPower);
        } catch (Exception opModeException) {
            setWarningMessage("lift");
            
            liftValue = initLiftPower;
        }
        try {
            motorLeftLift = hardwareMap.dcMotor.get ("leftLift");
            motorLeftLift.setPower(initLeftLiftPower);
            //motorLeftLift.setDirection(DcMotor.Direction.REVERSE);
        } catch (Exception opModeException) {
            setWarningMessage("leftLift");
            
            leftLiftValue = initLeftLiftPower;
        }
        try {
            motorRightLift = hardwareMap.dcMotor.get ("rightLift");
            motorRightLift.setPower(initRightLiftPower);
            motorRightLift.setDirection(DcMotor.Direction.REVERSE);
        } catch (Exception opModeException) {
            setWarningMessage("rightLift");
            
            rightLiftValue = initRightLiftPower;
        }
        try {
            motorChainHooks = hardwareMap.dcMotor.get ("chainHooks");
            motorChainHooks.setPower(initChainHooksPower);
        } catch (Exception opModeException) {
            setWarningMessage("chainHooks");
            
            chainHooksValue = initChainHooksPower;
        }
        try {
            motorLeftChain = hardwareMap.dcMotor.get ("leftChain");
            motorLeftChain.setPower(initLeftChainPower);
        } catch (Exception opModeException) {
            setWarningMessage("leftChain");
            
            leftChainValue = initLeftChainPower;
        }
        try {
            motorRightChain = hardwareMap.dcMotor.get ("rightChain");
            motorRightChain.setPower(initRightChainPower);
        } catch (Exception opModeException) {
            setWarningMessage("rightChain");
            
            rightChainValue = initRightChainPower;
        }
        try {
            motorSweeper = hardwareMap.dcMotor.get ("sweeper");
            motorSweeper.setPower(initSweeperPower);
            motorSweeper.setDirection(DcMotor.Direction.REVERSE);
        } catch (Exception opModeException) {
            setWarningMessage("sweeper");
            
            sweeperValue = initSweeperPower;
        }
        try {
            motorBucket = hardwareMap.dcMotor.get ("bucket");
            motorBucket.setPower(initBucketPower);
            motorBucket.setDirection(DcMotor.Direction.REVERSE);
        } catch (Exception opModeException) {
            setWarningMessage("bucket");
            
            bucketValue = initBucketPower;
        }
        //------------Servos------------
        //0.5 is off, 1 is forwards, and 0 is backwards
        double startBucketDoorPosition = 0.5;
        double startHookPosition = 0.5;
        double startManPosition = 0.0;
        try {
            motorSpinner = hardwareMap.dcMotor.get ("spinner");
            motorSpinner.setPower(initSpinnerPower);
            //motorSpinner.setDirection(DcMotor.Direction.REVERSE);
        } catch (Exception opModeException) {
            setWarningMessage("spinner");
            
            spinnerValue = initSpinnerPower;
        }
        //------------Servos------------
        try {
            servoBucketDoor = hardwareMap.servo.get ("sBucketDoor");
            servoBucketDoor.setPosition(initBucketDoorPosition);
        } catch (Exception opModeException) {
            setServoWarningMessage("sBucketDoor");
            
            sBucketDoorValue = initBucketDoorPosition;
        }
        try {
            servoHook = hardwareMap.servo.get ("sHook");
            servoHook.setPosition(initHookPosition);
        } catch (Exception opModeException) {
            setServoWarningMessage("sHook");
            
            sHookValue = initHookPosition;
        }
        try {
            servoMan = hardwareMap.servo.get ("sMan");
            servoMan.setPosition (initManPosition);
        } catch (Exception opModeException) {
            setServoWarningMessage("sMan");
            
            sManValue = initManPosition;
        }
        try {
            servoFlag = hardwareMap.servo.get ("sFlag");
            servoFlag.setPosition (initFlagPosition);
        } catch (Exception opModeException) {
            setServoWarningMessage("sFlag");
            
            sFlagValue = initFlagPosition;
        }
        try {
            servoRightFlag = hardwareMap.servo.get ("sRightFlag");
            servoRightFlag.setPosition (initRightFlagPosition);
        } catch (Exception opModeException) {
            setServoWarningMessage("sFlagClimber");
            
            sRightFlagValue = initRightFlagPosition;
        }
        try {
            servoLeftFlag = hardwareMap.servo.get ("sLeftFlag");
            servoLeftFlag.setPosition (initLeftFlagPosition);
        } catch (Exception opModeException) {
            setServoWarningMessage("sLeftFlag");
            
            sLeftFlagValue = initLeftFlagPosition;
        }
        try {
            servoRightClimber = hardwareMap.servo.get ("sRightClimber");
            servoRightClimber.setPosition (initRightClimberPosition);
        } catch (Exception opModeException) {
            setServoWarningMessage("sRightClimber");
            
            sRightClimberValue = initRightClimberPosition;
        }
        try {
            servoLeftClimber = hardwareMap.servo.get ("sLeftClimber");
            servoLeftClimber.setPosition (initLeftClimberPosition);
        } catch (Exception opModeException) {
            setServoWarningMessage("sLeftClimber");
            
            sLeftClimberValue = initLeftClimberPosition;
        }
        try {
            servoChainHooks = hardwareMap.servo.get ("sChainHooks");
            servoChainHooks.setPosition (initChainHooksPosition);
        } catch (Exception opModeException) {
            setServoWarningMessage("sChainHooks");
            
            sChainHooksValue = initChainHooksPosition;
        }
        try {
            servoLeftChain = hardwareMap.servo.get ("sLeftChain");
            servoLeftChain.setPosition (initLeftChainPosition);
        } catch (Exception opModeException) {
            setServoWarningMessage("sLeftChain");
            
            sLeftChainValue = initLeftChainPosition;
        }
        try {
            servoRightChain = hardwareMap.servo.get ("sRightChain");
            servoRightChain.setPosition (initRightChainPosition);
        } catch (Exception opModeException) {
            setServoWarningMessage("sRightChain");
            
            sRightChainValue = initRightChainPosition;
        }
        try {
            servoSweeper = hardwareMap.servo.get ("sSweeper");
            servoSweeper.setPosition (initSweeperPosition);
        } catch (Exception opModeException) {
            setServoWarningMessage("sSweeper");
            
            sSweeperValue = initSweeperPosition;
        }
        try {
            servoBucket = hardwareMap.servo.get ("sBucket");
            servoBucket.setPosition (initBucketPosition);
        } catch (Exception opModeException) {
            setServoWarningMessage("sBucket");
            
            sBucketValue = initBucketPosition;
        }
        try {
            servoSpinner = hardwareMap.servo.get ("sSpinner");
            servoSpinner.setPosition (initSpinnerPosition);
        } catch (Exception opModeException) {
            setServoWarningMessage("sSpinner");
            
            sSpinnerValue = initSpinnerPosition;
        }
        try {
            servoMan = hardwareMap.servo.get ("man");
            servoMan.setPosition (startManPosition);
        } catch (Exception opModeException) {
            setWarningMessage("man");
            
            servoMan = null;
        }
        //------------Sensors------------because no u
        try {
            acceleration = hardwareMap.accelerationSensor.get("acceleration");
        } catch (Exception opModeException) {
            setWarningMessage("acceleration");
            
            acceleration = null;
        }
        try {
            color = hardwareMap.colorSensor.get ("color");
            color.enableLed(true);
        } catch (Exception opModeException) {
            setWarningMessage("color");
            
            color = null;
        }
        /*try {
            color2 = hardwareMap.colorSensor.get ("color2");
            color2.enableLed(false);
        } catch (Exception opModeException) {
            setWarningMessage("color2");
            
            color2 = null;
        }*/
        try {
            compass = hardwareMap.compassSensor.get("compass");
        } catch (Exception opModeException) {
            setWarningMessage("compass");
            
            compass = null;
        }
        try {
            motorController = hardwareMap.dcMotorController.get("motorController");
        } catch (Exception opModeException) {
            setWarningMessage("motorController");
            
            motorController = null;
        }
        try {
            gyro = hardwareMap.gyroSensor.get ("gyro");
        } catch (Exception opModeException) {
            setWarningMessage("gyro");
            
            gyro = null;
        }
        try {
            ir = hardwareMap.irSeekerSensor.get ("ir");
        } catch (Exception opModeException) {
            setWarningMessage("ir");
            
            ir = null;
        }
        try {
            light = hardwareMap.lightSensor.get ("light");
            light.enableLed(true);
        } catch (Exception opModeException) {
            setWarningMessage("light");
            
            light = null;
        }
        try {
            servoController = hardwareMap.servoController.get ("servoController");
        } catch (Exception opModeException) {
            setWarningMessage("servoController");
            
            servoController = null;
        }
        try {
            touch = hardwareMap.touchSensor.get ("touch");
        } catch (Exception opModeException) {
            setWarningMessage("touch");
            
            touch = null;
        }
        try {
            multi = hardwareMap.touchSensorMultiplexer.get ("multi");
        } catch (Exception opModeException) {
            setWarningMessage("multi");
            
            multi = null;
        }
        try {
            sonar = hardwareMap.ultrasonicSensor.get ("sonar");
        } catch (Exception opModeException) {
            setWarningMessage("sonar");
            
            sonar = null;
        }
        compass.setMode(CompassSensor.CompassMode.CALIBRATION_MODE);
        /*try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }*/
    }
    //------------Warnings------------
    boolean getDriveWarningGenerated () {return driveWarningGenerated;}

    String getDriveWarningMessage () {return driveWarningMessage;}

    void setDriveWarningMessage (String opModeExceptionMessage) {
        if (driveWarningGenerated)
            driveWarningMessage += ", ";
        driveWarningGenerated = true;
        driveWarningMessage += opModeExceptionMessage;
    }
    boolean getServoWarningGenerated () {return servoWarningGenerated;}

    String getServoWarningMessage () {return servoWarningMessage;}

    void setServoWarningMessage (String opModeExceptionMessage) {
        if (servoWarningGenerated)
            servoWarningMessage += ", ";
        servoWarningGenerated = true;
        servoWarningMessage += opModeExceptionMessage;
    }
    boolean getWarningGenerated () {return warningGenerated;}

    String getWarningMessage () {return warningMessage;}

    void setWarningMessage (String opModeExceptionMessage) {
        if (warningGenerated)
            warningMessage += ", ";
        warningGenerated = true;
        warningMessage += opModeExceptionMessage;
    }
    //------------OpMode Methods------------
    @Override public void start() {
        compass.setMode(CompassSensor.CompassMode.MEASUREMENT_MODE);
        /*try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }*/
    } //The system calls this member once when the OpMode is enabled.
    @Override public void loop () {} //The system calls this member repeatedly while the OpMode is running.
    @Override public void stop () {} //The system calls this member once when the OpMode is disabled.
    //------------Clip Power------------
    float clipMotor(float power) {return Range.clip(power, -1, 1);}
    float clipServo(float position) {return Range.clip(position, 0, 1);}
    double clipMotor(double power) {return format(Range.clip(power, -1, 1));}
    double clipMotorPositive(double power) {return Range.clip(power, 0, 1);}
    double clipMotorNegative(double power) {return Range.clip(power, -1, 0);}
    double clipServoPositive(double position) {return Range.clip(position, 0.5, 1);}
    double clipServoNegative(double position) {return Range.clip(position, 0, 0.5);}
    double clipServo(double position) {return format(Range.clip(position, 0, 1));}

    private float[] array = {0.00f, 0.05f, 0.09f, 0.10f, 0.12f, 0.15f, 0.18f, 0.24f
            , 0.30f, 0.36f, 0.43f, 0.50f, 0.60f, 0.72f, 0.85f, 1.00f, 1.00f};

    private float[] array2 = {0.00f, 0.05f, 0.10f, 0.15f, 0.20f, 0.24f, 0.29f, 0.34f
            , 0.38f, 0.43f, 0.47f, 0.51f, 0.56f, 0.60f, 0.63f, 0.67f
            , 0.71f, 0.74f, 0.77f, 0.80f, 0.83f, 0.86f, 0.88f, 0.90f
            , 0.92f, 0.94f, 0.96f, 0.97f, 0.98f, 0.99f, 1.00f, 1.00f, 1.00f};
    private float[] array3 = {0.00f, 0.00f, 0.00f, 0.01f, 0.02f, 0.03f, 0.04f, 0.06f
            , 0.08f, 0.10f, 0.12f, 0.14f, 0.17f, 0.20f, 0.23f, 0.26f
            , 0.29f, 0.33f, 0.37f, 0.40f, 0.44f, 0.49f, 0.53f, 0.57f
            , 0.62f, 0.66f, 0.71f, 0.76f, 0.80f, 0.85f, 0.90f, 0.95f, 1.00f};
    private float[] array4 = {0.00f, 0.00f, 0.00f, 0.01f, 0.02f, 0.03f, 0.04f, 0.06f
            , 0.08f, 0.10f, 0.12f, 0.14f, 0.17f, 0.20f, 0.23f, 0.26f
            , 0.29f, 0.33f, 0.37f, 0.40f, 0.44f, 0.49f, 0.53f, 0.57f
            , 0.62f, 0.66f, 0.71f, 0.76f, 0.80f, 0.85f, 0.90f, 0.95f, 1.00f};

    float scaleMotorPower (float power) {
        power = clipMotor(power);
        int index = (int)(power * 16.0); // Get the corresponding index for the specified argument/parameter.
        if (index < 0)
            index = -index;
        else if (index > 16)
            index = 16;
        if (power < 0)
            return (float) format(-array[index]);
        return (float) format(array[index]);
    }
    float myScaleMotorPower (float power) {
        power = clipMotor(power);
        int index = (int)(power * 32.0); // Get the corresponding index for the specified argument/parameter.
        if (index < 0)
            index = -index;
        else if (index > 32)
            index = 32;
        if (power < 0)
            return (float) format(-array3[index]);
        return (float) format(array3[index]);
    }
    float scaleServoPosition (float position) {
        position = clipServo(position);
        int index = (int)(position * 16.0); // Get the corresponding index for the specified argument/parameter.
        if (index < 0)
            index = -index;
        else if (index > 16)
            index = 16;
        if (position < 0)
            return (float) format(-array[index]);
        return (float) format(array[index]);
    }
    double format (double dec) {
        NumberFormat num = new DecimalFormat("#0.00");
        return Double.parseDouble(num.format(dec));
    }
    //------------------------------------TeleOp Methods-------------------------------------
    //------------Get and Set Methods------------
    double getFullValue (double forwards, double backwards) {
        clipMotorPositive(forwards);
        clipMotorNegative(backwards);
        if (forwards == 0 && backwards < 0)
            return backwards;
        return forwards;
    }
    double getFullServoValue (double forwards, double backwards) {
        clipServoPositive(forwards);
        clipServoNegative(backwards);
        if (forwards == 0 && backwards < 0)
            return backwards;
        return forwards;
    }
    //------------Dc Motors------------
    double getLeftDrivePower () {
        if (motorLeftDrive != null)
            return motorLeftDrive.getPower ();
        return leftDriveValue;
    }
    double getRightDrivePower () {
        if (motorRightDrive != null)
            return motorRightDrive.getPower ();
        return rightDriveValue;
    }
    void setDrivePower (double leftPower, double rightPower) {
        leftPower = format(leftPower);
        rightPower = format(rightPower);
        if (motorLeftDrive != null)
            motorLeftDrive.setPower (leftPower);
        leftDriveValue = leftPower;
        if (motorRightDrive != null)
            motorRightDrive.setPower (rightPower);
        rightDriveValue = rightPower;
    }
    double getBackLeftPower () {
        if (motorBackLeft != null)
            return motorBackLeft.getPower ();
        return backLeftValue;
    }
    double getBackRightPower () {
        if (motorBackRight != null)
            return motorBackRight.getPower ();
        return backRightValue;
    }
    void setDrivePower (double leftPower, double rightPower, double backLeftPower, double backRightPower) {
        leftPower = format(leftPower);
        rightPower = format(rightPower);
        backLeftPower = format(backLeftPower);
        backRightPower = format(backRightPower);
        leftPower = clipMotor(leftPower);
        rightPower = clipMotor(rightPower);
        backLeftPower = clipMotor(backLeftPower);
        backRightPower = clipMotor(backRightPower);
        if (motorLeftDrive != null)
            motorLeftDrive.setPower (leftPower);
        leftDriveValue = leftPower;
        if (motorRightDrive != null)
            motorRightDrive.setPower (rightPower);
        rightDriveValue = rightPower;
        if (motorBackLeft != null)
            motorBackLeft.setPower (backLeftPower);
        backLeftValue = backLeftPower;
        if (motorBackRight != null)
            motorBackRight.setPower (backRightPower);
        backRightValue = backRightPower;
    }
    double getLiftArmPower () {
        if (motorLiftArm != null)
            return motorLiftArm.getPower ();
        return liftArmValue;
    }
    double getLeftArmPower () {
        if (motorLeftArm != null)
            return motorLeftArm.getPower ();
        return leftArmValue;
    }
    double getRightArmPower () {
        if (motorRightArm != null)
            return motorRightArm.getPower ();
        return rightArmValue;
    }
    void setLiftArmPower (double liftArmPower) {
        liftArmPower = format(liftArmPower);
        if (motorLiftArm != null)
            motorLiftArm.setPower(liftArmPower);
        liftArmValue = liftArmPower;
    }
    void setLiftArmPower (double leftArmPower, double rightArmPower) {
        leftArmPower = format(leftArmPower);
        rightArmPower = format(rightArmPower);
        if (motorLeftArm != null)
            motorLeftArm.setPower(leftArmPower);
        leftArmValue = leftArmPower;
        if (motorRightArm != null)
            motorRightArm.setPower(rightArmPower);
        rightArmValue = rightArmPower;
    }
    double getLiftPower () {
        if (motorLift != null)
            return motorLift.getPower ();
        return liftValue;
    }
    double getLeftLiftPower () {
        if (motorLeftLift != null)
            return motorLeftLift.getPower ();
        return leftLiftValue;
    }
    double getRightLiftPower () {
        if (motorRightLift != null)
            return motorRightLift.getPower ();
        return rightLiftValue;
    }
    void setLiftPower (double liftPower) {
        liftPower = format(liftPower);
        if (motorLift != null)
            motorLift.setPower(liftPower);
        liftValue = liftPower;
    }
    void setLiftPower (double leftLiftPower, double rightLiftPower) {
        leftLiftPower = format(leftLiftPower);
        rightLiftPower = format(rightLiftPower);
        if (motorLeftLift != null)
            motorLeftLift.setPower(leftLiftPower);
        leftLiftValue = leftLiftPower;
        if (motorRightLift != null)
            motorRightLift.setPower(rightLiftPower);
        rightLiftValue = rightLiftPower;
    }
    double getChainHooksPower () {
        if (motorChainHooks != null)
            return motorChainHooks.getPower ();
        return chainHooksValue;
    }
    double getLeftChainPower () {
        if (motorLeftChain != null)
            return motorLeftChain.getPower ();
        return leftChainValue;
    }
    double getRightChainPower () {
        if (motorRightChain != null)
            return motorRightChain.getPower ();
        return rightChainValue;
    }
    void setChainHooksPower (double chainHooksPower) {
        //chainHooksPower = clipMotor(chainHooksPower);
        if (motorChainHooks != null)
            motorChainHooks.setPower(chainHooksPower);
        chainHooksValue = chainHooksPower;
    }
    void setChainHooksPower (double leftChainPower, double rightChainPower) {
        leftChainPower = clipMotor(leftChainPower);
        if (motorLeftChain != null)
            motorLeftChain.setPower(leftChainPower);
        leftChainValue = leftChainPower;
        rightChainPower = clipMotor(rightChainPower);
        if (motorRightChain != null)
            motorRightChain.setPower(rightChainPower);
        rightChainValue = rightChainPower;
    }
    double getSweeperPower () {
        if (motorSweeper != null)
            return motorSweeper.getPower ();
        return sweeperValue;
    }
    void setSweeperPower (double sweeperPower, double backSweeperPower) {
        double fullSweeperPower = getFullValue(sweeperPower,backSweeperPower);
        fullSweeperPower = format(fullSweeperPower);
        if (motorSweeper != null)
            motorSweeper.setPower(fullSweeperPower);
        sweeperValue = fullSweeperPower;
    }
    double getBucketPower () {
        if (motorBucket != null)
            return motorBucket.getPower ();
        return bucketValue;
    }
    void setBucketPower (double bucketPower) {
        bucketPower = format(bucketPower);
        if (motorBucket != null)
            motorBucket.setPower(bucketPower);
        bucketValue = bucketPower;
    }
    void setBucketPower (double bucketPower, double backBucketPower) {
        double fullBucketPower = getFullValue(bucketPower,backBucketPower);
        fullBucketPower = format(fullBucketPower);
        if (motorBucket != null)
            motorBucket.setPower(fullBucketPower);
        bucketValue = fullBucketPower;
    }
    double getSpinnerPower () {
        if (motorSpinner != null)
            return motorSpinner.getPower();
        return spinnerValue;
    }
    void setSpinnerPower (double spinnerPower) {
        spinnerPower = format(spinnerPower);
        if (motorSpinner != null)
            motorSpinner.setPower(spinnerPower);
        spinnerValue = spinnerPower;
    }
    //------------Servos------------
    double getBucketDoorPosition () {
        if (servoBucketDoor != null)
            return servoBucketDoor.getPosition();
        return sBucketDoorValue;
    }
    void setBucketDoorPosition (double bucketDoorPosition) {
        bucketDoorPosition = clipServo(bucketDoorPosition);
        if (servoBucketDoor != null)
            servoBucketDoor.setPosition(bucketDoorPosition);
        sBucketDoorValue = bucketDoorPosition;
    }
    double getHookPosition () {
        if (servoHook != null)
            return servoHook.getPosition();
        return sHookValue;
    }
    void setHookPosition (double hookPosition) {
        hookPosition = clipServo(hookPosition);
        if (servoHook != null)
            servoHook.setPosition(hookPosition);
        sHookValue = hookPosition;
    }
    double getManPosition () {
        if (servoMan != null)
            return servoMan.getPosition();
        return sManValue;
    }
    void setManPosition (double manPosition) {
        manPosition = clipServo(manPosition);
        if (servoMan != null)
            servoMan.setPosition (manPosition);
        sManValue = manPosition;
    }
    double getFlagPosition () {
        if (servoFlag != null)
            return servoFlag.getPosition();
        return sFlagValue;
    }
    void setFlagPosition (double flagPosition) {
        flagPosition = clipServo(flagPosition);
        if (servoFlag != null)
            servoFlag.setPosition (flagPosition);
        sFlagValue = flagPosition;
    }
    double getRightFlagPosition () {
        if (servoRightFlag != null)
            return servoRightFlag.getPosition();
        return sRightFlagValue;
    }
    void setRightFlagPosition (double rightFlagPosition) {
        rightFlagPosition = clipServo(rightFlagPosition);
        if (servoRightFlag != null)
            servoRightFlag.setPosition (rightFlagPosition);
        sRightFlagValue = rightFlagPosition;
    }
    double getLeftFlagPosition () {
        if (servoLeftFlag != null)
            return servoLeftFlag.getPosition();
        return sLeftFlagValue;
    }
    void setLeftFlagPosition (double leftFlagPosition) {
        leftFlagPosition = clipServo(leftFlagPosition);
        if (servoLeftFlag != null)
            servoLeftFlag.setPosition(leftFlagPosition);
        sLeftFlagValue = leftFlagPosition;
    }
    double getRightClimberPosition () {
        if (servoRightClimber != null)
            return servoRightClimber.getPosition();
        return sRightClimberValue;
    }
    void setRightClimberPosition (double rightClimberPosition) {
        rightClimberPosition = clipServo(rightClimberPosition);
        if (servoRightClimber != null)
            servoRightClimber.setPosition (rightClimberPosition);
        sRightClimberValue = rightClimberPosition;
    }
    double getLeftClimberPosition () {
        if (servoLeftClimber != null)
            return servoLeftClimber.getPosition();
        return sLeftClimberValue;
    }
    void setLeftClimberPosition (double leftClimberPosition) {
        leftClimberPosition = clipServo(leftClimberPosition);
        if (servoLeftClimber != null)
            servoLeftClimber.setPosition (leftClimberPosition);
        sLeftClimberValue = leftClimberPosition;
    }
    double getChainHooksPosition () {
        if (servoChainHooks != null)
            return servoChainHooks.getPosition();
        return sChainHooksValue;
    }
    void setChainHooksPosition (double chainHooksPosition) {
        chainHooksPosition = clipServo(chainHooksPosition);
        if (servoChainHooks != null)
            servoChainHooks.setPosition (chainHooksPosition);
        sChainHooksValue = chainHooksPosition;
    }
    double getLeftChainPosition () {
        if (servoLeftChain != null)
            return servoLeftChain.getPosition();
        return sLeftChainValue;
    }
    void setLeftChainPosition (double leftChainPosition) {
        leftChainPosition = clipServo(leftChainPosition);
        if (servoLeftChain != null)
            servoLeftChain.setPosition (leftChainPosition);
        sLeftChainValue = leftChainPosition;
    }
    double getRightChainPosition () {
        if (servoRightChain != null)
            return servoRightChain.getPosition();
        return sRightChainValue;
    }
    void setRightChainPosition (double rightChainPosition) {
        rightChainPosition = clipServo(rightChainPosition);
        if (servoRightChain != null)
            servoRightChain.setPosition (rightChainPosition);
        sRightChainValue = rightChainPosition;
    }
    double getSweeperPosition () {
        if (servoSweeper != null)
            return servoSweeper.getPosition();
        return sSweeperValue;
    }
    void setSweeperPosition (double sweeperPosition, double backSweeperPosition) {
        double fullSweeperPosition = getFullServoValue(sweeperPosition, backSweeperPosition);
        fullSweeperPosition = format(fullSweeperPosition);
        if (servoSweeper != null)
            servoSweeper.setPosition(fullSweeperPosition);
        sSweeperValue = fullSweeperPosition;
    }
    double getBucketPosition () {
        if (servoBucket != null)
            return servoBucket.getPosition();
        return sBucketValue;
    }
    void setBucketPosition (double bucketPosition) {
        bucketPosition = clipServo(bucketPosition);
        bucketPosition = format(bucketPosition);
        if (servoBucket != null)
            servoBucket.setPosition (bucketPosition);
        sBucketValue = bucketPosition;
    }
    double getSpinnerPosition () {
        if (servoSpinner != null)
            return servoSpinner.getPosition();
        return sSpinnerValue;
    }
    void setSpinnerPosition (double spinnerPosition) {
        spinnerPosition = clipServo(spinnerPosition);
        spinnerPosition = format(spinnerPosition);
        if (servoSpinner != null)
            servoSpinner.setPosition (spinnerPosition);
        sSpinnerValue = spinnerPosition;
    }
    //------------------------------------Autonomous Methods-------------------------------------
    void allServosInitialPosition() {
        if (servoBucketDoor != null)
            servoBucketDoor.setPosition (initBucketDoorPosition);
        if (servoHook != null)
            servoHook.setPosition (initHookPosition);
        if (servoMan != null)
            servoMan.setPosition (initManPosition);
        if (servoFlag != null)
            servoFlag.setPosition (initFlagPosition);
        if (servoRightFlag != null)
            servoRightFlag.setPosition (initRightFlagPosition);
        if (servoLeftFlag != null)
            servoLeftFlag.setPosition (initLeftFlagPosition);
        if (servoRightClimber != null)
            servoRightClimber.setPosition (initRightClimberPosition);
        if (servoLeftClimber != null)
            servoLeftClimber.setPosition (initLeftClimberPosition);
        if (servoChainHooks != null)
            servoChainHooks.setPosition (initChainHooksPosition);
        if (servoLeftChain != null)
            servoLeftChain.setPosition (initLeftChainPosition);
        if (servoRightChain != null)
            servoRightChain.setPosition (initRightChainPosition);
        if (servoSweeper != null)
            servoSweeper.setPosition (initSweeperPosition);
        if (servoBucket != null)
            servoBucket.setPosition (initBucketPosition);
        if (servoSpinner != null)
            servoSpinner.setPosition (initSpinnerPosition);
        sBucketDoorValue = initBucketDoorPosition;
        sHookValue = initHookPosition;
        sManValue = initManPosition;
        sFlagValue = initFlagPosition;
        sRightClimberValue = initRightClimberPosition;
        sLeftClimberValue = initLeftClimberPosition;
        sChainHooksValue = initChainHooksPosition;
        sLeftChainValue = initLeftChainPosition;
        sRightChainValue = initRightChainPosition;
        sSweeperValue = initSweeperPosition;
        sBucketValue = initBucketPosition;
        sSpinnerValue = initSpinnerPosition;
    }
    //------------ Set With Motor Wheel Encoders------------
    /*public void runUsingLeftDriveEncoder() {
        if (motorLeftDrive != null)
            motorLeftDrive.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }
    public void runUsingRightDriveEncoder() {
        if (motorRightDrive != null)
            motorRightDrive.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }
    public void runUsingLiftArmEncoder () {
        if (motorLiftArm != null)
            motorLiftArm.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }
    public void runUsingLiftEncoder () {
        if (motorLift != null)
            motorLift.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }
    public void runUsingChainHooksEncoder () {
        if (motorChainHooks != null)
            motorChainHooks.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }
    public void runUsingSweeperEncoder () {
        if (motorSweeper != null)
            motorSweeper.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }
    public void runUsingBucketEncoder () {
        if (motorBucket != null)
            motorBucket.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }
    public void runUsingSpinnerEncoder () {
        if (motorSpinner != null)
            motorSpinner.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }
    public void runUsingDriveEncoders () {
        runUsingLeftDriveEncoder();
        runUsingRightDriveEncoder();
    }
    public void runUsingEncoders () {
        runUsingLeftDriveEncoder();
        runUsingRightDriveEncoder();
        runUsingLiftArmEncoder();
        runUsingLiftEncoder();
        runUsingChainHooksEncoder();
        runUsingSweeperEncoder();
        runUsingBucketEncoder();
        runUsingSpinnerEncoder();
    }
    //------------Set Without Motor Wheel Encoders------------
    public void runWithoutLeftDriveEncoder () {
        if (motorLeftDrive != null) {
            if (motorLeftDrive.getChannelMode () == DcMotorController.RunMode.RESET_ENCODERS)
                motorLeftDrive.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        }
    }
    public void runWithoutRightDriveEncoder () {
        if (motorRightDrive != null) {
            if (motorRightDrive.getChannelMode () == DcMotorController.RunMode.RESET_ENCODERS)
                motorRightDrive.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        }
    }
    public void runWithoutDriveEncoders () {
        runWithoutLeftDriveEncoder();
        runWithoutRightDriveEncoder();
    }
    public void runWithoutLiftArmEncoder () {
        if (motorLiftArm != null) {
            if (motorLiftArm.getChannelMode () == DcMotorController.RunMode.RESET_ENCODERS)
                motorLiftArm.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        }
    }
    public void runWithoutLiftEncoder () {
        if (motorLift != null) {
            if (motorLift.getChannelMode () == DcMotorController.RunMode.RESET_ENCODERS)
                motorLift.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        }
    }
    public void runWithoutChainHooksEncoder () {
        if (motorChainHooks != null) {
            if (motorChainHooks.getChannelMode () == DcMotorController.RunMode.RESET_ENCODERS)
                motorChainHooks.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        }
    }
    public void runWithoutSweeperEncoder () {
        if (motorSweeper != null) {
            if (motorSweeper.getChannelMode () == DcMotorController.RunMode.RESET_ENCODERS)
                motorSweeper.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        }
    }
    public void runWithoutBucketEncoder () {
        if (motorBucket != null) {
            if (motorBucket.getChannelMode () == DcMotorController.RunMode.RESET_ENCODERS)
                motorBucket.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        }
    }
    public void runWithoutSpinnerEncoder () {
        if (motorSpinner != null) {
            if (motorSpinner.getChannelMode () == DcMotorController.RunMode.RESET_ENCODERS)
                motorSpinner.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        }
    }
    //------------Reset Motor Wheel Encoders------------
    public void resetLeftDriveEncoder () {
        if (motorLeftDrive != null)
            motorLeftDrive.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
    }
    public void resetRightDriveEncoder () {
        if (motorRightDrive != null)
            motorRightDrive.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
    }
    public void resetDriveEncoders () {
        resetLeftDriveEncoder();
        resetRightDriveEncoder();
    }
    public void resetLiftArmEncoder () {
        if (motorLiftArm != null)
            motorLiftArm.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
    }
    public void resetLiftEncoder () {
        if (motorLift != null)
            motorLift.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
    }
    public void resetChainHooksEncoder () {
        if (motorChainHooks != null)
            motorChainHooks.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
    }
    public void resetSweeperEncoder () {
        if (motorSweeper != null)
            motorSweeper.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
    }
    public void resetBucketEncoder () {
        if (motorBucket != null)
            motorBucket.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
    }
    public void resetSpinnerEncoder () {
        if (motorSpinner != null)
            motorSpinner.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
    }
    //------------Get Motor Wheel Encoder Count------------
    int getLeftEncoderCount () {
        if (motorLeftDrive != null)
            return motorLeftDrive.getCurrentPosition ();
        return 0;
    }
    int getRightEncoderCount () {
        if (motorRightDrive != null)
            return motorRightDrive.getCurrentPosition ();
        return 0;
    }
    int getLiftArmEncoderCount () {
        if (motorLiftArm != null)
            return motorLiftArm.getCurrentPosition ();
        return 0;
    }
    int getLiftEncoderCount () {
        if (motorLift != null)
            return motorLift.getCurrentPosition ();
        return 0;
    }
    int getChainHooksEncoderCount () {
        if (motorChainHooks != null)
            return motorChainHooks.getCurrentPosition ();
        return 0;
    }
    int getSweeperEncoderCount () {
        if (motorSweeper != null)
            return motorSweeper.getCurrentPosition ();
        return 0;
    }
    int getBucketEncoderCount () {
        if (motorBucket != null)
            return motorBucket.getCurrentPosition();
        return 0;
    }
    int getSpinnerEncoderCount () {
        if (motorSpinner != null)
            return motorSpinner.getCurrentPosition();
        return 0;
    }
    //------------Indicate Motor Wheel Encoders Value------------
    boolean hasLeftDriveEncoderReached (double count) {
        if (motorLeftDrive != null) {
            // TODO Implement stall code using these variables.
            if (Math.abs (motorLeftDrive.getCurrentPosition ()) > count)
                return true;
        }
        return false;
    }
    boolean hasRightDriveEncoderReached (double count) {
        if (motorRightDrive != null) {
            // TODO Implement stall code using these variables.
            if (Math.abs (motorRightDrive.getCurrentPosition ()) > count)
                return true;
        }
        return false;
    }
    boolean haveDriveEncodersReached (double leftCount, double rightCount) {return hasLeftDriveEncoderReached(leftCount)
            && hasRightDriveEncoderReached(rightCount);}
    boolean hasLiftArmEncoderReached (double count) {
        if (motorLiftArm != null) {
            // TODO Implement stall code using these variables.
            if (Math.abs (motorLiftArm.getCurrentPosition ()) > count)
                return true;
        }
        return false;
    }
    boolean hasLiftEncoderReached (double count) {
        if (motorLift != null) {
            // TODO Implement stall code using these variables.
            if (Math.abs (motorLift.getCurrentPosition ()) > count)
                return true;
        }
        return false;
    }
    boolean hasChainHooksEncoderReached (double count) {
        if (motorChainHooks != null) {
            // TODO Implement stall code using these variables.
            if (Math.abs (motorChainHooks.getCurrentPosition ()) > count)
                return true;
        }
        return false;
    }
    boolean hasSweeperEncoderReached (double count) {
        if (motorSweeper != null) {
            // TODO Implement stall code using these variables.
            if (Math.abs (motorSweeper.getCurrentPosition ()) > count)
                return true;
        }
        return false;
    }
    boolean hasBucketEncoderReached (double count) {
        if (motorBucket != null) {
            // TODO Implement stall code using these variables.
            if (Math.abs (motorBucket.getCurrentPosition ()) > count)
                return true;
        }
        return false;
    }
    boolean hasSpinnerEncoderReached (double count) {
        if (motorSpinner != null) {
            // TODO Implement stall code using these variables.
            if (Math.abs (motorSpinner.getCurrentPosition ()) > count)
                return true;
        }
        return false;
    }
    //------------Indicate whether the motor wheel encoders have reached a value------------
    boolean driveUsingEncoders (double leftPower, double rightPower, double leftCount, double rightCount) {
        runUsingEncoders();
        setDrivePower(leftPower, rightPower);
        if (haveDriveEncodersReached(leftCount, rightCount)) {
            resetDriveEncoders();
            setDrivePower(0.0f, 0.0f);
            return true;
        }
        return false;
    }
    boolean runLiftArmUsingEncoder (double liftArmPower, double liftArmCount) {
        runUsingEncoders();
        setLiftArmPower(liftArmPower);
        if (hasLiftArmEncoderReached(liftArmCount)) {
            resetLiftArmEncoder();
            setLiftArmPower(0.0f);
            return true;
        }
        return false;
    }
    boolean runLiftUsingEncoder (double liftPower, double liftCount) {
        runUsingEncoders();
        setLiftPower(liftPower);
        if (hasLiftEncoderReached(liftCount)) {
            resetLiftEncoder();
            setLiftPower(0.0f);
            return true;
        }
        return false;
    }
    boolean runChainHooksUsingEncoder (double chainHooksPower, double chainHooksCount) {
        runUsingEncoders();
        setChainHooksPower(chainHooksPower);
        if (hasChainHooksEncoderReached(chainHooksCount)) {
            resetChainHooksEncoder();
            setChainHooksPower(0.0f);
            return true;
        }
        return false;
    }
    boolean runSweeperUsingEncoder (double sweeperPower, double backSweeperPower, double sweeperCount) {
        runUsingEncoders();
        setSweeperPower(sweeperPower, backSweeperPower);
        if (hasSweeperEncoderReached(sweeperCount)) {
            resetSweeperEncoder();
            setSweeperPower(0.0f, 0.0f);
            return true;
        }
        return false;
    }
    boolean runBucketUsingEncoder (double bucketPower, double backBucketPower, double bucketCount) {
        runUsingEncoders();
        setBucketPower(bucketPower, backBucketPower);
        if (hasBucketEncoderReached(bucketCount)) {
            resetBucketEncoder();
            setBucketPower(0.0f, 0.0f);
            return true;
        }
        return false;
    }
    boolean runSpinnerUsingEncoder (double spinnerPower, double spinnerCount) {
        runUsingEncoders();
        setSpinnerPower(spinnerPower);
        if (hasSpinnerEncoderReached(spinnerCount)) {
            resetSpinnerEncoder();
            setSpinnerPower(0.0f);
            return true;
        }
        return false;
    }
    //------------Indicate If Motor Wheel Encoder Has Reset------------
    boolean hasLeftDriveEncoderReset () {return getLeftEncoderCount() == 0;}
    boolean hasRightDriveEncoderReset() {return getRightEncoderCount() == 0;}
    boolean haveDriveEncodersReset   () {return hasLeftDriveEncoderReset() && hasRightDriveEncoderReset();}
    boolean hasLiftArmEncoderReset   () {return (getLiftArmEncoderCount() == 0);}
    boolean hasLiftEncoderReset      () {return (getLiftEncoderCount() == 0);}
    boolean hasChainHooksEncoderReset() {return (getChainHooksEncoderCount() == 0);}
    boolean hasSweeperEncoderReset   () {return (getSweeperEncoderCount() == 0);}
    boolean hasBucketEncoderReset    () {return (getBucketEncoderCount() == 0);}
    boolean hasSpinnerEncoderReset   () {return (getSpinnerEncoderCount() == 0);}*/
}
