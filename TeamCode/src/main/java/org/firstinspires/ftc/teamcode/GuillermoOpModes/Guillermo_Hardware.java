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
    public DcMotor fleft, fright, bleft, bright, lift;
    public Servo lub, rub;
    public static final float DRIVE_POWER = .3f;
    public static final float OPEN_SERVO_POSITION = .7f;
    public static final float MIDDLE_SERVO_POSITION = 0.2f;
    public static final float CLOSED_SERVO_POSITION = 0f;
    private HardwareMap hwMap;
    private Telemetry telemetry;

    float currentDrivePower = DRIVE_POWER;
    public ElapsedTime time = new ElapsedTime();


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
        lift = hwMap.dcMotor.get("lift");

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
        if(value > currentDrivePower || value < - currentDrivePower)
            return ((Math.abs(value) / value) * currentDrivePower);
        else
            return value;
    }

}
