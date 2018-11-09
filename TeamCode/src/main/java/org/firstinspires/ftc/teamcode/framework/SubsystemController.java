package org.firstinspires.ftc.teamcode.framework;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.framework.userHardware.DoubleTelemetry;

public abstract class SubsystemController {

    public DoubleTelemetry telemetry;
    public HardwareMap hwMap;
    public mode Mode;

    public abstract void init();

    public abstract void stop();

    public void opModeSetup(){
        telemetry = AbstractOpMode.getTelemetry();
        hwMap = AbstractOpMode.getOpModeInstance().hardwareMap;
    }

    public enum mode{
        Teleop,Auton,Match
    }
}
