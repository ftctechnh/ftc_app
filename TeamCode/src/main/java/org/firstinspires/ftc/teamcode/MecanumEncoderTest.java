package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by Recharged Orange on 10/7/2018.
 */

@Autonomous(name ="Mecanum Encoder Test")

public class MecanumEncoderTest extends linearOpmode {

    private DcMotor leftBack;
    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor rightBack;

    public void runOpMode() {

        initialization();

        waitForStart(); //driver hits play
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftFront.setPower(.1);
        leftBack.setPower(.1);
        rightBack.setPower(.1);
        rightFront.setPower(.1);

        while (opModeIsActive() && leftBack.getCurrentPosition() < 1500 && leftFront.getCurrentPosition() < 1500 && rightFront.getCurrentPosition() < 1500 && rightBack.getCurrentPosition() < 1500) {
            telemetry.addData("left back position", leftBack.getCurrentPosition());
            telemetry.addData("left front position", leftFront.getCurrentPosition());
            telemetry.addData("right front position", rightFront.getCurrentPosition());
            telemetry.addData("right back position", rightBack.getCurrentPosition());

            telemetry.update();
        }
        leftBack.setPower(0);
        leftFront.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);

    }

        public void initialization (){

            leftBack = hardwareMap.dcMotor.get("leftBack");
            leftFront = hardwareMap.dcMotor.get("leftFront");
            rightFront = hardwareMap.dcMotor.get("rightFront");
            rightBack = hardwareMap.dcMotor.get("rightBack");

            leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
            leftBack.setDirection(DcMotorSimple.Direction.REVERSE);


        }

    }
