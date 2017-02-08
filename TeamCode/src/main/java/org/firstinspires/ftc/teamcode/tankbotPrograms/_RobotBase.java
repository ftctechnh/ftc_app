package org.firstinspires.ftc.teamcode.tankbotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.BaseFunctions;

import java.util.ArrayList;

public abstract class _RobotBase extends BaseFunctions
{
    /*** CONFIGURE ALL ROBOT ELEMENTS HERE ***/
    //Drive motors (they are lists because it helps when we add on new motors.
    protected DcMotor leftMotor, rightMotor;

    // Called on initialization (once)
    protected void initializeHardware() throws InterruptedException
    {
        //Make sure that the robot components are found and initialized correctly.
        //This all happens during init()
        /*************************** DRIVING MOTORS ***************************/
        leftMotor = initialize(DcMotor.class, "Left Motor");
        rightMotor = initialize(DcMotor.class, "Right Motor");
    }
}