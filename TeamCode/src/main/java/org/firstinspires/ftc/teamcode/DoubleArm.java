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
    private Servo Claw;
    private Servo UpDown;
    private CRServo BigRotation;
    private double clawPosition = 0.4;
    private double clawHighEnd = 1.00;
    private double clawLowEnd = 0.3;
    private double upDownSpeed = 0.075;
    private double upDownPosition = 0.5;
    private double upDownHighEnd = 1.00;
    private double upDownLowEnd = 0.00;
    private double bigRotationSpeed = 0;
    private double bigRotationInit = 1.0;
    private double bigRotationHighEnd = -0.05;
    private double bigRotationLowEnd = -0.23;

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
        upDownPosition = Range.clip((gamepad1.right_stick_y*upDownSpeed/16),upDownLowEnd, upDownHighEnd) + upDownPosition;

        if (gamepad1.a){
            if (Claw.getPosition() < clawHighEnd) {
                clawPosition = 1.0;
            }
        }
        if (gamepad1.b){
            if (Claw.getPosition() > clawLowEnd) {
                clawPosition = 0.3;
            }
        }

        UpDown.setPosition(upDownPosition);
        UpDown.setPosition(upDownPosition);
        Claw.setPosition(clawPosition);
        Claw.setPosition(clawPosition);
        BigRotation.setPower(bigRotationSpeed);
        BigRotation.setPower(bigRotationSpeed);

        telemetry.addData("ClawServoPos",Claw.getPosition());
        telemetry.addData("Wrist servo position",UpDown.getPosition());
        telemetry.addData("Big Rotation goal",bigRotationSpeed);
        telemetry.addData("big rotation speed",BigRotation.getPower());
        telemetry.update();
    }
}