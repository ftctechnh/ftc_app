package org.firstinspires.ftc.teamcode.opmodes.hardware;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

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
        backLeft("bl", true);

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

    //opmode pointer
    private final OpMode mode;

    public BotHardware(OpMode mode) {
        this.mode = mode;
    }

    //initialize hardware
    public void init() {
        //init all motors
        for (int i = 0; i < Motor.values().length; i++) Motor.values()[i].initMotor(this.mode);
    }

    public void setLeftDrive(double power) {
        Motor.backLeft.motor.setPower(power);
        Motor.frontLeft.motor.setPower(power);
    }

    public void setRightDrive(double power) {
        Motor.backRight.motor.setPower(power);
        Motor.frontRight.motor.setPower(power);
    }

    public void stopAll() {
        for(Motor motor : Motor.values()) motor.motor.setPower(0);
    }
}
