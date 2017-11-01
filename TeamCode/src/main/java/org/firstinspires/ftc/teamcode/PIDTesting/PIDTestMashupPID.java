package org.firstinspires.ftc.teamcode.PIDTesting;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by guberti on 10/23/2017.
 */

public class PIDTestMashupPID extends PIDTestInterface {
    DcMotor m;
    double prevPower;
    ElapsedTime timeSinceLastPowerChange;

    public PIDTestMashupPID(DcMotor m, Telemetry tel) {
        this.m = m;
        this.m.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        prevPower = 0;
        timeSinceLastPowerChange = new ElapsedTime();
    }
    @Override
    public void setPower(double d) {
        DcMotor.RunMode mode;

        if (Math.abs(d) < 0.5) {
            mode = DcMotor.RunMode.RUN_USING_ENCODER;
        } else {
            mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER;
        }

        setMode(mode);
        if (d != prevPower) {
            m.setPower(d);
        }

        prevPower = d;
    }

    public void setMode(DcMotor.RunMode mode) {
        if (m.getMode() != mode) {
            m.setMode(mode);
        }
    }
}
