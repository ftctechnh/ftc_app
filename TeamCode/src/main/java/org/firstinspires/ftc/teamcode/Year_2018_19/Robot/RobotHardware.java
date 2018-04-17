package org.firstinspires.ftc.teamcode.Year_2018_19.Robot;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;

public class RobotHardware
{
    //The hardware map.
    HardwareMap hwMap;

    //The hardware components.
    public DcMotor topLeftMotor;
    public DcMotor topRightMotor;
    public DcMotor bottomLeftMotor;
    public DcMotor bottomRightMotor;

    //Constructor.
    public RobotHardware() {}

    //Hardware and components initiates.
    public void init(HardwareMap ahwMap)
    {
        hwMap = ahwMap;

        topLeftMotor = hwMap.get(DcMotor.class, "topLeftMotor");
        topRightMotor = hwMap.get(DcMotor.class, "topRightMotor");
        bottomLeftMotor = hwMap.get(DcMotor.class, "bottomLeftMotor");
        bottomRightMotor = hwMap.get(DcMotor.class, "bottomRightMotor");

        topRightMotor.setDirection(DcMotor.Direction.REVERSE);
        bottomRightMotor.setDirection(DcMotor.Direction.REVERSE);
    }
}