package org.firstinspires.ftc.teamcode.components.motors;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

public class DriveMotor implements Drivable{

    private DcMotor motor;

    /**
     * Creates a new drive motor to be used by a DriveSystem DcMotor
     * @param dcMotor The DcMotor motor to convert to a DriveMotor
     * @see {@link DcMotor}
     */
    public DriveMotor(DcMotor dcMotor) {
        this.motor = dcMotor;
    }

    /**
     * Runs the motor at a passed power, this is used to run the motor.
     * @param power The power to run the motor at
     */
    public void run(double power) {
        EnsurePowerIsInRange(power);
        motor.setPower(power);
    }

    /**
     * Checks that a power passed by a user is below the max motor power, which is 1
     * @param power The power to check that is below the max motor power.
     */
    private void EnsurePowerIsInRange(double power) {
        if (power > 1) {
            throw new IllegalArgumentException("Power must be less than 1.0");
        }
    }

    /**
     * Set the direction of the drive motor to run the motor either Forwards or Backwards.
     * @param direction Direction to run the motor
     * @see DcMotorSimple.Direction
     */
    public void setDirection(DcMotorSimple.Direction direction) {
        motor.setDirection(direction);
    }

    /**
     * Get the configuration of the drive motor.
     * @return Returns the motor configuration type
     * @see MotorConfigurationType
     */
    public MotorConfigurationType getMotorType() {
        return motor.getMotorType();
    }

    /**
     * Set the motor configuration type of a motor. This is used to set the type of the motor
     * @param motorType The new motor configuration of the motor
     * @see MotorConfigurationType
     */
    public void setMotorType(MotorConfigurationType motorType) {
        motor.setMotorType(motorType);
    }

    /**
     * Gets the DcMotor controller of a motor.
     * @return Returns the DcMotor controller
     * @see DcMotorController
     */
    public DcMotorController getController() {
        return motor.getController();
    }

    /**
     * Gets the port number of the motor
     * @return the port number
     */
    public int getPortNumber() {
        return motor.getPortNumber();
    }

    /**
     * Sets the current zero power behavior of the motor
     * @param zeroPowerBehavior The new zero power behavior of the motor
     * @see com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior
     */
    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
        motor.setZeroPowerBehavior(zeroPowerBehavior);
    }

    /**
     * Gets the current zero power behavior of the motor
     * @return Returns the current zero power behavior of the motor
     * @see com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior
     */
    public DcMotor.ZeroPowerBehavior getZeroPowerBehavior() {
        return motor.getZeroPowerBehavior();
    }

    /**
     * Sets the target position of the motor in ticks, this is only useful if the motor is run using encoders
     * @param position the position the motor should run towards assuming
     */
    public void setTargetPosition(int position) {
        motor.setTargetPosition(position);
    }

    /**
     * Gets the current target position of the motor in ticks
     * @return Returns the target position of the motor
     */
    public int getTargetPosition() {
        return motor.getTargetPosition();
    }

    /**
     * Checks if the motor is currently running
     * @return Returns true if the motor is running
     */
    public boolean isBusy() {
        return motor.isBusy();
    }

    /**
     * Gets the current position of the motor in ticks
     * @return Returns the current position of the motor in ticks
     */
    public int getCurrentPosition() {
        return motor.getCurrentPosition();
    }

    /**
     * Sets the run mode of the motor
     * @param mode The new run mode of the motor
     * @see com.qualcomm.robotcore.hardware.DcMotor.RunMode
     */
    public void setMode(DcMotor.RunMode mode) {
        motor.setMode(mode);
    }

    /**
     * Gets the run mode of the motor
     * @return Returns the run mode of the motor
     * @see com.qualcomm.robotcore.hardware.DcMotor.RunMode
     */
    public DcMotor.RunMode getMode() {
        return motor.getMode();
    }
}
