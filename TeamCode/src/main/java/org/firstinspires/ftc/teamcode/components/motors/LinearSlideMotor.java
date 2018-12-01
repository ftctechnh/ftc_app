package org.firstinspires.ftc.teamcode.components.motors;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

public class LinearSlideMotor {

    private DcMotor rightMotor;
    private DcMotor leftMotor;

    /**
     * Create the left and right drive motor
     * @param rightMotor The right DcMotor
     * @param leftMotor The left DcMotor
     * @see DcMotor
     */
    public LinearSlideMotor(DcMotor rightMotor, DcMotor leftMotor) {
        this.rightMotor = rightMotor;
        this.leftMotor = leftMotor;
    }

    /**
     * Set the power to run the motors
     * @param power Range of power from (0, 1)
     */
    public void run(double power) {
        EnsurePowerIsInRange(power);
        leftMotor.setPower(-power);
        rightMotor.setPower(power);
    }

    /**
     * Checks to verify that pwoer is not greater than 1
     * @param power The power value that should be checked
     */
    private void EnsurePowerIsInRange(double power) {
        if (power > 1) {
            throw new IllegalArgumentException("Power must be less than 1.0");
        }
    }

    /**
     * Sets the direction that the robot should move in
     * @param direction Should be a DcMotorSimple.Direction object
     * @see DcMotorSimple.Direction
     */
    public void setDirection(DcMotorSimple.Direction direction) {
        if (direction == DcMotorSimple.Direction.FORWARD) {
            leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        } else {
            leftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        }
        rightMotor.setDirection(direction);
    }

    /**
     * Get the motor type
     * @return The MotorConfigurationType
     */
    public MotorConfigurationType getMotorType() {
        return rightMotor.getMotorType();
    }

    /**
     * Sets the motor type
     * @param motorType The motorType to assign to left and right motors
     * @see MotorConfigurationType
     */
    public void setMotorType(MotorConfigurationType motorType) {
        leftMotor.setMotorType(motorType);
        rightMotor.setMotorType(motorType);
    }

    /**
     * Get the motor controller
     * @return The controller for the motors
     * @see DcMotorController
     */
    public DcMotorController getController() {
        return rightMotor.getController();
    }

    /**
     * Set the ZeroPowerBehavior for each motor
     * @param zeroPowerBehavior the zeroPowerBehavior to set to the motor
     * @see DcMotor.ZeroPowerBehavior
     */
    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
        leftMotor.setZeroPowerBehavior(zeroPowerBehavior);
        rightMotor.setZeroPowerBehavior(zeroPowerBehavior);
    }

    /**
     * Get the zeroPowerBehavior
     * @return The zeroPowerBehavior
     * @see DcMotor.ZeroPowerBehavior
     */
    public DcMotor.ZeroPowerBehavior getZeroPowerBehavior() {
        return rightMotor.getZeroPowerBehavior();
    }

    /**
     * Set the target position to move the motor to
     * @param position the position value
     */
    public void setTargetPosition(int position) {
        leftMotor.setTargetPosition(position);
        rightMotor.setTargetPosition(position);
    }

    /**
     * Gets the target position
     * @return the motor target position
     */
    public int getTargetPosition() {
        return rightMotor.getTargetPosition();
    }

    /**
     * Checks if motor is busy
     * @return true if motor is busy
     */
    public boolean isBusy() {
        return (rightMotor.isBusy() || leftMotor.isBusy());
    }

    /**
     * Gets the current motor position
     * @return the current motor position
     */
    public int getCurrentPosition() {
        return rightMotor.getCurrentPosition();
    }

    /**
     * Sets the run mode for the motor
     * @param mode The RunMode to set for the motors
     * @see DcMotor.RunMode
     */
    public void setMode(DcMotor.RunMode mode) {
        leftMotor.setMode(mode);
        rightMotor.setMode(mode);
    }

    /**
     * Gets the run mode
     * @return the DcMotor run mode
     * @see DcMotor.RunMode
     */
    public DcMotor.RunMode getMode() {
        return rightMotor.getMode();
    }
}
