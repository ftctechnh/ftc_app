package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;

//Baby Balancing Robot Iteration 1(BBBot I1) created by Eric on 8/29/2017.

@TeleOp (name="ChristineIsCrying",group="Christine" )
public class ChristineIsCrying extends LinearOpMode{

    DcMotor dcMotor;
    double power = 0;

    @Override
    public void runOpMode() throws InterruptedException {

        dcMotor = hardwareMap.dcMotor.get("dc");

        waitForStart();

        while (opModeIsActive()){
            /*
            if(gamepad1.x){
                power = .2;
            }

            if (gamepad1.y)
            {
                power = -.2;
            }
            */
            power = gamepad1.left_stick_y;

            if(gamepad1.a){
                dcMotor.setPower(0);
                dcMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                dcMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                dcMotor.setTargetPosition(200);
            }

            dcMotor.setPower(power);
            telemetry.addData("Encoder Position", dcMotor.getCurrentPosition());
            telemetry.update();
        }



    }
}
