package org.firstinspires.ftc.teamcode.GuillermoOpModes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/*
- Name: Garbage Hardware Map
- Creator[s]: Talon
- Date Created: 8/18/17
- Objective: To create a class that sets up the hardware map for our garbage display robot and has basic
             functions to reduce redundancies in other programs.
 */

public class Guillermo_Hardware {

    //Declaring variables
    public DcMotor fleft, fright, bleft, bright;
    public Servo lub, rub;
    public float drivePower = .3f; //Drive Power (range = 0-1)
    public float OUTSIDE_SERVO_POSITION = .2f;
    public float INSIDE_SERVO_POSITION = .8f;

    public ElapsedTime time = new ElapsedTime();
    private HardwareMap hwMap;
    private Telemetry telemetry;

    //Constructor; Put program's hardwaremap first, then telemetry. Call in init.
    public Guillermo_Hardware(HardwareMap hwmap, Telemetry telem){
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

        fright.setDirection(DcMotor.Direction.REVERSE);
        bright.setDirection(DcMotor.Direction.REVERSE);

        lub = hwMap.servo.get("lub");
        rub = hwMap.servo.get("rub");

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

    //Makes sure the motor value doesnt go over the desired power
    float ClipValue(float value) {
        if(value > drivePower || value < - drivePower)
            return ((Math.abs(value) / value) * drivePower);
        else
            return value;
    }

}
