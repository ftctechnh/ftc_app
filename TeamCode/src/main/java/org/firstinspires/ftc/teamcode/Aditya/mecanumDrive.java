package org.firstinspires.ftc.teamcode.Aditya;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by inspirationteam on 11/20/2016.
 */

@TeleOp(name = "mecanum drive")

public class mecanumDrive extends OpMode {

    DcMotor leftWheelMotorFront;
    DcMotor leftWheelMotorBack;
    DcMotor rightWheelMotorFront;
    DcMotor rightWheelMotorBack;
    DcMotor ballCollectorMotor;
    DcMotor ballShooterMotor;


    public void init() {

        leftWheelMotorFront = hardwareMap.dcMotor.get("leftWheelMotorFront");
        leftWheelMotorBack = hardwareMap.dcMotor.get("leftWheelMotorBack");
        rightWheelMotorFront = hardwareMap.dcMotor.get("rightWheelMotorFront");
        rightWheelMotorBack = hardwareMap.dcMotor.get("rightWheelMotorBack");


            /* lets reverse the direction of the right wheel motor*/
        rightWheelMotorFront.setDirection(DcMotor.Direction.REVERSE);
        rightWheelMotorBack.setDirection(DcMotor.Direction.REVERSE);
        leftWheelMotorFront.setDirection(DcMotor.Direction.FORWARD);
        leftWheelMotorBack.setDirection(DcMotor.Direction.FORWARD);

        ballShooterMotor = hardwareMap.dcMotor.get("ballShooterMotor");
        ballCollectorMotor = hardwareMap.dcMotor.get("ballCollectorMotor");

    }

    @Override
    public void init_loop() {

    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {

        mecanumDrive();
        CollectBalls();
        BallShooter();

    }

    @Override
    public void stop() {

    }

    public void mecanumDrive() {

        float leftY = gamepad1.left_stick_y;
        float rightY = gamepad1.right_stick_y;
        float leftX = gamepad1.left_stick_x;
<<<<<<< HEAD

        if (Math.abs(gamepad1.left_stick_x) > 0) {

            try {
                leftWheelMotorBack.setPower(leftX);


            } catch (Exception e) {
                e.printStackTrace();
            }
            rightWheelMotorBack.setPower(-leftX);
            leftWheelMotorFront.setPower(leftX);
            rightWheelMotorFront.setPower(leftX);
            leftWheelMotorBack.setPower(-leftX);
        }
        else {
            leftWheelMotorBack.setPower(leftY);
            rightWheelMotorBack.setPower(rightY);
            leftWheelMotorFront.setPower(leftY);
            rightWheelMotorFront.setPower(rightY);
        }


    }

    public void CollectBalls(){
        boolean intake = gamepad1.right_bumper;
        boolean outtake = gamepad1.left_bumper;

        if (intake) {
            ballCollectorMotor.setPower(1);
        } else if (outtake) {
            ballCollectorMotor.setPower(-1);
        } else {
            ballCollectorMotor.setPower(0);
        }
    }

    public void BallShooter(){
        float shoot = -gamepad2.right_stick_y;//gets value from 2nd gamepad's joystick

        ballShooterMotor.setPower(shoot);//set power


       /*saving previous code sample if using only one gamepad*/


        if(gamepad2.right_bumper) {
            ballShooterMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            ballShooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            ballShooterMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            ballShooterMotor.setTargetPosition((int) (ballShooterMotor.getCurrentPosition() + (1478 * 1.4)));
            telemetry.addData("", "Shooting...");
            telemetry.update();
            ballShooterMotor.setPower(0.7);
            while (ballShooterMotor.isBusy()) {

            }
            ballShooterMotor.setPower(0);
            telemetry.addData("", "Done Shooting");
            telemetry.update();
            ballShooterMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }

        //asdfasdfasd


=======
        float rightY = gamepad1.right_stick_y;

        leftWheelMotorBack.setPower(leftY - leftX);
        rightWheelMotorBack.setPower(rightY + leftX);
        leftWheelMotorFront.setPower(leftY + leftX);
        rightWheelMotorFront.setPower(rightY - leftX);
>>>>>>> 7d93069213312a0a863bbd2d7890f2de327bd7a8
    }

}