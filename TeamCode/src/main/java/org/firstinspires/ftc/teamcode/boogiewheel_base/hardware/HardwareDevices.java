package org.firstinspires.ftc.teamcode.boogiewheel_base.hardware;

import org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.devices.drive.DriveController;
import org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.devices.intake.IntakeController;
import org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.devices.mineral_lift.MineralLiftController;
import org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.devices.robot_lift.RobotLiftController;

public class HardwareDevices {

    public DriveController drive;
    public IntakeController intake;
    public MineralLiftController mineralLift;
    public RobotLiftController robotLift;

    public HardwareDevices(){
        drive = new DriveController();
        intake = new IntakeController();
        mineralLift = new MineralLiftController();
        robotLift = new RobotLiftController();
    }

    public void stop(){
        //YOU CANNOT USE AN OBJECT BEFORE ASSIGNING IT
        //drive.stop();
        //intake.stop();
        //mineralLift.stop();
        //robotLift.stop();
    }
}
