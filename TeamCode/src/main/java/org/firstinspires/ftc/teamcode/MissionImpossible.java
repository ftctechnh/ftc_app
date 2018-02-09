package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.relicrecoveryv2.PengwinFin;
import org.firstinspires.ftc.teamcode.relicrecoveryv2.PengwinWing;

/**
 * Created by wildgirls on 2/8/2018.
 */
@TeleOp(name="Mission Impossible",group="Jeff" )
public class MissionImpossible extends LinearOpMode {

    public void runOpMode() throws InterruptedException {

        DcMotor leftFrontMotor;
        DcMotor rightFrontMotor;
        DcMotor leftBackMotor;
        DcMotor rightBackMotor;
        leftBackMotor = hardwareMap.dcMotor.get("lback"); //left back
        rightBackMotor = hardwareMap.dcMotor.get("rback"); //right back
        leftFrontMotor = hardwareMap.dcMotor.get("lfront"); //left front
        rightFrontMotor = hardwareMap.dcMotor.get("rfront"); //right front

        waitForStart();

        while(opModeIsActive()){
           double power = gamepad1.left_stick_y ;
            leftBackMotor.setPower(power);
            rightBackMotor.setPower(power);
            leftFrontMotor.setPower(power);
            rightFrontMotor.setPower(power);
        }
    }
}
