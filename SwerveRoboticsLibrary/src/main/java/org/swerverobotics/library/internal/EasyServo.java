package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;

/**
 * EasyServo modifies the FTC-provided Servo so that it is thread-safe.
 * In addition, flooding of the underlying servo controller is prevented.
 */
public class EasyServo extends com.qualcomm.robotcore.hardware.Servo
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    Double lastKnownPosition;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public EasyServo(ServoController controller, int portNumber, Servo.Direction direction)
        {
        super(controller, portNumber, direction);
        this.lastKnownPosition = null;
        }

    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------

    @Override
    public synchronized void setDirection(Servo.Direction direction)
        {
        // super sets direction
        super.setDirection(direction);

        // If the direction changed, our known position may now be off
        this.lastKnownPosition = null;
        }

    @Override
    public synchronized void setPosition(double position)
        {
        // super uses direction, minPosition, maxPosition
        if (this.lastKnownPosition == null || this.lastKnownPosition != position)
            {
            this.lastKnownPosition = position;
            super.setPosition(position);
            }
        }

    @Override
    public synchronized double getPosition()
        {
        // super uses direction, minPosition, maxPosition
        return super.getPosition();
        }

    @Override
    public synchronized void scaleRange(double min, double max) throws IllegalArgumentException
        {
        // super sets minPosition, maxPosition
        super.scaleRange(min, max);

        // If the scale range changed, our known position may now be off
        this.lastKnownPosition = null;
        }

    }
