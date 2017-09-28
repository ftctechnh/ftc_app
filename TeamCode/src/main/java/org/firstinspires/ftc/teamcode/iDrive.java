package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by BeehiveRobotics on 9/7/2017.
 */

public interface iDrive{
    void Init(DcMotor FrontLeft, DcMotor FrontRight, DcMotor RearLeft, DcMotor RearRight);
    void Drive(float steering, float speed, float distance);
    void Tank(float left, float right);
    void Stop();
}