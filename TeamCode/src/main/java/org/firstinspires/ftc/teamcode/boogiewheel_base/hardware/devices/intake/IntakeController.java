package org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.devices.intake;

import org.firstinspires.ftc.teamcode.framework.SubsystemController;

public class IntakeController extends SubsystemController{

    private Intake intake;

    public IntakeController(){
        init();
    }

    @Override
    public synchronized void init() {

       opModeSetup();

       intake = new Intake(hardwareMap);
    }

    public synchronized void beginIntaking(){
        intake.setIntakePower(1);
    }

    public synchronized void finishIntaking(){
        intake.setIntakePower(0);
    }

    public synchronized void reverseIntake(){
        intake.setIntakePower(-1);
    }

    @Override
    public synchronized void stop() {
        intake.stop();
    }
}
