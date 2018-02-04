package org.firstinspires.ftc.teamcode.ftc2017to2018season.Test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Inspiration Team on 2/3/2018.
 */
@Autonomous(name = "driveForward")
public class driveForward extends LinearOpMode {
    DcMotor leftWheelMotorFront;
    DcMotor leftWheelMotorBack;
    DcMotor rightWheelMotorFront;
    DcMotor rightWheelMotorBack;
    public void runOpMode(){
        leftWheelMotorFront = hardwareMap.dcMotor.get("leftWheelMotorFront");
        leftWheelMotorBack = hardwareMap.dcMotor.get("leftWheelMotorBack");
        rightWheelMotorFront = hardwareMap.dcMotor.get("rightWheelMotorFront");
        rightWheelMotorBack = hardwareMap.dcMotor.get("rightWheelMotorBack");

        rightWheelMotorFront.setDirection(DcMotor.Direction.REVERSE);
        rightWheelMotorBack.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        leftWheelMotorBack.setPower(0.5);
        leftWheelMotorFront.setPower(0.5);
        rightWheelMotorBack.setPower(0.5);
        rightWheelMotorFront.setPower(0.5);

        sleep(1000);

        leftWheelMotorBack.setPower(0);
        leftWheelMotorFront.setPower(0);
        rightWheelMotorBack.setPower(0);
        rightWheelMotorFront.setPower(0);

    }
}
