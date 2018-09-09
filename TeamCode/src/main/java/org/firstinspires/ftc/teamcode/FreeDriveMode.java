package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Drive forward, backward, or turn with leftMotor and rightMotor, both reversed.
 * Control using gamepad 1, left stick controls left motor and right controls right.
 */
@TeleOp
public class FreeDriveMode extends LinearOpMode {

    //Declare left and right to be of type Motor
    private DcMotor left;
    private DcMotor right;

    /** Run when Init is pressed */
    @Override
    public void runOpMode(){

        //Initialize the motors to the devices "leftMotor" and "rightMotor".
        //To change which motors those are, look in Configure Robot on one of the phones
        //TBH don't remember which phone.
        left = hardwareMap.get(DcMotor.class, "leftMotor");
        right = hardwareMap.get(DcMotor.class, "rightMotor");

        //Set the driver station to say "Meep! Initialized!"
        telemetry.addData("Status", "Meep! Initialized!");
        telemetry.update();

        //Wait until play is pressed
        waitForStart();

        //Do this until you click stop
        while(opModeIsActive()){
            //set these variables to wherever the gamepad sticks are at.
            //Negative because the motors are backwards
            float leftPower = -gamepad1.left_stick_y;
            float rightPower = -gamepad1.right_stick_y;
            //Set the motors power to those
            left.setPower(leftPower);
            right.setPower(rightPower);
            //Set the driver station to say what the gamepad stick values are.
            telemetry.addData("Status", "Left: "+leftPower+", Right: "+rightPower);
            telemetry.update();
        }
    }
}
