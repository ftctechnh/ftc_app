package org.firstinspires.ftc.team6417;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name="Calibrate", group="6417")
public class Calibrate extends LinearOpMode {
    Integer cpr = 28;
    Integer gearratio = 26;
    Double diameter = 3.93701;
    Double cpi = (cpr * gearratio) / (Math.PI * diameter);
    Double bias = 0.5;
    Boolean exit = false;

    Hardware6417 robot = new Hardware6417();

    //
    Double conversion = cpi * bias;
    //
    public void runOpMode() {
        //
        /***
        frontleft = hardwareMap.dcMotor.get("FrontLeft");
        frontright = hardwareMap.dcMotor.get("FrontRight");
        backleft = hardwareMap.dcMotor.get("BackLeft");
        backright = hardwareMap.dcMotor.get("BackRight");
        frontleft.setDirection(DcMotorSimple.Direction.REVERSE);//If your robot goes backward, switch this from right to left
        backleft.setDirection(DcMotorSimple.Direction.REVERSE);//If your robot goes backward, switch this from right to left
        //
         ***/

        robot.init(hardwareMap);

        waitForStartify();
        //
        moveToPosition(20, .2);//Don't change this line, unless you want to calibrate with different speeds
        //
    }
    //
    /*
    This function's purpose is simply to drive forward or backward.
    To drive backward, simply make the inches input negative.
     */

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

    /***
    public void moveToPosition(double inches, double speed) {
        //
        if (inches < 5) {
            int move = (int) (Math.round(inches * conversion));
            //
            frontleft.setTargetPosition(frontleft.getCurrentPosition() + move);
            frontright.setTargetPosition(frontright.getCurrentPosition() + move);
            backleft.setTargetPosition(backleft.getCurrentPosition() + move);
            backright.setTargetPosition(backright.getCurrentPosition() + move);
            //
            frontleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontright.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backright.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //
            frontleft.setPower(speed);
            frontright.setPower(speed);
            backleft.setPower(speed);
            backright.setPower(speed);
            //
            while (frontleft.isBusy() && frontright.isBusy() && backleft.isBusy() && backright.isBusy()) {
            }
            frontleft.setPower(0);
            frontright.setPower(0);
            backleft.setPower(0);
            backright.setPower(0);
        } else {
            int move1 = (int) (Math.round((inches - 5) * conversion));
            int movefl2 = frontleft.getCurrentPosition() + (int) (Math.round(inches * conversion));
            int movefr2 = frontright.getCurrentPosition() + (int) (Math.round(inches * conversion));
            int movebl2 = backleft.getCurrentPosition() + (int) (Math.round(inches * conversion));
            int movebr2 = backright.getCurrentPosition() + (int) (Math.round(inches * conversion));
            //
            frontleft.setTargetPosition(frontleft.getCurrentPosition() + move1);
            frontright.setTargetPosition(frontright.getCurrentPosition() + move1);
            backleft.setTargetPosition(backleft.getCurrentPosition() + move1);
            backright.setTargetPosition(backright.getCurrentPosition() + move1);
            //
            frontleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontright.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backright.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //
            frontleft.setPower(speed);
            frontright.setPower(speed);
            backleft.setPower(speed);
            backright.setPower(speed);
            //
            while (frontleft.isBusy() && frontright.isBusy() && backleft.isBusy() && backright.isBusy()) {
            }
            //
            frontleft.setTargetPosition(movefl2);
            frontright.setTargetPosition(movefr2);
            backleft.setTargetPosition(movebl2);
            backright.setTargetPosition(movebr2);
            //
            frontleft.setPower(.1);
            frontright.setPower(.1);
            backleft.setPower(.1);
            backright.setPower(.1);
            //
            while (frontleft.isBusy() && frontright.isBusy() && backleft.isBusy() && backright.isBusy()) {
            }
            frontleft.setPower(0);
            frontright.setPower(0);
            backleft.setPower(0);
            backright.setPower(0);
        }
        return;
    }
     ***/
    /*
    A tradition within the Thunder Pengwins code, we always start programs with waitForStartify,
    our way of adding personality to our programs.
     */
    public void waitForStartify() {
        waitForStart();
    }
}