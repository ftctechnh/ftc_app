package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * Created by Recharged Orange on 10/9/2018.
 */

public abstract class superClass extends linearOpmode {

    public DcMotor leftBack;
    public DcMotor leftFront;
    public DcMotor rightFront;
    public DcMotor rightBack;
    public DcMotor sweeper;

    public Servo servo;
    public CRServo REVServo;

    public BNO055IMU imu;
    public RevTouchSensor touchSensor;
    public float imuStartingPosition;
    public DigitalChannel beam;

    double frontlefttarget = 0;
    double frontrighttarget = 0;
    double backrighttarget = 0;
    double backlefttarget = 0;

    Orientation lastAngles = new Orientation();
    double globalAngle, power = .30, correction;

    public void initialization(boolean autonomous) {

        telemetry.addLine("drive Init");
        telemetry.update();
        initDrive();

        telemetry.addLine("sweeper init");
        telemetry.update();
        initSweeper();

        telemetry.addLine("servo");
        telemetry.update();
        initServo();

        telemetry.addLine("beam");
        telemetry.update();
        initBeam();

        telemetry.addLine("init touch");
        telemetry.update();
        initTouch();

        if (autonomous) {
            telemetry.addLine("imu init");
            telemetry.update();
            initiate_imu();


            leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        } else {
            leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        }
        telemetry.addLine("Finished init - ready to go captain!");
        telemetry.update();
    }

