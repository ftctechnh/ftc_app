package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import java.lang.Math;
/**
 * Created by Peter on 1/31/2016.
 * Edited by Kaitlin on 1/31/2016
 */
public class AutonomousTime_T extends LinearOpMode
{
    DcMotor left;
    DcMotor right;
    Servo climberReleaseServo;

    public void runOpMode() throws InterruptedException  { }

    public void Forwards (float inches, boolean isForward) throws InterruptedException
    {
        if (isForward)
        {
            left.setPower(1.0f);
            right.setPower(1.0f);
        }
        else
        {
            left.setPower(-1.0f);
            right.setPower(-1.0f);
        }
        sleep((long) (inches * 50.9554));
        left.setPower(0f);
        right.setPower(0f);
        sleep(100);
    }

    public void turnOnCenter (float degrees, boolean isLeft) throws InterruptedException
    {
        float inches = (float) ((degrees)*(Math.PI * 20)/180);
        if(isLeft)
        {
            left.setPower(-1.0f);
            right.setPower(1.0f);
        }
        else
        {
            left.setPower(1.0f);
            right.setPower(-1.0f);
        }
        sleep((long) (inches * 50.9554));
        left.setPower(0f);
        right.setPower(0f);
        sleep(100);
    }

    public void flippythethingyANDdispense (float degrees, boolean isFullyDispensed)
    {
        climberReleaseServo().setPosition(1.0f);
    }

    private Servo climberReleaseServo()
    {
        return climberReleaseServo;
    }

    /*
    public void toggleRotorSpeed(boolean button, float speed) {
        if (button) {
            speedSwitch ^= buttonReady;
            buttonReady = false;
        } else {
            buttonReady = true;
        }
        if (speedSwitch)
        {
            speed = 0.85f;
        }
        else
        {
            speed = 0.65f;
        }
    }
     */
}
