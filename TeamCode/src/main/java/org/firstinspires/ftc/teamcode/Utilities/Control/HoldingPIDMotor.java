package org.firstinspires.ftc.teamcode.Utilities.Control;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class HoldingPIDMotor {
    DcMotorEx m;
    int targetPos;
    double MAX_POWER;
    double dir;
    public HoldingPIDMotor (DcMotorEx m, double MAX_POWER) {
        this.m = m;
        this.dir = 0;
        this.MAX_POWER = MAX_POWER;
        targetPos = 0;
    }

    public void setPower(double p) {
        if (dir != 0 && p == 0) {
            m.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            targetPos = m.getCurrentPosition();
            m.setTargetPosition(targetPos);
            m.setPower(MAX_POWER);
            dir = 0;
        } else if (dir == 0 && p != 0) {
            m.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            m.setPower(p);
            dir = p;
        } else if (p != 0 && dir != p) {
            m.setPower(p);
            dir = p;
        }
    }

    public void setTargetPos(int p) {
        m.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        m.setTargetPosition(p);
        m.setPower(MAX_POWER);
        dir = 0;
    }

    public int getCurrentPosition() {
        return m.getCurrentPosition();
    }
}
