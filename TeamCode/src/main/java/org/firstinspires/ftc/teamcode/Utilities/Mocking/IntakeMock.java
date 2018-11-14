package org.firstinspires.ftc.teamcode.Utilities.Mocking;

import com.qualcomm.robotcore.hardware.CRServoImplEx;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.Mechanisms.Intake;

public class IntakeMock extends Intake {
    public IntakeMock(ServoImplEx leftIntakeFlipper, ServoImplEx rightIntakeFlipper, CRServoImplEx leftIntakeRoller, CRServoImplEx rightIntakeRoller) {
        super(null, null, null, null);
    }

    @Override
    public void setIntakeSpeed(double s) {}

    @Override
    public void deposit() {}

    @Override
    public void collect() {}

    @Override
    public void goToMin() {}

    @Override
    public void setPos(double pos) {}

    @Override
    public void enableFlippers() {}

    @Override
    public void disableFlippers() {}
}
