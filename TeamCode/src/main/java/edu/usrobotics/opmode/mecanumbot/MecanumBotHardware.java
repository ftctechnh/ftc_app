package edu.usrobotics.opmode.mecanumbot;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import edu.usrobotics.opmode.BaseHardware;


/**
 * Created by mborsch19 & dsiegler19 on 10/13/16.
 */
public class MecanumBotHardware extends BaseHardware {

    public DcMotor frontRight;
    public DcMotor frontLeft;
    public DcMotor backRight;
    public DcMotor backLeft;

    public DcMotor blockLift;

    public Servo gripperRight;
    public Servo gripperLeft;

    public Servo ballKnocker;

    public ColorSensor colorSensor;

    public DigitalChannel topLimitSwitch;
    public DigitalChannel bottomLimitSwitch;

    public boolean frCorrectDirection = true;
    public boolean flCorrectDirection = false;
    public boolean brCorrectDirection = true;
    public boolean blCorrectDirection = false;

    public boolean blockLiftCorrectDirection = false;

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

        blockLift = hardwareMap.dcMotor.get("gripperlift");
        blockLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        gripperLeft = hardwareMap.servo.get("gl");
        gripperRight = hardwareMap.servo.get("gr");

        ballKnocker = hardwareMap.servo.get("bk");

        colorSensor = hardwareMap.colorSensor.get("cs");

        topLimitSwitch = hardwareMap.get(DigitalChannel.class, "tls");
        // bottomLimitSwitch = hardwareMap.get(DigitalChannel.class, "bls");

        topLimitSwitch.setMode(DigitalChannel.Mode.INPUT);
        // bottomLimitSwitch.setMode(DigitalChannel.Mode.INPUT);

        bringUpBallKnocker();

        frontRight.setDirection(frCorrectDirection ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);
        frontLeft.setDirection(flCorrectDirection ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(brCorrectDirection ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(blCorrectDirection ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);

        blockLift.setDirection(blockLiftCorrectDirection ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);

    }

    public void setPower(float p){

        frontRight.setPower(p);
        frontLeft.setPower(p);
        backRight.setPower(p);
        backLeft.setPower(p);

    }

    public void closeGripper(){

        gripperRight.setPosition(0.69f);
        gripperLeft.setPosition(0.58f);

    }

    public void openGripper(){

        gripperRight.setPosition(0.55f);
        gripperLeft.setPosition(0.83f);

    }

    public void bringDownBallKnocker(){

        ballKnocker.setPosition(0.2);

    }

    public void bringUpBallKnocker(){

        ballKnocker.setPosition(0.8);

    }

    public boolean readingRed(){

        return colorSensor.red() > colorSensor.blue();

    }

    public boolean readingBlue(){

        return colorSensor.blue() > colorSensor.red();

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
