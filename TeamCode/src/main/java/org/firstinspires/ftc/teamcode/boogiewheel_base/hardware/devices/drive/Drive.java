package org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.devices.drive;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.framework.userHardware.inputs.sensors.IMU;
import org.firstinspires.ftc.teamcode.framework.userHardware.outputs.SlewDcMotor;

public class Drive {

    private SlewDcMotor leftMotor, rightMotor;
    private IMU imu;
    private Servo servoMarker;

    public Drive(HardwareMap hardwareMap) {

        //Motors
        leftMotor = new SlewDcMotor(hardwareMap.dcMotor.get("left"));
        rightMotor = new SlewDcMotor(hardwareMap.dcMotor.get("right"));

        //Motor Set Up
        leftMotor.setDirection(DcMotor.Direction.FORWARD);
        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        leftMotor.setSlewSpeed(1);
        rightMotor.setSlewSpeed(1);

        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); //can be.FLOAT if required
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Encoders
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftMotor.setPower(0);
        rightMotor.setPower(0);

        /*
        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        */

        imu = new IMU(hardwareMap);

        servoMarker = hardwareMap.servo.get("servo_marker");
        servoMarker.setPosition(0);

    }

    public void setSlewSpeed(double ss) {
        leftMotor.setSlewSpeed(ss);
        rightMotor.setSlewSpeed(ss);
    }

    public void setPower(double l, double r) {
        leftMotor.setPower(l);
        rightMotor.setPower(r);
    }

    public void resetAngleToZero() {
        imu.resetAngleToZero();

    }

    public void setTargetPosition(int position) {
        leftMotor.setTargetPosition(position);
        rightMotor.setTargetPosition(position);
    }

    public void setTargetPosition(int leftPosition, int rightPosition) {
        leftMotor.setTargetPosition(leftPosition);
        rightMotor.setTargetPosition(rightPosition);
    }

    public void setMode(DcMotor.RunMode mode) {
        leftMotor.setMode(mode);
        rightMotor.setMode(mode);
    }

    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior behavior) {
        leftMotor.setZeroPowerBehavior(behavior);
        rightMotor.setZeroPowerBehavior(behavior);
    }

    public void setPosisionP(double p) {
        //leftMotor.setPositionPIDFCoefficients(p);
        //rightMotor.setPositionPIDFCoefficients(p);
    }

    public int getLeftPosition() {
        return leftMotor.getCurrentPosition();
    }

    public int getRightPosition() {
        return rightMotor.getCurrentPosition();
    }

    public double getLeftPower() {
        return leftMotor.getPower();
    }

    public double getRightPower() {
        return rightMotor.getPower();
    }

    public double getHeading() {
        return imu.getHeading();
    }

    public boolean isBusy() {
        return leftMotor.isBusy() || rightMotor.isBusy();
    }

    public void setPositionP(double p) {
        leftMotor.setPositionPIDFCoefficients(p);
        rightMotor.setPositionPIDFCoefficients(p);
    }

    public boolean isGyroCalibrated() {
        return imu.isGyroCalibrated();
    }

    public void stop() {
        //Stops Update Threads
        leftMotor.stop();
        rightMotor.stop();
    }

    public void setMarkerServo(double servoPosition) {
        servoMarker.setPosition(servoPosition);
    }


}
