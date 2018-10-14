package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.lynx.LynxI2cColorRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Eva on 9/16/18.
 */

@Autonomous
public abstract class MainAutonomous extends LinearOpMode {

    protected DcMotor driveTrainMotorLeft;
    protected DcMotor driveTrainMotorRight;

    protected DcMotor loweringMotor;

    // Servos
    protected Servo colorServo;

    // Color sensor
    protected LynxI2cColorRangeSensor color0;

    protected void initOpMode() {
    }

    protected void lower() {
        //write code for lowering and
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
        // Change 1440 appropriately if you are not using
        // Modern Robotics encoders
        driveTrainMotorLeft.setTargetPosition((int) (degrees * 12.8));
        driveTrainMotorRight.setTargetPosition((int) (degrees * 12.8));
        driveTrainMotorLeft.setPower(0.35);
        driveTrainMotorRight.setPower(0.35);
        while (driveTrainMotorLeft.isBusy() || driveTrainMotorRight.isBusy()) ;
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

        driveTrainMotorLeft.setDirection(DcMotor.Direction.FORWARD);
        driveTrainMotorRight.setDirection(DcMotor.Direction.FORWARD);
        driveTrainMotorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveTrainMotorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveTrainMotorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveTrainMotorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //  If you are using AndyMark or REV motors, replace
        // 1440 with
        //     AndyMark NeveRest 40: 1120
        //     REV Hex Motor: 2240
        driveTrainMotorLeft.setTargetPosition((int) (inches * -88));
        driveTrainMotorRight.setTargetPosition((int) (inches * 88));
        // the maximum speed of the motors.
        driveTrainMotorLeft.setPower(0.2);
        driveTrainMotorRight.setPower(0.2);
        // Loop until both motors are no longer busy.
        while (driveTrainMotorLeft.isBusy() || driveTrainMotorRight.isBusy()) ;
        driveTrainMotorLeft.setPower(0);
        driveTrainMotorRight.setPower(0);
    }

}