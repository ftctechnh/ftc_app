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
    double clawPosition = 0;
    double upDownSpeed = 0.05;
    double upDownPosition = 0;
    double bigRotationSpeed = 0.05;
    double bigRotationPosition = 0;
    
    @Override

    public void init(){
        Claw = hardwareMap.servo.get("s1");
        Claw.setPosition(0.6);
        UpDown = hardwareMap.servo.get("s2");
        UpDown.setPosition(0.5);
        BigRotation = hardwareMap.servo.get("s3");
        BigRotation.setPosition(0.2);
    }

    @Override
    public void loop(){
        bigRotationPosition = gamepad1.left_stick_y*bigRotationSpeed + bigRotationPosition;

        if (gamepad1.a){
            if (Claw.getPosition() < 1.00) {
                clawPosition = Claw.getPosition() + clawSpeed;
            }
        }
        if (gamepad1.b){
            if (Claw.getPosition() > 0.4) {
                clawPosition = Claw.getPosition() + -clawSpeed;
            }
        }

        Claw.setPosition(clawPosition);
        UpDown.setPosition(upDownPosition);
        BigRotation.setPosition(bigRotationPosition);

        telemetry.addData("ClawServoPos",Claw.getPosition());
        telemetry.addData("Wirst servo position",UpDown.getPosition());
        telemetry.addData("big rotation position",BigRotation.getPosition());
        telemetry.update();
    }
}