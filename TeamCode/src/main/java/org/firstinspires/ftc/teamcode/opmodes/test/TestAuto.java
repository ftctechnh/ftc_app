package org.firstinspires.ftc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.robot.Claw;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

/**
 * Created by Derek on 11/8/2017.
 */

@TeleOp(group = "Test",name = "TestAuto")
public class TestAuto extends LinearOpMode {

    private DcMotor leftDrive, rightDrive,joint;
    private Claw claw;
    private ColorSensor colorSensor;

    @Override
    public void runOpMode() throws InterruptedException {
        try {
            leftDrive = hardwareMap.dcMotor.get("left");
            rightDrive = hardwareMap.dcMotor.get("right");
            joint = hardwareMap.dcMotor.get("joint");

            Servo left = hardwareMap.servo.get("leftClaw");
            Servo right = hardwareMap.servo.get("rightClaw");

            left.setDirection(Servo.Direction.REVERSE);

            claw = new Claw(left,right);

            rightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
            joint.setZeroPowerBehavior(BRAKE);

            colorSensor = hardwareMap.colorSensor.get("color");


        } catch (Exception e) {
            telemetry.addData("",e.getLocalizedMessage());
        }

        waitForStart();

        leftDrive.setPower(-0.5);
        rightDrive.setPower(-0.5);

    }
}
