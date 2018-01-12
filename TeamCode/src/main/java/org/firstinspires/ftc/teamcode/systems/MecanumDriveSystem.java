package org.firstinspires.ftc.teamcode.systems;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Mahim on 12/4/2017.
 */

public class MecanumDriveSystem {
    private DcMotor frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor;
    private static final double X_COUNTS_PER_INCH = 0.0;
    private static final double Y_COUNTS_PER_INCH = 0.0;
    private OpMode opMode;
    private LinearOpMode linearOpMode;
    private HardwareMap hardwareMap;
    private AHRS navX;


    public MecanumDriveSystem(OpMode opMode) {
        this.opMode = opMode;
        this.hardwareMap = opMode.hardwareMap;
        this.frontLeftMotor = hardwareMap.get(DcMotor.class,"front left motor");
        this.rearLeftMotor = hardwareMap.get(DcMotor.class,"rear left motor");
        this.frontRightMotor = hardwareMap.get(DcMotor.class,"front right motor");
        this.rearRightMotor = hardwareMap.get(DcMotor.class, "rear right motor");
        this.rearRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        this.frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public MecanumDriveSystem(LinearOpMode opMode) {
        this.linearOpMode = opMode;
        this.hardwareMap = linearOpMode.hardwareMap;
        this.frontLeftMotor = hardwareMap.get(DcMotor.class,"front left motor");
        this.rearLeftMotor = hardwareMap.get(DcMotor.class,"rear left motor");
        this.frontRightMotor = hardwareMap.get(DcMotor.class,"front right motor");
        this.rearRightMotor = hardwareMap.get(DcMotor.class, "rear right motor");
        this.rearRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        this.frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void initEncoderDrive() {
        this.frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.rearLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.rearRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.rearLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.rearRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void encoderDriveY(double inches, double speed) {
        int frontLeftMotorTargetPosition;
        int rearLeftMotorTargetPosition;
        int frontRightMotorTargetPosition;
        int rearRightMotorTargetPosition;

        if(this.linearOpMode.opModeIsActive()) {
            frontLeftMotorTargetPosition = frontLeftMotor.getCurrentPosition() + (int)(inches * Y_COUNTS_PER_INCH);
            rearLeftMotorTargetPosition = rearLeftMotor.getCurrentPosition() + (int)(inches * Y_COUNTS_PER_INCH);
            frontRightMotorTargetPosition = frontRightMotor.getCurrentPosition() + (int)(inches * Y_COUNTS_PER_INCH);
            rearRightMotorTargetPosition = rearRightMotor.getCurrentPosition() + (int)(inches * Y_COUNTS_PER_INCH);
            frontLeftMotor.setTargetPosition(frontLeftMotorTargetPosition);
            rearLeftMotor.setTargetPosition(rearLeftMotorTargetPosition);
            frontRightMotor.setTargetPosition(frontRightMotorTargetPosition);
            rearRightMotor.setTargetPosition(rearRightMotorTargetPosition);

            frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rearLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rearRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            drive(speed, speed, speed, speed);

            while (this.linearOpMode.opModeIsActive() &&
                    (this.frontLeftMotor.isBusy() &&
                            this.rearLeftMotor.isBusy() &&
                            this.frontRightMotor.isBusy() &&
                            this.rearRightMotor.isBusy())) {

            }
            stop();
        }
    }

    public void encoderDriveX(double inches, double speed) {
        int frontLeftMotorTargetPosition;
        int rearLeftMotorTargetPosition;
        int frontRightMotorTargetPosition;
        int rearRightMotorTargetPosition;

        if(this.linearOpMode.opModeIsActive()) {
            frontLeftMotorTargetPosition = frontLeftMotor.getCurrentPosition() - (int)(inches * X_COUNTS_PER_INCH);
            rearLeftMotorTargetPosition = rearLeftMotor.getCurrentPosition() + (int)(inches * X_COUNTS_PER_INCH);
            frontRightMotorTargetPosition = frontRightMotor.getCurrentPosition() + (int)(inches * X_COUNTS_PER_INCH);
            rearRightMotorTargetPosition = rearRightMotor.getCurrentPosition() - (int)(inches * X_COUNTS_PER_INCH);
            frontLeftMotor.setTargetPosition(frontLeftMotorTargetPosition);
            rearLeftMotor.setTargetPosition(rearLeftMotorTargetPosition);
            frontRightMotor.setTargetPosition(frontRightMotorTargetPosition);
            rearRightMotor.setTargetPosition(rearRightMotorTargetPosition);

            frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rearLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rearRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);


            frontLeftMotor.setPower(-speed);
            rearLeftMotor.setPower(speed);
            frontRightMotor.setPower(speed);
            rearRightMotor.setPower(-speed);
        }
    }

    public void drive(double x, double y, double turn) {
        this.frontLeftMotor.setPower(y - x - turn);
        this.rearLeftMotor.setPower(y + x - turn);
        this.frontRightMotor.setPower(y + x + turn);
        this.rearRightMotor.setPower(y - x + turn);
    }

    public void drive(double frontLeftSpeed, double rearLeftSpeed,
                      double frontRightSpeed, double rearRightSpeed) {
        this.frontRightMotor.setPower(frontRightSpeed);
        this.rearRightMotor.setPower(rearRightSpeed);
        this.frontLeftMotor.setPower(frontLeftSpeed);
        this.rearRightMotor.setPower(rearRightSpeed);
    }

    public void drive(double left, double right) {
        this.frontLeftMotor.setPower(left);
        this.rearLeftMotor.setPower(left);
        this.rearRightMotor.setPower(right);
        this.frontRightMotor.setPower(right);
    }

    public void stop() {
        this.frontLeftMotor.setPower(0.0);
        this.rearLeftMotor.setPower(0.0);
        this.frontRightMotor.setPower(0.0);
        this.rearRightMotor.setPower(0.0);
    }

    public double getFrontRightSpeed() {
        return this.frontRightMotor.getPower();
    }

    public double getRearRightSpeed() {
        return this.rearRightMotor.getPower();
    }

    public double getFrontLeftSpeed() {
        return this.frontLeftMotor.getPower();
    }

    public double getRearLeftSpeed() {
        return  this.rearLeftMotor.getPower();
    }

    public int getFrontLeftMotorEncoderTick() {
        return frontLeftMotor.getCurrentPosition();
    }

    public int getRearLeftMotorEncoderTick() {
        return rearLeftMotor.getCurrentPosition();
    }

    public int getFrontRightMotorEncoderTick() {
        return frontRightMotor.getCurrentPosition();
    }

    public int getRearRightMotorEncoderTick() {
        return rearRightMotor.getCurrentPosition();
    }
}
