package org.firstinspires.ftc.teamcode.robotutil;

import android.util.Log;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.internal.android.dex.util.ExceptionWithContext;

//import com.qualcomm.hardware.adafruit.JustLoggingAccelerationIntegrator;

public class DriveTrainNew {
    private ElapsedTime timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    private DcMotor lfDrive,lbDrive,rfDrive,rbDrive;
    private IMUNew imu;
    private LinearOpMode opMode;
    private MotorGroup driveMotors;

    public DriveTrainNew(LinearOpMode opMode) {
        this.opMode = opMode;
        lfDrive = opMode.hardwareMap.dcMotor.get("lfDrive");
        lbDrive = opMode.hardwareMap.dcMotor.get("rbDrive");
        rfDrive = opMode.hardwareMap.dcMotor.get("rfDrive");
        rbDrive = opMode.hardwareMap.dcMotor.get("rbDrive");

        lfDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        lbDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rbDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rfDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        this.imu = new IMUNew("imu",this.opMode);
        DcMotor[] driveMotorArray = new DcMotor[]{lfDrive,rfDrive,lbDrive,rbDrive};
        this.driveMotors = new MotorGroup(driveMotorArray);
        initMotors();
        this.imu.init();
    }



    public void initMotors(){
        driveMotors.useEncoders();
        driveMotors.resetEncoders();
        driveMotors.setBrake();
    }

    public void setPowers(double lf, double rf, double lb,double rb){
        lfDrive.setPower(lf);
        lbDrive.setPower(lb);
        rbDrive.setPower(rb);
        rfDrive.setPower(rf);
    }

    public void move(Direction direction,double power){
        power = Math.abs(power);

        switch(direction){
            case BACK:
                setPowers(-power,-power,
                        -power,-power);
                break;
            case FORWARD:
                setPowers(power,power,
                        power,power);
                break;
            case LEFT:
                setPowers(-power,-power,
                        power,power);
            case RIGHT:
                setPowers(power,power,
                        -power,-power);
            case CW:
                setPowers(power,-power,
                        power,-power);
            case CCW:
                setPowers(-power,power,
                        power,-power);
        }
    }

    public void move(Direction direction,double power, double timeS){
        move(direction,power);
        
    }

    public void move(Direction direction,double power,double inches,double timeoutS) {
        driveMotors.resetEncoders();
        driveMotors.useEncoders();
        int rfTarget,lfTarget,lbTarget,rbTarget;
        switch (direction) {
            case FORWARD:
                rfTarget = rfDrive.getCurrentPosition() + (int) (inches * Values.ticksPerInch);
                lfTarget = lfDrive.getCurrentPosition() + (int) (inches * Values.ticksPerInch);
                lbTarget = lbDrive.getCurrentPosition() + (int) (inches * Values.ticksPerInch);
                rbTarget = rbDrive.getCurrentPosition() + (int) (inches * Values.ticksPerInch);
                break;
            case BACK:
                rfTarget = rfDrive.getCurrentPosition() - (int) (inches * Values.ticksPerInch);
                lfTarget = lfDrive.getCurrentPosition() - (int) (inches * Values.ticksPerInch);
                lbTarget = lbDrive.getCurrentPosition() - (int) (inches * Values.ticksPerInch);
                rbTarget = rbDrive.getCurrentPosition() - (int) (inches * Values.ticksPerInch);
                break;
            case RIGHT:
                rfTarget = rfDrive.getCurrentPosition() + (int) (inches * Values.ticksPerInch);
                lfTarget = lfDrive.getCurrentPosition() + (int) (inches * Values.ticksPerInch);
                lbTarget = lbDrive.getCurrentPosition() - (int) (inches * Values.ticksPerInch);
                rbTarget = rbDrive.getCurrentPosition() - (int) (inches * Values.ticksPerInch);
                break;
            case LEFT:
                rfTarget = rfDrive.getCurrentPosition() - (int) (inches * Values.ticksPerInch);
                lfTarget = lfDrive.getCurrentPosition() - (int) (inches * Values.ticksPerInch);
                lbTarget = lbDrive.getCurrentPosition() + (int) (inches * Values.ticksPerInch);
                rbTarget = rbDrive.getCurrentPosition() + (int) (inches * Values.ticksPerInch);
                break;

            default:
                rfTarget = 0;
                lfTarget = 0;
                lbTarget = 0;
                rbTarget = 0;

        }
        rbDrive.setTargetPosition(rbTarget);
        lbDrive.setTargetPosition(lbTarget);
        lfDrive.setTargetPosition(lfTarget);
        rfDrive.setTargetPosition(rfTarget);

        driveMotors.runToPosition();
        timer.reset();

        move(direction,power);

        while(opMode.opModeIsActive() && (timer.time()< timeoutS*1000) && (rbDrive.isBusy() || lbDrive.isBusy() || lfDrive.isBusy() || rfDrive.isBusy())) {
            if(!rbDrive.isBusy()){
                rbDrive.setPower(0);
            }
            if(!lbDrive.isBusy()){
                lbDrive.setPower(0);
            }
            if(!rfDrive.isBusy()){
                rfDrive.setPower(0);
            }
            if(!lfDrive.isBusy()){
                lfDrive.setPower(0);
            }
        }
        driveMotors.stopAll();
        driveMotors.useEncoders();
    }


    // Untested proportional IMU rotation
    public void rotateIMU(Direction direction, double angle, double power, double allowedError, double timeoutS) {
        double currentHeading = imu.getAngle(Axis.HEADING);
        double targetHeading;

        switch (direction) {
            case CW:
                targetHeading = currentHeading + angle;
                break;
            case CCW:
                targetHeading = currentHeading - angle;
                break;
            default:
                targetHeading = currentHeading;
        }
        targetHeading = fixAngle(targetHeading);

        double error = getError(currentHeading, targetHeading);
        double startTime = System.currentTimeMillis();

        while (error > allowedError && (System.currentTimeMillis() - startTime) / 1000 < timeoutS) {
            double proportionalPower = power * error / 180;
            move(direction,proportionalPower);

            currentHeading = imu.getAngle(Axis.HEADING);
            error = getError(currentHeading, targetHeading);
        }

        driveMotors.stopAll();
    }

    // Get angle between -180 and 180
    private double fixAngle(double angle) {
        while (angle > 180 || angle < -180) {
            angle += (angle < 180) ? 360 : -360;
        }
        return angle;
    }

    private double getError(double currentHeading, double targetHeading) {
        return Math.abs(fixAngle(targetHeading - currentHeading));
    }

}
