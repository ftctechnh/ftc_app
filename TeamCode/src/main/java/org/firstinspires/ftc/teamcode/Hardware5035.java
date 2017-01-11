package org.firstinspires.ftc.teamcode;


import android.hardware.Sensor;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.ElapsedTime;


/**
 * Created by Kota Baer on 10/11/2016.
 */

/**
 * This is NOT an opmode.
 * <p/>
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a Pushbot.
 * See PushbotTeleopTank_Iterative and others classes starting with "Pushbot" for usage examples.
 * <p/>
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 * <p/>
 * Motor channel:  Left  driveReverse motor:        "left_drive"
 * Motor channel:  Right driveReverse motor:        "right_drive"
 */
public class Hardware5035 {
    /* Public OpMode members. */
    public DcMotor leftMotor = null;
    public DcMotor rightMotor = null;
    public DcMotor ballBooster1 = null;
    public DcMotor ballBooster2 = null;
    public DcMotor ballDump = null;
    public DcMotor sweeperMotor = null;
    public Servo popUp = null;
    public TouchSensor grabbutton;
    public TouchSensor balldumpup;
    public LightSensor leftLightSensor;
    public LightSensor rightLightSensor;
    public ColorSensor colorDetector;
    public UltrasonicSensor frontUltra;
    public OpticalDistanceSensor sideUltra;
    public Servo constServo;
    //color
    //ultrasonic
    //light
    //servo
    //ultrasonic



    /* local OpMode members. */
    HardwareMap hwMap = null;
    private ElapsedTime period = new ElapsedTime();

    /* Constructor */
    public Hardware5035() {

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        colorDetector = hwMap.colorSensor.get("Detector");
        frontUltra = hwMap.ultrasonicSensor.get("front ultra");
        sideUltra = hwMap.opticalDistanceSensor.get("side ultra");
        constServo = hwMap.servo.get("const servo");
        ballBooster1 = hwMap.dcMotor.get("ball booster 1");
        ballBooster2 = hwMap.dcMotor.get("ball booster 2");
        leftMotor = hwMap.dcMotor.get("left driveReverse");
        rightMotor = hwMap.dcMotor.get("right driveReverse");
        ballDump = hwMap.dcMotor.get("ball dump");
        popUp = hwMap.servo.get("pop up");
        sweeperMotor = hwMap.dcMotor.get("sweeperMotor");
        grabbutton = hwMap.touchSensor.get("grab button");
        balldumpup = hwMap.touchSensor.get("ballarmup");
        leftLightSensor = hwMap.lightSensor.get("left Sensor");
        rightLightSensor = hwMap.lightSensor.get("right Sensor");
        leftMotor.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        rightMotor.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors
        ballBooster1.setDirection(DcMotor.Direction.FORWARD);
        ballBooster2.setDirection(DcMotor.Direction.REVERSE);
        ballDump.setDirection(DcMotor.Direction.REVERSE);
        ballBooster1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        ballBooster2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftLightSensor.enableLed(true);
        rightLightSensor.enableLed(true);


        // Set all motors to zero power
        sweeperMotor.setPower(0);
        leftMotor.setPower(0);
        rightMotor.setPower(0);
        ballBooster1.setPower(0);
        ballBooster2.setPower(0);
        ballDump.setPower(0.10);
        popUp.setPosition(1);
        constServo.setPosition(0.51);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //Encoders are not currently hooked up. uncoment these lines when they are.
        ballBooster1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        ballBooster2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /***
     * waitForTick implements a periodic delay. However, this acts like a metronome with a regular
     * periodic tick.  This is used to compensate for varying processing times for each cycle.
     * The function looks at the elapsed cycle time, and sleeps for the remaining time interval.
     *
     * @param periodMs Length of wait cycle in mSec.
     * @throws InterruptedException
     */
    public void waitForTick(long periodMs) throws InterruptedException {

        long remaining = periodMs - (long) period.milliseconds();


        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0)
            Thread.sleep(remaining);

        // Reset the cycle clock for the next pass.
        period.reset();
    }
    public void triggered() {
        if(null != popUp)              popUp.setPosition(.70);
    }

    public void detriggered() {
        if(null != popUp)              popUp.setPosition(1);
    }

    public double getPowerForTicksfordrive (int ticksToGo)
    {
        int multi = 3;
        if (ticksToGo > 1440 * multi)
        {
            return .6;
        }
        if (ticksToGo > 720 * multi)
        {
            return .375;
        }
        if (ticksToGo > 520 * multi)
        {
            return .375;
        }
        if (ticksToGo > 120 * multi)
        {
            return .3555;
        }
        if (ticksToGo >= 0 * multi)
        {
            return .350;
        }
        return 0;
    }

    public double getPowerForTicksforturn (int ticksToGo)
    {
        int multi = 3;
        if (ticksToGo > 1440 * multi)
        {
            return 1;
        }
        if (ticksToGo > 720 * multi)
        {
            return .75;
        }
        if (ticksToGo > 520 * multi)
        {
            return .5;
        }
        if (ticksToGo > 120 * multi)
        {
            return .4;
        }
        if (ticksToGo >= 0 * multi)
        {
            return .2;
        }
        return 0;
    }

    public void setDrivePower (double power)
    {
        leftMotor.setPower(power);
        rightMotor.setPower(power);
    }

