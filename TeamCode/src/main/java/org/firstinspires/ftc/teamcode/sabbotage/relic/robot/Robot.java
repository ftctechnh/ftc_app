package org.firstinspires.ftc.teamcode.sabbotage.relic.robot;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

//   TODO consider using the robot for single loopCounter and delayUntilLoopCount
// and stillWaiting.

public class Robot {

    private HardwareMap hardwareMap;
    public int HARDWARE_DELAY = 30;

    public int loopCounter;
    private int delayUntilLoopCount = 0;


    public Telemetry telemetry;

    public DcMotor motorDriveRight;
    public DcMotor motorDriveLeft;

    public Servo servoRightPaddle;
    public Servo servoLeftPaddle;

    private RelicRecoveryVuMark vuMark = null;

    public Robot(HardwareMap hardwareMap, Telemetry telemetry) {

        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;

        if (this.hardwareMap == null) {

            Log.i("ROBOT", "OUCH!!!! hardwareMap is null");

        }


        this.motorDriveLeft = this.hardwareMap.dcMotor.get("motorDriveLeft");
        this.motorDriveRight = this.hardwareMap.dcMotor.get("motorDriveRight");

        this.servoRightPaddle = hardwareMap.servo.get("servoRightPaddle");
        this.servoLeftPaddle = hardwareMap.servo.get("servoLeftPaddle");

        resetHardwarePositions();
    }

    public HardwareMap getHardwareMap() {
        return hardwareMap;
    }

    public void resetDriveMotors() {

        setDriveMotorForwardDirection();
        runWithoutEncoders();
    }


    public void resetEncodersAndStopMotors() {

        Log.w("ROBOT", "resetEncodersAndStopMotors..." + loopCounter);
        this.motorDriveLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.motorDriveRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


    }


    private void resetHardwarePositions() {

        resetDriveMotors();
        resetServos();

    }


    public void resetServos() {

        this.servoLeftPaddle.setDirection(Servo.Direction.REVERSE);
        this.servoRightPaddle.setDirection(Servo.Direction.REVERSE);

        this.servoLeftPaddle.setPosition(0.0);
        this.servoRightPaddle.setPosition(0.0);
    }

    public void setDriveMotorForwardDirection() {

        this.motorDriveLeft.setDirection(DcMotor.Direction.REVERSE);
        this.motorDriveRight.setDirection(DcMotor.Direction.FORWARD);


    }

    public void setDriveMotorReverseDirection() {

        this.motorDriveLeft.setDirection(DcMotor.Direction.FORWARD);
        this.motorDriveRight.setDirection(DcMotor.Direction.REVERSE);


    }

    public void runWithoutEncoders() {

        this.motorDriveRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.motorDriveLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }


    public void runWithEncoders_MAINTAINS_SPEED() {

        this.motorDriveRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.motorDriveLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

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

    public void setVuMark(RelicRecoveryVuMark vuMark) {
        this.vuMark = vuMark;
    }

    public RelicRecoveryVuMark getVuMark() {
        return this.vuMark;
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