    public void initDrive() {

        leftBack = hardwareMap.dcMotor.get("leftBack");
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        rightBack = hardwareMap.dcMotor.get("rightBack");

        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);


    }

    public void initSweeper() {
        sweeper = hardwareMap.dcMotor.get("sweeper");
        sweeper.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void initServo() {
        servo = hardwareMap.servo.get("servo");
        REVServo = hardwareMap.crservo.get("REVServo");
    }

    public void initBeam() {
        beam = hardwareMap.digitalChannel.get("beam");
    }

    public void initTouch() {
        touchSensor = hardwareMap.get(RevTouchSensor.class, "touchSensor");

    }

    public void initiate_imu() {

        telemetry.addLine("Initiating IMU");
        telemetry.update();

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        while (opModeIsActive()) {

            imuStartingPosition = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;

        }
    }


    public void driveForwardEncoders(double distance, double power) {
        leftBack.setPower(power);
        leftFront.setPower(power);
        rightBack.setPower(power);
        rightFront.setPower(power);

        backlefttarget = (leftBack.getCurrentPosition() + distance);
        backrighttarget = (rightBack.getCurrentPosition() + distance);
        frontlefttarget = (leftFront.getCurrentPosition() + distance);
        frontrighttarget = (rightFront.getCurrentPosition() + distance);

        while (leftBack.getCurrentPosition() < backlefttarget
                && leftFront.getCurrentPosition() < frontlefttarget
                && rightBack.getCurrentPosition() < backrighttarget
                && rightFront.getCurrentPosition() < backrighttarget && opModeIsActive()) {

            telemetry.addData("left back", leftBack.getCurrentPosition());
            telemetry.addData("left front", leftFront.getCurrentPosition());
            telemetry.addData("right back", rightBack.getCurrentPosition());
            telemetry.addData("right front", rightFront.getCurrentPosition());

            telemetry.update();
        }
        leftBack.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        rightFront.setPower(0);
    }


    public void driveBackwardEncoders(double distance, double power) {

        backlefttarget = (leftBack.getCurrentPosition() - distance);
        backrighttarget = (rightBack.getCurrentPosition() - distance);
        frontlefttarget = (leftFront.getCurrentPosition() - distance);
        frontrighttarget = (rightFront.getCurrentPosition() - distance);

        while (leftBack.getCurrentPosition() > backrighttarget
                && leftFront.getCurrentPosition() > backlefttarget
                && rightBack.getCurrentPosition() > frontrighttarget
                && rightFront.getCurrentPosition() > frontlefttarget && opModeIsActive()) {
            leftBack.setPower(-power);
            leftFront.setPower(-power);
            rightBack.setPower(-power);
            rightFront.setPower(-power);

            telemetry.addData("left back", leftBack.getCurrentPosition());
            telemetry.addData("left front", leftFront.getCurrentPosition());
            telemetry.addData("right back", rightBack.getCurrentPosition());
            telemetry.addData("right front", rightFront.getCurrentPosition());

            telemetry.update();

        }
        leftBack.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        rightFront.setPower(0);
    }

    public void driveleft(double distance, double power) {
        leftFront.setPower(-power);
        leftBack.setPower(power);
        rightFront.setPower(power);
        rightBack.setPower(-power);

        backlefttarget = (leftBack.getCurrentPosition() - distance);
        backrighttarget = (rightBack.getCurrentPosition() + distance);
        frontlefttarget = (leftFront.getCurrentPosition() + distance);
        frontrighttarget = (rightFront.getCurrentPosition() - distance);

        while (leftFront.getCurrentPosition() > backrighttarget
                && leftBack.getCurrentPosition() < backlefttarget
                && rightFront.getCurrentPosition() < frontrighttarget
                && rightBack.getCurrentPosition() > frontlefttarget && opModeIsActive()) {

            telemetry.addData("left back", leftBack.getCurrentPosition());
            telemetry.addData("left front", leftFront.getCurrentPosition());
            telemetry.addData("right back", rightBack.getCurrentPosition());
            telemetry.addData("right front", rightFront.getCurrentPosition());

            telemetry.update();

        }
        leftBack.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        rightFront.setPower(0);
    }

    public void driveright(double distance, double power) {
        leftFront.setPower(power);
        leftBack.setPower(-power);
        rightFront.setPower(-power);
        rightBack.setPower(+power);

        backlefttarget = (leftBack.getCurrentPosition() + distance);
        backrighttarget = (rightBack.getCurrentPosition() - distance);
        frontlefttarget = (leftFront.getCurrentPosition() - distance);
        frontrighttarget = (rightFront.getCurrentPosition() + distance);

        while (leftFront.getCurrentPosition() < backlefttarget
                && leftBack.getCurrentPosition() > backrighttarget
                && rightFront.getCurrentPosition() > frontlefttarget
                && rightBack.getCurrentPosition() < frontrighttarget && opModeIsActive()) {

            telemetry.addData("left back", leftBack.getCurrentPosition());
            telemetry.addData("left front", leftFront.getCurrentPosition());
            telemetry.addData("right back", rightBack.getCurrentPosition());
            telemetry.addData("right front", rightFront.getCurrentPosition());

            telemetry.update();
        }
        leftBack.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        rightFront.setPower(0);
    }

    public void rotateLeft(double targetAngle, double power) {
        leftBack.setPower(-power);
        leftFront.setPower(-power);
        rightBack.setPower(power);
        rightFront.setPower(power);

        while (opModeIsActive() && getAngle() < targetAngle) {
            telemetry.addData("curentAngle", getAngle());
            telemetry.addData("targetAngle", targetAngle);
            telemetry.update();
        }
        leftBack.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        rightFront.setPower(0);

    }

    public void rotateRight(double targetAngle, double power) {
        leftBack.setPower(power);
        leftFront.setPower(power);
        rightBack.setPower(-power);
        rightFront.setPower(-power);

        while (opModeIsActive() && getAngle() > targetAngle) {
            telemetry.addData("curentAngle", getAngle());
            telemetry.addData("targetAngle", targetAngle);
            telemetry.update();
        }
        leftBack.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        rightFront.setPower(0);
    }


    public void resetAngle() {
        lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        globalAngle = 0;
    }

    public double getAngle() {
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double deltaAngle = angles.firstAngle - lastAngles.firstAngle;

        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;

        globalAngle += deltaAngle;

        lastAngles = angles;

        return globalAngle;
    }
}
