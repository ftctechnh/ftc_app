package org.firstinspires.ftc.teamcode.seasons.relicrecovery.mechanism.impl;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.mechanism.IMechanism;
import org.firstinspires.ftc.teamcode.seasons.relicrecovery.RelicRecoveryRobot;

/**
 *
 */

public class RelicArm implements IMechanism {
    private DcMotor arm;

    private Servo gripper;


    public RelicArm(Robot robot) {
        HardwareMap hwMap = robot.getCurrentOpMode().hardwareMap;
        this.arm = hwMap.dcMotor.get("NAME HERE");

        this.gripper = hwMap.servo.get("NAME HERE");
    }

    // TODO get hwMaps and positions for gripper

    public void openGrip() {
        gripper.setPosition(0);
    }

    public void closeGrip() {
        gripper.setPosition(0);
    }

}