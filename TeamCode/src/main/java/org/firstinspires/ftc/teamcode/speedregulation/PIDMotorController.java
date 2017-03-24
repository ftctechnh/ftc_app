package org.firstinspires.ftc.teamcode.speedregulation;

import com.qualcomm.robotcore.hardware.DcMotor;

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

    public void resetEncoder()
    {
        encoderMotor.setMode (DcMotor.RunMode.RUN_USING_ENCODER);
        encoderMotor.setMode (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        encoderMotor.setMode (DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /******* PID STUFF *********/
    private double desiredRPS = 0;

    //Initial conversion factor, will be changed a LOT through the course of the program.
    private volatile double rpsConversionFactor = 1;

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
    private long lastPIDUpdateTime = 0;
    private int previousMotorPosition;
    public void updateMotorPowerWithPID ()
    {
        long currentTime = System.currentTimeMillis ();

        int currentEncoderPosition = encoderMotor.getCurrentPosition ();

        int expectedTicksSinceUpdate = (int) (1120.0 * desiredRPS * ((currentTime - lastPIDUpdateTime) / 1000.0));
        int actualTicksSinceUpdate = currentEncoderPosition - previousMotorPosition;

        //Sensitivity is the coefficient below.
        rpsConversionFactor += (expectedTicksSinceUpdate - actualTicksSinceUpdate) * 0.0001;

        updateMotorPowers ();

        lastPIDUpdateTime = currentTime;
        previousMotorPosition = currentEncoderPosition;
    }

    private void updateMotorPowers()
    {
        //Set the initial power which the PID will soon modify.
        encoderMotor.setPower (desiredRPS * rpsConversionFactor);
        if (linkedMotor != null)
            linkedMotor.setPower (desiredRPS * rpsConversionFactor);
    }
}
