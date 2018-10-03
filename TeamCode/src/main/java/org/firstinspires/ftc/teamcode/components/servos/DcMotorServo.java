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

    public DcMotorServo(DcMotor motor, AnalogInput armPotentiometer, IConfig config)
    {
        this.motor = motor;
        this.armPotentiometer = armPotentiometer;
        this.intervalTimer = new IntervalTimer(TIME_INTERVAL);
        this.motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.miniPID = MiniPIDFactory.getMiniPIDFromConfig(config);
        powerAdjustmentScale = new ExponentialRamp(
                Point.getOriginPoint(),
                Point.getOriginPoint()
        );
    }

    public void setTargetPosition(double targetPosition) {
        this.targetPosition = targetPosition;
    }

    public double getPower()
    {
        return motor.getPower();
    }

    public void runMotor()
    {
        this.motor.setPower(MOTOR_POWER);
    }

    public void runMotorBack()
    {
        this.motor.setPower(-MINIMUM_POWER);
    }

    public void stop()
    {
        this.motor.setPower(ZERO_POWER);
    }

    public void loop()
    {
        if (intervalTimer.hasCurrentIntervalPassed())
        {
            intervalTimer.update();
            miniPID.setSetpoint(targetPosition);
            motor.setPower(miniPID.getOutput(getCurrentPosition(), targetPosition));
        }
    }

    public double getCurrentPosition()
    {
        return VOLTAGE_SCALE.scaleX(armPotentiometer.getVoltage());
    }

    public double getAdjustedPowerFromCurrentPosition(double maxPower)
    {
        IScale ramp = getPowerAdjustmentRamp(maxPower);
        double distanceToTarget = targetPosition - getCurrentPosition();
        double power = Math.signum(distanceToTarget) * ramp.scaleX(Math.abs(distanceToTarget));
        return Math.abs(power) <= MINIMUM_POSITION ? ZERO_POWER : power;
    }

    private IScale getPowerAdjustmentRamp(double maxPower) {
        if (shouldUpdateRamp())
            powerAdjustmentScale = new ExponentialRamp(SCALE_POINT1, new Point(targetPosition, maxPower));
        return powerAdjustmentScale;
    }

    private boolean shouldUpdateRamp() {
        return powerAdjustmentScale == null || ((Ramp) powerAdjustmentScale).getPoint2().getX() != targetPosition;
    }
}
