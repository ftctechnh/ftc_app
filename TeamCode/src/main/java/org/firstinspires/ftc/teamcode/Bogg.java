package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Bogg
{
    Gamepad gamepad;
    HardwareMap hardwareMap;
    DriveEngine driveEngine;
//    Sensors sensors;

    double alpha = .2;
    double xAve = 0;
    double yAve = 0;

    public Bogg(HardwareMap hardwareMap, Gamepad gamepad)
    {
        this.gamepad = gamepad;
        this.hardwareMap = hardwareMap;
        driveEngine = new DriveEngine(hardwareMap);
//        sensors = new Sensors(hardwareMap);
    }

    private double smoothX(double x)
    {
        if(x == 0)
            xAve = 0;
        else
            xAve = alpha * x + (1-alpha) * xAve;
        return xAve;
    }

    private double smoothY(double y)
    {
        if(y == 0)
            yAve = 0;
        else
            yAve = alpha * y + (1-alpha) * yAve;
        return yAve;
    }

    public void manualDrive()
    {
        driveEngine.drive(smoothX(gamepad.left_stick_x), smoothY(gamepad.left_stick_y));
        driveEngine.drive(gamepad.left_stick_x, gamepad.left_stick_y);
    }

    public void manualRotate()
    {
        driveEngine.rotate(gamepad.right_stick_y);
    }
}
