package org.firstinspires.ftc.team8745;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;


/**
 * Created by some guy "named" Nintendo8 on 11/6/2016.
 */
//@Disabled //For now...
@TeleOp(name="8K_Tank_Shooter")

public class TankDriveShooter extends OpMode{

    DcMotor leftFRONT;
    DcMotor rightFRONT;
    DcMotor leftBACK;
    DcMotor rightBACK;
    DcMotor shooterLeft;
    DcMotor shooterRight;
    Servo shooterServo;
    long startTime = 0;

    final double kServoNullPosition = 0.8;
    final double kServoRange = 0.6;
    final double kShootPower = 0.7;

    public void init() {
        //Front Motors
        leftFRONT = hardwareMap.dcMotor.get("motor-left");
        rightFRONT = hardwareMap.dcMotor.get("motor-right");

        //Back Motors
        leftBACK = hardwareMap.dcMotor.get("motor-leftBACK");
        rightBACK = hardwareMap.dcMotor.get("motor-rightBACK");

        //Shooters
        shooterRight = hardwareMap.dcMotor.get("shooter-right");
        shooterLeft = hardwareMap.dcMotor.get("shooter-left");
        //Servos
        shooterServo = hardwareMap.servo.get("shooter-servo");


        //Reverse Mode
        leftFRONT.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBACK.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterRight.setDirection(DcMotorSimple.Direction.FORWARD);
    }
    @Override
    public void loop() {
        float leftDC = gamepad1.left_stick_y;
        float rightDC = gamepad1.right_stick_y;
        leftFRONT.setPower(leftDC);
        rightFRONT.setPower(rightDC);
        leftBACK.setPower(leftDC);
        rightBACK.setPower(rightDC);

        float rightTrigger = gamepad2.right_trigger;
        boolean rightBumperPressed = gamepad2.right_bumper;
        float leftTrigger = gamepad2.left_trigger;

        //shooterServo.setPosition(0.1);
        shooterServo.setPosition((leftTrigger * (-kServoRange)) + kServoNullPosition);
        //telemetry.addData("Servo Position", shooterServo.getPosition());

        if (rightBumperPressed) {
            shooterLeft.setPower(1.0);
            shooterRight.setPower(1.0);
        }
        else {
            shooterRight.setPower(rightTrigger * kShootPower);
            shooterLeft.setPower(rightTrigger * kShootPower);
        }




    }
}
