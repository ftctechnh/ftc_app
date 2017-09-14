package org.firstinspires.ftc.teamcode.GuillermoOpModes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/*
- Name: Guillero Hardware Map
- Creator[s]: Talon
- Date Created: 8/16/17
- Objective: To create a class that sets up the hardware map for Guillermo and has basic
             functions to reduce redundancies in other programs.
 */

public class Guillermo_Hardware {

    //Declaring variables
    public DcMotor fleft, fright, bleft, bright;
    public Servo lub, rub;
    public float drivePower = .3f;
    public float OPEN_SERVO_POSITION = .7f;
    public float MIDDLE_SERVO_POSITION = 0.2f;
    public float CLOSED_SERVO_POSITION = 0f;

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

        fleft.setDirection(DcMotor.Direction.REVERSE);
        fright.setDirection(DcMotor.Direction.REVERSE);
        bright.setDirection(DcMotor.Direction.REVERSE);

        //Setting up servos
        lub = hwMap.servo.get("lub");
        rub = hwMap.servo.get("rub");

        lub.setDirection(Servo.Direction.REVERSE);

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

    //Utilizes the nubs to grab a glyph in front of the robot
    public void grabGlyph() {
        lub.setPosition(CLOSED_SERVO_POSITION);
        rub.setPosition(CLOSED_SERVO_POSITION);
    }

    //Utilizes the nubs to set the glyph in the cryptobox
    public void slightlyReleaseGlyph() {
        lub.setPosition(MIDDLE_SERVO_POSITION);
        rub.setPosition(MIDDLE_SERVO_POSITION);
    }
    //Fully opens the nubs
    public void releaseGlyph() {
        lub.setPosition(OPEN_SERVO_POSITION);
        rub.setPosition(OPEN_SERVO_POSITION);
    }

    //Makes sure the motor value doesn't go over the desired power
    float ClipValue(float value) {
        if(value > drivePower || value < - drivePower)
            return ((Math.abs(value) / value) * drivePower);
        else
            return value;
    }

}
