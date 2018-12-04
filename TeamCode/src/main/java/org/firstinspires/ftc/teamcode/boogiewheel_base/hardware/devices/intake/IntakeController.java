package org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.devices.intake;

import org.firstinspires.ftc.teamcode.framework.opModes.AbstractOpMode;
import org.firstinspires.ftc.teamcode.framework.util.SubsystemController;

public class IntakeController extends SubsystemController {

    private Intake intake;

    public IntakeController() {
        init();
    }

    @Override
    public synchronized void init() {

        opModeSetup();

        intake = new Intake(hardwareMap);
    }

    public synchronized void beginIntaking() {
        intake.setIntakePower(1);
    }

    public synchronized void finishIntaking() {
        intake.setIntakePower(-1);
        AbstractOpMode.delay(1000);
        intake.setIntakePower(0);
    }

    public synchronized void reverseIntake() {
        intake.setIntakePower(-1);
    }

    public synchronized void liftIntake() {
        intake.setLiftServoPosition(0);
        intake.setIntakePower(0.2);
        AbstractOpMode.delay(1000);
        intake.setIntakePower(0);
    }

    public synchronized void lowerIntake() {
        intake.setLiftServoPosition(-1);
        intake.setIntakePower(-0.5);
        AbstractOpMode.delay(1000);
        intake.setIntakePower(1);
    }

    @Override
    public synchronized void stop() {
        intake.stop();
    }
}
