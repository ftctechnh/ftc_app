package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Bogg
{
    Gamepad gamepad;
    HardwareMap hardwareMap;
    DriveEngine driveEngine;
    DcMotor lift;
    Camera camera;
//    Sensors sensors;

    double alpha = 0.0039;
    double alphaInc = 0.000001;
    double xAve = 0;
    double yAve = 0;
    double SpinAve = 0;

    public Bogg(HardwareMap hardwareMap, Gamepad gamepad)
    {
        this.gamepad = gamepad;
        this.hardwareMap = hardwareMap;
        driveEngine = new DriveEngine(hardwareMap);
        lift  = hardwareMap.dcMotor.get("lift");
//        sensors = new Sensors(hardwareMap);
    }

    public double smoothX(double x)
    {
        if(x == 0)
            xAve = 0;
        else
            xAve = alpha * x + (1-alpha) * xAve;
        return xAve;
    }

    public double smoothY(double y)
    {
        if(y == 0)
            yAve = 0;
        else
            yAve = alpha * y + (1-alpha) * yAve;
        return yAve;
    }
    public double smoothSpin(double Spin)
    {
        if(Spin == 0)
            SpinAve = 0;
        else
            SpinAve = alpha * Spin + (1-alpha ) * SpinAve;
        return SpinAve;
    }
    public void lift()
    {
        if(gamepad.y)
            lift.setPower(.1);
        else if(gamepad.a)
            lift.setPower(-.1);
        else
            lift.setPower(0);
    }

    public void manualDrive()
    {
        if(gamepad.left_stick_button)
            driveEngine.drive(gamepad.left_stick_x, gamepad.left_stick_y);
        else
            driveEngine.drive(smoothX(gamepad.left_stick_x), smoothY(gamepad.left_stick_y));
    }

    public void incAlpha()
    {
        if(alpha + alphaInc<1)
            alpha += alphaInc;
    }

    public void decAlpha()
    {
        if(alpha - alphaInc>0)
            alpha -= alphaInc;
    }

    public double getAlpha()
    {
        return alpha;
    }


    public void manualRotate()
    {
        driveEngine.rotate(gamepad.right_stick_y);
    }
}
