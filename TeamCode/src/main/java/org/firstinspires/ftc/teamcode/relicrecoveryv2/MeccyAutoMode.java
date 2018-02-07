package org.firstinspires.ftc.teamcode.relicrecoveryv2;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * Created by Pilt on 12/14/17.
 */

public abstract class MeccyAutoMode extends MeccyMode{
    //
    //PengwinFin pengwinFin;
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
    static final double countify = 53.45;
    //</editor-fold>
    //
    //<editor-fold desc="Extraneous">
    //
    public void configureMotors(){
        super.configureMotors();
        //
        leftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBackMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBackMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    //
    public void configureMotorsYay(){
        //
        leftFrontMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBackMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBackMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
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
    //
    public void stops(){
        while (leftBackMotor.isBusy() && leftFrontMotor.isBusy() && rightBackMotor.isBusy() && rightFrontMotor.isBusy()){}
        //
        rightBackMotor.setPower(0);
        rightFrontMotor.setPower(0);
        leftFrontMotor.setPower(0);
        leftBackMotor.setPower(0);
    }
    //</editor-fold>
    //
    //<editor-fold desc="Moving">
    public void toPosition(double inches, double speed){
        int move = (int)(Math.round(inches*countify));
        //
        leftBackMotor.setTargetPosition(leftBackMotor.getCurrentPosition() + move);
        leftFrontMotor.setTargetPosition(leftFrontMotor.getCurrentPosition() + move);
        rightBackMotor.setTargetPosition(rightBackMotor.getCurrentPosition() + move);
        rightFrontMotor.setTargetPosition(rightFrontMotor.getCurrentPosition() + move);
        //
        setSpeed(speed);
        //
        stops();
    }
    //
    public void strafeToPosition(double inches, double speed){
        int move = (int)(Math.round(inches*countify));
        //
        leftBackMotor.setTargetPosition(leftBackMotor.getCurrentPosition() - move);
        leftFrontMotor.setTargetPosition(leftFrontMotor.getCurrentPosition() + move);
        rightBackMotor.setTargetPosition(rightBackMotor.getCurrentPosition() + move);
        rightFrontMotor.setTargetPosition(rightFrontMotor.getCurrentPosition() - move);
        //
        setSpeed(speed);
        //
        stops();
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
    public void initGyro(){
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        //parameters.calibrationDataFile = "GyroCal.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        //
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        telemetry.addData("init imu","");
        telemetry.update();
        imu.initialize(parameters);
        telemetry.addData("imu initiated", "");
        telemetry.update();
    }
    //
    public void turnWithGyro(double degrees, double speedDirection){
        telemetry.addData("Working", "");
        telemetry.update();
        //
        //<editor-fold desc="Everything Else">
        telemetry.addData("Still working", "");
        telemetry.update();
        angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double yaw = -angles.firstAngle;
        telemetry.addData("Speed Direction", speedDirection);
        telemetry.addData("Yaw", yaw);
        telemetry.update();
        //
        configureMotorsYay();
        //
        turn(speedDirection);
        //
        double input2 = (2 * Math.ceil((speedDirection + Math.abs(speedDirection)) / 2) - 1);
        telemetry.addLine("Output 1: " + input2);
        double target = ( -1 * (((Math.floor(Math.abs((degrees * input2) + yaw) / 180)) * input2 * 360) - ((degrees * input2) + yaw)));
        while (!((target - 1 - (2 * input2)) <= -angles.firstAngle && -angles.firstAngle < (target + 1 - (2 * input2)))){
            telemetry.addData("Position", angles.firstAngle);
            angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            gravity  = imu.getGravity();
            telemetry.addData("NumberTargetThing", target);
            telemetry.addData("Yaw", yaw);
            //
            telemetry.update();
        }
        turn(0);
        //
        stopAndResetify();
        //
        //</editor-fold>


    }
    //</editor-fold>
    //

}


