package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


/**
 * Created by samih on 9/30/17.
 */

public class testAutonomous extends LinearOpMode {

    public static DcMotor leftFront = null;
    public static DcMotor rightFront = null;
    public static DcMotor leftRear = null;
    public static DcMotor rightRear = null;

    public void runOpMode() throws InterruptedException {

        waitForStart();

        zero(1);
        oneEighty(1);
        fourtyFive(1);
        negOneThirtyFive(1);
        ninety(1);
        negNinety(1);
        oneThirtyFive(1);
        negFourtyFive(1);
        oneEighty(1);
        zero(1);
        negOneThirtyFive(1);
        fourtyFive(1);
        negNinety(1);
        ninety(1);
        negFourtyFive(1);
        oneThirtyFive(1);
        zero(1);
        oneEighty(1);



    }
    public void zero(double time) {
        leftFront.setPower(1.0);
        rightFront.setPower(1.0);
        leftRear.setPower(1.0);
        rightRear.setPower(1.0);
        sleep((long)(time*1000));
        leftFront.setPower(0.0);
        rightFront.setPower(0.0);
        leftRear.setPower(0.0);
        rightRear.setPower(0.0);

    }

    public void fourtyFive(double time) {
        leftFront.setPower(1.0);
        rightRear.setPower(1.0);
        sleep((long)(time*1000));
        leftFront.setPower(0.0);
        rightRear.setPower(0.0);
    }

    public void negFourtyFive(double time) {
        rightFront.setPower(1.0);
        leftRear.setPower(1.0);
        sleep((long)(time*1000));
        rightFront.setPower(0.0);
        leftRear.setPower(0.0);
    }

    public void ninety(double time) {
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        leftRear.setDirection(DcMotor.Direction.REVERSE);
        leftFront.setPower(1.0);
        rightFront.setPower(1.0);
        leftRear.setPower(1.0);
        rightRear.setPower(1.0);
        sleep((long)(time*1000));
        leftFront.setPower(0.0);
        rightFront.setPower(0.0);
        leftRear.setPower(0.0);
        rightRear.setPower(0.0);
    }

    public void negNinety(double time){
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        rightRear.setDirection(DcMotor.Direction.REVERSE);
        leftFront.setPower(1.0);
        rightFront.setPower(1.0);
        leftRear.setPower(1.0);
        rightRear.setPower(1.0);
        sleep((long)(time*1000));
        leftFront.setPower(0.0);
        rightFront.setPower(0.0);
        leftRear.setPower(0.0);
        rightRear.setPower(0.0);
    }

    public void oneThirtyFive(double time){
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        leftRear.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setPower(1.0);
        leftRear.setPower(1.0);
        sleep((long)(time*1000));
        rightFront.setPower(0.0);
        leftRear.setPower(0.0);
    }

    public void negOneThirtyFive(double time){
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        rightRear.setDirection(DcMotor.Direction.REVERSE);
        leftFront.setPower(1.0);
        rightRear.setPower(1.0);
        sleep((long)(time*1000));
        leftFront.setPower(0.0);
        rightRear.setPower(0.0);
    }

    public void oneEighty(double time) {
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        leftRear.setDirection(DcMotor.Direction.REVERSE);
        rightRear.setDirection(DcMotor.Direction.REVERSE);
        leftFront.setPower(1.0);
        rightFront.setPower(1.0);
        leftRear.setPower(1.0);
        rightRear.setPower(1.0);
        sleep((long)(time*1000));
        leftFront.setPower(0.0);
        rightFront.setPower(0.0);
        leftRear.setPower(0.0);
        rightRear.setPower(0.0);

    }

}

//(+lf) + (+rf) + (+lr) + (+rr) = 0
//(+lf) + (+rr) = 45
//(+rf) + (+lr) = -45
//(+lf) + (-rf) + (-lr) + (+rr) = 90
//(-lf) + (+rf) + (+lr) + (-rr) = -90
//(-rf) + (-lr) = 135
//(-lf) + (-rr) = -135
//(-lf) + (-rf) + (-lr) + (-rr) = 180
