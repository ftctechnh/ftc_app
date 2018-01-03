package org.firstinspires.ftc.teamcode.ftc2017to2018season.Vuforia;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;


@Autonomous(name="Vuforia Test")
@Disabled
public class Vuforia_test extends vuforia_general {

    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftBack;
    DcMotor rightBack;


    @Override
    public void runOpMode() {

        leftBack = hardwareMap.dcMotor.get("leftBack");
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightBack = hardwareMap.dcMotor.get("rightBack");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);

        initialize(true, true);

        waitForStart();

        startTracking();

        while(!vuMarkFound())  {

        }
        //returnImage();

        telemetry.addData("Vumark" , vuMark);
        telemetry.update();

        sleep(1000);

        if(vuMark == RelicRecoveryVuMark.CENTER){
            leftBack.setPower(-0.5);
            leftFront.setPower(-0.5);
            rightFront.setPower(-0.5);
            rightBack.setPower(-0.5);
            sleep(500);
        }
        else if(vuMark == RelicRecoveryVuMark.LEFT){
            leftBack.setPower(-0.5);
            leftFront.setPower(-0.5);
            rightFront.setPower(0.5);
            rightBack.setPower(0.5);
            sleep(500);
        }

        else if(vuMark == RelicRecoveryVuMark.RIGHT){
            leftBack.setPower(0.5);
            leftFront.setPower(0.5);
            rightFront.setPower(-0.5);
            rightBack.setPower(-0.5);
            sleep(500);
        }

        else {
            telemetry.addData("No Found", "");
            telemetry.update();
        }


        leftBack.setPower(0);
        leftFront.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);

    }


}
