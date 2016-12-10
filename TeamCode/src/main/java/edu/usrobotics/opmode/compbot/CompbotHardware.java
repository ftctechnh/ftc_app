package edu.usrobotics.opmode.compbot;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.concurrent.locks.Lock;

import edu.usrobotics.opmode.BaseHardware;


/**
 * Created by mborsch19 & dsiegler19 on 10/13/16.
 */
public class CompbotHardware extends BaseHardware {

    public DcMotor frontRight;
    public DcMotor frontLeft;
    public DcMotor backRight;
    public DcMotor backLeft;

    public DcMotor harvester;

    public DcMotor lift;

    public DcMotor shooterRight;
    public DcMotor shooterLeft;

    public Servo liftServo;
    public Servo lockServo;

    public ColorSensor buttonPresserColorSensor;
    public ColorSensor leftBottomColorSensor;

    public DeviceInterfaceModule dim;

    public boolean frCorrectDirection = false;
    public boolean flCorrectDirection = true;
    public boolean brCorrectDirection = false;
    public boolean blCorrectDirection = true;

    public boolean harvesterCorrectDirection = false;

    public boolean liftCorrectDirection = true;

    public boolean rightShooterCorrectDirection = true;
    public boolean leftShooterCorrectDirection = false;

    public double liftServoClosePosition = 0f;
    public double liftServoOpenPosition = 0.5f;

    public double lockServoStartPosition;
    public double lockServoMaxPosition;
    public double lockServoDelta = 0.25f;

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

        harvester = hardwareMap.dcMotor.get("harvester");

        harvester.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        lift = hardwareMap.dcMotor.get("lft");

        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        shooterRight = hardwareMap.dcMotor.get("sr");
        shooterLeft = hardwareMap.dcMotor.get("sl");

        shooterRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooterLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        liftServo = hardwareMap.servo.get("ls");

        lockServo = hardwareMap.servo.get("los");

        lockServoStartPosition = lockServo.MIN_POSITION;
        lockServoMaxPosition = lockServo.MAX_POSITION;

        buttonPresserColorSensor = hardwareMap.colorSensor.get("cs");
        buttonPresserColorSensor.enableLed(false);

        dim = hardwareMap.deviceInterfaceModule.get("dim");

        leftBottomColorSensor = hardwareMap.colorSensor.get("lcs");
        leftBottomColorSensor.enableLed(true);

        harvester.setDirection(harvesterCorrectDirection ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);

        shooterRight.setDirection(rightShooterCorrectDirection ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);
        shooterLeft.setDirection(leftShooterCorrectDirection ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);

        lift.setDirection(liftCorrectDirection ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);

        liftServo.setPosition(liftServoClosePosition);

        lockServo.setPosition(lockServoStartPosition);

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

}
