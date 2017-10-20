package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import static org.firstinspires.ftc.teamcode.ConstrainedPIDMotor.Direction.HOLD;

public class ConstrainedPIDMotor {

    public enum Direction {
        FORWARD, BACKWARD, HOLD, COAST
    }
    DcMotor m;
    ElapsedTime timer;
    int timeTillLock;
    int lockPos;
    double runSpeed;
    int min;
    int max;
    public boolean override;

    public ConstrainedPIDMotor(DcMotor m, int t, double runSpeed, int min, int max) {
        this.m = m;
        timeTillLock = t;
        this.runSpeed = runSpeed;
        timer = new ElapsedTime();
        this.min = min;
        this.max = max;
        override = false;
        lockPos = 0;
    }

    public void setDirection(Direction d) {
        if (d != HOLD) {
            timer.reset();
        }

        switch (d) {
            case COAST:
                releaseMotor();
                break;

            case HOLD:
                if (timer.milliseconds() < -timeTillLock) {
                    releaseMotor();
                    lockPos = m.getCurrentPosition();
                } else {
                    goToPos(lockPos, 0.05, false, 0);
                }
                break;

            case FORWARD:
                goToPos(max, runSpeed, true, 1);
                break;

            case BACKWARD:
                goToPos(min, runSpeed, true, -1);
                break;
        }
    }

    private void releaseMotor() {
        if (m.getMode() != RunMode.RUN_WITHOUT_ENCODER) { // Otherwise, we've already done this
            m.setMode(RunMode.RUN_WITHOUT_ENCODER);
            m.setPower(0);
            m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
    }
    private void goToPos(int pos, double speed, boolean enableOverride, int dir) {
        if (enableOverride && override) {
            if (m.getMode() != RunMode.RUN_USING_ENCODER) {
                m.setMode(RunMode.RUN_USING_ENCODER);
            }
        } else {
            if (m.getMode() != RunMode.RUN_TO_POSITION) {
                m.setMode(RunMode.RUN_TO_POSITION);
            }
        }

        if (m.getTargetPosition() != pos) {
            m.setTargetPosition(pos);
        }

        if (m.getPower() != speed) {
            if (enableOverride && override) {
                m.setPower(speed * dir);
            } else {
                m.setPower(speed);
            }
        }
    }
}
