package org.firstinspires.ftc.teamcode.enhancements;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

public class PIDMotorController
{
    //Only certain motors have encoders on them, so the linkedMotor object is implemented.
    public final DcMotor encoderMotor, linkedMotor;

    public PIDMotorController(DcMotor encoderMotor, double rpsConversionFactor)
    {
        this (encoderMotor, null, rpsConversionFactor);
    }
    public PIDMotorController(DcMotor encoderMotor, DcMotor linkedMotor, double rpsConversionFactor)
    {
        this.encoderMotor = encoderMotor;
        this.linkedMotor = linkedMotor;

        this.rpsConversionFactor = rpsConversionFactor;
    }

    public void reset()
    {
        boolean doneSuccessfully = false;
        long additionalTime = 0;
        while (!doneSuccessfully)
        {
            try
            {
                encoderMotor.setMode (DcMotor.RunMode.RUN_USING_ENCODER);
                Thread.sleep(100 + additionalTime);
                doneSuccessfully = true;
            }
            catch (Exception e)
            {
                if (e instanceof InterruptedException)
                    return;

                additionalTime += 20;
            }
        }

        doneSuccessfully = false;
        additionalTime = 0;
        while (!doneSuccessfully)
        {
            try
            {
                encoderMotor.setMode (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                Thread.sleep(100 + additionalTime);
                doneSuccessfully = true;
            }
            catch (Exception e)
            {
                if (e instanceof InterruptedException)
                    return;

                additionalTime += 20;
            }
        }

        doneSuccessfully = false;
        while (!doneSuccessfully)
        {
            try
            {
                encoderMotor.setMode (DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                Thread.sleep(100 + additionalTime);
                doneSuccessfully = true;
            }
            catch (Exception e)
            {
                if (e instanceof InterruptedException)
                    return;

                additionalTime += 20;
            }
        }
    }

    /******* PID STUFF *********/
    public double desiredRPS = 0;

    //Initial conversion factor, will be changed a LOT through the course of the program.
    private double rpsConversionFactor = .25;
    public double getRPSConversionFactor() //Primarily for debugging.
    {
        return rpsConversionFactor;
    }

    public void setRPS (double givenRPS)
    {
        //Will soon be modified by PID.
        desiredRPS = givenRPS;
        updateMotorPowers ();
    }

    /**
     * VERY Simplistic PID control which decreases or increases the rpsConversionFactor variable, thus changing the speed
     * at which the motor turns based on previous and current encoder positions.
     */
    //Stored for each run.
    private int previousMotorPosition;
    private long lastAdjustTime = 0;

    private double expectedTicksSinceUpdate, actualTicksSinceUpdate;
    public double getExpectedTicksSinceUpdate()
    {
        return expectedTicksSinceUpdate;
    }
    public double getActualTicksSinceUpdate()
    {
        return actualTicksSinceUpdate;
    }

    public void updateMotorPowerWithPID ()
    {
        int currentEncoderPosition = encoderMotor.getCurrentPosition ();

        if (lastAdjustTime != 0)
        {
            /**
             * desired RPS (revolution/second) * 560 (ticks/revolution) * 30 / 1000 (seconds) * .5 gear ratio.
             */

            expectedTicksSinceUpdate = 1120.0 * .5 * desiredRPS * ((System.currentTimeMillis () - lastAdjustTime) / 1000.0);
            actualTicksSinceUpdate = currentEncoderPosition - previousMotorPosition;

            //Sensitivity is the coefficient below.
            //If expected = -50 and actual = -28 then diff = -22 but motor power needs to INCREASE.
            rpsConversionFactor += Math.signum (desiredRPS) * Range.clip (((expectedTicksSinceUpdate - actualTicksSinceUpdate) * 0.0002), -.5, .5);

            updateMotorPowers ();
        }

        previousMotorPosition = currentEncoderPosition;
        lastAdjustTime = System.currentTimeMillis ();
    }

    private void updateMotorPowers()
    {
        //Set the initial power which the PID will soon modify.
        double desiredPower = Range.clip(desiredRPS * rpsConversionFactor, -1, 1);
        encoderMotor.setPower (desiredPower);
        if (linkedMotor != null)
            linkedMotor.setPower (desiredPower);
    }

    public void setDirectMotorPower(double power)
    {
        double actualPower = Range.clip(power, -1, 1);
        encoderMotor.setPower(actualPower);
        if (linkedMotor != null)
            linkedMotor.setPower (actualPower);
    }
}
