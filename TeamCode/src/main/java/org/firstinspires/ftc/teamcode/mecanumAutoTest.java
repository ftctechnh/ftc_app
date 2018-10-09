package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Recharged Orange on 9/27/2018.
 */

@Autonomous(name = "mecanum Auto Test")//register Opmode


public class mecanumAutoTest extends linearOpmode {

    private DcMotor leftBack;
    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor rightBack;

    public void runOpMode() {

        initialization();

        waitForStart();//driver hits play
        while (opModeIsActive()) {//while driver has not hit stop
            leftBack.setPower(-1);// drive foword at full power
            leftFront.setPower(-1);
            rightBack.setPower(1);
            rightFront.setPower(1);
            sleep(1000);
            leftBack.setPower(.5);
            leftFront.setPower(.5);
            rightBack.setPower(.5);
            rightFront.setPower(.5);
            sleep(500);
            leftBack.setPower(0);
            leftFront.setPower(0);
            rightBack.setPower(0);
            rightFront.setPower(0);
            sleep(30000);
        }
    }

    public void initialization() {


        leftBack = hardwareMap.dcMotor.get("leftBack");
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        rightBack = hardwareMap.dcMotor.get("rightBack");

        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }
}
