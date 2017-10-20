package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Kaden on 10/19/2017.
 */
@TeleOp(name = "DoubleArmDrive", group = "linear OpMode")
public class DoubleArmDrive extends OpMode {
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
        Claw = hardwareMap.servo.get("s1");
        Claw.setPosition(clawPosition);
        UpDown = hardwareMap.servo.get("s2");
        UpDown.setPosition(upDownPosition);
        BigRotation = hardwareMap.crservo.get("s3");
        BigRotation.setPower(bigRotationInit);
        reverseMotor(FrontLeft);
        reverseMotor(RearLeft);
    }

    @Override
    public void loop() {
        double right = gamepad1.right_stick_y;
        double left = gamepad1.left_stick_y;
        FrontRight.setPower(right);
        FrontLeft.setPower(left);
        RearRight.setPower(right);
        RearLeft.setPower(left);
        reverseMotor(FrontLeft);
        reverseMotor(RearLeft);

        bigRotationSpeed = Range.scale(gamepad1.left_stick_y,-1.0,1.0,bigRotationLowEnd,bigRotationHighEnd);
        upDownPosition = Range.clip((gamepad1.right_stick_y*upDownSpeed/16) + upDownPosition,upDownLowEnd, upDownHighEnd);

        if (gamepad1.a){

                clawPosition = clawHighEnd;

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

        telemetry.addData("ClawServoPos",Claw.getPosition());
        telemetry.addData("Wrist servo position",UpDown.getPosition());
        telemetry.addData("Big Rotation goal",bigRotationSpeed);
        telemetry.addData("big rotation speed",BigRotation.getPower());
        telemetry.update();
    }
    public void reverseMotor(DcMotor motor) {

        motor.setDirection(DcMotor.Direction.REVERSE);
    }
}
