package org.firstinspires.ftc.team8745;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


/**
 * Created by some guy "named" Nintendo8 on 11/6/2016.
 */
@Disabled //For now...
@TeleOp(name="8K_Tank_NoShooter")

public class TankDrive extends OpMode{

    DcMotor leftFRONT;
    DcMotor rightFRONT;
    DcMotor leftBACK;
    DcMotor rightBACK;
    //DcMotor shooterLeft;
    //DcMotor shooterRight;

    long startTime = 0;
    public void init() {
        //Front Motors
        leftFRONT = hardwareMap.dcMotor.get("motor-left");
        rightFRONT = hardwareMap.dcMotor.get("motor-right");

        //Back Motors
        leftBACK = hardwareMap.dcMotor.get("motor-leftBACK");
        rightBACK = hardwareMap.dcMotor.get("motor-rightBACK");

        //Shooters
        //shooterRight = hardwareMap.dcMotor.get("shooter-right");
        //shooterLeft = hardwareMap.dcMotor.get("shooter-left");

        //Reverse Mode
        leftFRONT.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBACK.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBACK.setDirection(DcMotorSimple.Direction.REVERSE);
        //shooterLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        //shooterRight.setDirection(DcMotorSimple.Direction.REVERSE);
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

        if (rightBumperPressed) {
            /*shooterLeft.setPower(1);
            shooterRight.setPower(1);
        }
        else {
            shooterRight.setPower(rightTrigger);
            shooterLeft.setPower(rightTrigger);*/
        }
    }
}