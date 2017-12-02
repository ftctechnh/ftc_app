package org.firstinspires.ftc.teamcode.opmodes.hardware;

import android.hardware.Sensor;
import android.support.annotation.NonNull;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorControllerEx;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.configuration.MotorConfigurationType;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.SensorLib;
import org.firstinspires.ftc.teamcode.libraries.interfaces.HeadingSensor;
import org.firstinspires.ftc.teamcode.libraries.interfaces.SetPower;

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
        suckLeft("sl", false),
        suckRight("sr", true),
        lift("l", false);

        private final String name;
        private final boolean reverse;
        public DcMotorEx motor;
        Motor(String name, boolean reverse) {
            this.name = name;
            this.reverse = reverse;
        }

        void initMotor(OpMode mode){
            try{
                this.motor = mode.hardwareMap.get(DcMotorEx.class, this.name);
                //set run mode
                //this.motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                //config
                if(this.reverse) this.motor.setDirection(DcMotorSimple.Direction.REVERSE);
            }
            catch (Exception e) {
                mode.telemetry.addData(this.name, "Failed to find");
            }
        }
    }

    public enum ServoE {
        stick("stick"),
        stickBase("sb"),
        dropLeft("dl"),
        dropRight("dr"),
        backDropLeft("bdl"),
        backDropRight("bdr");

        public static final double leftGrabOpen = 0;
        public static final double leftGrabClose = 1.0;

        public static final double stickUp = 0.2;
        public static final double stickDown = 0.9;

        public static final double stickBaseCenter = 0.5;
        public static final double stickBaseHidden = 0.0;

        public static final double garyUp = 0.1;
        public static final double garyDown = 0.55;

        public static final double dropLeftDown = 0.0;
        public static final double dropLeftUp = 0.0;

        public static final double dropRightDown = 0.0;
        public static final double dropRightUp = 0.0;

        public static final double backDropLeftDown = 0.0;
        public static final double backDropLeftUp = 0.0;

        public static final double backDropRightDown = 0.0;
        public static final double backDropRightUp = 0.0;

        private final String name;
        public Servo servo;
        ServoE(String name) {
            this.name = name;
        }

        void initServo(OpMode mode) {
            try{
                this.servo = mode.hardwareMap.get(Servo.class, this.name);
            }
            catch (Exception e) {
                mode.telemetry.addData(this.name, "Failed to find");
            }
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

    public void setSuck(double power) {
        Motor.suckLeft.motor.setPower(power);
        Motor.suckRight.motor.setPower(power);
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

    public Servo getStick() {
        return ServoE.stick.servo;
    }

    public Servo getStickBase() { return ServoE.stickBase.servo; }

    public void dropFront() {
        ServoE.dropLeft.servo.setPosition(ServoE.dropLeftDown);
        ServoE.dropRight.servo.setPosition(ServoE.dropRightDown);
    }

    public void raiseFront() {
        ServoE.dropLeft.servo.setPosition(ServoE.dropLeftUp);
        ServoE.dropRight.servo.setPosition(ServoE.dropRightUp);
    }

    public void dropBack() {
        ServoE.backDropLeft.servo.setPosition(ServoE.backDropLeftDown);
        ServoE.backDropRight.servo.setPosition(ServoE.backDropRightDown);
    }

    public void raiseBack() {
        ServoE.backDropLeft.servo.setPosition(ServoE.backDropLeftUp);
        ServoE.backDropRight.servo.setPosition(ServoE.backDropRightUp);
    }

    public DcMotorEx getMotor(String name) {
        return Motor.valueOf(name).motor;
    }

    public DcMotorEx[] getMotorRay() {
        return new DcMotorEx[] { Motor.frontRight.motor, Motor.backRight.motor, Motor.frontLeft.motor, Motor.backLeft.motor };
    }

    public DcMotorWrap[] getMotorVelocityShimArray() {
        return new DcMotorWrap[] { new DcMotorWrap(Motor.frontRight.motor), new DcMotorWrap(Motor.backRight.motor), new DcMotorWrap(Motor.frontLeft.motor), new DcMotorWrap(Motor.backLeft.motor) };
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

    private static class DcMotorWrap extends DcMotorImplEx {
        public DcMotorWrap(DcMotorEx motor) {
            super(motor.getController(), motor.getPortNumber(), motor.getDirection(), motor.getMotorType());
        }

        @Override
        public void setPower(double power) {
            super.setVelocity(power, AngleUnit.DEGREES);
        }
    }
}
