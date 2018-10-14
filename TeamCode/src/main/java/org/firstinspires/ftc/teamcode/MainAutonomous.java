package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.lynx.LynxI2cColorRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Eva on 9/16/18.
 */

@Autonomous
public abstract class MainAutonomous extends LinearOpMode {

    // Motors
    protected DcMotor driveTrainMotorLeft;
    protected DcMotor driveTrainMotorRight;
    protected DcMotor jointMotor;
    protected DcMotor liftMotor;

    // Servos
    protected Servo colorServo;

    // Continuous rotation servos
    protected CRServo intakeServo;

    // Color sensor
    protected LynxI2cColorRangeSensor colorSensor;

    // Constants
    protected static final int FORWARD_MULTIPLIER = 88;
    protected static final double MAX_SPEED = 0.35;
    protected static final double TURN_MULTIPLIER = 12.8;
    protected static final int JOINT_EXTENDED = 0; // TODO: Find position
    protected static final int JOINT_FOLDED = 0; // TODO: Find position

    protected void initOpMode() {
        driveTrainMotorLeft = hardwareMap.get(DcMotor.class, "driveTrainMotorLeft");
        driveTrainMotorRight = hardwareMap.get(DcMotor.class, "driveTrainMotorRight");
        jointMotor = hardwareMap.get(DcMotor.class, "jointMotor");
        liftMotor = hardwareMap.get(DcMotor.class, "liftMotor");

        jointMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        jointMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        colorServo = hardwareMap.get(Servo.class, "colorServo");
        intakeServo = hardwareMap.get(CRServo.class, "intakeServo");

        colorSensor = hardwareMap.get(LynxI2cColorRangeSensor.class, "colorSensor");
    }

    protected void jointPosition(String position) {
        if (position == "extended") {
            jointMotor.setTargetPosition(JOINT_EXTENDED); // TODO: Add position
        } else {
            jointMotor.setTargetPosition(JOINT_FOLDED); // TODO: Add position
        }
    }
    protected void intake() {
        intakeServo.setPower(1.0);
    }

    protected void lower() {
        // write code for lowering and
        // raising the robot here
    }

    /**
     *this method makes the robot turn left or right
     * For ex: to turn left you call the method turn(-90)
     * To turn right you call the method turn(90)
     * 
     * @param degrees
     */
    protected void turn(int degrees) {

        driveTrainMotorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveTrainMotorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveTrainMotorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveTrainMotorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Multiply degrees input by a determined constant to tell motor how far to turn
        driveTrainMotorLeft.setTargetPosition((int) (degrees * TURN_MULTIPLIER));
        driveTrainMotorRight.setTargetPosition((int) (degrees * TURN_MULTIPLIER));

        // Maximum speed
        driveTrainMotorLeft.setPower(MAX_SPEED);
        driveTrainMotorRight.setPower(MAX_SPEED);

        // Loop until motors are no longer busy
        while (driveTrainMotorLeft.isBusy() || driveTrainMotorRight.isBusy());

        // Shut off motors
        driveTrainMotorLeft.setPower(0);
        driveTrainMotorRight.setPower(0);

    }


    /**
     * This method helps move forward or backwords.
     * For ex: to move forward 5 inches, you call the method moveInch(5)
     * To move backwards 5 inches, you call the method moveInch(-5)
     *
     * @param inches
     */
    protected void moveInch(int inches) {

        driveTrainMotorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveTrainMotorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveTrainMotorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveTrainMotorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Multiply the distance we require by a determined constant to tell the motors how far to turn
        driveTrainMotorLeft.setTargetPosition((int) (inches * -FORWARD_MULTIPLIER));
        driveTrainMotorRight.setTargetPosition((int) (inches * FORWARD_MULTIPLIER));

        // The maximum speed of the motors.
        driveTrainMotorLeft.setPower(MAX_SPEED);
        driveTrainMotorRight.setPower(MAX_SPEED);
        // Loop until both motors are no longer busy.
        while (driveTrainMotorLeft.isBusy() || driveTrainMotorRight.isBusy()) ;
        driveTrainMotorLeft.setPower(0);
        driveTrainMotorRight.setPower(0);
    }

}