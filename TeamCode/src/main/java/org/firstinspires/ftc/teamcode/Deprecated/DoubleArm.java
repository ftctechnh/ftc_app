package org.firstinspires.ftc.teamcode.Deprecated;

/**
 * Created by BeehiveRobotics-3648 on 9/19/2017.
 */

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "DoubleArm", group = "linear OpMode")
@Disabled
public class DoubleArm extends OpMode{
    private Servo Claw;
    private Servo UpDown;
    private CRServo BigRotation;
    private DcMotor Motor;
    private double clawPosition = 0.4;
    private double clawHighEnd = 1.00;
    private double clawLowEnd = 0.3;
    private double upDownSpeed = 0.075;
    private double upDownPosition = 0.5;
    private double upDownHighEnd = 1.00;
    private double upDownLowEnd = 0.00;
    private double bigRotationSpeed = 0;
    private double bigRotationInit = -0.15;
    private double bigRotationHighEnd = -0.05;
    private double bigRotationLowEnd = -0.23;
    private double motorSpeed;

    @Override
    public void init(){
        Claw = hardwareMap.servo.get("s1");
        Claw.setPosition(clawPosition);
        UpDown = hardwareMap.servo.get("s2");
        UpDown.setPosition(upDownPosition);
        BigRotation = hardwareMap.crservo.get("s3");
        BigRotation.setPower(bigRotationInit);
        Motor = hardwareMap.dcMotor.get("m5");

    }

    @Override
    public void loop(){
        bigRotationSpeed = Range.scale(gamepad1.left_stick_y,-1.0,1.0,bigRotationLowEnd,bigRotationHighEnd);
        upDownPosition = Range.clip((gamepad1.right_stick_y*upDownSpeed/16) + upDownPosition,upDownLowEnd, upDownHighEnd);
        motorSpeed = 0;
        if (gamepad1.right_trigger > gamepad1.left_trigger && gamepad1.right_trigger > 0.1){
            motorSpeed = 1;
        }
        else if (gamepad1.left_trigger > gamepad1.right_trigger && gamepad1.left_trigger > 0.1){
            motorSpeed = -1;
        }

            motorSpeed = Range.clip(gamepad1.right_trigger - gamepad1.left_trigger, -1, 1);
        if (gamepad1.a){
            if (Claw.getPosition() < clawHighEnd) {
                clawPosition = clawHighEnd;
            }
        }
        if (gamepad1.b){
            clawPosition = clawLowEnd;
        }

        UpDown.setPosition(upDownPosition);
        UpDown.setPosition(upDownPosition);
        Claw.setPosition(clawPosition);
        Claw.setPosition(clawPosition);
        BigRotation.setPower(bigRotationSpeed);
        BigRotation.setPower(bigRotationSpeed);
        Motor.setPower(motorSpeed);

        telemetry.addData("ClawServoPos",Claw.getPosition());
        telemetry.addData("Wrist servo position",UpDown.getPosition());
        telemetry.addData("Big Rotation goal",bigRotationSpeed);
        telemetry.addData("big rotation speed",BigRotation.getPower());
        telemetry.update();
    }
}