package org.firstinspires.ftc.teamcode.smarthardware;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class SmartServo
{
    public final Servo servo;
    private double position, upperLimit, lowerLimit;

    public SmartServo(Servo servo, double initialPosition)
    {
        this(servo, 0, initialPosition, 1);
    }
    public SmartServo(Servo servo, double lowerLimit, double initialPosition, double upperLimit)
    {
        this.servo = servo;
        this.lowerLimit = Range.clip(lowerLimit, 0, 1);
        this.upperLimit = Range.clip(upperLimit, 0, 1);
        this.position = Range.clip(initialPosition, lowerLimit, upperLimit);
        updateServoPosition ();
    }

    public void setToUpperLim()
    {
        setServoPosition (upperLimit);
    }

    public void setToLowerLim()
    {
        setServoPosition (lowerLimit);
    }

    public void setServoPosition(double desiredPosition)
    {
        position = Range.clip(desiredPosition, lowerLimit, upperLimit);
        updateServoPosition ();
    }

    private void updateServoPosition()
    {
        servo.setPosition (position);
    }

    public double getServoPosition()
    {
        return position;
    }
}
