package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import static org.firstinspires.ftc.teamcode.ConstrainedPIDMotor.Direction.HOLD;

public class ConstrainedPIDMotor {

    public enum Direction {
        FORWARD, BACKWARD, HOLD, COAST
    }
    DcMotorEx m;
    ElapsedTime timer;
    int timeTillLock;
    int lockPos;
    double forwardRunSpeed;
    double backwardRunSpeed;
    int min;
    int max;
    int encoderOffset;
    public boolean override;
    boolean unlockAtZero;
    Telemetry logging;
    boolean seekingPosition;
    Integer targetPos;

    public ConstrainedPIDMotor(DcMotorEx m, int t, double forwardRunSpeed, double backwardRunSpeed,
                               int min, int max, Telemetry tel, boolean unlockAtZero) {
        this.m = m;
        timeTillLock = t;
        this.forwardRunSpeed = forwardRunSpeed;
        this.backwardRunSpeed = backwardRunSpeed;
        timer = new ElapsedTime();
        this.min = min;
        this.max = max;
        override = false;
        lockPos = 0;
        encoderOffset = 0;
        logging = tel;
        seekingPosition = false;
        targetPos = null;
        this.unlockAtZero = unlockAtZero;
    }

    public void setDirection(Direction d) {
        setDirection(d, true);
    }
    public void setDirection(Direction d, boolean stopPositionSeeking) {
        logging.addData("Lock position", lockPos);

        if (d != HOLD) {
            timer.reset();
        }

        if (stopPositionSeeking && seekingPosition) {
            seekingPosition = false;
            targetPos = null;

        } else if (seekingPosition) {

            int ticksToGo = targetPos - m.getCurrentPosition();
            //logging.addData("Ticks to go", ticksToGo);

            if (Math.abs(ticksToGo) < 100) { // Doesn't work if encoder position jumps for some reason
                seekingPosition = false;
                targetPos = null;

            } else {
                goToPos(targetPos, 1, false, (int) Math.signum(ticksToGo));
                return;
            }
        }

        //logging.addData("Going to lock position?", (d == HOLD && timer.milliseconds() >= timeTillLock));
        switch (d) {
            case COAST:
                releaseMotor();
                break;

            case HOLD:
                if (timer.milliseconds() < timeTillLock) {
                    setMotorMode(RunMode.RUN_USING_ENCODER);
                    lockPos = m.getCurrentPosition();
                    logging.addData("lockPos", lockPos);
                    if (m.getZeroPowerBehavior() != DcMotor.ZeroPowerBehavior.BRAKE) {
                        m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    }
                    m.setPower(0);
                } else {
                    if (Math.abs(lockPos - m.getCurrentPosition()) < 100 && lockPos < 100 && unlockAtZero) {
                        m.setMotorDisable();
                        logging.addData("Zarm on: ", true);
                    } else {
                        if (unlockAtZero) {
                            m.setMotorEnable();
                            logging.addData("Zarm off: ", false);
                        }
                        goToPos(lockPos, 1.0, false, 0);
                    }
                }
                break;

            case FORWARD:
                goToPos(max, forwardRunSpeed, true, 1);
                break;

            case BACKWARD:
                goToPos(min, backwardRunSpeed, true, -1);
                break;
        }
        //logging.addData("Current lock position: ", lockPos);
        //logging.addData("Current target: ", m.getTargetPosition());
    }

    public void setTargetToSeek(int targetEncoderPos) {
        targetPos = targetEncoderPos;
        seekingPosition = true;
        setDirection(HOLD, false);
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
            setMotorMode(RunMode.RUN_USING_ENCODER);
        } else {
            setMotorMode(RunMode.RUN_TO_POSITION);
        }

        if (m.getTargetPosition() != pos) {
            m.setTargetPosition(pos + encoderOffset);
        }

        if (m.getPower() != speed) {
            if (enableOverride && override) {
                m.setPower(speed * dir / 2.0);
            } else {
                m.setPower(speed);
            }
        }
    }

    private void setMotorMode(DcMotor.RunMode mode) {
        if (m.getMode() != mode) {
            m.setMode(mode);
        }
    }
}
