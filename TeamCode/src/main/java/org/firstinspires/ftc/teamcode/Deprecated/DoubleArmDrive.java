package org.firstinspires.ftc.teamcode.Deprecated;

/**
 * Created by Kaden on 10/19/2017.
 */

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "DoubleArmDrive", group = "linear OpMode")
@Disabled
public class DoubleArmDrive extends OpMode {
    private Servo Claw;
    private Servo UpDown;
    private CRServo BigRotation;
    private DcMotor Motor;
    private double motorSpeed;
    private double clawPosition = 0.4;
    private double clawHighEnd = 1.00;
    private double clawLowEnd = 0.3;
    private double upDownSpeed = 0.075;
    private double upDownPosition = 0.5;
    private double upDownHighEnd = 1.00;
    private double upDownLowEnd = 0.00;
    private double bigRotationSpeed = 0;
    private double bigRotationHighEnd = -0.02;
    private double bigRotationLowEnd = -0.28;
    private double bigRotationInit = -0.15;
    private DcMotor FrontLeft;
    private DcMotor FrontRight;
    private DcMotor RearLeft;
    private DcMotor RearRight;

    @Override
    public void init() {
        FrontLeft = hardwareMap.dcMotor.get("m1");
        FrontRight = hardwareMap.dcMotor.get("m2");
        RearLeft = hardwareMap.dcMotor.get("m3");
        RearRight = hardwareMap.dcMotor.get("m4");
        Motor = hardwareMap.dcMotor.get("m5");
        Claw = hardwareMap.servo.get("s1");
        Claw.setPosition(clawPosition);
        UpDown = hardwareMap.servo.get("s2");
        UpDown.setPosition(upDownPosition);
        BigRotation = hardwareMap.crservo.get("s3");
        BigRotation.setPower(bigRotationInit);
        reverseMotor(FrontRight);
        reverseMotor(RearRight);
    }

    @Override
    public void loop() {
        double right = gamepad1.right_stick_y;
        double left = gamepad1.left_stick_y;
        FrontRight.setPower(right);
        FrontLeft.setPower(left);
        RearRight.setPower(right);
        RearLeft.setPower(left);
        bigRotationSpeed = Range.clip(bigRotationSpeed + (gamepad2.left_stick_y/1000), bigRotationLowEnd, bigRotationHighEnd);
        //bigRotationSpeed = Range.scale(gamepad2.left_stick_y,-1.0,1.0,bigRotationLowEnd,bigRotationHighEnd);
        upDownPosition = Range.clip((gamepad2.right_stick_y*upDownSpeed/16) + upDownPosition,upDownLowEnd, upDownHighEnd);
        motorSpeed = Range.clip(gamepad2.right_trigger - gamepad2.left_trigger, -1, 1);

        if (gamepad2.a){
                clawPosition = clawHighEnd;
        }
        if (gamepad2.b){
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
        telemetry.addData("Wrist upDownServo position",UpDown.getPosition());
        telemetry.addData("Big Rotation goal",bigRotationSpeed);
        telemetry.addData("big rotation speed",BigRotation.getPower());
        telemetry.update();
    }
    public void reverseMotor(DcMotor motor) {

        motor.setDirection(DcMotor.Direction.REVERSE);
    }
}
