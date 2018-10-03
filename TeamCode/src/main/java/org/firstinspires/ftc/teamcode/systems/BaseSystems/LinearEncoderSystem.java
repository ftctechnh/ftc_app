package org.firstinspires.ftc.teamcode.systems.BaseSystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Michael on 3/15/2018.
 */

public abstract class LinearEncoderSystem extends LinearSystem {

    private int maxEncoderTicks;
    private int minEncoderTicks;
    private DcMotor dcMotor;

    private int currentPosition;

    public LinearEncoderSystem(OpMode opMode, String systemName, int maxTicks, int minTicks,
                               DcMotor dcMotor) {
        super(opMode, systemName);

        this.maxEncoderTicks = maxTicks;
        this.minEncoderTicks = minTicks;
        this.dcMotor = dcMotor;
        initializeMotor();
    }

    public void initializeMotor() {
        dcMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        dcMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    @Override
    public void goToPosition(double targetPosition) {

    }

    public void runMotorToPosition(int ticks, double power) {

    }
}
