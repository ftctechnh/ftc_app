package org.firstinspires.ftc.teamcode.speedregulation;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

public class PIDMotorController
{
    //Only certain motors have encoders on them, so the linkedMotor object is implemented.
    public final DcMotor encoderMotor, linkedMotor;

    public PIDMotorController(DcMotor encoderMotor)
    {
        this (encoderMotor, null);
    }
    public PIDMotorController(DcMotor encoderMotor, DcMotor linkedMotor)
    {
        this.encoderMotor = encoderMotor;
        this.linkedMotor = linkedMotor;
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
    public double rpsConversionFactor = .25;

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
    public double expectedTicksSinceUpdate, actualTicksSinceUpdate;
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
            rpsConversionFactor += Range.clip (((expectedTicksSinceUpdate - actualTicksSinceUpdate) * 0.0002), -.5, .5);

            updateMotorPowers ();
        }

        previousMotorPosition = currentEncoderPosition;
        lastAdjustTime = System.currentTimeMillis ();
    }

    private void updateMotorPowers()
    {
        //Set the initial power which the PID will soon modify.
        encoderMotor.setPower (desiredRPS * rpsConversionFactor);
        if (linkedMotor != null)
            linkedMotor.setPower (desiredRPS * rpsConversionFactor);
    }
}
