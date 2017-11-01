package org.firstinspires.ftc.teamcode.PIDTesting;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by guberti on 10/21/2017.
 */

public class PIDTestDefaultPID extends PIDTestInterface{
    DcMotor m;

    PIDTestDefaultPID(DcMotor m, Telemetry tel) {
        this.m = m;
        this.m.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    @Override
    public void setPower(double d) {
        m.setPower(d);
    }
}
