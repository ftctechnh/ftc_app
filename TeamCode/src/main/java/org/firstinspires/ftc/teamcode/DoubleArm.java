package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
/**
 * Created by BeehiveRobotics-3648 on 9/19/2017.
 */
@TeleOp(name = "DoubleArm", group = "linear OpMode")
public class DoubleArm extends OpMode{
    Servo Claw;
    Servo UpDown;
    Servo BigRotation;
    double clawSpeed = 0.075;
    double clawPosition = 0.4;
    double clawHighEnd = 1.00;
    double clawLowEnd = 0.3;
    double upDownSpeed = 0.05;
    double upDownPosition = 0.5;
    double bigRotationSpeed = 0.025;
    double bigRotationPosition = 0.43;
    double bigRotationHighEnd = 0.49;
    double bigRotationLowEnd = 0.38;
    
    @Override

    public void init(){
        Claw = hardwareMap.servo.get("s1");
        Claw.setPosition(clawPosition);
        UpDown = hardwareMap.servo.get("s2");
        UpDown.setPosition(upDownPosition);
        BigRotation = hardwareMap.servo.get("s3");
        BigRotation.setPosition(bigRotationPosition);
    }

    @Override
    public void loop(){
        bigRotationPosition = (gamepad1.left_stick_y*bigRotationSpeed/16) + bigRotationPosition;

        if (gamepad1.a){
            if (Claw.getPosition() < clawHighEnd) {
                clawPosition = Claw.getPosition() + clawSpeed;
            }
        }
        if (gamepad1.b){
            if (Claw.getPosition() > clawLowEnd) {
                clawPosition = Claw.getPosition() + -clawSpeed;
            }
        }

        Claw.setPosition(clawPosition);
        Claw.setPosition(clawPosition);
        UpDown.setPosition(upDownPosition);
        UpDown.setPosition(upDownPosition);
        if (bigRotationPosition < bigRotationHighEnd && bigRotationPosition > bigRotationLowEnd) {
            BigRotation.setPosition(bigRotationPosition);
            BigRotation.setPosition(bigRotationPosition);
            bigRotationPosition = BigRotation.getPosition();
        }
        else if (bigRotationPosition < bigRotationLowEnd){
            bigRotationPosition = bigRotationLowEnd;
        }
        else if (bigRotationPosition > bigRotationHighEnd){
            bigRotationPosition = bigRotationHighEnd;
        }

        telemetry.addData("ClawServoPos",Claw.getPosition());
        telemetry.addData("Wrist servo position",UpDown.getPosition());
        telemetry.addData("Big Rotation goal",bigRotationPosition);
        telemetry.addData("big rotation position",BigRotation.getPosition());
        telemetry.update();
    }
}