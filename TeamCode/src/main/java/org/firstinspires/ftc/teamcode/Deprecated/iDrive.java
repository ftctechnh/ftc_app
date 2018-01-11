package org.firstinspires.ftc.teamcode.Deprecated;

/**
 * Created by BeehiveRobotics on 9/7/2017.
 */

import com.qualcomm.robotcore.hardware.DcMotor;

public interface iDrive{
    void Init(DcMotor FrontLeft, DcMotor FrontRight, DcMotor RearLeft, DcMotor RearRight);
    void Drive(float steering, float speed, float distance);
    void Tank(float left, float right);
    void Stop();
}