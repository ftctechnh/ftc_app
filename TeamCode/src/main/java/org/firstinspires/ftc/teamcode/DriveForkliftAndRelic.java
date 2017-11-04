package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Kaden on 11/4/2017.
 */
@TeleOp(name = "ForkliftAndRelicDrive" , group = "Linear OpMode")
public class DriveForkliftAndRelic extends OpMode {
    private Servo Claw;
    private double clawPosition = 0.0;
    private double clawHighEnd = 0.45;
    private double clawLowEnd = 0.225;
    private DcMotor FrontLeft;
    private DcMotor FrontRight;
    private DcMotor RearLeft;
    private DcMotor RearRight;
    private DcMotor DrawerSlide;
    private double DrawerSlideLowEnd;
    private double DrawerSlideHighEnd;
    private double DrawerSlideSpeed = 0;
    private double up = 0;
    private double down = 0;
    //relic starts here
    private Servo claw;
    private DcMotor motor;
    private double servolowend = 0.0;
    private double servohighend = 0.8;
    private double motorSpeed;
    private Servo arm;
    private double armDir;
    private double armlowend = 0.0;
    private double armhighend = 1.0;

    @Override
    public void init() {
        FrontLeft = hardwareMap.dcMotor.get("m1");
        FrontRight = hardwareMap.dcMotor.get("m2");
        RearLeft = hardwareMap.dcMotor.get("m3");
        RearRight = hardwareMap.dcMotor.get("m4");
        DrawerSlide = hardwareMap.dcMotor.get("m5");
        Claw = hardwareMap.servo.get("s1");
        Claw.setPosition(clawPosition);
        reverseMotor(FrontRight);
        reverseMotor(RearRight);
        //where relic starts
        claw = hardwareMap.servo.get("s2");
        claw.setPosition(servohighend);
        motor = hardwareMap.dcMotor.get("m6");
        arm = hardwareMap.servo.get("s3");
        arm.setPosition(armlowend);
    }

    @Override
    public void loop() {
        double right = gamepad1.right_stick_y;
        double left = gamepad1.left_stick_y;
        FrontRight.setPower(right);
        FrontLeft.setPower(left);
        RearRight.setPower(right);
        RearLeft.setPower(left);

        if (gamepad1.a) {
            clawPosition = clawHighEnd;
            //clawPosition = clawPosition + 0.001;

        }
        if (gamepad1.b) {
            clawPosition = clawLowEnd;
            //clawPosition = clawPosition - 0.001;
        }
        Claw.setPosition(clawPosition);
        Claw.setPosition(clawPosition);
        up = gamepad1.right_trigger;
        down = gamepad1.left_trigger;
        DrawerSlideSpeed = up - down;
        DrawerSlide.setPower(DrawerSlideSpeed);
        telemetry.addData("Current Claw Position", Claw.getPosition());
        //so this is where the relic starts
        motorSpeed = gamepad2.left_stick_y/2;
        armDir = Range.scale(gamepad2.right_stick_y, -1.0, 1.0, 0.0, 1.0);
        if (gamepad2.a) {
            //claw.setPosition(Range.clip(claw.getPosition() + 0.005, servolowend, servohighend));
            claw.setPosition(servolowend);
        }
        if (gamepad2.b) {
            //claw.setPosition(Range.clip(claw.getPosition() - 0.005, servolowend, servohighend));
            claw.setPosition(servohighend);
        }
        motor.setPower(motorSpeed);
        arm.setPosition(armDir);
        arm.setPosition(armDir);
        telemetry.addData("clawposition: ", claw.getPosition());
        telemetry.addData("servo pos", arm.getPosition());
    }

    public void reverseMotor(DcMotor motor) {

        motor.setDirection(DcMotor.Direction.REVERSE);
    }
}

