package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by thund on 10/19/2017.
 */

public class JeffThePenguin {

    HardwareSensorMap robot   = new HardwareSensorMap();   // Use a Pushbot's hardware
    //power variables
    DcMotor leftFrontMotor;
    DcMotor rightFrontMotor;
    DcMotor leftBackMotor;
    DcMotor rightBackMotor;


    public JeffThePenguin(HardwareMap hardwareMap){
        //get motors
        leftBackMotor = hardwareMap.dcMotor.get("m0"); //left back
        rightBackMotor = hardwareMap.dcMotor.get("m1"); //right back
        leftFrontMotor = hardwareMap.dcMotor.get("m2"); //left front
        rightFrontMotor = hardwareMap.dcMotor.get("m3"); //right front
    }


    public void turnRight(double power){
        leftBackMotor.setPower(power);
        leftFrontMotor.setPower(power);
        rightBackMotor.setPower(-power);
        rightFrontMotor.setPower(-power);
    }

    public void turnLeft(double power){
        leftBackMotor.setPower(-power);
        leftFrontMotor.setPower(-power);
        rightBackMotor.setPower(power);
        rightFrontMotor.setPower(power);
    }

    public void driveForward(double power){
        leftBackMotor.setPower(power);
        leftFrontMotor.setPower(power);
        rightBackMotor.setPower(power);
        rightFrontMotor.setPower(power);
    }

    public void driveBackward(double power){
        leftBackMotor.setPower(-power);
        leftFrontMotor.setPower(-power);
        rightBackMotor.setPower(-power);
        rightFrontMotor.setPower(-power);
    }

    public void strafeLeft(double power){
        leftBackMotor.setPower(-power);
        leftFrontMotor.setPower(power);
        rightBackMotor.setPower(power);
        rightFrontMotor.setPower(-power);
    }

    public void strafeRight(double power){
        leftBackMotor.setPower(power);
        leftFrontMotor.setPower(-power);
        rightBackMotor.setPower(-power);
        rightFrontMotor.setPower(power);
    }

}
