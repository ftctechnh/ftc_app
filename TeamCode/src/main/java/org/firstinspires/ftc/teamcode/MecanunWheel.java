package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "mecanum", group = "benprojects")

public class MecanunWheel extends LinearOpMode {

    private DcMotor frontright;
    private DcMotor frontleft;
    private DcMotor backright;
    private DcMotor backleft;

    @Override
    public void runOpMode() throws InterruptedException
    {
        frontright = hardwareMap.dcMotor.get("motorLeft"); //required for using the motors on the controller
        frontleft = hardwareMap.dcMotor.get("motorRight");
        backleft = hardwareMap.dcMotor.get("motorLeft"); //required for using the motors on the controller
        backright = hardwareMap.dcMotor.get("motorRight");

    while(opModeIsActive())
    {
        if (-gamepad1.left_stick_y>0){

        }

        idle();//required at the end for op to run properly
    }}
}
