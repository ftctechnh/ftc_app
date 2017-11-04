package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Nithilan on 9/2/2017.
 */
@TeleOp(name = "NS: Simple Tank Drive", group = "Training")
public class NS_SimpleTankDrive extends OpMode {

    DcMotor leftwheel;
    DcMotor rightwheel;
    DcMotor elevator;
    Servo leftclaw;
    Servo rightclaw;
    double leftWheelPower;
    double rightWheelPower;
    double multiplier = 0.10;
    double elevatorPower;

    @Override
    public void init() {
        leftwheel = hardwareMap.dcMotor.get("leftwheel");
        rightwheel = hardwareMap.dcMotor.get("rightwheel");
        elevator = hardwareMap.dcMotor.get("elevator");

        leftclaw = hardwareMap.servo.get("leftclaw");
        rightclaw = hardwareMap.servo.get("rightclaw");

        leftwheel.setDirection(DcMotor.Direction.REVERSE);
        rightclaw.setDirection(Servo.Direction.REVERSE);
    }

    @Override
    public void start(){

    }

    @Override
    public void loop() {

        leftWheelPower = gamepad1.left_stick_y;
        rightWheelPower = gamepad1.right_stick_y;
        elevatorPower = gamepad2.left_stick_y * multiplier;

        leftwheel.setPower(leftWheelPower);
        rightwheel.setPower(rightWheelPower);
        elevator.setPower(elevatorPower);

        if(gamepad2.a){
            leftclaw.setPosition(0);
            rightclaw.setPosition(0);
        }
        else{
            leftclaw.setPosition(1);
            rightclaw.setPosition(1);
        }
    }


    @Override
    public void stop(){

    }
}