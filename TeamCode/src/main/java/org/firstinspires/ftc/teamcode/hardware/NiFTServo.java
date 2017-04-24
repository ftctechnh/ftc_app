/**
 * This code enables the servo to be set to a max and min position, which is super helpful since you would otherwise be required to create a bunch of different variables to hold those values.
 */

package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class NiFTServo
{
    public final Servo servo;
    private double position, upperLimit, lowerLimit;

    public NiFTServo (String servoName, double initialPosition)
    {
        this(servoName, 0, initialPosition, 1);
    }
    public NiFTServo (String servoName, double lowerLimit, double initialPosition, double upperLimit)
    {
        this.servo = NiFTInitializer.initialize (Servo.class, servoName);
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
