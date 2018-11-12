package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Joshua on 11/12/2018.
 * This is where all the code for controlling the robot go
 * Version 1.0.0
 */

public class robotOpMode extends OpMode{
    //Declare two motors for robot
    private DcMotor motorLeft = null;
    private DcMotor motorRight = null;
    @Override
    public void init()
    {
        //Initilize Hardware Variables
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorRight = hardwareMap.dcMotor.get("motorRight");
    }
    @Override
    public void loop()
    {
        //sets power of motor to gamepad left and right stick y values
        motorLeft.setPower(gamepad1.left_stick_y);
        motorRight.setPower(gamepad1.right_stick_y);
    }
}