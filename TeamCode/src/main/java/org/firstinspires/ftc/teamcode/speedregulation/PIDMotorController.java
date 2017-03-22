package org.firstinspires.ftc.teamcode.speedregulation;

import com.qualcomm.robotcore.hardware.DcMotor;

import static java.lang.Double.isInfinite;
import static java.lang.Double.isNaN;

public class PIDMotorController
{
    //Only certain motors have encoders on them, so the linkedMotor object is implemented.
    public final DcMotor encoderMotor, linkedMotor;
    private final double RPS_TO_POWER_IDEAL_CONVERSION_FACTOR = 1;

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
    //Thank you to team 7244: Out of the Box Robotics for a PID Template!

    /**
     * This is the coefficient used to calculate the effect of the proportional of the robot.
     * <p>
     * Increase the P gain until the response to a disturbance is steady oscillation.
     */
    private final double kP = .02;

    /**
     * This is the coefficient used to calculate the effect of the integral of the robot.
     * <p>
     * Increase the I gain until it brings you to the setpoint with the number of oscillations
     * desired (normally zero but a quicker response can be had if you don't mind a couple
     * oscillations of overshoot)
     */
    private final double kI = .00004;

    /**
     * This is the coefficient used to calculate the effect of the derivative of the robot.
     * <p>
     * Increase the D gain until the the oscillations go away (i.e. it's critically damped).
     */
    private final double kD = 3.5;

    private double proportional = 0, integral = 0, derivative = 0;

    /**
     * Information required to calculate each PID Loop
     */
    private double previousEncoderCountError = 0;


    private double pidRefreshRate = 30, integralRange = 6, outputRange = .8;

    /**
     * How often will the new PID value be calculated.
     */
    private long lastPIDUpdateTime = 0;

    /**
     * Update the target value of the PID loop so that the algorithm can respond in a new way
     * the new target value will be used in the queue.
     *
     * Also sets the initial power based on some conversion factor I have yet to figure out.
     *
     * @param givenRPS the new value
     */
    private double currentRPS = 0;
    private double desiredEncoderTicksPerUpdate = 0;
    public void moveAtRPS (double givenRPS)
    {
        currentRPS = givenRPS;
        desiredEncoderTicksPerUpdate = givenRPS * pidRefreshRate / 1000 * 1120;

        //Set the initial power which the PID will soon modify.
        encoderMotor.setPower (currentRPS * RPS_TO_POWER_IDEAL_CONVERSION_FACTOR);
        linkedMotor.setPower (currentRPS * RPS_TO_POWER_IDEAL_CONVERSION_FACTOR);
    }

    /**
     * Update PID loop based off previous results. This number will be stored in a queue. As well as
     * being returned to the user
     *
     * @param actualEncoderTicksSinceUpdate what is the measured value? This will give us info based off the target
     * @return the error correction value from the PID loop
     */
    public void updateMotorPowerWithPID ()
    {
        //Make sure that the refresh time minimum has been reached.
        long currentTime = System.currentTimeMillis ();
        long timeSinceLastUpdate = currentTime - lastPIDUpdateTime;
        if (timeSinceLastUpdate <= pidRefreshRate)
            return;

        //grab the error for caching or use in other calculates
        double offFromDesiredEncoderTicks = desiredEncoderTicksPerUpdate - encoderMotor.getCurrentPosition ();

        //if the PID has yet to execute more than once grab a timestamp to use in the future
        if (lastPIDUpdateTime == 0) {
            lastPIDUpdateTime = System.currentTimeMillis();
            previousEncoderCountError = offFromDesiredEncoderTicks;
            return;
        }

        //calculate error and then find proprtional through adjusting
        proportional = kP * offFromDesiredEncoderTicks;

        //check if integral is in range otherwise zero it out
        if ((integralRange == 0 || Math.abs(offFromDesiredEncoderTicks) < integralRange))
            integral += kI * offFromDesiredEncoderTicks * timeSinceLastUpdate;
        else
            integral = 0;

        if (Math.signum(previousEncoderCountError) != Math.signum(offFromDesiredEncoderTicks))
            integral = 0;

        //calculate derivative and then increase it by its kD
        derivative = kD * (offFromDesiredEncoderTicks - previousEncoderCountError) / timeSinceLastUpdate;

        //sanity check to prevent errors in the derivative
        derivative = (isNaN(derivative) || isInfinite(derivative) ? 0 : derivative);

        //save previous error for next integral
        previousEncoderCountError = offFromDesiredEncoderTicks;

        //ensure that the PID is only calculating at certain intervals otherwise halt till next time
        lastPIDUpdateTime = currentTime;

        //calculate the PID result
        double finalPIDSuggestion = proportional + integral + derivative;

        //limit the PID result if range is present
        if (outputRange != 0)
            finalPIDSuggestion = Math.max(-outputRange, Math.min(outputRange, finalPIDSuggestion));

        //Set final powers for the linked motors.
        double finalPower = currentRPS * RPS_TO_POWER_IDEAL_CONVERSION_FACTOR + finalPIDSuggestion;
        encoderMotor.setPower(finalPower);
        linkedMotor.setPower (finalPower);
    }
}
