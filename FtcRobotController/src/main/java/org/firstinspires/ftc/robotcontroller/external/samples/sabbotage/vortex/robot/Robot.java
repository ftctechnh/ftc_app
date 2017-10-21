package org.firstinspires.ftc.robotcontroller.external.samples.sabbotage.vortex.robot;

import android.util.Log;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

//   TODO consider using the robot for single loopCounter and delayUntilLoopCount
// and stillWaiting.

public class Robot {

    private HardwareMap hardwareMap;
    public int HARDWARE_DELAY = 30;

    public int loopCounter;
    private int delayUntilLoopCount = 0;


    public Telemetry telemetry;

    public DcMotor motorRightFront;
    public DcMotor motorRightRear;
    public DcMotor motorLeftFront;
    public DcMotor motorLeftRear;

    public DcMotor motorShoot;

    public Servo servoRightButtonA;
    public Servo servoLeftButtonA;
    public Servo servoRightButtonB;
    public Servo servoLeftButtonB;

    public Servo servoShoot;


    public ModernRoboticsI2cGyro gyroSensor;
    public ColorSensor colorSensorFloor;
    public ColorSensor colorSensorBeacon;
    public TouchSensor touchSensor;


    public Robot(HardwareMap hardwareMap, Telemetry telemetry) {

        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;

        if (this.hardwareMap == null) {

            Log.i("ROBOT", "OUCH!!!! hardwareMap is null");

        }


        this.motorLeftFront = this.hardwareMap.dcMotor.get("motorLeftFront");
        this.motorLeftRear = this.hardwareMap.dcMotor.get("motorLeftRear");
        this.motorRightFront = this.hardwareMap.dcMotor.get("motorRightFront");
        this.motorRightRear = this.hardwareMap.dcMotor.get("motorRightRear");

        this.motorShoot = this.hardwareMap.dcMotor.get("motorShoot");

        this.gyroSensor = (ModernRoboticsI2cGyro) this.hardwareMap.gyroSensor.get("gyroSensor");
        this.colorSensorFloor = hardwareMap.colorSensor.get("colorSensorFloor");
        this.colorSensorFloor.enableLed(true);

        this.colorSensorBeacon = hardwareMap.colorSensor.get("colorSensorBeacon");

        this.touchSensor = hardwareMap.touchSensor.get("touchSensor");


        this.servoRightButtonA = hardwareMap.servo.get("servoRightButtonA");
        this.servoRightButtonB = hardwareMap.servo.get("servoRightButtonB");

        this.servoLeftButtonA = hardwareMap.servo.get("servoLeftButtonA");
        this.servoLeftButtonB = hardwareMap.servo.get("servoLeftButtonB");

        this.servoShoot = hardwareMap.servo.get("servoShoot");

        resetHardwarePositions();
    }


    public void resetDriveMotors() {

        setDriveMotorForwardDirection();
        runWithoutEncoders();
    }


    public void resetEncodersAndStopMotors() {

        Log.w("ROBOT", "resetEncodersAndStopMotors..." + loopCounter);
        this.motorLeftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.motorLeftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.motorRightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.motorRightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


    }


    private void resetHardwarePositions() {

        resetDriveMotors();
        resetServos();

        this.motorShoot.setDirection(DcMotor.Direction.FORWARD);
    }


    public void resetServos() {

        this.servoRightButtonA.setDirection(Servo.Direction.REVERSE);
        this.servoRightButtonB.setDirection(Servo.Direction.REVERSE);

        this.servoLeftButtonA.setDirection(Servo.Direction.REVERSE);
        this.servoLeftButtonB.setDirection(Servo.Direction.REVERSE);

        this.servoShoot.setDirection(Servo.Direction.FORWARD);

        this.servoRightButtonA.setPosition(0.0);
        this.servoRightButtonB.setPosition(0.0);
        this.servoLeftButtonA.setPosition(0.0);
        this.servoLeftButtonB.setPosition(0.0);

        this.servoShoot.setPosition(0.0);
    }

    public void setDriveMotorForwardDirection() {

        this.motorLeftFront.setDirection(DcMotor.Direction.REVERSE);
        this.motorLeftRear.setDirection(DcMotor.Direction.REVERSE);
        this.motorRightFront.setDirection(DcMotor.Direction.FORWARD);
        this.motorRightRear.setDirection(DcMotor.Direction.FORWARD);


    }

    public void setDriveMotorReverseDirection() {

        this.motorLeftFront.setDirection(DcMotor.Direction.FORWARD);
        this.motorLeftRear.setDirection(DcMotor.Direction.FORWARD);
        this.motorRightFront.setDirection(DcMotor.Direction.REVERSE);
        this.motorRightRear.setDirection(DcMotor.Direction.REVERSE);


    }

    public void runWithoutEncoders() {

        this.motorRightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.motorRightRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.motorLeftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.motorLeftRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }


    public void runWithEncoders_MAINTAINS_SPEED() {

        this.motorRightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.motorRightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.motorLeftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.motorLeftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    public boolean isStillWaiting() {

        if (delayUntilLoopCount > loopCounter) {
            Log.i("ROBOT", "Waiting..." + loopCounter);
            return true;
        }
        return false;
    }

    public void setLoopDelay() {

        this.delayUntilLoopCount = loopCounter + HARDWARE_DELAY;
    }

    public static enum ColorEnum {

        RED,

        BLUE,

        WHITE


    }

    public enum TurnEnum {

        RIGHT,

        LEFT
    }


    public enum TeamEnum {
        RED,

        BLUE
    }

    public enum StrafeEnum {

        RIGHT,

        LEFT
    }


    public enum DirectionEnum {

        FORWARD,

        REVERSE
    }


    public enum MotorPowerEnum {

        LowLow(0.1),

        Low(0.2),

        Med(0.4),

        High(0.6),

        FTL(0.8);

        private double motorPower;

        private MotorPowerEnum(double motorPower) {
            this.motorPower = motorPower;

        }

        public double getValue() {
            return this.motorPower;

        }

    }


}


