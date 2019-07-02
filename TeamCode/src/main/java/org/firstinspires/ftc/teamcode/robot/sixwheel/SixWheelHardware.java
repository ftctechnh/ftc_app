package org.firstinspires.ftc.teamcode.robot.sixwheel;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import java.util.List;
import java.util.*;

public class SixWheelHardware {
    public DcMotorEx driveLeft;
    public DcMotorEx driveRight;
    public DcMotorEx PTOleft;
    public DcMotorEx PTOright;

    public List<DcMotorEx> chassisMotors;
    public List<DcMotorEx> driveMotors;
    public List<DcMotorEx> PTOMotors;
    public List<DcMotorEx> leftChassisMotors;
    public List<DcMotorEx> rightChassisMotors;



    public SixWheelHardware(LinearOpMode opMode) {
        driveLeft = (DcMotorEx) opMode.hardwareMap.dcMotor.get("frontLeft");
        driveRight = (DcMotorEx) opMode.hardwareMap.dcMotor.get("frontRight");
        PTOleft = (DcMotorEx) opMode.hardwareMap.dcMotor.get("backLeft");
        PTOright = (DcMotorEx) opMode.hardwareMap.dcMotor.get("backRight");

        // Reverse right hand motors
        driveRight.setDirection(DcMotor.Direction.REVERSE);
        PTOright.setDirection(DcMotor.Direction.REVERSE);

        // Set up fast access linked lists
        chassisMotors = Arrays.asList(driveLeft, driveRight, PTOleft, PTOright);
        driveMotors = Arrays.asList(driveLeft, driveRight);
        PTOMotors = Arrays.asList(PTOleft, PTOright);
        leftChassisMotors = Arrays.asList(driveLeft, PTOleft);
        rightChassisMotors = Arrays.asList(driveRight, PTOright);
    }
}
