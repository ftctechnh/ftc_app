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

    //Declare drive to be of type DriveBase
    private DriveBase drive;

    /** Run when Init is pressed */
    @Override
    public void runOpMode(){

        //Set drive to a DriveBase object, which lets us control the motors that move the whole
        //robot. Pass hardwareMap as an argument so that DriveBase can find the motors.
        drive = new DriveBase(hardwareMap);

        //Set the driver station to say "Meep! Initialized!"
        telemetry.addData("Status", "Meep! Initialized!");
        telemetry.update();

        //Wait until play is pressed
        waitForStart();

        //Do this until you click stop
        while(opModeIsActive()){
            //set these variables to wherever the gamepad sticks are at.
            float leftStick = gamepad1.left_stick_y;
            float rightStick = gamepad1.right_stick_y;
            //Set the motors power to those
            drive.set(leftStick, rightStick);
            //Set the driver station to say what the gamepad stick values are.
            telemetry.addData("Status", "Left: "+leftStick+", Right: "+rightStick);
            telemetry.update();
        }
    }
}
