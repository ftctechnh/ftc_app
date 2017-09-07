package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by connorespenshade on 9/7/17.
 */

@TeleOp(name = "Weird Analog")
public class WeirdAnalog extends LinearOpMode{

    private DcMotor leftFront;
    private DcMotor leftBack;
    private DcMotor rightFront;
    private DcMotor rightBack;

    @Override
    public void runOpMode() throws InterruptedException {


        initDCMotors();

        waitForStart();

        while(opModeIsActive()) {


            idle();
        }

    }

    public void initDCMotors() {

        leftFront = hardwareMap.dcMotor.get("frontLeft");
        leftBack = hardwareMap.dcMotor.get("backLeft");
        rightFront = hardwareMap.dcMotor.get("backRight");
        rightBack = hardwareMap.dcMotor.get("frontRight");

        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    private float function1(float gamepadAnalog) {

        return 0
    }

}
