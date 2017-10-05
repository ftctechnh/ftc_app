package edu.usrobotics.opmode.mecanumbot;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.Servo;

import edu.usrobotics.opmode.BaseHardware;


/**
 * Created by mborsch19 & dsiegler19 on 10/13/16.
 */
public class MecanumBotHardware extends BaseHardware {

    public DcMotor frontRight;
    public DcMotor frontLeft;
    public DcMotor backRight;
    public DcMotor backLeft;

    public Servo ballKnocker;

    public boolean frCorrectDirection = false;
    public boolean flCorrectDirection = true;
    public boolean brCorrectDirection = false;
    public boolean blCorrectDirection = true;

    public float wheelDiameter = 4.0f;
    public float wheelRadius = wheelDiameter / 2f;
    public float wheelCircumference = 2f * (float)(Math.PI) * wheelRadius;

    public enum MovementDirection {
        NORTH,
        SOUTH,
        EAST,
        WEST,
        NORTH_EAST,
        NORTH_WEST,
        SOUTH_EAST,
        SOUTH_WEST,
        TURN_RIGHT,
        TURN_LEFT
    }

    @Override
    public void getDevices() {

        frontRight = hardwareMap.dcMotor.get ("fr");
        frontLeft = hardwareMap.dcMotor.get ("fl");
        backRight = hardwareMap.dcMotor.get ("br");
        backLeft = hardwareMap.dcMotor.get ("bl");

        ballKnocker = hardwareMap.servo.get("bk");

    }

    public int inchesToEncoderTicks(float inches){

        return (int) (inches / wheelCircumference * 360f * 3.12);

    }

    public int inchesStraifingToEncoderTicks(float inches){

        return (int) (inches / wheelCircumference * 360f * 4.16);

    }

    public int degreesToEncoderTicks(float degrees){

        return (int) (degrees * 24f);

    }

    public void setDirection (MovementDirection direction) {
        switch (direction) {
            case NORTH:
                frontRight.setDirection(getMotorDirection(DcMotorSimple.Direction.FORWARD, frCorrectDirection));
                frontLeft.setDirection(getMotorDirection(DcMotorSimple.Direction.FORWARD, flCorrectDirection));
                backRight.setDirection(getMotorDirection(DcMotorSimple.Direction.FORWARD, brCorrectDirection));
                backLeft.setDirection(getMotorDirection(DcMotorSimple.Direction.FORWARD, blCorrectDirection));
                break;

            case SOUTH:
                frontRight.setDirection(getMotorDirection(DcMotorSimple.Direction.REVERSE, frCorrectDirection));
                frontLeft.setDirection(getMotorDirection(DcMotorSimple.Direction.REVERSE, flCorrectDirection));
                backRight.setDirection(getMotorDirection(DcMotorSimple.Direction.REVERSE, brCorrectDirection));
                backLeft.setDirection(getMotorDirection(DcMotorSimple.Direction.REVERSE, blCorrectDirection));
                break;

            case EAST:
                frontRight.setDirection(getMotorDirection(DcMotorSimple.Direction.REVERSE, frCorrectDirection));
                frontLeft.setDirection(getMotorDirection(DcMotorSimple.Direction.FORWARD, flCorrectDirection));
                backRight.setDirection(getMotorDirection(DcMotorSimple.Direction.FORWARD, brCorrectDirection));
                backLeft.setDirection(getMotorDirection(DcMotorSimple.Direction.REVERSE, blCorrectDirection));
                break;

            case WEST:
                frontRight.setDirection(getMotorDirection(DcMotorSimple.Direction.FORWARD, frCorrectDirection));
                frontLeft.setDirection(getMotorDirection(DcMotorSimple.Direction.REVERSE, flCorrectDirection));
                backRight.setDirection(getMotorDirection(DcMotorSimple.Direction.REVERSE, brCorrectDirection));
                backLeft.setDirection(getMotorDirection(DcMotorSimple.Direction.FORWARD, blCorrectDirection));
                break;

            case NORTH_EAST:
                frontRight.setDirection(getMotorDirection(DcMotorSimple.Direction.REVERSE, frCorrectDirection));
                frontLeft.setDirection(getMotorDirection(DcMotorSimple.Direction.FORWARD, flCorrectDirection));
                backRight.setDirection(getMotorDirection(DcMotorSimple.Direction.FORWARD, brCorrectDirection));
                backLeft.setDirection(getMotorDirection(DcMotorSimple.Direction.REVERSE, blCorrectDirection));
                break;

            case NORTH_WEST:
                frontRight.setDirection(getMotorDirection(DcMotorSimple.Direction.FORWARD, frCorrectDirection));
                frontLeft.setDirection(getMotorDirection(DcMotorSimple.Direction.REVERSE, flCorrectDirection));
                backRight.setDirection(getMotorDirection(DcMotorSimple.Direction.REVERSE, brCorrectDirection));
                backLeft.setDirection(getMotorDirection(DcMotorSimple.Direction.FORWARD, blCorrectDirection));
                break;

            case SOUTH_EAST:
                frontRight.setDirection(getMotorDirection(DcMotorSimple.Direction.REVERSE, frCorrectDirection));
                frontLeft.setDirection(getMotorDirection(DcMotorSimple.Direction.FORWARD, flCorrectDirection));
                backRight.setDirection(getMotorDirection(DcMotorSimple.Direction.FORWARD, brCorrectDirection));
                backLeft.setDirection(getMotorDirection(DcMotorSimple.Direction.REVERSE, blCorrectDirection));
                break;

            case SOUTH_WEST:
                frontRight.setDirection(getMotorDirection(DcMotorSimple.Direction.FORWARD, frCorrectDirection));
                frontLeft.setDirection(getMotorDirection(DcMotorSimple.Direction.REVERSE, flCorrectDirection));
                backRight.setDirection(getMotorDirection(DcMotorSimple.Direction.REVERSE, brCorrectDirection));
                backLeft.setDirection(getMotorDirection(DcMotorSimple.Direction.FORWARD, blCorrectDirection));
                break;

            case TURN_RIGHT:
                frontRight.setDirection(getMotorDirection(DcMotorSimple.Direction.REVERSE, frCorrectDirection));
                frontLeft.setDirection(getMotorDirection(DcMotorSimple.Direction.FORWARD, flCorrectDirection));
                backRight.setDirection(getMotorDirection(DcMotorSimple.Direction.REVERSE, brCorrectDirection));
                backLeft.setDirection(getMotorDirection(DcMotorSimple.Direction.FORWARD, blCorrectDirection));
                break;

            case TURN_LEFT:
                frontRight.setDirection(getMotorDirection(DcMotorSimple.Direction.FORWARD, frCorrectDirection));
                frontLeft.setDirection(getMotorDirection(DcMotorSimple.Direction.REVERSE, flCorrectDirection));
                backRight.setDirection(getMotorDirection(DcMotorSimple.Direction.FORWARD, brCorrectDirection));
                backLeft.setDirection(getMotorDirection(DcMotorSimple.Direction.REVERSE, blCorrectDirection));
                break;

        }

    }

    public DcMotorSimple.Direction getMotorDirection(DcMotorSimple.Direction regular, boolean correctDirection) {

        return (correctDirection ? regular : (regular.equals(DcMotorSimple.Direction.REVERSE) ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE));

    }

    public boolean sensingWhite(ColorSensor sensor){

        return sensor.red() >= 2 && sensor.green() >= 2 && sensor.blue() >= 2;

    }

}
