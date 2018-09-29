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

//import com.qualcomm.hardware.adafruit.JustLoggingAccelerationIntegrator;

public class DriveTrainNew {
    private ElapsedTime timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    private DcMotor lDrive,rDrive;
    private IMU imu;
    private LinearOpMode opMode;
    private DcMotor[] driveMotors  = new DcMotor[2];

    public DriveTrainNew(LinearOpMode opMode) {
        this.opMode = opMode;
        lDrive = opMode.hardwareMap.dcMotor.get("lDrive");
        rDrive = opMode.hardwareMap.dcMotor.get("rDrive");


        BNO055IMU adaImu = opMode.hardwareMap.get(BNO055IMU.class, "imu");
        this.imu = new IMU(adaImu);

        this.driveMotors[0] = rDrive;
        this.driveMotors[1] = lDrive;


        initMotors();

    }

    public void initMotors(){
        Motors.useEncoders(this.driveMotors);
        Motors.resetEncoders(this.driveMotors);
        Motors.setBrake(this.driveMotors);
    }

    public void move(Direction direction,double power){
        power = Math.abs(power);
        if(direction == Direction.BACK){
            power = -power;
        }
        setPowers(power,power);
    }



    public void rotate(Direction direction,RotateMethod method,double power){
        power = Math.abs(power);
        double rPower = 0;
        double lPower = 0;
        if(method == RotateMethod.SPIN){
            if(direction == Direction.CW){
                rPower = -power;
                lPower = power;
            } else if(direction == Direction.CCW){
                lPower = -power;
                rPower = power;

            }
        }else if (method == RotateMethod.BACKWARD){
            if(direction == Direction.CW){
                rPower = -power;
            } else if(direction == Direction.CCW){
                lPower = -power;
            }
        }else if (method == RotateMethod.FORWARD){
            if(direction == Direction.CW){
                lPower = power;
            } else if(direction == Direction.CCW){
                rPower = power;
            }
        }
        setPowers(lPower,rPower);
    }

    public void setPowers(double lPower, double rPower){
        rDrive.setPower(lPower);
        lDrive.setPower(rPower);
    }


    public void move(Direction direction,double power,double inches,double timeoutS) {
        Motors.resetEncoders(driveMotors);
        Motors.useEncoders(driveMotors);
        int rTarget;
        int lTarget;
        switch (direction) {
            case FORWARD:
                rTarget = rDrive.getCurrentPosition() + (int) (inches * Values.ticksPerInch);
                lTarget = lDrive.getCurrentPosition() + (int) (inches * Values.ticksPerInch);
                break;
            case BACK:
                rTarget = rDrive.getCurrentPosition() - (int) (inches * Values.ticksPerInch);
                lTarget = lDrive.getCurrentPosition() - (int) (inches * Values.ticksPerInch);
                break;
            default:
                 rTarget = 0;
                 lTarget = 0;
        }
        lDrive.setTargetPosition(lTarget);
        rDrive.setTargetPosition(rTarget);
        Motors.runToPosition(driveMotors);
        timer.reset();
        switch (direction) {
            case FORWARD:
                setPowers(Math.abs(power),Math.abs(power));
                break;
            case BACK:
                setPowers(-Math.abs(power),-Math.abs(power));
                break;
            default:
                break;
        }
        while(opMode.opModeIsActive() && (timer.time()< timeoutS*1000) && (rDrive.isBusy() && lDrive.isBusy())) {
            if(!rDrive.isBusy()){
                rDrive.setPower(0);
            }
            if(!lDrive.isBusy()){
                lDrive.setPower(0);
            }
        }
        setPowers(0,0);

        Motors.useEncoders(driveMotors);
    }
    private int headingCCWError(int start, int turn, int current ) {

        int offset;
        int target;

        offset = 270 - start;
        target = start - turn + offset;
        current += offset;
        if ( current >=360 ) {
            current-=360;
        } // avoid wrap around problem
        return target - current;

    }

    private int headingCWError(int start, int turn, int current ) {

        int offset;
        int target;

        offset = 90 - start;
        target = start + turn + offset;
        current += offset;
        if ( current < 0 ) {
            current+=360;
        } // avoid wrap around problem
        return target - current;
    }


//    public double rotateIMURamp(int degrees,Direction direction, double power, int timeoutS, Telemetry telemetry) {
//        //public double rotateIMURamp(int degrees, double power, int timeoutS, IMU imu, Telemetry telemetry) {
//        //COUNTERCLOCKWISE IS POSITIVE DEGREES
//        double heading;
//        int e;
//        long endtime = System.currentTimeMillis() + (timeoutS * 1000);
//        double start = imu.getAngle();
//        double target;
//        if(direction == Direction.CW){
//            target = start + Math.abs(degrees);
//        }else{
//            target = start - Math.abs(degrees);
//        }
//
//        if ( target < 0) {
//            target += 360;
//        } else if (target >=360 ) {
//            target -=360;
//        }
//        telemetry.clear();
//        telemetry.addData("Start", start);
//        telemetry.addData("Target", target);
//
//        if (Math.abs(degrees) <= Values.gyroTurnErrorMargin ){
//            telemetry.addData("Too small to turn ", degrees);
//            telemetry.update();
//            return start;
//        }
//        telemetry.update();
//        do {
//            heading = imu.getAngle();
//            if ( degrees > 0 ) { // Turn clockwise
//                e = headingCWError((int) start, degrees, (int) heading); // Heading error
//            } else { // turn counter clockwise
//                e = headingCCWError((int) start, -degrees, (int) heading); // Heading error
//            }
//            if ( e > 0 ) {
//                setPowers(Math.max(Values.minRotationPower, power * powerAdjust(e)),-Math.max(Values.minRotationPower, power * powerAdjust(e)));
//            } else {  // overshoot
//                setPowers(-Math.max(Values.minRotationPower, power * powerAdjust(e)),Math.max(Values.minRotationPower, power * powerAdjust(e)));
//            }
//            if (Math.abs(e) <= Values.gyroTurnErrorMargin) {
//                this.stopAll();
//                Utils.waitFor(100); // wait for 500 msec.
//                heading = imu.getAngle(); // read heading again
//                if ( degrees > 0 ) { // Turn clockwise
//                    e = headingCWError((int) start, degrees, (int) heading); // Heading error
//                } else { // turn counter clockwise
//                    e = headingCCWError((int) start, -degrees, (int) heading); // Heading error
//                }
//            }
//
//            telemetry.clear();
//            telemetry.addData("Start", start);
//            telemetry.addData("Heading", imu.getAngle());
//            telemetry.addData("Target", target);
//            telemetry.addData("timeout", timeoutS * 1000);
//            telemetry.addData("End Time", endtime);
//            telemetry.addData("Error", e);
//            telemetry.update();
//        } while ((Math.abs(e) > Values.gyroTurnErrorMargin) &&  (System.currentTimeMillis() < endtime) && opMode.opModeIsActive());
//
//        this.stopAll();
//        return heading;
//
////      setDriveMotorMode(DcMotor.RunMode.RUN_USING_ENCODER);
//
//    }

}
