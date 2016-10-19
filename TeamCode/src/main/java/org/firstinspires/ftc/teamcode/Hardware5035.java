package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
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
 * Motor channel:  Left  drive motor:        "left_drive"
 * Motor channel:  Right drive motor:        "right_drive"
 */
public class Hardware5035 {
    /* Public OpMode members. */
    public DcMotor leftMotor = null;
    public DcMotor rightMotor = null;


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
        leftMotor = hwMap.dcMotor.get("left drive");
        rightMotor = hwMap.dcMotor.get("right drive");
        leftMotor.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        rightMotor.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors

        // Set all motors to zero power
        leftMotor.setPower(0);
        rightMotor.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
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

    public double getPowerForTicksfordrive (int ticksToGo)
    {
        int multi = 3;
        if (ticksToGo > 1440 * multi)
        {
            return .2;
        }
        if (ticksToGo > 720 * multi)
        {
            return .175;
        }
        if (ticksToGo > 520 * multi)
        {
            return .175;
        }
        if (ticksToGo > 120 * multi)
        {
            return .1555;
        }
        if (ticksToGo >= 0 * multi)
        {
            return .150;
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
        return (int) (65.482 * inches);
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

        //telemetry.addData("drive start", String.format("tickR=%d tickL=%d motorR=%d motorL=%d", tickR, tickL, motorRightRemote1.getCurrentPosition(), motorLeftRemote1.getCurrentPosition()));
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

    public void drive (double inches) throws InterruptedException {
//12.6

        int multier = 0;
        Reset_All_Encoders();
        int tickR = inchToTickConverter(inches);
        int tickL = inchToTickConverter(inches);

        //telemetry.addData("drive start", String.format("tickR=%d tickL=%d motorR=%d motorL=%d", tickR, tickL, motorRightRemote1.getCurrentPosition(), motorLeftRemote1.getCurrentPosition()));
        int count = 0;
        while(rightMotor.getCurrentPosition() < tickR || leftMotor.getCurrentPosition() < tickL) {
            leftMotor.setPower(getPowerForTicksfordrive(tickL - leftMotor.getCurrentPosition()));
            rightMotor.setPower(getPowerForTicksfordrive(tickR - rightMotor.getCurrentPosition()));
            //waitOneFullHardwareCycle();
            //telemetry.addData("drive count", String.format("count= %d tickR=%d tickL=%d motorR=%d motorL=%d", count, tickR, tickL, motorRightRemote1.getCurrentPosition(), motorLeftRemote1.getCurrentPosition()));
            ++count;
        }
        //telemetry.addData("drive end", String.format("tickR=%d tickL=%d motorR=%d motorL=%d", tickR, tickL, motorRightRemote1.getCurrentPosition(), motorLeftRemote1.getCurrentPosition()));
        setDrivePower(0);
    }


}
