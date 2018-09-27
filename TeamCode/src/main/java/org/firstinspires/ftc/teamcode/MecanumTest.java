package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Recharged Orange on 9/27/2018.
 */
@TeleOp(name = "mecenum test")

public class MecanumTest extends LinearOpMode{


    double drive = -gamepad1.left_stick_y;  // Negative because the gamepad is weird
    double strafe = gamepad1.left_stick_x;
    double rotate = gamepad1.right_stick_x;


    double frontLeftPower;
    double backLeftPower;
    double frontRightPower;
    double backRightPower;



    public void runOpMode(){

        waitForStart();
    while (opModeIsActive()){

    }
    }



    public void mecanumDrive(){

        // You might have to play with the + or - depending on how your motors are installed
        frontLeftPower = drive + strafe + rotate;
        backLeftPower = drive - strafe + rotate;
        frontRightPower = drive - strafe - rotate;
        backRightPower = drive + strafe - rotate;

    }






}
