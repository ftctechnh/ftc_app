package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Instances of LastKnown can keep track of a last known value for a certain datum, together
 * with whether in fact any such last value is known at all. Values can become unknown either
 * due to explicit invalidation, or by not being fresh enough.
 *
 * This class is NOT thread-safe.
 */
public class LastKnown<T>
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private T           value;
    private ElapsedTime timer;
    private double      msFreshness;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public LastKnown()
        {
        this(100);
        }

    public LastKnown(double msFreshness)
        {
        this.value       = null;
        this.timer       = new ElapsedTime();
        this.msFreshness = msFreshness;
        }

    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------

    /**
     * Returns the last known value, or null if that is invalid
     * @return the last known value
     */
    T getValue()
        {
        return this.isValid() ? this.value : null;
        }

    void setValue(T value)
        {
        if (null == value)
            invalidate();
        else
            {
            this.value = value;
            this.timer.reset();
            }
        }

    boolean isValue(T valueQ)
        {
        if (this.isValid())
            return this.value == valueQ;
        else
            return false;
        }

    boolean updateValue(T valueQ)
        {
        if (!isValue(valueQ))
            {
            setValue(valueQ);
            return false;
            }
        else
            return true;
        }

    void invalidate()
        {
        this.value = null;
        }

    boolean isValid()
        {
        return this.value != null && this.timer.milliseconds() <= msFreshness;
        }
    }
