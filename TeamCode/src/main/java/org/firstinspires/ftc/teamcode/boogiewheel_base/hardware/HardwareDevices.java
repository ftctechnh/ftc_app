package org.firstinspires.ftc.teamcode.boogiewheel_base.hardware;

import org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.devices.drive.DriveController;
import org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.devices.intake.IntakeController;
import org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.devices.mineral_lift.MineralLiftController;
import org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.devices.robot_lift.RobotLiftController;

public class HardwareDevices {

    public DriveController drive = null;
    public IntakeController intake = null;
    public MineralLiftController mineralLift = null;
    public RobotLiftController robotLift = null;

    public HardwareDevices() {
        drive = new DriveController();
        intake = new IntakeController();
        mineralLift = new MineralLiftController();
        robotLift = new RobotLiftController();
    }

    public void stop() {
        if(drive!=null)drive.stop();
        if(intake!=null)intake.stop();
        if(mineralLift!=null)mineralLift.stop();
        if(robotLift!=null)robotLift.stop();
    }
}
