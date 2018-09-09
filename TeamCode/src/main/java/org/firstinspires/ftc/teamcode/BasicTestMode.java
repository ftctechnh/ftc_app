package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Prints out basic messages on init and play, and sets testMotor to full forward.
 */
@TeleOp
public class BasicTestMode extends LinearOpMode{

    //Declare motor as a DcMotor
    private DcMotor motor;

    /** Run when Init is pressed on the driver station.*/
    @Override
    public void runOpMode(){

        //Set motor to whatever was decided in Configure Robot as testMotor.
        motor = hardwareMap.get(DcMotor.class, "testMotor");

        //Print Meep! Initialized!
        telemetry.addData("Status", "Meep! Initialized!");
        telemetry.update();

        //Wait until start pressed
        waitForStart();

        //Do this forever
        while(opModeIsActive()){
            //Set the motor's power to full
            motor.setPower(1);
            //Say Meep! Running!
            telemetry.addData("Status", "Meep! Running!");
            telemetry.update();
        }
    }
}