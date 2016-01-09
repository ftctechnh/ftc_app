package com.qualcomm.ftcrobotcontroller.units;

import com.qualcomm.ftcrobotcontroller.hardware.MotorRunner;

/**
 * A {@link Unit} for the {@link MotorRunner}, using motor encoders
 */
public class EncoderUnit extends Unit {

    public static final int ROTATION_TETRIX = 1440;
    public static final int ROTATION_ANDYMARK = 1220;
    public static final int ROTATION_LEGO = 360;

    /**
     * Creates a new {@link EncoderUnit}, based on a raw number of encoder units.
     * Use the ROTATION constants to specify values in terms of rotations.
     *
     * @param value Number of encoder units to turn.
     * @see MotorRunner
     */
    public EncoderUnit(int value) {
        setValue(value);
    }

    /**
     * Creates a new {@link EncoderUnit}, based on estimated distance in centimeters.
     * Use the ROTATION constants to specify values in terms of rotations.
     *
     * @param cm           Approximate distance in centimeters
     * @param circumfrence Circumfrence of your wheel
     * @param rotation     Number of encoder units in one rotation
     * @see MotorRunner
     */
    public EncoderUnit(float cm, float circumfrence, int rotation) {
        float rotationPerCm = rotation / circumfrence;
        setValue((int) (rotationPerCm * cm));
    }

}
