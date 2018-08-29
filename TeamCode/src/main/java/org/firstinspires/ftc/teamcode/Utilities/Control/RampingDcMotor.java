package org.firstinspires.ftc.teamcode.Utilities.Control;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by guberti on 5/11/2018.
 */

public class RampingDcMotor {

    final double TIME_FOR_0_100; // Milliseconds
    final double POWER_CHANGE_PER_MS;

    DcMotor m;
    double desiredPower;

    public RampingDcMotor(DcMotor m, double accelTime) {
        this.m = m;
        desiredPower = 0;
        TIME_FOR_0_100 = accelTime;
        POWER_CHANGE_PER_MS = 1 / TIME_FOR_0_100;
    }

    public void setPower(double p) {
        desiredPower = p;
    }

    public void ramp(double elapsedMS) {
        double maxChange = elapsedMS * POWER_CHANGE_PER_MS;
        double currentPower = m.getPower();
        double eventualDesiredChange = desiredPower - currentPower;

        if (Math.abs(eventualDesiredChange) <= maxChange) {
            m.setPower(desiredPower);
        } else {
            maxChange = Math.copySign(maxChange, eventualDesiredChange);
            m.setPower(currentPower + maxChange);
        }
    }
}
