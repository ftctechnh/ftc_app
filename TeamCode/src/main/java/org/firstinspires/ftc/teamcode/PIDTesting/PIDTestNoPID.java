package org.firstinspires.ftc.teamcode.PIDTesting;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by guberti on 10/21/2017.
 */

public class PIDTestNoPID extends PIDTestInterface{
    DcMotor m;

    PIDTestNoPID(DcMotor m, Telemetry tel) {
        this.m = m;
        this.m.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    @Override
    public void setPower(double d) {
        m.setPower(d);
    }
}