    public int getTicksForTurn (double degrees)
    {
        int ticks = (int)(11.78 * Math.abs(degrees));
        return ticks;
    }

    public int inchToTickConverter (double inches)
    {
        return (int) (72.858708 * inches);
    }



    public void turnDegrees (double degrees) throws InterruptedException {
        Reset_All_Encoders();
        int tickR = getTicksForTurn(degrees);
        int tickL = getTicksForTurn(degrees);
        int basePowerL = 0;
        int basePowerR = 0;
        if (degrees < 0)   // - makes the robot turn left + makes the robot turn right
        {
            basePowerL = -1;
            basePowerR = 1;
        } else if (degrees >= 0) {
            basePowerL = 1;
            basePowerR = -1;
        }

        //tickR *= basePowerR;
        //tickL *= basePowerL;

        //telemetry.addData("driveReverse start", String.format("tickR=%d tickL=%d motorR=%d motorL=%d", tickR, tickL, motorRightRemote1.getCurrentPosition(), motorLeftRemote1.getCurrentPosition()));
        int count = 0;

        int rightTicksToGo = (tickR - rightMotor.getCurrentPosition() * basePowerR);
        int leftTicksToGo = (tickL - leftMotor.getCurrentPosition() * basePowerL);

        while(rightTicksToGo > 0 || leftTicksToGo > 0) {
            double LPower = getPowerForTicksforturn(leftTicksToGo);
            if (leftTicksToGo > 0)
            {
                leftMotor.setPower(LPower * basePowerL);
            } else {
                leftMotor.setPower(0);
            }

            double RPower = getPowerForTicksforturn(rightTicksToGo);
            if (rightTicksToGo > 0)
            {
                rightMotor.setPower(RPower * basePowerR);
            } else {
                rightMotor.setPower(0);
            }

            //waitOneFullHardwareCycle();

            rightTicksToGo = (tickR - rightMotor.getCurrentPosition() * basePowerR);
            leftTicksToGo = (tickL - leftMotor.getCurrentPosition() * basePowerL);

            ++count;
        }

    }

    public void Reset_All_Encoders () throws InterruptedException
    {
        while (rightMotor.getCurrentPosition() != 0 || leftMotor.getCurrentPosition() != 0)
        {
            if(rightMotor != null)
            {
                rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            }
            if(leftMotor != null)
            {
                leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            }
            ///waitOneFullHardwareCycle(); needed?

        }


        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //waitOneFullHardwareCycle(); needed?
    }

    public void driveReverse(double inches) throws InterruptedException {
//12.6

        int multier = 0;
        Reset_All_Encoders();
        int tickR = inchToTickConverter(inches);
        int tickL = inchToTickConverter(inches);

        //telemetry.addData("driveReverse start", String.format("tickR=%d tickL=%d motorR=%d motorL=%d", tickR, tickL, motorRightRemote1.getCurrentPosition(), motorLeftRemote1.getCurrentPosition()));
        int count = 0;
        while(rightMotor.getCurrentPosition() < tickR || leftMotor.getCurrentPosition() < tickL) {
            leftMotor.setPower(-getPowerForTicksfordrive(tickL - leftMotor.getCurrentPosition()));
            rightMotor.setPower(-getPowerForTicksfordrive(tickR - rightMotor.getCurrentPosition()));
            //waitOneFullHardwareCycle();
            //telemetry.addData("driveReverse count", String.format("count= %d tickR=%d tickL=%d motorR=%d motorL=%d", count, tickR, tickL, motorRightRemote1.getCurrentPosition(), motorLeftRemote1.getCurrentPosition()));
            ++count;
        }
        //telemetry.addData("driveReverse end", String.format("tickR=%d tickL=%d motorR=%d motorL=%d", tickR, tickL, motorRightRemote1.getCurrentPosition(), motorLeftRemote1.getCurrentPosition()));
        setDrivePower(0);
    }
    public void driveForward(double inches) throws InterruptedException {
//12.6

        int multier = 0;
        Reset_All_Encoders();
        int ReversetickR = inchToTickConverter(inches);
        int ReversetickL = inchToTickConverter(inches);

        //telemetry.addData("driveReverse start", String.format("tickR=%d tickL=%d motorR=%d motorL=%d", tickR, tickL, motorRightRemote1.getCurrentPosition(), motorLeftRemote1.getCurrentPosition()));
        int count = 0;
        while(rightMotor.getCurrentPosition() < ReversetickR || leftMotor.getCurrentPosition() < ReversetickL) {
            leftMotor.setPower(getPowerForTicksfordrive(ReversetickL - leftMotor.getCurrentPosition()));
            rightMotor.setPower(getPowerForTicksfordrive(ReversetickL - rightMotor.getCurrentPosition()));
            //waitOneFullHardwareCycle();
            //telemetry.addData("driveReverse count", String.format("count= %d tickR=%d tickL=%d motorR=%d motorL=%d", count, tickR, tickL, motorRightRemote1.getCurrentPosition(), motorLeftRemote1.getCurrentPosition()));
            ++count;
        }
        //telemetry.addData("driveReverse end", String.format("tickR=%d tickL=%d motorR=%d motorL=%d", tickR, tickL, motorRightRemote1.getCurrentPosition(), motorLeftRemote1.getCurrentPosition()));
        setDrivePower(0);
    }


}
