package org.firstinspires.ftc.teamcode.ftc2017to2018season.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Pahel and Rohan on 11/22/2017.
 */

@TeleOp(name = "Delta_Jr")

public class Delta_Jr extends OpMode {
    /*Delta_Jr is designed for the smaller robot with AndyMark Mecanum wheels. This robot consits of Tetrix beams and Tetrix screws.
    This TeleOp is tested and run with this robot so if it is used with another robot it may not work.*/

    DcMotor leftWheelMotorFront;
    DcMotor leftWheelMotorBack;
    DcMotor rightWheelMotorFront;
    DcMotor rightWheelMotorBack;
    Servo glyphServoRight;
    Servo glyphServoLeft;
    //Initial value for slide motor
    public int IVFSM;
    DcMotor slideMotor;

    int servoPress = 0;


    @Override
    public void init() {

        leftWheelMotorFront = hardwareMap.dcMotor.get("leftWheelMotorFront");
        leftWheelMotorBack = hardwareMap.dcMotor.get("leftWheelMotorBack");
        rightWheelMotorFront = hardwareMap.dcMotor.get("rightWheelMotorFront");
        rightWheelMotorBack = hardwareMap.dcMotor.get("rightWheelMotorBack");

        glyphServoRight = hardwareMap.servo.get("glyphServo1");
        glyphServoLeft = hardwareMap.servo.get("glyphServo2");
        slideMotor = hardwareMap.dcMotor.get("slideMotor");
        IVFSM = slideMotor.getCurrentPosition();

        leftWheelMotorFront.setDirection(DcMotor.Direction.REVERSE);
        leftWheelMotorBack.setDirection(DcMotor.Direction.REVERSE);
        //left servo
        glyphServoLeft.setPosition(0.75);
        //right servo
        glyphServoRight.setPosition(0.25);

    }


    @Override
    public void loop() {
        FourWheelDrive();
        slideMove();
        glyphManipulator();
    }


    public void FourWheelDrive() {
        /*

        read the gamepad values and put into variables
         */
        float leftY_gp1 = (-gamepad1.left_stick_y);//*leftWheelMotorFront.getMaxSpeed();
        float rightY_gp1 = (-gamepad1.right_stick_y);//*leftWheelMotorFront.getMaxSpeed();
        float strafeStickLeft = (-gamepad1.left_trigger);//*leftWheelMotorFront.getMaxSpeed();
        float strafeStickRight = (-gamepad1.right_trigger);//*leftWheelMotorFront.getMaxSpeed();
        //run the motors by setting power to the motors with the game pad value

        if (gamepad1.left_trigger > 0) {
//strafing
            leftWheelMotorFront.setPower(-1);
            leftWheelMotorBack.setPower(1);
            rightWheelMotorFront.setPower(1);
            rightWheelMotorBack.setPower(-1);

        } else if (gamepad1.right_trigger > 0) {
//strafing
            leftWheelMotorFront.setPower(1);
            leftWheelMotorBack.setPower(-1);
            rightWheelMotorFront.setPower(-1);
            rightWheelMotorBack.setPower(1);

        } else {
            leftWheelMotorFront.setPower(rightY_gp1);
            leftWheelMotorBack.setPower(rightY_gp1);
            rightWheelMotorFront.setPower(leftY_gp1);
            rightWheelMotorBack.setPower(leftY_gp1);
        }

        //telemetry.addData("Left Front value is", leftWheelMotorFront.getPower());
        //telemetry.addData("Left Back value is", leftWheelMotorBack.getPower());
        //telemetry.addData("Right Front value is", rightWheelMotorFront.getPower());
        //telemetry.addData("Right Back value is", rightWheelMotorBack.getPower());
        //telemetry.update();
        //telemetry.addData("",)
       //telemetry.update();
        //These were going to be used to find the values of triggers but we couldn't acomplish it
        //run the motors by setting power to the motors with the game pad values
        //leftWheelMotorFront.setPower(leftY_gp1);
        //leftWheelMotorBack.setPower(leftY_gp1);
        //rightWheelMotorFront.setPower(rightY_gp1);
        //rightWheelMotorBack.setPower(rightY_gp1);


    }
    public void slideMove() {

        IVFSM = slideMotor.getCurrentPosition();

        if (gamepad2.right_stick_y > 0) {
            slideMotor.setPower(gamepad2.right_stick_y);

        } else if (gamepad2.right_stick_y < 0) {
            slideMotor.setPower(-1);
        } else {
            slideMotor.setPower(0);
        }
    }

    public void glyphManipulator() {
        Boolean Right_Bumper = (gamepad1.right_bumper);
        double right_claw = (glyphServoRight.getPosition());
        double left_claw = (glyphServoLeft.getPosition());
        if (Right_Bumper) {
            servoPress++;

            //open
            if (gamepad1.left_bumper) {

//opening the claw
                telemetry.addData("Is the claw opening loop?", glyphServoRight.getPosition());
                telemetry.update();
                // glyph servo 1 is the right claw
                glyphServoRight.setPosition(0.0);

                // glyph servo 2 is the left claw
                glyphServoLeft.setPosition(0.5);

                //close
            } else if (servoPress % 2 == 1) {
                telemetry.addData("Is the claw closed?", glyphServoRight.getPosition());
                telemetry.update();
                glyphServoRight.setPosition(0.25);
                glyphServoLeft.setPosition(0.75);
            }
        }

        telemetry.addData("The value of the right servo is", right_claw);
        telemetry.addData("The value of the left servo is", left_claw);
        telemetry.update();
    }
}