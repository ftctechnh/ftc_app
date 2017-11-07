package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Dolphinos on 9/8/2017.
 */


@TeleOp(name = "testing", group = "benprojects")//name of project and group it is in
//@Disabled
public class TestingFromScratch extends LinearOpMode //class that is in
{
    private DcMotor motorLeft; //this and the one below establishes the motor name
    private DcMotor motorRight; //THIS MUST MATCH THE NAMES ON THE PHONES
    //it also establishes what you talking about, in this case dc motors

    @Override//required statement
    public void runOpMode() throws InterruptedException//everything in thse brackets runs in op mode
    {
        motorLeft = hardwareMap.dcMotor.get("motorLeft"); //required for using the motors on the controller
        motorRight = hardwareMap.dcMotor.get("motorRight"); //lets controller know its using the motors

        motorLeft.setDirection(DcMotor.Direction.REVERSE); //reverses one motors direction to go straight
        waitForStart(); //makes sure code dosnt exectute until started

        while(opModeIsActive())
        {
            motorLeft.setPower(-gamepad1.left_stick_y); //motor power is set equal to the value of the stick on controller
            motorRight.setPower(-gamepad1.right_stick_y); //the controller stick gives a value of -1 for backward and 1 for forward

            idle();//required at the end for op to run properly
        }


    }



}
