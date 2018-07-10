package org.firstinspires.ftc.teamcode.ftc2016to2017season.chobani;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by zach on 7/16/17.
 */



@Autonomous(name = "chobani")
@Disabled
public class robotRun extends LinearOpMode{

    public void runOpMode(){

        DcMotor leftFront;
        DcMotor rightFront;
        DcMotor leftBack;
        DcMotor rightBack;

        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightBack = hardwareMap.dcMotor.get("rightBack");

        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightBack.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        leftFront.setPower(.2);
        rightFront.setPower(.2);
        leftBack.setPower(.2);
        rightBack.setPower(.2);

        sleep(1000);

        rightFront.setPower(.2);
        rightBack.setPower(.2);
        leftFront.setPower(-.2);
        leftBack.setPower(-.2);

        sleep(2000);

        leftFront.setPower(.2);
        rightFront.setPower(.2);
        leftBack.setPower(.2);
        rightBack.setPower(.2);

        sleep(1000);

        rightFront.setPower(.2);
        rightBack.setPower(.2);
        leftFront.setPower(-.2);
        leftBack.setPower(-.2);

        sleep(2000);

        leftFront.setPower(.2);
        rightFront.setPower(.2);
        leftBack.setPower(.2);
        rightBack.setPower(.2);

        sleep(1000);

        rightFront.setPower(.2);
        rightBack.setPower(.2);
        leftFront.setPower(-.2);
        leftBack.setPower(-.2);

        sleep(2000);

        leftFront.setPower(.2);
        rightFront.setPower(.2);
        leftBack.setPower(.2);
        rightBack.setPower(.2);

        sleep(1000);

        rightFront.setPower(.2);
        rightBack.setPower(.2);
        leftFront.setPower(-.2);
        leftBack.setPower(-.2);

        sleep(2000);

        leftFront.setPower(.2);
        rightFront.setPower(.2);
        leftBack.setPower(.2);
        rightBack.setPower(.2);

        sleep(1000);



        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);

        //set the power of the motors with the game pad values




        //the inputs on the controller



    }


}
