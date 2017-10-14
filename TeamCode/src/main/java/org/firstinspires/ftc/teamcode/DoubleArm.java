package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by BeehiveRobotics-3648 on 9/19/2017.
 */
@TeleOp(name = "DoubleArm", group = "linear OpMode")
public class DoubleArm extends OpMode{
    Servo Claw;
    Servo UpDown;
    CRServo BigRotation;
    double clawSpeed = 0.075;
    double clawPosition = 0.4;
    double clawHighEnd = 1.00;
    double clawLowEnd = 0.3;
    double upDownSpeed = 0.075;
    double upDownPosition = 0.5;
    double upDownHighEnd = 1.00;
    double upDownLowEnd = 0.00;
    double bigRotationSpeed = 0;
    double bigRotationInit = 0.3;
    double bigRotationMaxSpeed = 0.03;
    double bigRotationHighEnd = 0.49;
    double bigRotationLowEnd = 0.43;
    
    @Override
    public void init(){
        Claw = hardwareMap.servo.get("s1");
        Claw.setPosition(clawPosition);
        UpDown = hardwareMap.servo.get("s2");
        UpDown.setPosition(upDownPosition);
        BigRotation = hardwareMap.crservo.get("s3");
        BigRotation.setPower(bigRotationInit);
    }

    @Override
    public void loop(){
        bigRotationSpeed = Range.scale(gamepad1.left_stick_y,-1.0,1.0,bigRotationLowEnd,bigRotationHighEnd);
        upDownPosition = (gamepad1.right_stick_y*upDownSpeed/16) + upDownPosition;
        BigRotation.setPower(bigRotationSpeed);

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

        if (upDownPosition < upDownHighEnd && upDownPosition > upDownLowEnd) {
            UpDown.setPosition(upDownPosition);
            UpDown.setPosition(upDownPosition);
            upDownPosition = UpDown.getPosition();
        }
        else if (upDownPosition < upDownLowEnd){
            upDownPosition = upDownLowEnd;
        }
        else if (upDownPosition > upDownHighEnd){
            upDownPosition = upDownHighEnd;
        }

        telemetry.addData("ClawServoPos",Claw.getPosition());
        telemetry.addData("Wrist servo position",UpDown.getPosition());
        telemetry.addData("Big Rotation goal",bigRotationSpeed);
        telemetry.addData("big rotation speed",BigRotation.getPower());
        telemetry.update();
    }
}