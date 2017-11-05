package org.firstinspires.ftc.teamcode.opmodes.hardware;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.libraries.AutoLib;

/**
 * Created by Noah on 10/27/2017.
 * All teh hardwares
 */

public class BotHardware {
    //names
    private static final String MOTOR_NAMES[] = {"fr", "br", "fl", "bl"};
    //motors, in order of front right, back right, front left, etc.
    DcMotorEx[] motorRay = new DcMotorEx[4];
    //enums to make everything purty
    public enum Motor {
        frontRight("fr", true),
        backRight("br", false),
        frontLeft("fl", false),
        backLeft("bl", true),
        lift("l", false),
        lights("green", false),
        stick("s", true);

        private final String name;
        private final boolean reverse;
        public DcMotorEx motor;
        Motor(String name, boolean reverse) {
            this.name = name;
            this.reverse = reverse;
        }

        void initMotor(OpMode mode){
            this.motor = mode.hardwareMap.get(DcMotorEx.class, this.name);
            //set run mode
            //this.motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            //config
            if(this.reverse) this.motor.setDirection(DcMotorSimple.Direction.REVERSE);
        }
    }

    public enum ServoE {
        leftGrab("lg"),
        rightGrab("rg");

        public static final double rightGrabOpen = 0.75;
        public static final double rightGrabClose = 0.95;

        public static final double leftGrabOpen = 0;
        public static final double leftGrabClose = 1.0;

        private final String name;
        public Servo servo;
        ServoE(String name) {
            this.name = name;
        }

        void initServo(OpMode mode) {
            this.servo = mode.hardwareMap.get(Servo.class, this.name);
        }
    }

    //opmode pointer
    private final OpMode mode;

    public BotHardware(OpMode mode) {
        this.mode = mode;
    }

    //initialize hardware
    public void init() {
        //init all motors
        for (int i = 0; i < Motor.values().length; i++) Motor.values()[i].initMotor(this.mode);
        //init all servos
        for (int i = 0; i < ServoE.values().length; i++) ServoE.values()[i].initServo(this.mode);
    }

    public void setLeftDrive(double power) {
        Motor.backLeft.motor.setPower(power);
        Motor.frontLeft.motor.setPower(power);
    }

    public void setRightDrive(double power) {
        Motor.backRight.motor.setPower(power);
        Motor.frontRight.motor.setPower(power);
    }

    public void setLift(double power) {
        Motor.lift.motor.setPower(power);
    }

    public void stopAll() {
        for(Motor motor : Motor.values()) motor.motor.setPower(0);
    }

    public void openGrab() {
        ServoE.leftGrab.servo.setPosition(ServoE.leftGrabOpen);
        //ServoE.rightGrab.servo.setPosition(ServoE.rightGrabOpen);
    }

    public void closeGrab() {
        ServoE.leftGrab.servo.setPosition(ServoE.leftGrabClose);
        //ServoE.rightGrab.servo.setPosition(ServoE.rightGrabClose);
    }

    public void setWhack(float power) {
        Motor.stick.motor.setPower(power);
    }

    public void setLights(boolean on) {
        if(on) Motor.lights.motor.setPower(1.0);
        else Motor.lights.motor.setPower(0.0);
    }

    public Servo getLeftLift() {
        return ServoE.leftGrab.servo;
    }

    public Servo getRightLift() {
        return ServoE.rightGrab.servo;
    }

    public DcMotorEx getMotor(String name) {
        return Motor.valueOf(name).motor;
    }

    public DcMotorEx[] getMotorRay() {
        return new DcMotorEx[] { Motor.frontRight.motor, Motor.backRight.motor, Motor.frontLeft.motor, Motor.backLeft.motor };
    }
}
