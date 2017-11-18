package org.firstinspires.ftc.teamcode.opmodes.hardware;

import android.hardware.Sensor;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.SensorLib;
import org.firstinspires.ftc.teamcode.libraries.interfaces.HeadingSensor;

/**
 * Created by Noah on 10/27/2017.
 * All teh hardwares
 */

public class BotHardware {
    //enums to make everything purty
    public enum Motor {
        frontRight("fr", true),
        backRight("br", true),
        frontLeft("fl", false),
        backLeft("bl", false),
        lift("l", false),
        lights("green", false);

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
        stick("stick"),
        gary("gary");

        public static final double rightGrabOpen = 0.75;
        public static final double rightGrabClose = 0.95;

        public static final double leftGrabOpen = 0;
        public static final double leftGrabClose = 1.0;

        public static final double stickUp = 0;
        public static final double stickDown = 0.95;

        public static final double garyUp = 0.27;
        public static final double garyDown = 0.75;

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

    //IMU pointer
    private BNO055IMU imu;
    private IMUHeading heading;

    public BotHardware(OpMode mode) {
        this.mode = mode;
    }

    //initialize hardware
    public void init() {
        //init all motors
        for (int i = 0; i < Motor.values().length; i++) Motor.values()[i].initMotor(this.mode);
        //init all servos
        for (int i = 0; i < ServoE.values().length; i++) ServoE.values()[i].initServo(this.mode);
        //init IMU
        BNO055IMU.Parameters par = new BNO055IMU.Parameters();
        par.mode = BNO055IMU.SensorMode.IMU;
        par.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu = mode.hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(par);
        heading = new IMUHeading(imu);
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

    public void dropStick() {
        ServoE.stick.servo.setPosition(ServoE.stickDown);
    }

    public void liftStick() {
        ServoE.stick.servo.setPosition(ServoE.stickUp);
    }

    public void dropGary() {
        ServoE.gary.servo.setPosition(ServoE.garyDown);
    }

    public void liftGary() {
        ServoE.gary.servo.setPosition(ServoE.garyUp);
    }

    public Servo getStick() {
        return ServoE.stick.servo;
    }

    public void setLights(boolean on) {
        if(on) Motor.lights.motor.setPower(1.0);
        else Motor.lights.motor.setPower(0.0);
    }

    public DcMotorEx getMotor(String name) {
        return Motor.valueOf(name).motor;
    }

    public DcMotorEx[] getMotorRay() {
        return new DcMotorEx[] { Motor.frontRight.motor, Motor.backRight.motor, Motor.frontLeft.motor, Motor.backLeft.motor };
    }

    private static class IMUHeading implements HeadingSensor {
        private final BNO055IMU imu;

        IMUHeading(BNO055IMU imu){
            this.imu = imu;
        }

        @Override
        public float getHeading() {
            return SensorLib.Utils.wrapAngle(imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle);
        }
    }

    public HeadingSensor getHeadingSensor() {
        return heading;
    }
}
