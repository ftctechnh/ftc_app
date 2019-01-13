package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class MiniBogg
{
    Gamepad gamepad;
    HardwareMap hardwareMap;
    DriveEngine driveEngine;
    DcMotor lift;
    Sensors sensors;
    Servo brake;

    double alpha = 0.039;
    double alphaInc = 0.000001;
    double xAve = 0;
    double yAve = 0;
    double spinAve = 0;
    double liftAve = 0;
    boolean goingUp;

    public MiniBogg(HardwareMap hardwareMap, Gamepad gamepad, Telemetry telemetry)
    {
        this.gamepad = gamepad;
        this.hardwareMap = hardwareMap;
        driveEngine = new DriveEngine(hardwareMap, telemetry);
        lift  = hardwareMap.dcMotor.get("lift");
        sensors = new Sensors(hardwareMap);
        brake = hardwareMap.servo.get("brake");
    }

    public double smoothX(double x)
    {
        if(x * xAve < 0 || x == 0)
            xAve = 0;
        else
            xAve = alpha * x + (1-alpha) * xAve;
        return xAve;
    }

    public double smoothY(double y)
    {
        if(y * yAve < 0 || y == 0)
            yAve = 0;
        else
            yAve = alpha * y + (1-alpha) * yAve;
        return yAve;
    }

    public double smoothLift(double l)
    {
        if(l* liftAve < 0 || l == 0)
            liftAve = 0;
        else if(l == -.02)
        {
            liftAve = alpha*3 * l + (1-alpha*3) * liftAve;
        }
        else
            liftAve = alpha * l + (1-alpha) * liftAve;
        return liftAve;
    }

    public double smoothSpin(double spin)
    {
        if(spin * spinAve < 0 || spin == 0)
            spinAve = 0;
        else
            spinAve = alpha * spin + (1-alpha ) * spinAve;
        return spinAve;
    }

    public void manualLift()
    {
        if(gamepad.y)
        {
            goingUp = true;
        }

        if(gamepad.a)
        {
            goingUp = false;
        }

        if(gamepad.y ) //!sensors.touchTop.isPressed() && goingUp)
        {
            lift.setPower(smoothLift(1));
        }
        else if(gamepad.a )
        {
            goingUp = false;
            if (sensors.touchBottomIsPressed())
            {
                lift.setPower(smoothLift(-.02));
            } else {
                lift.setPower(smoothLift(-1));
            }
        }
        else
            lift.setPower(smoothLift(0));
    }

    public void lift(double power)
    {
        if(power > 0  && !sensors.touchTopIsPressed())
            lift.setPower(smoothLift(power));
        else if(power < 0  && !sensors.touchBottomIsPressed())
            lift.setPower(smoothLift(power));
        else
            lift.setPower(smoothLift(0));
    }

    public void setBrake(boolean on)
    {
        if(on)
            brake.setPosition(.5);
        else //off
            brake.setPosition(.6);
    }

    public void setBrake(double position)
    {
        brake.setPosition(position);
    }

    public void manualDrive()
    {
        if(gamepad.left_stick_button)
            driveEngine.drive(true,gamepad.left_stick_x/2, gamepad.left_stick_y/2);
        else
            driveEngine.drive(false,smoothX(gamepad.left_stick_x)/2, smoothY(gamepad.left_stick_y)/2);
    }
    public void manualRotate()
    {
        if(gamepad.right_stick_button)
            driveEngine.rotate(gamepad.right_stick_x);
        else
            driveEngine.rotate(smoothSpin(gamepad.right_stick_x));
    }
}
