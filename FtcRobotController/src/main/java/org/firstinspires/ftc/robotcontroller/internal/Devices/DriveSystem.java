// Created by Adam Sweiger on 10/22/16

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class DriveSystem
{
    private DcMotor[] leftMotors;
    private DcMotor[] rightMotors;

    public DriveSystem(HardwareMap hardwareMap)
    {
        leftMotors = new DcMotor[2];
        leftMotors[1] = hardwareMap.dcMotor.get("frontLeftDriveMotor");
        leftMotors[2] = hardwareMap.dcMotor.get("backLeftDriveMotor");
        rightMotors = new DcMotor[2];
        rightMotors[1] = hardwareMap.dcMotor.get("frontRightDriveMotor");
        rightMotors[2] = hardwareMap.dcMotor.get("backRightDriveMotor");
    }

    // Sets power of the two left motors
    public synchronized void setLeft(int power)
    {
        double convertedPower = ((double)power)/100.0;
        // for each motor in leftMotors
        for (DcMotor motor : leftMotors) {
            // Set the motor power to power
            motor.setPower(convertedPower);
        }
    }

    // Sets power of the two right motors
    public synchronized void setRight(int power)
    {
        double convertedPower = ((double)power)/100.0;
        // for each motor in RightMotors
        for (DcMotor motor : rightMotors) {
            // Set the motor power to power
            motor.setPower(convertedPower);
        }
    }
    public synchronized void setPower(int leftPower, int rightPower)
    {

    }
}