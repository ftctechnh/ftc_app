package org.firstinspires.ftc.teamcode.components.servos;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.components.miniPID.MiniPID;
import org.firstinspires.ftc.teamcode.components.miniPID.MiniPIDFactory;
import org.firstinspires.ftc.teamcode.components.configs.IConfig;
import org.firstinspires.ftc.teamcode.components.scale.ExponentialRamp;
import org.firstinspires.ftc.teamcode.components.scale.IScale;
import org.firstinspires.ftc.teamcode.components.scale.LinearScale;
import org.firstinspires.ftc.teamcode.components.scale.Point;
import org.firstinspires.ftc.teamcode.components.scale.Ramp;
import org.firstinspires.ftc.teamcode.components.timers.IntervalTimer;

public class DcMotorServo
{
    private static final int TIME_INTERVAL = 10000;
    private static final double MINIMUM_POWER = 0.2;
    private static final double ZERO_POWER = 0;
    private static final double MINIMUM_POSITION = 0;
    private static final double MOTOR_POWER = 0.5;
    private static final Point SCALE_POINT1 = new Point(MINIMUM_POSITION, MINIMUM_POWER);
    private static final IScale VOLTAGE_SCALE = new LinearScale(1d/ 3.3d, 0);

    private DcMotor motor;
    private AnalogInput armPotentiometer;
    private MiniPID miniPID;
    private IntervalTimer intervalTimer;
    private double targetPosition;
    private IScale powerAdjustmentScale;

    /**
     * Creates a DcMotorServo object which is a motor that acts as a servo
     * @param motor the DcMotor that will be used
     * @param potentiometer the potentiometer that will measure the motor
     * @param config the config to use
     * @see DcMotor
     * @see AnalogInput
     * @see IConfig
     */
    public DcMotorServo(DcMotor motor, AnalogInput potentiometer, IConfig config)
    {
        this.motor = motor;
        this.armPotentiometer = potentiometer;
        this.intervalTimer = new IntervalTimer(TIME_INTERVAL);
        this.motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.miniPID = MiniPIDFactory.getMiniPIDFromConfig(config);
        powerAdjustmentScale = new ExponentialRamp(
                Point.getOriginPoint(),
                Point.getOriginPoint()
        );
    }

    /**
     * Sets the target position for the motor
     * @param targetPosition the target position
     */
    public void setTargetPosition(double targetPosition) {
        this.targetPosition = targetPosition;
    }

    /**
     * Gets the power
     * @return the power
     */
    public double getPower()
    {
        return motor.getPower();
    }

    /**
     * Runs the motor with the set motor power
     */
    public void runMotor()
    {
        this.motor.setPower(MOTOR_POWER);
    }

    /**
     * Sets the motor power to the minimum power
     */
    public void runMotorBack()
    {
        this.motor.setPower(-MINIMUM_POWER);
    }


    /**
     * Sets the motor to zero power
     */
    public void stop()
    {
        this.motor.setPower(ZERO_POWER);
    }

    /**
     * Loop
     */
    public void loop()
    {
        if (intervalTimer.hasCurrentIntervalPassed())
        {
            intervalTimer.update();
            miniPID.setSetpoint(targetPosition);
            motor.setPower(miniPID.getOutput(getCurrentPosition(), targetPosition));
        }
    }

    /**
     * Gets the current position
     * @return the current position
     */
    public double getCurrentPosition()
    {
        return VOLTAGE_SCALE.scaleX(armPotentiometer.getVoltage());
    }

    /**
     * Gets the adjusted power from teh current position
     * @param maxPower the max power
     * @return the adjusted power
     */
    public double getAdjustedPowerFromCurrentPosition(double maxPower)
    {
        IScale ramp = getPowerAdjustmentRamp(maxPower);
        double distanceToTarget = targetPosition - getCurrentPosition();
        double power = Math.signum(distanceToTarget) * ramp.scaleX(Math.abs(distanceToTarget));
        return Math.abs(power) <= MINIMUM_POSITION ? ZERO_POWER : power;
    }

    /**
     * Gets the power adjusted ramp
     * @param maxPower the max power
     * @return the adjusted power
     */
    private IScale getPowerAdjustmentRamp(double maxPower) {
        if (shouldUpdateRamp())
            powerAdjustmentScale = new ExponentialRamp(SCALE_POINT1, new Point(targetPosition, maxPower));
        return powerAdjustmentScale;
    }

    /**
     * Checks if ramp should be updated
     * @return true is ramp should be updated
     */
    private boolean shouldUpdateRamp() {
        return powerAdjustmentScale == null || ((Ramp) powerAdjustmentScale).getPoint2().getX() != targetPosition;
    }
}
