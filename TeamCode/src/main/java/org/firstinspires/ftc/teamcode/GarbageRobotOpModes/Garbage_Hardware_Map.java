package org.firstinspires.ftc.teamcode.GarbageRobotOpModes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/*
- Name: Garbage Hardware Map
- Creator[s]: Talon
- Date Created: 8/18/17
- Objective: To create a class that sets up the hardware map for our garbage display robot and has basic
             functions to reduce redundancies in other programs.
 */

public class Garbage_Hardware_Map {

    //Declaring variables
    public DcMotor fleft, fright, bleft, bright, fleckerino;
    public float dp = .3f; //Drive Power (range = 0-1)
    public float shootPower = 1f; //Flicker Power (range = 0-1)
    private HardwareMap hwMap;
    private Telemetry telemetry;

    //Constructor; Put program's hardwaremap first, then telemetry. Call in init.
    public Garbage_Hardware_Map(HardwareMap hwmap, Telemetry telem){
        //Takes telemetry and hardwaremap from opmode for use in this program
        hwMap = hwmap;
        telemetry = telem;

        //Lets the user know that the initialization is not yet complete
        telemetry.addData("Ready to go", false);
        telemetry.update();

        //Setting up drive motors
        fleft = hwMap.dcMotor.get("fleft");
        fright = hwMap.dcMotor.get("fright");
        bleft = hwMap.dcMotor.get("bleft");
        bright = hwMap.dcMotor.get("bright");
        fleckerino = hwMap.dcMotor.get("fleckerino");
        fleft.setDirection(DcMotor.Direction.REVERSE);
        bleft.setDirection(DcMotor.Direction.REVERSE);

        //Alerts user that initialization is done
        telemetry.addData("Ready to go", true);
        telemetry.update();
    }

    //Function to reduce redundancies when driving the robot
    public void drive(float left, float right) {
        fleft.setPower(ClipValue(left));
        fright.setPower(ClipValue(right));
        bleft.setPower(ClipValue(left));
        bright.setPower(ClipValue(right));
    }

    //Makes sure the motor value doesnt go over the desired power
    float ClipValue(float value) {
        if(value > dp || value < - dp)
            return ((Math.abs(value) / value) * dp);
        else
            return value;
    }

}
