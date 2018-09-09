package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class BasicTestMode extends LinearOpMode{

    private DcMotor motor;

    @Override
    public void runOpMode(){

        motor = hardwareMap.get(DcMotor.class, "testMotor");

        telemetry.addData("Status", "Meep! Initialized!");
        telemetry.update();

        waitForStart();

        while(opModeIsActive()){
            motor.setPower(1);
            telemetry.addData("Status", "Meep! Running!");
            telemetry.update();
        }
    }
}