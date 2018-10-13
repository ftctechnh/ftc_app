package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Bogg
{
    Gamepad gamepad;
    HardwareMap hardwareMap;
    DriveEngine driveEngine;
//    Sensors sensors;

    public Bogg(HardwareMap hardwareMap, Gamepad gamepad)
    {
        this.gamepad = gamepad;
        this.hardwareMap = hardwareMap;
        driveEngine = new DriveEngine(hardwareMap);
//        sensors = new Sensors(hardwareMap);
    }

    public void manualDrive()
    {
        driveEngine.drive(gamepad.left_stick_x, gamepad.left_stick_y);
    }

    public void manualRotate()
    {
        driveEngine.rotate(gamepad.right_stick_y);
    }
}
