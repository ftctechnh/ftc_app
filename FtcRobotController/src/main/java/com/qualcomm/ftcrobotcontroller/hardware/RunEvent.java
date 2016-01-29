package com.qualcomm.ftcrobotcontroller.hardware;

import com.qualcomm.ftcrobotcontroller.CycleTimer;
import com.qualcomm.ftcrobotcontroller.units.EncoderUnit;
import com.qualcomm.ftcrobotcontroller.units.TimeUnit;
import com.qualcomm.ftcrobotcontroller.units.Unit;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * An object used for non-blocking use of {@link MotorRunner}
 */
public class RunEvent {

    DcMotor motor;
    Unit unit;
    double valueLeft;

    /**
     * Creates a new {@link RunEvent}
     *
     * @param motor The motor to run with.
     * @param unit  The unit to use
     */
    public RunEvent(DcMotor motor, Unit unit) {
        this.motor = motor;
        this.unit = unit;
        valueLeft = unit.getValue();
    }

    /**
     * Updates the unit
     *
     * @see MotorRunner#update()
     */
    public void update() {
        if (unit instanceof TimeUnit) {
            valueLeft -= CycleTimer.getDeltaTime();
            if (valueLeft <= 0) {
                motor.setPower(0);
            }
        }
        if (unit instanceof EncoderUnit) {
            if (!motor.isBusy()) {
                motor.setPower(0);
                motor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
                motor.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
            }
        }
    }

    /**
     * Checks if the value specified has been reached
     *
     * @return If the event is done
     */
    public boolean isDone() {
        if (unit instanceof TimeUnit)
            return valueLeft <= 0;
        if (unit instanceof EncoderUnit)
            return !motor.isBusy();
        return true;
    }

    /**
     * The motor this event acts on
     *
     * @return The motor
     */
    public DcMotor getMotor() {
        return motor;
    }
}
