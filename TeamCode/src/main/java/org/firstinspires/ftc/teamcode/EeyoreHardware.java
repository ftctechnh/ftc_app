package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

public class EeyoreHardware
{
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

    /* local OpMode members. */
    HardwareMap hwMap = null;
    private ElapsedTime period = new ElapsedTime();

    /* Constructor */
    public EeyoreHardware() {

    }

    public void shootShooter(int power) {
        shooter1.setPower(power);
        shooter2.setPower(power);
    }

    public void pressLeftButton() {
        leftPresser.setPosition(0);

        try {
            Thread.sleep(1500);
        } catch(Exception e) {

        }

        leftPresser.setPosition(0.275);
    }

    public void pressRightButton() {
        rightPresser.setPosition(0);

        try {
            Thread.sleep(1500);
        } catch(Exception e) {

        }

        rightPresser.setPosition(0.275);
    }

    public void moveRobotGyro(double speed, int targetDirection, int time) //Speed is from -1 to 1 and direction is 0 to 360 degrees
    {
        int currentDirection = gyro.getHeading();
        double turnMultiplier = 0.05;
        double driveMultiplier = 0.03;

        long turnStartTime = System.currentTimeMillis();
        //First, check to see if we are pointing in the correct direction
        while(Math.abs(targetDirection - currentDirection) > 5) //If we are more than 5 degrees off target, make corrections before moving
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

        //Now we can move forward, making corrections as needed

        long startTime = System.currentTimeMillis(); //Determine the time as we enter the loop

        while(System.currentTimeMillis() - startTime < time) //Loop for the desired amount of time
        {
            currentDirection = gyro.getHeading();
            int error = targetDirection - currentDirection;
            double speedAdjustment = driveMultiplier * error;

            double leftPower = Range.clip(speed + speedAdjustment, -1, 1);
            double rightPower = Range.clip(speed - speedAdjustment, -1, 1);

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
        leftPresser.setDirection(Servo.Direction.FORWARD);
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
        leftPresser.setPosition(0.275);
        rightPresser.setPosition(0.275);
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
}

