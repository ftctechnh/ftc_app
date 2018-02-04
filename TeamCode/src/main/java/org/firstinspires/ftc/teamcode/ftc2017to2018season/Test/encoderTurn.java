package org.firstinspires.ftc.teamcode.ftc2017to2018season.Test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.Autonomous_General_George;

/**
 * Created by Inspiration Team on 1/7/2018.
 */
@Autonomous(name = "encoderTurn360")
@Disabled
public class encoderTurn extends Autonomous_General_George {

    public static double ENCODERSPER360 = 5645;

    public void runOpMode(){
        initiate(true);

        waitForStart();

        turn360(85,0.5);
    }

    public void turn360(int degrees, double speed){

        double leftSpeed = 0;
        double rightSpeed = 0;
        int leftFrontPos;
        int rightFrontPos;
        int leftBackPos;
        int rightBackPos;

        back_left_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        back_right_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        front_left_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        front_right_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        sleep(200);

        leftFrontPos = front_left_motor.getCurrentPosition() + (int)(ENCODERSPER360 * Math.abs(degrees)/360);
        rightFrontPos = front_right_motor.getCurrentPosition() + (int)(ENCODERSPER360 * Math.abs(degrees)/360);
        leftBackPos = back_left_motor.getCurrentPosition() + (int)(ENCODERSPER360 * Math.abs(degrees)/360);
        rightBackPos = back_right_motor.getCurrentPosition() + (int)(ENCODERSPER360 * Math.abs(degrees)/360);

        if (degrees > 0){
            leftSpeed = -speed;
            rightSpeed = speed;

            front_left_motor.setTargetPosition(-leftFrontPos);
            front_right_motor.setTargetPosition(rightFrontPos);
            back_left_motor.setTargetPosition(-leftBackPos);
            back_right_motor.setTargetPosition(rightBackPos);
        }
        else if (degrees < 0){
            leftSpeed = speed;
            rightSpeed = -speed;

            front_left_motor.setTargetPosition(leftFrontPos);
            front_right_motor.setTargetPosition(-rightFrontPos);
            back_left_motor.setTargetPosition(leftBackPos);
            back_right_motor.setTargetPosition(-rightBackPos);
        }
        //if degrees is more than 0, you'll move counterclockwise
        //if degrees is less than 0, you'll move clockwise


        back_left_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        back_right_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        front_left_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        front_right_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        back_right_motor.setPower(rightSpeed);
        front_right_motor.setPower(rightSpeed);
        front_left_motor.setPower(leftSpeed);
        back_left_motor.setPower(leftSpeed);

        telemetry.addData("rightSpeed", rightSpeed);
        telemetry.addData("leftSpeed", leftSpeed);
        telemetry.addData("angle", degrees);
        telemetry.addData("front_left_target", leftFrontPos);
        while (opModeIsActive() &&
                (back_left_motor.isBusy() && back_right_motor.isBusy()&&
                        front_left_motor.isBusy() && front_right_motor.isBusy())) {
            back_left_motor.setPower(Math.abs(leftSpeed));
            //idle();
            back_right_motor.setPower(Math.abs(rightSpeed));
            //idle();
            front_left_motor.setPower(Math.abs(leftSpeed));
            //idle();
            front_right_motor.setPower(Math.abs(rightSpeed));
            //idle();
            telemetry.addData("rightSpeed", rightSpeed);
            telemetry.addData("leftSpeed", leftSpeed);
            telemetry.addData("angle", degrees);
            telemetry.addData("front_left_target", leftFrontPos);
            telemetry.addData("front_left_current", front_left_motor.getCurrentPosition());
            telemetry.addData("front_right_target", rightFrontPos);
            telemetry.addData("front_right_current", front_right_motor.getCurrentPosition());
            telemetry.update();
            idle();
        }
        idle();
        // Stop all motion;
        stopMotors();
    }
}
