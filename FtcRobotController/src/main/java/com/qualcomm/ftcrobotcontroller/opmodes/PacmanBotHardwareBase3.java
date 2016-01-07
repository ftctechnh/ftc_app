package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.opmodes.control.TwoPositionServo;
import com.qualcomm.ftcrobotcontroller.opmodes.drive.Tesla;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by tdoylend on 2015-12-20.
 *
 * This is the hardware base for use with PacmanBot.
 *
 * Robot Type: PacmanBot
 * Robot Version: 3000.0
 * Config File: Final2
 *
 * Change log:
 * 1.0.0 - First version.
 */

public class PacmanBotHardwareBase3 extends OpMode {

    public String hwbVersion = "1.0.0";

    @Override
    public void loop() {}
    @Override
    public void init() {}

    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor rearLeft;
    DcMotor rearRight;

    DcMotor basket;
    DcMotor winch;

    DcMotor brush;

    DcMotor spareTire;

    public TwoPositionServo climberTripper;
    public TwoPositionServo hookRelease;
    public TwoPositionServo basketDoor;

    public Tesla drive;

    public boolean side=false; //False = red; True = blue

    public void setupHardware() {
        //Everyone's old friend, setupHardware!

        frontLeft  = hardwareMap.dcMotor.get("front_left");
        frontRight = hardwareMap.dcMotor.get("front_right");
        rearLeft   = hardwareMap.dcMotor.get("rear_left");
        rearRight  = hardwareMap.dcMotor.get("rear_right");

        drive = new Tesla(frontLeft,frontRight,rearLeft,rearRight); //Create a new Tesla car - err, drive ;)

        if (!side) climberTripper = new TwoPositionServo(hardwareMap.servo.get("finger"),0.4,0);
        //else       climberTripper = new TwoPositionServo(hardwareMap.servo.get("finger"),0.6,1);
    }
}
