package org.firstinspires.ftc.teamcode.systems.BaseSystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.components.EncoderBasedDriveable;

/**
 * Created by Michael on 3/15/2018.
 */

public abstract class LinearEncoderSystem extends LinearSystem {

    private int maxEncoderTicks;
    private DcMotor dcMotor;

    private int currentPosition;

    public LinearEncoderSystem(OpMode opMode, String systemName, int maxTicks,
                               DcMotor dcMotor) {
        super(opMode, systemName);

        this.maxEncoderTicks = maxTicks;
        this.dcMotor = dcMotor;
    }

    // May need to be overridden
    public void initializeMotor() {
        dcMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        calibrateMotor();
        dcMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        dcMotor.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void goToPosition(double targetPosition) {

        Ramp ramp = new ExponentialRamp(new Point(0, 0.1), new Point(motorBackLeft.inchesToTicks(rampLength), power));
    }

    public void goToPosition(int targetPosition) {
        if (targetPosition > positions.length) {
            throw new IllegalArgumentException("Target postition (" + targetPosition +
                    ") is beyond range of positions (" + positions.length + ")");
        }
        goToPosition(positions[targetPosition]);
    }

    // Should be overwritten if the system does not always begin at the minimum of its range
    public void calibrateMotor() {
        dcMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
}
