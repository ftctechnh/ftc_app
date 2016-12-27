package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.hardware.adafruit.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a K9 robot.
 *
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 *
 * Motor channel:  Left  drive motor:        "left motor"
 * Motor channel:  Right drive motor:        "right motor"
 * Servo channel:  Servo to raise/lower arm: "arm"
 * Servo channel:  Servo to open/close claw: "claw"
 *
 * Note: the configuration of the servos is such that:
 *   As the arm servo approaches 0, the arm position moves up (away from the floor).
 *   As the claw servo approaches 0, the claw opens up (drops the game element).
 */
public class HardwareDM
{
    /* Public OpMode members. */

    /* Drive train motors */
    public DcMotor  lfDrive = null;
    public DcMotor  lrDrive = null;
    public DcMotor  rfDrive = null;
    public DcMotor  rrDrive = null;

    /* Shooter motors */
    public DcMotor lShoot = null;
    public DcMotor rShoot = null;

    /*  Intake motor */
    public DcMotor intake = null;

    /* Lift motor */
    public DcMotor liftMotor = null;

    /* Shooter feed cam */
    public CRServo fire = null;

    /* beacon and pivot servos */
    public Servo beacon = null;
    public Servo pivot = null;

    /* lift limit switch - touch sensor */
    public TouchSensor liftLimit = null;

    // The IMU sensor object
    BNO055IMU imu;

    /* Adafruit RGB Sensor */
    ColorSensor beaconColor;

    // MR Color sensor on bottom
    ColorSensor stripeColor;

    DeviceInterfaceModule cdim;

    // we assume that the LED pin of the RGB sensor is connected to
    // digital port 5 (zero indexed).
    static final int LED_CHANNEL = 0;

    // Servo max min ranges
    public final static double PIVOT_HOME = 0.025;
    public final static double BEACON_HOME = 0.0;
    public final static double PIVOT_MIN_RANGE  = 0.025;
    public final static double PIVOT_MAX_RANGE  = 0.55;
    public final static double BEACON_MIN_RANGE  = 0.0;
    public final static double BEACON_MAX_RANGE  = 0.52;

    /* Local OpMode members. */
    HardwareMap hwMap  = null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public HardwareDM() {
    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // save reference to HW Map
        hwMap = ahwMap;

        // Define and Initialize Motors
        lfDrive   = hwMap.dcMotor.get("lf motor");
        lrDrive   = hwMap.dcMotor.get("lr motor");
        rfDrive   = hwMap.dcMotor.get("rf motor");
        rrDrive   = hwMap.dcMotor.get("rr motor");
        lShoot    = hwMap.dcMotor.get("l shoot");
        rShoot    = hwMap.dcMotor.get("r shoot");
        intake    = hwMap.dcMotor.get("intake");
        liftMotor = hwMap.dcMotor.get("lift");

        // Define and initialize servos
        fire = hwMap.crservo.get("fire");
        beacon = hwMap.servo.get("beacon");
        pivot = hwMap.servo.get("pivot");
        beacon.setPosition(BEACON_HOME);
        pivot.setPosition(PIVOT_HOME);

        // Define touch sensor
        liftLimit = hwMap.touchSensor.get("ts");

        // Set all motors to zero power
        lfDrive.setPower(0.0);
        lrDrive.setPower(0.0);
        rfDrive.setPower(0.0);
        rrDrive.setPower(0.0);
        lShoot.setPower(0.0);
        rShoot.setPower(0.0);
        fire.setPower(0.0);
        intake.setPower(0.0);

        // Set all motors to run with or without encoders
        //
        setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lShoot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rShoot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Set motor directions
        lfDrive.setDirection(DcMotor.Direction.REVERSE);
        lrDrive.setDirection(DcMotor.Direction.REVERSE);
        rfDrive.setDirection(DcMotor.Direction.FORWARD);
        rrDrive.setDirection(DcMotor.Direction.FORWARD);
        lShoot.setDirection(DcMotor.Direction.REVERSE);
        rShoot.setDirection(DcMotor.Direction.FORWARD);
        intake.setDirection(DcMotor.Direction.REVERSE);
        liftMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        // Set motors to float mode to protect gearboxes from shock load
        setDriveZeroPower(DcMotor.ZeroPowerBehavior.FLOAT);
        lShoot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rShoot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        // Intake and lift can be brake mode to hold positon better
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        // Set up the parameters with which we will use our IMU. Note that integration
        // algorithm here just reports accelerations to the logcat log; it doesn't actually
        // provide positional information.
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "AdafruitIMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hwMap.get(BNO055IMU.class, "gyro");
        imu.initialize(parameters);

        // Retrieve and initialize the Adafruit color sensor
        beaconColor = hwMap.colorSensor.get("color");

        cdim = hwMap.deviceInterfaceModule.get("dim");
        cdim.setDigitalChannelState(LED_CHANNEL, false); // Turn RGB light off

        // And also for the MR color sensor
        stripeColor = hwMap.colorSensor.get("stripe");
        stripeColor.enableLed(false);

    }

    /***
     *
     * waitForTick implements a periodic delay. However, this acts like a metronome with a regular
     * periodic tick.  This is used to compensate for varying processing times for each cycle.
     * The function looks at the elapsed cycle time, and sleeps for the remaining time interval.
     *
     * @param periodMs  Length of wait cycle in mSec.
     * @throws InterruptedException
     */
    public void waitForTick(long periodMs)  throws InterruptedException {

        long  remaining = periodMs - (long)period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0)
            Thread.sleep(remaining);

        // Reset the cycle clock for the next pass.
        period.reset();
    }

    public void setDriveMode(DcMotor.RunMode mode) {
        setDriveFrontMode(mode);
        setDriveRearMode(mode);
    }

    public void setDriveFrontMode(DcMotor.RunMode mode){
        lfDrive.setMode(mode);
        rfDrive.setMode(mode);
    }

    public void setDriveRearMode(DcMotor.RunMode mode){
        lrDrive.setMode(mode);
        rrDrive.setMode(mode);
    }

    public void setDriveZeroPower(DcMotor.ZeroPowerBehavior behavior) {
        setDriveFrontZeroPower(behavior);
        setDriveRearZeroPower(behavior);
    }

    public void setDriveFrontZeroPower(DcMotor.ZeroPowerBehavior behavior) {
        lfDrive.setZeroPowerBehavior(behavior);
        rfDrive.setZeroPowerBehavior(behavior);
    }

    public void setDriveRearZeroPower(DcMotor.ZeroPowerBehavior behavior) {
        lrDrive.setZeroPowerBehavior(behavior);
        lfDrive.setZeroPowerBehavior(behavior);
    }

}
