package org.firstinspires.ftc.teamcode;

/**
 * Created by albusdumbledore on 8/31/17.
 */

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Hard")
//declare variables
public class SimultaneousButtonOpMode extends LinearOpMode {
    private DcMotor leftMotor;
    private DcMotor rightMotor;
    private DcMotor leftBackMotor;
    private DcMotor rightBackMotor;

//change
    //something

    private int thing = 2;

    @Override
    public void runOpMode() throws InterruptedException {
        hardwareMap.dcMotor.get("LeftFront");
        hardwareMap.dcMotor.get("RightFront");
        hardwareMap.dcMotor.get("LeftBack");
        hardwareMap.dcMotor.get("RightBack");

        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBackMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {
            //straights basic motors and fourwheel drive
            //y1 and x2 is rightfront
            if (gamepad1.y && gamepad2.x) {
                rightMotor.setPower(1);
            }
            //y1 and 2a is rightback
            if (gamepad1.y && gamepad2.a) {
                rightBackMotor.setPower(1);
            }
            //rightbumper1 and leftbumper2 is leftback
            if (gamepad1.right_bumper && gamepad2.left_bumper) {
                leftBackMotor.setPower(1);
            }
            //b1 and a2 is leftfront
            if (gamepad1.b && gamepad2.a) {
                leftMotor.setPower(1);
            }
            //a1 and b1 and leftbumper1 and x2 and down2 and right2 is rightfront and rightback and leftback and leftfront
            if(gamepad1.a && gamepad1.b && gamepad1.left_bumper && gamepad2.x && gamepad2.dpad_down && gamepad2.dpad_right){
                leftMotor.setPower(1);
                leftBackMotor.setPower(1);
                rightBackMotor.setPower(1);
                rightMotor.setPower(1);
            }
            //The reverses
            //y1, x2, rightbumper2 = rightfront reverse
            if (gamepad1.y && gamepad2.x && gamepad2.right_bumper) {
                rightMotor.setPower(-1);
            }
            //y1, a2, rightbumper2 = rightBack reverse
            if (gamepad1.y && gamepad2.a && gamepad2.right_bumper) {
                rightBackMotor.setPower(-1);
            }
            //rightbumper1, leftbumper2, rightbumper2 = leftback reverse
            if (gamepad1.right_bumper && gamepad2.left_bumper && gamepad2.right_bumper) {
                leftBackMotor.setPower(-1);
            }
            //b1, x2, a2 = leftfront reverse
            if (gamepad1.b && gamepad2.a && gamepad2.right_bumper) {
                leftMotor.setPower(-1);
            }
            //a1 and b1 and leftbumper1 and x2 and down2 and right2 and rightbumper2 is rightfront and rightback and leftback and leftfront reverse
            if(gamepad1.a && gamepad1.b && gamepad1.left_bumper && gamepad2.x && gamepad2.dpad_down && gamepad2.dpad_right && gamepad2.right_bumper){
                leftMotor.setPower(-1);
                leftBackMotor.setPower(-1);
                rightBackMotor.setPower(-1);
                rightMotor.setPower(-1);
            }
            //the random combinations
            //if b1 and y2 leftback is right front
            if (gamepad1.b && gamepad2.y) {
                leftBackMotor.setPower(1);
                rightMotor.setPower(1);
            }
            //a1 and y2 is leftfront is leftback
            if (gamepad1.a && gamepad2.y) {
                leftBackMotor.setPower(1);
                leftMotor.setPower(1);
            }
            //y1 and b2 is leftfront is rightBack
            if (gamepad1.y && gamepad2.b) {
                leftMotor.setPower(1);
                rightBackMotor.setPower(1);
            }
            //y1 and y2 is leftfront
            if (gamepad1.y && gamepad2.y && gamepad1.left_bumper) {
                leftMotor.setPower(1);
                rightMotor.setPower(1);
            }
            //a1 and leftbumper2 is leftfront and rightfront
            if (gamepad1.a && gamepad2.left_bumper) {
                leftMotor.setPower(1);
                rightMotor.setPower(1);
            }
            //rightbumper1 and leftbumper2 is rightfront and rightback
            if (gamepad1.right_bumper && gamepad2.left_bumper) {
                rightBackMotor.setPower(1);
                rightMotor.setPower(1);
            }
            //leftbumper1 and leftbumper2 is leftfront and leftback reverse and rightfront and rightback
            if (gamepad1.left_bumper && gamepad2.left_bumper) {
                leftMotor.setPower(-1);
                leftBackMotor.setPower(-1);
                rightMotor.setPower(1);
                rightBackMotor.setPower(1);
            }


            //the random combinations reversed (add rightbumper2 to reverse anything)
            //if b1 and y2 and rightbumper2 is leftback and right front reverse
            if (gamepad1.b && gamepad2.y) {
                leftBackMotor.setPower(-1);
                rightMotor.setPower(-1);
            }
            //a1 and y2 is leftfront and rightbumper2 is leftback reverse
            if (gamepad1.a && gamepad2.y) {
                leftBackMotor.setPower(-1);
                leftMotor.setPower(-1);
            }
            //y1 and b2 is leftfront and rightbumper2 is rightBack reverse
            if (gamepad1.y && gamepad2.b) {
                leftMotor.setPower(-1);
                rightBackMotor.setPower(-1);
            }
            //y1 and y2 and rightbumper2 is leftfront reverse
            if (gamepad1.y && gamepad2.y && gamepad1.left_bumper) {
                leftMotor.setPower(-1);
                rightMotor.setPower(-1);
            }
            //a1 and leftbumper2 and rightbumper2 is leftfront and rightfront reverse
            if (gamepad1.a && gamepad2.left_bumper) {
                leftMotor.setPower(-1);
                rightMotor.setPower(-1);
            }
            //rightbumper1 and leftbumper2 and rightbumper2 is rightfront and rightback reverse
            if (gamepad1.right_bumper && gamepad2.left_bumper) {
                rightBackMotor.setPower(-1);
                rightMotor.setPower(-1);
            }
            //leftbumper1 and leftbumper2 and rightbumper2 is leftfront and leftback forward and rightfront and rightback reverse
            if (gamepad1.left_bumper && gamepad2.left_bumper) {
                leftMotor.setPower(1);
                leftBackMotor.setPower(1);
                rightMotor.setPower(-1);
                rightBackMotor.setPower(-1);
            }

            idle();
        }

    }
}
