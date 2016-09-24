package com.lasarobotics.library.skynet;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorImpl;

/**
 * Drive encoder support
 */
public class EncodedMotor extends DcMotorImpl {
    private boolean isEncoderEnabled = true;
    private boolean encodersResetting = false;
    private int encoderOffset = 0;

    /**
     * Initialize the encoded motor. You will need to run this during the init() method and run
     * the update() method every loop().
     *
     * @param motor DcMotor instance.
     */
    public EncodedMotor(DcMotor motor) {
        super(motor.getController(), motor.getPortNumber(), motor.getDirection());
        enableEncoder();
    }

    protected EncodedMotor(DcMotorController controller, int portNumber) {
        super(controller, portNumber);
        enableEncoder();
    }

    protected EncodedMotor(DcMotorController controller, int portNumber, Direction direction) {
        super(controller, portNumber, direction);
        enableEncoder();
    }

    /**
     * Move a certain distance, in encoder counts
     * If the distance is negative, the wheel will move in reverse - forward otherwise
     *
     * @param distance The distance to move, in encoder counts
     */
    public void moveDistance(int distance) {
        if (distance == 0)
            return;

        resetEncoder();

        if (distance < 0)
            super.setDirection(Direction.REVERSE);
        else
            super.setDirection(Direction.FORWARD);

        setTargetPosition(distance);
    }

    /**
     * Checks if the robot has reached the expected encoder position.
     * After reaching the position, it is recommended to reset the encoders.
     *
     * @param position Expected position, in encoder counts
     * @return True if the robot has reached the position
     */
    public boolean hasReachedPosition(double position) {
        return Math.abs(getCurrentPosition()) > Math.abs(position);
    }

    public void enableEncoder() {
        //This command requires (at least ?) one execution loop to reset the encoders
        super.setMode(RunMode.RUN_WITHOUT_ENCODER);

        isEncoderEnabled = true;
    }

    public void disableEncoder() {
        //This command requires (at least ?) one execution loop to reset the encoders
        super.setMode(RunMode.RUN_WITHOUT_ENCODER);

        isEncoderEnabled = false;
    }

    public boolean isEncoderEnabled() {
        return isEncoderEnabled;
    }

    public void update() {
        //Check for encoder reset
        if (encodersResetting && super.getCurrentPosition() == 0) {
            encodersResetting = false;
            encoderOffset = 0;
        }
    }

    /**
     * Reset encoders.
     */
    public void resetEncoder() {
        if (encodersResetting)
            return;

        //This command requires (at least ?) one execution loop to reset the encoders
        super.setMode(RunMode.RESET_ENCODERS);

        encodersResetting = true;
        encoderOffset = -super.getCurrentPosition();
    }

    @Override
    public void setTargetPosition(int position) {
        super.setTargetPosition(position + encoderOffset);
    }

    /**
     * Tests if the encoder has reset.
     * Note that encoders take at least one execution loop to fully reset.
     *
     * @return True for reset, false for not reset.
     */
    public boolean hasEncoderReset() {
        return getCurrentPosition() == 0;
    }

    @Override
    public int getCurrentPosition() {
        if (encodersResetting)
            return 0;
        return super.getCurrentPosition() + encoderOffset;
    }
}
