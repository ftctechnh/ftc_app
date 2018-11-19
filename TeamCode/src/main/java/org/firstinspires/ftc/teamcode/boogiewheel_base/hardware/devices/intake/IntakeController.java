package org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.devices.intake;

import org.firstinspires.ftc.teamcode.framework.SubsystemController;

public class IntakeController extends SubsystemController{

    private Intake intake;

    public IntakeController(){
        init();
    }

    @Override
    public void init() {

       opModeSetup();

       intake = new Intake(hardwareMap);
    }

    public void beginIntaking(){
        intake.setIntakePower(1);
    }

    public void finishIntaking(){
        intake.setIntakePower(0);
    }

    @Override
    public void stop() {
        intake.stop();
    }
}
