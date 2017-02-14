package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

public class EeyoreHardware
{
    static final double COUNTS_PER_MOTOR_REV = 1120;
    static final double DRIVE_GEAR_REDUCTION = 0.75;
    static final double WHEEL_DIAMETER_INCHES = 4.0;
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * Math.PI);

    byte[] range1Cache; //The read will return an array of bytes. They are stored in this variable
    byte[] range2Cache;

    I2cAddr RANGE1ADDRESS = new I2cAddr(0x14); //Default I2C address for MR Range (7-bit)
    I2cAddr RANGE2ADDRESS = new I2cAddr(0x14); //Default I2C address for MR Range (7-bit)
    public static final int RANGE1_REG_START = 0x04; //Register to start reading
    public static final int RANGE2_REG_START = 0x04; //Register to start reading
    public static final int RANGE1_READ_LENGTH = 2; //Number of byte to read
    public static final int RANGE2_READ_LENGTH = 2; //Number of byte to read

    public I2cDevice RANGE1;
    public I2cDevice RANGE2;

    public I2cDeviceSynch RANGE1Reader;
    public I2cDeviceSynch RANGE2Reader;

    /* Public OpMode members. */
    public DcMotor l1 = null;
    public DcMotor l2 = null;
    public DcMotor r1 = null;
    public DcMotor r2 = null;
    public DcMotor shooter1 = null;
    public DcMotor shooter2 = null;
    public DcMotor collection = null;
    public Servo leftPresser = null;
    public Servo rightPresser = null;
    public GyroSensor gyro = null;
    public ColorSensor color = null;
    public

    /* local OpMode members. */
    HardwareMap hwMap = null;
    private ElapsedTime period = new ElapsedTime();

    /* Constructor */
    public EeyoreHardware() {

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors and Sensors
        l1 = hwMap.dcMotor.get("l1");
        l2 = hwMap.dcMotor.get("l2");
        r1 = hwMap.dcMotor.get("r1");
        r2 = hwMap.dcMotor.get("r2");
        shooter1 = hwMap.dcMotor.get("shooter1");
        shooter2 = hwMap.dcMotor.get("shooter2");
        collection = hwMap.dcMotor.get("collection");
        leftPresser = hwMap.servo.get("button_left");
        rightPresser = hwMap.servo.get("button_right");

        color = hwMap.colorSensor.get("color");
        gyro = hwMap.gyroSensor.get("gyro");

        // Set motor direction
        l1.setDirection(DcMotor.Direction.REVERSE);
        l2.setDirection(DcMotor.Direction.REVERSE);
        r1.setDirection(DcMotor.Direction.FORWARD);
        r2.setDirection(DcMotor.Direction.FORWARD);
        shooter1.setDirection(DcMotor.Direction.FORWARD);
        shooter2.setDirection(DcMotor.Direction.FORWARD);
        collection.setDirection(DcMotor.Direction.FORWARD);

        // Set servo direction
        leftPresser.setDirection(Servo.Direction.REVERSE);
        rightPresser.setDirection(Servo.Direction.REVERSE);

        // Set all motors to zero power
        l1.setPower(0);
        l2.setPower(0);
        r1.setPower(0);
        r2.setPower(0);
        shooter1.setPower(0);
        shooter2.setPower(0);
        collection.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        l1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        l2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        r1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        r2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooter1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooter2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        collection.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Initialize servos
        leftPresser.setPosition(0.8);
        rightPresser.setPosition(0.8);

        //Initialize range sensor
        RANGE1 = hwMap.i2cDevice.get("range1");
        RANGE1Reader = new I2cDeviceSynchImpl(RANGE1, RANGE1ADDRESS, false);
        RANGE1Reader.engage();

        RANGE2 = hwMap.i2cDevice.get("range1");
        RANGE2Reader = new I2cDeviceSynchImpl(RANGE2, RANGE2ADDRESS, false);
        RANGE2Reader.engage();
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
    public void waitForTick(long periodMs) throws InterruptedException {
        long  remaining = periodMs - (long)period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0)
            Thread.sleep(remaining);

        // Reset the cycle clock for the next pass.
        period.reset();
    }
    public double[] getWallDistance()
    {
        range1Cache = RANGE1Reader.read(RANGE1_REG_START, RANGE1_READ_LENGTH);
        range2Cache = RANGE2Reader.read(RANGE2_REG_START, RANGE2_READ_LENGTH);

        double range[] = {range1Cache[0] & 0xFF, range2Cache[0]};

        return range;

    }
    public void moveRobotGyro(int targetDirection) //Speed is from -1 to 1 and direction is 0 to 360 degrees
    {
        int currentDirection = gyro.getHeading();
        double turnMultiplier = 0.06; //P value in PID-speak
        double integral;

        while(Math.abs(targetDirection - currentDirection) > 3) //If we are more than 5 degrees off target, make corrections before moving
        {
            currentDirection = gyro.getHeading();

            int error = targetDirection - currentDirection;
            double speedAdjustment = turnMultiplier * error;

            double leftPower = 0.5 * Range.clip(speedAdjustment, -1, 1);
            double rightPower = 0.5 * Range.clip(-speedAdjustment, -1, 1);

            //Finally, assign these values to the motors

            r1.setPower(rightPower);
            r2.setPower(rightPower);
            l1.setPower(leftPower);
            l2.setPower(leftPower);
        }

        r1.setPower(0);
        r2.setPower(0);
        l1.setPower(0);
        l2.setPower(0);
    }

}

