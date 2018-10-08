package org.firstinspires.ftc.teamcode.systems.BaseSystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.components.scale.ExponentialRamp;
import org.firstinspires.ftc.teamcode.components.scale.Point;
import org.firstinspires.ftc.teamcode.components.scale.Ramp;
import org.firstinspires.ftc.teamcode.systems.LimitSensor;

/**
 * Created by Michael on 3/15/2018.
 */

public abstract class LinearEncoderSystem extends LinearSystem {

    private static double REGRESS_POWER = 0.3;
    private static double RAMP_POWER_CUTOFF = 0.3;
    private static double CALIBRATION_POWER = 0.3;
    private static int LIMIT_SENSOR_SIZE_TICKS = 20;

    private int maxEncoderTicks; // the distance between the limit Sensors (bet

    private int currentPosition;
    private LimitSensor maxLimitSensor;
    private LimitSensor minLimitSensor;
    private DcMotor dcMotor;


    public LinearEncoderSystem(OpMode opMode, String systemName, int maxTicks, DcMotor dcMotor,
                               LimitSensor maxLimitSensor, LimitSensor minLimitSensor) {
        super(opMode, systemName);

        this.maxEncoderTicks = maxTicks;
        this.dcMotor = dcMotor;

        initializeMotor();
    }

    // May need to be overridden
    public void initializeMotor() {
        dcMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        dcMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        dcMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        calibrateSystem();
    }

    public void goToPosition(double targetPosition, double power) {
        int driveTicks = (int) ((targetPosition * maxEncoderTicks) - currentPosition);
        if (targetPosition == 0) {
            dcMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            dcMotor.setPower(-power);
            checkForBounds();
        } else if (targetPosition == 1) {
            dcMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            dcMotor.setPower(power);
            checkForBounds();
        } else {
            dcMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            dcMotor.setTargetPosition(driveTicks);

            Ramp ramp = new ExponentialRamp(new Point(0, RAMP_POWER_CUTOFF), new Point(targetPosition, power));

            while (dcMotor.isBusy()) {
                // ramp assumes the distance away from the target is positive,
                // so we make it positive here and account for the direction when
                // the motor power is set.
                double direction = 1.0;
                if (driveTicks < 0)
                {
                    driveTicks = -driveTicks;
                    direction = -1.0;
                }

                double scaledPower = ramp.scaleX(driveTicks);

                dcMotor.setPower(direction * scaledPower);
            }
            dcMotor.setPower(0);

            currentPosition += driveTicks;
        }
    }

    public void goToPosition(int targetPosition, double power) {
        if (targetPosition > positions.length) {
            throw new IllegalArgumentException("Target postition (" + targetPosition +
                    ") is beyond range of positions (" + positions.length + ")");
        }
        goToPosition(positions[targetPosition], power);
    }

    // Should be overwritten if the system does not always begin at the minimum of its range
    public void calibrateSystem() {
        goToPosition(0, CALIBRATION_POWER);
    }

    private void checkForBounds() {
        if (maxLimitSensor.isTriggered()) {
            dcMotor.setPower(0);
            regressFromLimitSensor();
        } else if (minLimitSensor.isTriggered()) {
            dcMotor.setPower(0);
            regressFromLimitSensor();
        }
    }

    private void regressFromLimitSensor() {
        boolean topTriggered = false;
        dcMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        while (minLimitSensor.isTriggered() || maxLimitSensor.isTriggered()) {
            if (maxLimitSensor.isTriggered()) {
                topTriggered = true;
                dcMotor.setPower(REGRESS_POWER);
            } else if (minLimitSensor.isTriggered()) {
                topTriggered = false;
                dcMotor.setPower(-REGRESS_POWER);
            }
        }
        if (topTriggered) {
            currentPosition = maxEncoderTicks;
        } else {
            currentPosition = 0;
        }
    }
}
