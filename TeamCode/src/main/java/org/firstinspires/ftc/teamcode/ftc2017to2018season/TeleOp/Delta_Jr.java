package org.firstinspires.ftc.teamcode.ftc2017to2018season.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Pahel and Rohan on 11/22/2017.
 */

@TeleOp(name = "Delta_Jr")

public class Delta_Jr extends OpMode {

    DcMotor leftWheelMotorFront;
    DcMotor leftWheelMotorBack;
    DcMotor rightWheelMotorFront;
    DcMotor rightWheelMotorBack;

    @Override
    public void init() {

        leftWheelMotorFront = hardwareMap.dcMotor.get("leftWheelMotorFront");
        leftWheelMotorBack = hardwareMap.dcMotor.get("leftWheelMotorBack");
        rightWheelMotorFront = hardwareMap.dcMotor.get("rightWheelMotorFront");
        rightWheelMotorBack = hardwareMap.dcMotor.get("rightWheelMotorBack");

        leftWheelMotorFront.setDirection(DcMotor.Direction.REVERSE);
        leftWheelMotorBack.setDirection(DcMotor.Direction.REVERSE);

    }


    @Override
    public void loop() {

        FourWheelDrive();
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

        telemetry.addData("Left Front value is", leftWheelMotorFront.getPower());
        telemetry.addData("Left Back value is", leftWheelMotorBack.getPower());
        telemetry.addData("Right Front value is", rightWheelMotorFront.getPower());
        telemetry.addData("Right Back value is", rightWheelMotorBack.getPower());
        telemetry.update();
        //telemetry.addData("",)
       //telemetry.update();
        //These were going to be used to find the values of triggers but we couldn't acomplish it
        //run the motors by setting power to the motors with the game pad values
        //leftWheelMotorFront.setPower(leftY_gp1);
        //leftWheelMotorBack.setPower(leftY_gp1);
        //rightWheelMotorFront.setPower(rightY_gp1);
        //rightWheelMotorBack.setPower(rightY_gp1);


    }
    
}