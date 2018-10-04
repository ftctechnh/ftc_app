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

    private int maxEncoderTicks;
    private int minEncoderTicks;
    private LimitSensor limitSensor;
    private DcMotor dcMotor;

    private int currentPosition;

    public LinearEncoderSystem(OpMode opMode, String systemName, int maxTicks, int minTicks,
                               DcMotor dcMotor, LimitSensor limitSensor) {
        super(opMode, systemName);

        this.maxEncoderTicks = maxTicks;
        this.minEncoderTicks = minTicks;
        this.dcMotor = dcMotor;
    }

    // May need to be overridden
    public void initializeMotor() {
        dcMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        calibrateMotor();
        dcMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        dcMotor.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void goToPosition(double targetPosition, double power) {
        if (targetPosition == 0) {

        } else if (targetPosition == 1) {

        }
        Ramp ramp = new ExponentialRamp(new Point(0, 0.1), new Point(targetPosition, power));
    }

    public void goToPosition(int targetPosition, double power) {
        if (targetPosition > positions.length) {
            throw new IllegalArgumentException("Target postition (" + targetPosition +
                    ") is beyond range of positions (" + positions.length + ")");
        }
        goToPosition(positions[targetPosition], power);
    }

    // Should be overwritten if the system does not always begin at the minimum of its range
    public void calibrateMotor() {
        dcMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
}
