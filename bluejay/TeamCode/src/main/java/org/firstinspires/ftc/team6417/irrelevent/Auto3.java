package org.firstinspires.ftc.team6417.irrelevent;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.team6417.Hardware6417;

@Autonomous(name="ParkLeft", group="Autonomous")
public class Auto3 extends LinearOpMode {

    Hardware6417 robot = new Hardware6417();
    BNO055IMU imu;
    Orientation angles;
    Acceleration gravity;

    Boolean exit = false;

    public void runOpMode(){

        robot.init(hardwareMap);
        waitForStart();

        robot.drive(-0.2);
        sleep(2200);
        robot.stop();

        robot.leftDragServo.setPosition(0);
        robot.rightDragServo.setPosition(0.75);

        sleep(1000);

        robot.drive(0.2);
        sleep(2500);
        robot.stop();

        robot.leftDragServo.setPosition(0.75);
        robot.rightDragServo.setPosition(0);

        sleep(1000);

        robot.strafe(0.75);
        sleep(1000);
        robot.stop();

        /***
        while(robot.colorSensor.blue() < 40 && robot.colorSensor.red() < 40){
        }
        sleep(1000);
         ***/

    }

    /***
    public void moveToPosition(double inches, double speed){
        //
        int move = (int)(Math.round(inches*conversion));
        //
        robot.leftBack.setTargetPosition(robot.leftBack.getCurrentPosition() + move);
        robot.leftFront.setTargetPosition(robot.leftFront.getCurrentPosition() + move);
        robot.rightBack.setTargetPosition(robot.rightBack.getCurrentPosition() + move);
        robot.rightFront.setTargetPosition(robot.rightFront.getCurrentPosition() + move);
        //
        robot.leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //
        robot.leftBack.setPower(speed);
        robot.leftFront.setPower(speed);
        robot.rightBack.setPower(speed);
        robot.rightFront.setPower(speed);
        //
        while (robot.leftFront.isBusy() && robot.rightFront.isBusy() && robot.leftBack.isBusy() && robot.rightBack.isBusy()){
            if (exit){
                robot.leftBack.setPower(0);
                robot.leftFront.setPower(0);
                robot.rightBack.setPower(0);
                robot.rightFront.setPower(0);
                return;
            }
        }
        robot.leftBack.setPower(0);
        robot.leftFront.setPower(0);
        robot.rightBack.setPower(0);
        robot.rightFront.setPower(0);
        robot.leftDragServo.setPosition(0);
        robot.rightDragServo.setPosition(0);
        return;

    }
     ***/

}
