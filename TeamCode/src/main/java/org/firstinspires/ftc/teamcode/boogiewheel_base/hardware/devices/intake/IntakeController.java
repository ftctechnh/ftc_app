package org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.devices.intake;

import org.firstinspires.ftc.teamcode.framework.opModes.AbstractOpMode;
import org.firstinspires.ftc.teamcode.framework.util.SubsystemController;

import static org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.Constants.*;
import static org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.RobotState.*;

public class IntakeController extends SubsystemController {

    private Intake intake;

    public IntakeController() {
        init();
    }

    public synchronized void init() {

        opModeSetup();

        intake = new Intake(hardwareMap);
    }

    public synchronized void update(){

    }

    public synchronized void stop() {
        intake.stop();
    }

    public synchronized void beginIntaking() {
        if (currentIntakeLiftState == IntakeLiftState.LOWERED) intake.setIntakePower(INTAKE_FORWARD_POWER);
        else lowerIntake();
    }

    public synchronized void finishIntaking() {
        //intake.setIntakePower(-1);
        //AbstractOpMode.delay(1000);
        intake.setIntakePower(INTAKE_STOP_POWER);
    }

    public synchronized void reverseIntake() {
        if (currentIntakeLiftState == IntakeLiftState.LOWERED) intake.setIntakePower(INTAKE_REVERSE_POWER);
    }

    public synchronized void lowerIntake() {
        currentIntakeLiftState = IntakeLiftState.IN_MOTION;
        intake.setLiftServoPosition(INTAKE_LIFT_LOWERED_POSITION);
        intake.setIntakePower(INTAKE_REVERSE_POWER);
        AbstractOpMode.delay(1000);
        intake.setIntakePower(INTAKE_FORWARD_POWER);
        currentIntakeLiftState = IntakeLiftState.LOWERED;
    }

    public synchronized void liftIntake() {
        currentIntakeLiftState = IntakeLiftState.IN_MOTION;
        intake.setLiftServoPosition(INTAKE_LIFT_RAISED_POSITION);
        intake.setIntakePower(INTAKE_LOWER_POWER);
        AbstractOpMode.delay(1000);
        intake.setIntakePower(INTAKE_STOP_POWER);
        currentIntakeLiftState = IntakeLiftState.RAISED;
    }
}
