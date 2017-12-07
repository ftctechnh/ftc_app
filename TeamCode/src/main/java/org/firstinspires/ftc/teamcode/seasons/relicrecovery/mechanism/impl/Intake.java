package org.firstinspires.ftc.teamcode.seasons.relicrecovery.mechanism.impl;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.mechanism.IMechanism;

/**
 * This class represents the glyph intake system on the robot.
 */

public class Intake implements IMechanism {
    private Servo leftArmServo, rightArmServo;
    private CRServo leftWheelServo, rightWheelServo;

    /**
     * Construct a new {@link Intake} with a reference to the utilizing robot.
     *
     * @param robot the robot using this intake
     */
    public Intake(Robot robot) {
        HardwareMap hwMap = robot.getCurrentOpMode().hardwareMap;

        this.leftArmServo = hwMap.servo.get("al");
        this.rightArmServo = hwMap.servo.get("ar");

        this.leftWheelServo = hwMap.crservo.get("wl");
        this.rightWheelServo = hwMap.crservo.get("wr");
    }

    /**
     * Lower the intake servo motors.
     */
    public void lowerIntake() {
        leftArmServo.setPosition(0.05);
        rightArmServo.setPosition(0.9);
    }

    /**
     * Raise the intake servo motors.
     */
    public void raiseIntake() {
        leftArmServo.setPosition(0.8);
        rightArmServo.setPosition(0.2);
    }

    /**
     * Set the power of the intake wheels.
     *
     * @param power any value in the range of 1.0 to -1.0.
     *              Negative values run the intake in reverse.
     */
    public void setIntakePower(double power) {
        leftWheelServo.setPower(power);

        // this servo needs to run in the opposite direction:
        rightWheelServo.setPower(-power);
    }
}
