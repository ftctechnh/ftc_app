package org.firstinspires.ftc.teamcode.Year_2018_19.Robot;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;

public class ModernRoboticsRobot
{
    //The hardware components.
    public DcMotor leftDrive;
    public DcMotor rightDrive;

    //The hardware data.
    public double drivePower = 0.5;

    //Set up commands here.
    public void init(HardwareMap hwMap)     //Hardware and components initiates.
    {
        leftDrive = hwMap.get(DcMotor.class, "leftDrive");
        rightDrive = hwMap.get(DcMotor.class, "rightDrive");

        leftDrive.setDirection(DcMotor.Direction.REVERSE);
    }

    public void safetyStop() //Safely stops all motors and other running components.
    {
        leftDrive.setPower(0);
        rightDrive.setPower(0);
    }
}
