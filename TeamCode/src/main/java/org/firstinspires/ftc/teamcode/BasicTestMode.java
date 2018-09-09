package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class BasicTestMode extends LinearOpMode{
    @Override
    public void runOpMode(){
        telemetry.addData("Status", "Meep! Initialized!");
        telemetry.update();

        waitForStart();

        while(opModeIsActive()){
            telemetry.addData("Status", "Meep! Running!");
            telemetry.update();
        }
    }
}