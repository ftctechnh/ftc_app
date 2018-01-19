package org.firstinspires.ftc.teamcode.relicrecoveryv2;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * Created by Pilt on 12/14/17.
 */

public abstract class MeccyAutoMode extends MeccyMode{
    //
    PengwinFin pengwinFin;
    PengwinWing pengwinWing;
    double rotationInches;
    //
    //
    BNO055IMU imu;
    Orientation angles;
    Acceleration gravity;
    //
    //<editor-fold desc="Yay">
    abstract public void runOpMode();
    //
    static final double countify = 116.501;
    //</editor-fold>
    //
    //<editor-fold desc="Extraneous">
    //
    public void startify (){
        pengwinFin = new PengwinFin(hardwareMap);
        pengwinWing = new PengwinWing(hardwareMap);
    }
    //
    public void configureMotors(String leftFront, String rightFront, String leftBack, String rightBack){
        super.configureMotors();
        //
        leftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBackMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBackMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    //
    public void stopAndResetify(){
        leftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //
        leftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBackMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBackMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBackMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    //
    private void setSpeed(double speed){
        leftBackMotor.setPower(speed);
        rightBackMotor.setPower(speed);
        leftFrontMotor.setPower(speed);
        rightFrontMotor.setPower(speed);
    }
    //</editor-fold>
    //
    //<editor-fold desc="Moving">
    public void forwardToPosition(double inches, double speed){
        int move = (int)(Math.round(inches*countify));
        //
        leftBackMotor.setTargetPosition(leftBackMotor.getCurrentPosition() + move);
        leftFrontMotor.setTargetPosition(leftFrontMotor.getCurrentPosition() + move);
        rightBackMotor.setTargetPosition(rightBackMotor.getCurrentPosition() + move);
        rightFrontMotor.setTargetPosition(rightFrontMotor.getCurrentPosition() + move);
        //
        setSpeed(speed);
    }
    //
    //
    public void backToPosition(double inches, double speed){
        int move = (int)(Math.round(inches*countify));
        //
        leftBackMotor.setTargetPosition(leftBackMotor.getCurrentPosition() + move);
        leftFrontMotor.setTargetPosition(leftFrontMotor.getCurrentPosition() + move);
        rightBackMotor.setTargetPosition(rightBackMotor.getCurrentPosition() + move);
        rightFrontMotor.setTargetPosition(rightFrontMotor.getCurrentPosition() + move);
        //
        setSpeed(speed);
    }

    //
    public void rightToPosition(double inches, double speed){
        int move = (int)(Math.round(inches*countify));
        //
        leftBackMotor.setTargetPosition(leftBackMotor.getCurrentPosition() - move);
        leftFrontMotor.setTargetPosition(leftFrontMotor.getCurrentPosition() + move);
        rightBackMotor.setTargetPosition(rightBackMotor.getCurrentPosition() + move);
        rightFrontMotor.setTargetPosition(rightFrontMotor.getCurrentPosition() - move);
        //
        setSpeed(speed);
    }
    //
    public void leftToPosition(double inches, double speed){
        int move = (int)(Math.round(inches*countify));
        //
        leftBackMotor.setTargetPosition(leftBackMotor.getCurrentPosition() + move);
        leftFrontMotor.setTargetPosition(leftFrontMotor.getCurrentPosition() - move);
        rightBackMotor.setTargetPosition(rightBackMotor.getCurrentPosition() - move);
        rightFrontMotor.setTargetPosition(rightFrontMotor.getCurrentPosition() + move);
        //
        setSpeed(speed);
    }
    //</editor-fold>
    //
    //<editor-fold desc="turning">
    public void turnRightToPosition(double inches, double speed){
        int move = (int)(Math.round(inches*countify));
        //
        leftBackMotor.setTargetPosition(leftBackMotor.getCurrentPosition() + move);
        leftFrontMotor.setTargetPosition(leftFrontMotor.getCurrentPosition() + move);
        rightBackMotor.setTargetPosition(rightBackMotor.getCurrentPosition() - move);
        rightFrontMotor.setTargetPosition(rightFrontMotor.getCurrentPosition() - move);
        //
        setSpeed(speed);
    }
    //
    public void turnLeftToPosition (double inches, double speed){
        int move = (int)(Math.round(inches*countify));
        //
        leftBackMotor.setTargetPosition(leftBackMotor.getCurrentPosition() - move);
        leftFrontMotor.setTargetPosition(leftFrontMotor.getCurrentPosition() - move);
        rightBackMotor.setTargetPosition(rightBackMotor.getCurrentPosition() + move);
        rightFrontMotor.setTargetPosition(rightFrontMotor.getCurrentPosition() + move);
        //
        setSpeed(speed);
    }
    //
    public void turnWithGyro(double degrees, double speedDirection){
        //<editor-fold desc="Gyro">
        BNO055IMU.Parameters parameter = new BNO055IMU.Parameters();
        parameter.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameter.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameter.calibrationDataFile = "GyroCal.json"; // see the calibration sample opmode
        parameter.loggingEnabled = true;
        parameter.loggingTag = "IMU";
        parameter.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        //
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameter);
        //</editor-fold>
        double yaw = angles.firstAngle;
        turn(speedDirection);
        while (yaw == 3){}
        turn(0);
    }
    //</editor-fold>
    //
}
