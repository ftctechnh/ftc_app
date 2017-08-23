package org.firstinspires.ftc.teamcode.GarbageRobotOpModes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/*
- Name: Holonomic Hardware Map
- Creator[s]: Talon
- Date Created: 6/16/17
- Objective: To create a class that sets up the hardware map for our holonomic robot and has basic
             functions to reduce redundancies in other programs.
 */

public class Garbage_Hardware_Map {

    //Declaring variables
    public DcMotor fleft, fright, bleft, bright, fleckerino;
    public float dp = .3f; //Drive Power (range = 0-1)
    public float shootPower = 1f;
    private HardwareMap hwMap;
    private Telemetry telemetry;

    //Constructor; Put program's hardwaremap first, then telemetry,  then put true if gyro will be used or false if it won't
    public Garbage_Hardware_Map(HardwareMap hwmap, Telemetry telem){

        //telemetry.addData("Ready to go", false);
        //telemetry.update();

        hwMap = hwmap;
        telemetry = telem;

        //Setting up drive motors
        fleft = hwMap.dcMotor.get("fleft");
        fright = hwMap.dcMotor.get("fright");
        bleft = hwMap.dcMotor.get("bleft");
        bright = hwMap.dcMotor.get("bright");
        fleckerino = hwMap.dcMotor.get("fleckerino");
        fleft.setDirection(DcMotor.Direction.REVERSE);
        bleft.setDirection(DcMotor.Direction.REVERSE);

        //Alerts user that initialization is done
        //telemetry.addData("Ready to go", true);
        //telemetry.update(); //This line is giving us issues, so we may just have to cut the telemetry during init
    }

    public void drive(float left, float right) {
        fleft.setPower(ClipValue(left));
        fright.setPower(ClipValue(right));
        bleft.setPower(ClipValue(left));
        bright.setPower(ClipValue(right));
    }

    float ClipValue(float value) {
        if(value > dp || value < - dp)
            return ((Math.abs(value) / value) * dp);
        else
            return value;
    }

}
