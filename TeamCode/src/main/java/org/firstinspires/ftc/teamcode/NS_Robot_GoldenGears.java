package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Nithya on 10/29/2017.
 * This class implements Golden Gears robot.
 * Any OpMode can use this class to control the hardware.
 */

public class NS_Robot_GoldenGears {
    // Hardware Map of robot
    private HardwareMap hardwareMap = null;

    private DcMotor driveLeftMotor = null;
    private DcMotor driveRightMotor = null;
    private DcMotor armElevationMotor = null;
    private Servo clawLeftServo = null;
    private Servo clawRightServo = null;


    public NS_Robot_GoldenGears(HardwareMap hm){
        hardwareMap = hm;

        driveLeftMotor = hardwareMap.dcMotor.get("driveLeftMotor");
        driveRightMotor = hardwareMap.dcMotor.get("driveRightMotor");
        driveLeftMotor.setDirection(DcMotor.Direction.REVERSE);

        armElevationMotor = hardwareMap.dcMotor.get("armElevationMotor");

        clawLeftServo = hardwareMap.servo.get("clawLeftServo");
        clawRightServo = hardwareMap.servo.get("clawRightServo");
        clawRightServo.setDirection(Servo.Direction.REVERSE);

        this.Reset();
    }

    public void Reset(){
        driveLeftMotor.setPower(0);
        driveRightMotor.setPower(0);

        clawRightServo.setPosition(0);
        clawLeftServo.setPosition(0);
    }

    public void DriveRobot(double driveLeftPower, double driveRightPower){
        driveLeftMotor.setPower(driveLeftPower);
        driveRightMotor.setPower(driveRightPower);
    }
}
