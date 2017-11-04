package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

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
        driveRightMotor.setDirection(DcMotor.Direction.REVERSE);

        armElevationMotor = hardwareMap.dcMotor.get("armElevationMotor");

        clawLeftServo = hardwareMap.servo.get("clawLeftServo");
        clawRightServo = hardwareMap.servo.get("clawRightServo");
        clawRightServo.setDirection(Servo.Direction.REVERSE);

        this.Reset();
    }

    public void Reset(){
        driveLeftMotor.setPower(0);
        driveRightMotor.setPower(0);

        clawRightServo.setPosition(0.5);
        clawLeftServo.setPosition(0.5);
    }

    public void DriveRobot(double driveLeftPower, double driveRightPower){
        driveLeftMotor.setPower(driveLeftPower);
        driveRightMotor.setPower(driveRightPower);
    }

    public void RotateArm(double armPower){
        double regulator = 0.10;
        armElevationMotor.setPower(armPower * regulator);
    }

    public void ActuateClaw(boolean open){
        double position;
        double delta = 0.005;

        if (open == true){
            position = clawLeftServo.getPosition() + delta;
        }
        else {
            position = clawLeftServo.getPosition() - delta;
        }
        position = Range.clip(position, 0.0, 1.0);

        clawLeftServo.setPosition(position);
        clawRightServo.setPosition(position);
    }

}
