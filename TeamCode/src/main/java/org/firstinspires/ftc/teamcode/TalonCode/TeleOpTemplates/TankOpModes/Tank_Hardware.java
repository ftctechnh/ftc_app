package org.firstinspires.ftc.teamcode.TalonCode.TeleOpTemplates.TankOpModes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/*
- Name: Guillero Hardware Map
- Creator[s]: Talon
- Date Created: 8/16/17
- Objective: To create a class that sets up the hardware map for Guillermo and has basic
             functions to reduce redundancies in other programs.
 */

public class Tank_Hardware {

    //Declaring variables
    public DcMotor fleft, fright, bleft, bright, lift;
    public static final float DRIVE_POWER = .3f;
    private HardwareMap hwMap;
    private Telemetry telemetry;

    float currentDrivePower = DRIVE_POWER;
    public ElapsedTime time = new ElapsedTime();


    //Constructor; Put program's hardwaremap first, then telemetry. Call in init.
    public Tank_Hardware(HardwareMap hwmap, Telemetry telem){
        //Takes telemetry and hardwaremap from opmode for use in this program
        hwMap = hwmap;
        telemetry = telem;

        //Lets the user know that the initialization is not yet complete
        telemetry.addData("Ready to go", false);
        telemetry.update();

        //Setting up drive motors
        fleft = hwMap.dcMotor.get("fleft");
        bleft = hwMap.dcMotor.get("bleft");
        fright = hwMap.dcMotor.get("fright");
        bright = hwMap.dcMotor.get("bright");

        fleft.setDirection(DcMotor.Direction.REVERSE);
        fright.setDirection(DcMotor.Direction.REVERSE);
        bright.setDirection(DcMotor.Direction.REVERSE);



        //Alerts user that initialization is done
        telemetry.addData("Ready to go", true);
        telemetry.update();
    }

    //Function to reduce redundancies when driving the robot
    public void drive(float l, float r) {
        fleft.setPower(ClipValue(l));
        fright.setPower(ClipValue(r));
        bleft.setPower(ClipValue(l));
        bright.setPower(ClipValue(r));
    }

    //Makes sure the motor value doesn't go over the desired power
    float ClipValue(float value) {
        if(value > currentDrivePower || value < - currentDrivePower)
            return ((Math.abs(value) / value) * currentDrivePower);
        else
            return value;
    }

}
