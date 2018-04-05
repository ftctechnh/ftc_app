package org.firstinspires.ftc.teamcode.libraries.hardware;

import android.graphics.Path;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.sun.source.tree.ContinueTree;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.SensorLib;
import org.firstinspires.ftc.teamcode.libraries.StupidMotorLib;
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
        lift("l", true),
        suckLeft("sl", false),
        suckRight("sr", true),
        relic("relic", true);

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
                this.motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
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
        backDropLeft("bdl", true),
        backDropRight("bdr"),
        arm("arm", true),
        grab("grab");

        public static final double stickUp = 0.4;
        public static final double stickDown = .94;
        public static final double stickDetect = 0.9;
        public static final double stick90 = 0.44;
        public static final double stick0 = 0.95;

        public static final double stickBaseCenterBlue = 0.53;
        public static final double stickBaseCenterRed = 0.55;
        public static final double stickBaseCenter = 0.56;
        public static final double stickBaseSwingSize = 0.2;
        public static final double stickBaseHidden = 0.95;

        public static final double backDropDown = 0.06;
        public static final double backDropUp = 0.72;

        public static final double frontDropUp = 0.7;
        public static final double frontDropDown = 0.2;

        public static final double grabClosed = 0;
        public static final double grabOpen = 0.5;

        public static final double armClosed = 0;
        public static final double armOpen = 1;

        private final String name;
        public Servo servo;
        private boolean reversed;
        ServoE(String name, boolean reversed) {
            this.reversed = reversed;
            this.name = name;
        }

        ServoE(String name) {
            this(name, false);
        }

        void initServo(OpMode mode) {
            try{
                this.servo = mode.hardwareMap.get(Servo.class, this.name);
                if(this.reversed) this.servo.setDirection(Servo.Direction.REVERSE);
            }
            catch (Exception e) {
                mode.telemetry.addData(this.name, "Failed to find");
            }
        }
    }


    //opmode pointer
    private final OpMode mode;
    //motor wrap array pointer
    private DcMotorWrap[] shimRay;
    private StupidMotorLib[] liftMotors;

    //IMU pointer
    private BNO055IMU imu;
    private IMUHeading heading;

    //private BlinkyGlowLib blink;

    public BotHardware(OpMode mode) {
        this.mode = mode;
    }

    //initialize hardware
    public void init() {
        //init all motors
        for (int i = 0; i < Motor.values().length; i++) Motor.values()[i].initMotor(this.mode);
        Motor.lift.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //init motor shim array
        shimRay = new DcMotorWrap[] { new DcMotorWrap(Motor.frontRight.motor), new DcMotorWrap(Motor.backRight.motor), new DcMotorWrap(Motor.frontLeft.motor), new DcMotorWrap(Motor.backLeft.motor) };
        //liftMotors = new StupidMotorLib[] { new StupidMotorLib(Motor.liftLeft.motor, 0.5f), new StupidMotorLib(Motor.liftRight.motor, 0.5f) };
        //blink = new BlinkyGlowLib(Motor.green.motor, 0.5f);
        //init all servos
        for (int i = 0; i < ServoE.values().length; i++) ServoE.values()[i].initServo(this.mode);
        //for(ContiniuosServoE s : ContiniuosServoE.values()) s.initServo(this.mode);
        //init IMU
        BNO055IMU.Parameters par = new BNO055IMU.Parameters();
        par.mode = BNO055IMU.SensorMode.IMU;
        par.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu = mode.hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(par);
        heading = new IMUHeading(imu);
    }

    public void start() {
        ServoE.stickBase.servo.setPosition(ServoE.stickBaseHidden);
        ServoE.stick.servo.setPosition(ServoE.stickUp);
        setDropPos(ServoE.frontDropUp);
        ServoE.arm.servo.setPosition(ServoE.armOpen);
        ServoE.grab.servo.setPosition(ServoE.grabOpen);
    }

    public void setLeftDrive(double power) {
        Motor.backLeft.motor.setPower(power);
        Motor.frontLeft.motor.setPower(power);
    }

    public void setRightDrive(double power) {
        Motor.backRight.motor.setPower(power);
        Motor.frontRight.motor.setPower(power);
    }

    //public void setLift(double power) {
    //    Motor.lift.motor.setPower(power);
    //}

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

    public void dropBack() {
        ServoE.backDropLeft.servo.setPosition(ServoE.backDropDown);
        ServoE.backDropRight.servo.setPosition(ServoE.backDropDown);
    }

    public void raiseBack() {
        ServoE.backDropLeft.servo.setPosition(ServoE.backDropUp);
        ServoE.backDropRight.servo.setPosition(ServoE.backDropUp);
    }

    public void setDropPos(double pos) {
        ServoE.backDropLeft.servo.setPosition(pos);
        ServoE.backDropRight.servo.setPosition(pos);
    }

    public double getDropPos() {
        return ServoE.backDropRight.servo.getPosition();
    }

    public AutoLib.Sequence getReverseDropStep() {
        AutoLib.Sequence lift = new AutoLib.ConcurrentSequence();
        lift.add(new AutoLib.TimedServoStep(ServoE.backDropLeft.servo, ServoE.backDropUp, 0.5, false));
        lift.add(new AutoLib.TimedServoStep(ServoE.backDropRight.servo, ServoE.backDropUp, 0.5, false));
        return lift;
    }

    public AutoLib.Step getDropStep() {
        AutoLib.Sequence drop = new AutoLib.ConcurrentSequence();
        //drop the block
        drop.add(new AutoLib.TimedServoStep(ServoE.backDropLeft.servo, ServoE.backDropDown, 0.7, false));
        drop.add(new AutoLib.TimedServoStep(ServoE.backDropRight.servo, ServoE.backDropDown, 0.7, false));
        return drop;
    }

    public AutoLib.Step getLiftRaiseStep() {
        return new AutoLib.EncoderMotorStep(Motor.lift.motor, 1.0f, 400, true);
    }

    public AutoLib.Step getLiftLowerStep() {
        return new AutoLib.EncoderMotorStep(Motor.lift.motor, -1.0f, 300, true);
    }

    public void setSuckLeft(double power) {
        Motor.suckLeft.motor.setPower(power);
    }

    public void setSuckRight(double power) {
        Motor.suckRight.motor.setPower(power);
    }


    public void setLights(double power) {
        /*Motor.green.motor.setPower(Math.abs(power));*/
    }

    public DcMotorEx getMotor(String name) {
        return Motor.valueOf(name).motor;
    }

    public DcMotorEx[] getMotorRay() {
        return new DcMotorEx[] { Motor.frontRight.motor, Motor.backRight.motor, Motor.frontLeft.motor, Motor.backLeft.motor };
    }

    public DcMotorWrap[] getMotorVelocityShimArray() {
        return shimRay;
    }

    public void setLiftMotors(float power) {
        Motor.lift.motor.setPower(power);
    }

    public void setLiftMotorsDegrees(float deg) {
        Motor.lift.motor.setVelocity(deg, AngleUnit.DEGREES);
    }

    /*
    public boolean loopLEDs() {
        return blink.loop();
    }

    public void setLights(BlinkyEffect effect) {
        blink.setAnimation(effect);
    }
    */

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

    public BNO055IMU getImu() {
        return imu;
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
