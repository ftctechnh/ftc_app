package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import java.lang.*;


/**
 * Created by BeehiveRobotics on 9/7/2017.
 */

public class OpenLoop implements iDrive{
    private DcMotor FrontLeftMotor;
    private DcMotor RearLeftMotor;
    private DcMotor FrontRightMotor;
    private DcMotor RearRightMotor;
    @Override
    public void Init(DcMotor FrontLeft, DcMotor FrontRight, DcMotor RearLeft, DcMotor RearRight) {
        FrontLeftMotor = FrontLeft;
        FrontRightMotor = FrontRight;
        RearLeftMotor = RearLeft;
        RearRightMotor = RearRight;

    }
    @Override
    public void Stop() {
        FrontLeftMotor.setPower(0);
        FrontRightMotor.setPower(0);
        RearLeftMotor.setPower(0);
        RearRightMotor.setPower(0);
    }
    @Override
    public void Drive(float steering, float speed, float distance) {
        float middle = 0;
        if (speed > 0) {
            steering = steering * speed;
            middle = speed - Math.abs(steering);
        }
        else {
            speed = -speed;
            steering = -steering * speed;
            middle = speed + Math.abs(steering);
        }

        float right = middle - steering;
        float left = middle + steering;

        FrontRightMotor.setPower(right);
        RearRightMotor.setPower(right);
        FrontLeftMotor.setPower(left);
        RearLeftMotor.setPower(left);

    }
    @Override
    public void Tank(float left, float right) {
        FrontRightMotor.setPower(right);
        RearRightMotor.setPower(right);
        FrontLeftMotor.setPower(left);
        RearLeftMotor.setPower(left);
    }
}