package org.usfirst.ftc.theintersect.code;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;


public class DriveTrain {
    ElapsedTime timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    static final double TICKS_PER_INCH_FORWARD = 56;
    static final double TICKS_PER_INCH_STRAFE = 61.3;
    static DcMotor rF, rB, lF, lB, flywheel1, flywheel2, sweeperLow, sweeperHigh;
    static GyroSensor gyro;
    static ModernRoboticsI2cGyro mrGyro;
    static ColorSensor beaconColor, floorColor;

    // Tunable parameters
    private int conversionFactor = 50;
    private int gyroTurnErrorMargin = 1; // turn stop if within the margin of error
    private int gyroTurnRampMax = 15;  // starting point of scaling back speed of motor for turning
    private int gyroTurnRampMin = 3;   // stopping point to turn off motor abs(heading-target)<vlaue
    private double minRotationPower = 0.05; // minimum power to move robot

    double average;
    LinearOpMode opMode;

    public DriveTrain(DcMotor lb, DcMotor rb, DcMotor lf, DcMotor rf, LinearOpMode opMode, GyroSensor gyro, ColorSensor floorColor) {
        DriveTrain.lB = lb;
        DriveTrain.rB = rb;
        DriveTrain.lF = lf;
        DriveTrain.rF = rf;
        this.opMode = opMode;
        DriveTrain.floorColor = floorColor;
        DriveTrain.mrGyro = (ModernRoboticsI2cGyro)gyro; // map to MR Gyro to use special method
        DriveTrain.gyro = gyro; // map to generic Gyro class

        lB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        lB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        rF.setDirection(DcMotor.Direction.FORWARD);
        rB.setDirection(DcMotor.Direction.FORWARD);
        lB.setDirection(DcMotor.Direction.REVERSE);
        lf.setDirection(DcMotor.Direction.REVERSE);

    }

    public void strafeRight(double power, int time) {
        lF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        strafeRight(power);
        Functions.waitFor(time);
        stopAll();
    }

    public void strafeLeft(double power, int time) {
        lF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        strafeLeft(power);
        Functions.waitFor(time);
        stopAll();
    }

    /**
     * Mecanum drive function
     * @param direction Direction of robot movement. 0 - 360
     * @param vD velocity
     * @param time how long is the drive in msec
     */
    public void mecanumDrive(int direction, double vD, int time) {

//        if (direction < 0 ) direction = 360 + direction;
//        double thetaD = direction *2.0 * Math.PI / 360.0;

        //conver from degree to radian
        double thetaD = (double)((direction<0)? 360 + direction: direction ) *2.0 * Math.PI / 360.0;
        double frontLeft = vD * Math.sin(thetaD + Math.PI / 4);
        double frontRight = vD * Math.cos(thetaD + Math.PI / 4);
        double backLeft = vD * Math.cos(thetaD + Math.PI / 4);
        double backRight = vD * Math.sin(thetaD + Math.PI / 4);
        double maxF = Math.max(Math.abs(frontLeft), Math.abs(frontRight));
        double maxB = Math.max(Math.abs(backLeft), Math.abs(backRight));
        double max = Math.max(maxF, maxB);
        //
        if (max > 1.0) {
            frontLeft = frontLeft / max;
            frontRight = frontRight / max;
            backLeft = backLeft / max;
            backRight = backRight / max;
        }

        lF.setPower(frontLeft);
        lB.setPower(backLeft);
        rF.setPower(frontRight);
        rB.setPower(backRight);
        Functions.waitFor(time);
        stopAll();

    }

    public void moveEncoder(String direction, double power, int rotations) {

        rF.setTargetPosition(rotations * conversionFactor);
        lF.setTargetPosition(rotations * conversionFactor);
        rB.setTargetPosition(rotations * conversionFactor);
        lB.setTargetPosition(rotations * conversionFactor);
        if (direction == "fwd") {
            moveFwd(power);
        } else if (direction == "bkwd") {
            moveBkwd(power);
        } else if (direction == "rCCW") {
            rotateCCW(power);
        } else if (direction == "rCW") {
            rotateCW(power);
        } else if (direction == "sL") {
            strafeLeft(power);
        } else if (direction == "sR") {
            strafeRight(power);
        }
        while (rF.getCurrentPosition() <= rF.getTargetPosition()
                && lF.getCurrentPosition() <= rF.getTargetPosition()
                && rB.getCurrentPosition() <= rF.getTargetPosition()
                && lF.getCurrentPosition() <= rF.getTargetPosition()) {
            Functions.waitFor(10);
        }
        this.stopAll();
    }


    public void moveFwd(double power) {
        lF.setPower(power);
        lB.setPower(power);
        rF.setPower(power);
        rB.setPower(power);
    }

    public void moveBkwd(double power) {
        lF.setPower(-power);
        lB.setPower(-power);
        rF.setPower(-power);
        rB.setPower(-power);
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

/*****
    private int headingCCWError(int start, int target, int current ) {

        int offset;

        if ( target >= 270 ) {
            offset = -180;
        } else if ( target < 90 ) {
            offset = 90;
        } else {
            offset = 0;
        }

        current = current + offset;
        if ( current >= 360) {
            current -= 360;
        } else if ( current < 0 ) {
            current +=360;
        }

        return target - current;
    }
******/

    public void SetGryrTurnParameters(int max, int min, double speed){
        gyroTurnRampMax = max;
        gyroTurnRampMin = min;
        minRotationPower = speed;
    }

    private double powerAdjust ( int e ) {
        e = Math.abs(e);
        if ( e > gyroTurnRampMax) {
            return 1.0;
        }
        if ( e <= gyroTurnRampMin ) {
            return 0;
        }
        return (e-gyroTurnRampMin)/(gyroTurnRampMax-gyroTurnRampMin) ;
    }


    // @param degree - turn in degree up to +/- 180.   +/- = clockwise/counterclockwise
    // @param power - turn motor power
    // @param timeout - time out in seconds
    // @param gyro pointer to Gyro object
    // @param telemetry - pointer to telemetry object
    public int rotateGyroRamp(int degrees, double power, int timeout, GyroSensor gyro, Telemetry telemetry){
        int heading;
        int e;
        long endtime = System.currentTimeMillis() + (timeout * 1000);
        int start = gyro.getHeading();

        int target = start + degrees;
        if ( target < 0) {
            target += 360;
        } else if (target >=360 ) {
            target -=360;
        }
//        setDriveMotorMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        telemetry.clear();
        telemetry.addData("Start", start);
        telemetry.addData("Target", target);

        if (Math.abs(degrees) <= gyroTurnErrorMargin ){
            telemetry.addData("Too small to turn ", degrees);
            telemetry.update();
            return start;
        }
        telemetry.update();
        do {
            heading = gyro.getHeading();
            if ( degrees > 0 ) { // Turn clockwise
                e = headingCWError(start, degrees, heading); // Heading error
            } else { // turn counter clockwise
                e = headingCCWError(start, -degrees, heading); // Heading error
            }
            if ( e > 0 ) {
                rotateCW(Math.max(minRotationPower, power * powerAdjust(e)));
            } else {  // overshoot
                rotateCCW(Math.max(minRotationPower, power * powerAdjust(e)));
            }
            if (Math.abs(e) <= gyroTurnErrorMargin) {
                this.stopAll();
                Functions.waitFor(100); // wait for 500 msec.
                heading = gyro.getHeading(); // read heading again
                if ( degrees > 0 ) { // Turn clockwise
                    e = headingCWError(start, degrees, heading); // Heading error
                } else { // turn counter clockwise
                    e = headingCCWError(start, -degrees, heading); // Heading error
                }
            }
        } while (  (Math.abs(e) > gyroTurnErrorMargin) &&  (System.currentTimeMillis() < endtime) && opMode.opModeIsActive());

        this.stopAll();

//        setDriveMotorMode(DcMotor.RunMode.RUN_USING_ENCODER);
        telemetry.clear();
        telemetry.addData("Start", start);
        telemetry.addData("Heading", gyro.getHeading());
        telemetry.addData("Target", target);
        telemetry.addData("timeout", timeout * 1000);
        telemetry.addData("End Time", endtime);
        telemetry.update();
        return heading;
    }

    public void rotateCWGyroRamp(int degree, double power, int timeoutS, GyroSensor gyro, Telemetry telemetry){


        int start = gyro.getHeading();
        int heading;
        int target = start + degree;
        long endtime = System.currentTimeMillis() + (timeoutS * 1000);
        int e;

        if( target > 360 ) {
            target -= 360;
        }
        setDriveMotorMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.clear();
        telemetry.update();
        telemetry.addData("Start", start);
        telemetry.addData("Heading", gyro.getHeading());
        telemetry.addData("Target", target);
        telemetry.addData("End Time", endtime);
        telemetry.update();

        do {
            double x;
            heading = gyro.getHeading();
            e = headingCWError(start, degree, heading); // Heading error
            if ( e > 0 ) {
                rotateCW(Math.max(minRotationPower, power * powerAdjust(e)));
            } else {  // overshoot
                rotateCCW(Math.max(minRotationPower, power * powerAdjust(e)));
            }
        } while (  (Math.abs(e) > gyroTurnErrorMargin) &&  (System.currentTimeMillis() < endtime) && opMode.opModeIsActive());

        this.stopAll();

        // setDriveMotorMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Start", start);
        telemetry.addData("Heading", gyro.getHeading());
        telemetry.addData("Target", target);
        telemetry.addData("End Time", endtime);
        telemetry.update();
    }

    public void rotateCCWGyroRamp(int degrees, double power, int timeout, GyroSensor gyro, Telemetry telemetry){


        int start = gyro.getHeading();
        int heading;
        int target = start - degrees;
        long endtime = System.currentTimeMillis() + (timeout * 1000);
        int e;

        if ( target < 0) {
            target += 360;
        }
//        setDriveMotorMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.clear();
        telemetry.update();
        telemetry.addData("Start", start);
        telemetry.addData("Heading", gyro.getHeading());
        telemetry.addData("Target", target);
        telemetry.addData("timeout", timeout * 1000);
        telemetry.addData("End Time", endtime);
        telemetry.update();


        do {
            heading = gyro.getHeading();
            e = headingCCWError(start, target, heading); // Heading error
            if (e > 0) {
                rotateCW(Math.max(minRotationPower, power * powerAdjust(e)));
            } else {  // overshoot
                rotateCCW(Math.max(minRotationPower, power * powerAdjust(e)));
            }
        } while (  (Math.abs(e) > gyroTurnErrorMargin) &&  (System.currentTimeMillis() < endtime) && opMode.opModeIsActive());

        this.stopAll();

//        setDriveMotorMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Start", start);
        telemetry.addData("Heading", gyro.getHeading());
        telemetry.addData("Target", target);
        telemetry.addData("timeout", timeout * 1000);
        telemetry.addData("End Time", endtime);
        telemetry.update();

    }


    public void rotateGyroRamp(int degrees, double power, int timeoutS, Telemetry telemetry){


        int start = mrGyro.getIntegratedZValue();
        int heading;
        int target = start + degrees;
        long endtime = System.currentTimeMillis() + (timeoutS * 1000);
        int e;

        setDriveMotorMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.clear();
        telemetry.addData("Start", start);
        telemetry.addData("Target", target);
        telemetry.update();

        do {
            double x;
            heading = mrGyro.getIntegratedZValue();
            // e = headingCWError(start, target, heading); // Heading error
            e = target - heading;
            if ( e > 0 ) {
                rotateCW(Math.max(minRotationPower, power * powerAdjust(e)));
            } else {  // overshoot
                rotateCCW(Math.max(minRotationPower, power * powerAdjust(e)));
            }
        } while ( (Math.abs(e) > gyroTurnErrorMargin) &&  (System.currentTimeMillis() < endtime) && opMode.opModeIsActive());

        this.stopAll();

        // setDriveMotorMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Start", start);
        telemetry.addData("Heading", mrGyro.getIntegratedZValue());
        telemetry.addData("Target", target);
        telemetry.addData("End Time", endtime);
        telemetry.update();
    }



    public void rotateCWGyro(int degrees, double power, int timeout, GyroSensor gyro, Telemetry telemetry) {
        lB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        int start = gyro.getHeading();
        int heading;
        long endtime = System.currentTimeMillis() + (timeout * 1000);
        int protectedValue = start - 20;
        int target = start + degrees;
        if (target >= 360) {
            target = target - 360;
        }
        if (protectedValue < 0) {
            protectedValue = protectedValue + 360;
        }/* else if (start < target) {
            protectedValue = 360;
        }*/
        telemetry.clear();
        telemetry.update();
        telemetry.addData("Start", start);
        telemetry.addData("Heading", gyro.getHeading());
        telemetry.addData("Target", target);
        telemetry.addData("Protected Value", protectedValue);
        telemetry.addData("timeout", timeout * 1000);
        telemetry.addData("End Time", endtime);
        telemetry.update();
        heading = gyro.getHeading();
        if (heading > protectedValue) {
            heading = heading - 360;
        }
        rotateCW(power);
        while ((heading < target) && (System.currentTimeMillis() < endtime) && opMode.opModeIsActive()) {
            heading = gyro.getHeading();
            if (heading > protectedValue) {
                heading = heading - 360;
            }
           /* telemetry.addData("Start", start);
            telemetry.addData("Heading", gyro.getHeading());
            telemetry.addData("Target", target);
            telemetry.addData("Protected Value", protectedValue);
            telemetry.update();*/
        }
        this.stopAll();

        lB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        telemetry.addData("Start", start);
        telemetry.addData("Heading", gyro.getHeading());
        telemetry.addData("Target", target);
        telemetry.addData("Protect", protectedValue);
        telemetry.addData("timeout", timeout * 1000);
        telemetry.addData("End Time", endtime);
        telemetry.update();

    }
    public void rotateCWGyroAnton(int degrees,int slowHeading, double power,double endPower, int timeout, GyroSensor gyro, Telemetry telemetry) {
        lB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        int start = gyro.getHeading();
        int heading;
        long endtime = System.currentTimeMillis() + (timeout * 1000);
        int protectedValue = start - 20;
        int target = start + degrees;
        if (target >= 360) {
            target = target - 360;
        }
        if (protectedValue < 0) {
            protectedValue = protectedValue + 360;
        }/* else if (start < target) {
            protectedValue = 360;
        }*/
        telemetry.clear();
        telemetry.update();
        telemetry.addData("Start", start);
        telemetry.addData("Heading", gyro.getHeading());
        telemetry.addData("Target", target);
        telemetry.addData("Protected Value", protectedValue);
        telemetry.addData("timeout", timeout * 1000);
        telemetry.addData("End Time", endtime);
        telemetry.update();
        heading = gyro.getHeading();
        if (heading > protectedValue) {
            heading = heading - 360;
        }
        rotateCW(power);
        while ((heading < target) && (System.currentTimeMillis() < endtime) && opMode.opModeIsActive()) {
            heading = gyro.getHeading();
            if (heading > protectedValue) {
                heading = heading - 360;
            }
            if(Math.abs(heading - target)<slowHeading){
                rotateCW(endPower);
            }
           /* telemetry.addData("Start", start);
            telemetry.addData("Heading", gyro.getHeading());
            telemetry.addData("Target", target);
            telemetry.addData("Protected Value", protectedValue);
            telemetry.update();*/
        }
        this.stopAll();

        lB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        telemetry.addData("Start", start);
        telemetry.addData("Heading", gyro.getHeading());
        telemetry.addData("Target", target);
        telemetry.addData("Protect", protectedValue);
        telemetry.addData("timeout", timeout * 1000);
        telemetry.addData("End Time", endtime);
        telemetry.update();

    }

    public void rotateCCWGyro(int degrees, double power, int timeout, Telemetry telemetry) {
        int start = gyro.getHeading();
        int heading = start;
        long endtime = System.currentTimeMillis() + (timeout * 1000);
        int protectedValue = start + 20;
        int target = start + degrees;
        if (target >= 360) {
            target = target - 360;
        }
        if (protectedValue >= 360) {
            protectedValue = protectedValue - 360;
        } else if (target < start) {
            protectedValue = -1;
        }
        rotateCCW(power);
        while ((heading > target) && (System.currentTimeMillis() < endtime) && opMode.opModeIsActive()) {
            heading = gyro.getHeading();
            if (heading < protectedValue) {
                heading = heading + 360;
            }
            /*telemetry.addData("Start", start);
            telemetry.addData("Heading", gyro.getHeading());
            telemetry.addData("Target", target);
            telemetry.addData("Protect", protectedValue);
            telemetry.update();*/
        }
        this.stopAll();
        telemetry.addData("Start", start);
        telemetry.addData("Heading", gyro.getHeading());
        telemetry.addData("Target", target);
        telemetry.addData("Protect", protectedValue);
        telemetry.update();
    }

    public void rotateCWGyroCorrection(int degrees, double power, int timeout, GyroSensor gyro, Telemetry telemetry) {
        boolean fineAdjustment = false;
        int start = gyro.getHeading();
        int heading;
        long endtime = System.currentTimeMillis() + (timeout * 1000);
        int protectedValue = start - 20;
        int target = start + degrees;
        if (target >= 360) {
            target = target - 360;
        }
        if (protectedValue < 0) {
            protectedValue = protectedValue + 360;
        } else if (start < target) {
            protectedValue = 360;
        }
        telemetry.clear();
        telemetry.update();
        telemetry.addData("Start", start);
        telemetry.addData("Heading", gyro.getHeading());
        telemetry.addData("Target", target);
        telemetry.addData("Protected Value", protectedValue);
        telemetry.addData("timeout", timeout * 1000);
        telemetry.addData("End Time", endtime);
        telemetry.update();
        heading = gyro.getHeading();
        rotateCW(power);
        while ((heading < target) && (System.currentTimeMillis() < endtime) && opMode.opModeIsActive()) {
            heading = gyro.getHeading();
            if (heading > protectedValue) {
                heading = heading - 360;
            }
        }
        stopAll();
        telemetry.addData("Start", start);
        telemetry.addData("Heading", gyro.getHeading());
        telemetry.addData("Target", target);
        telemetry.addData("Protect", protectedValue);
        telemetry.addData("timeout", timeout * 1000);
        telemetry.addData("End Time", endtime);
        telemetry.update();
        while (Math.abs(target - gyro.getHeading()) > 2 && endtime > System.currentTimeMillis() && opMode.opModeIsActive()){
            if (gyro.getHeading()<target) {
                rotateCW(Functions.adjustmentSpeed);
            } else {
                rotateCCW(Functions.adjustmentSpeed);
                }
        }
        stopAll();

    }
    public void rotateCW(double power) {
        lF.setPower(-power);
        lB.setPower(-power);
        rF.setPower(power);
        rB.setPower(power);
    }
    public void rotateCCW(double power) {
        lF.setPower(power);
        lB.setPower(power);
        rF.setPower(-power);
        rB.setPower(-power);
    }

    public void strafeLeft(double power) {
        lF.setPower(power);
        lB.setPower(-power);
        rF.setPower(-power);
        rB.setPower(power);
    }

    public void strafeRight(double power) {

        lF.setPower(-power);
        lB.setPower(power);
        rF.setPower(power);
        rB.setPower(-power);
    }
    public void strafeRight(double power, double inches, double timeout){
        timer.reset();
        strafeRight(power);
        int startPosition = rF.getCurrentPosition();
        double endPosition = startPosition + inches*TICKS_PER_INCH_STRAFE;
        while (opMode.opModeIsActive() && rF.getCurrentPosition()< endPosition &&  timer.time()< timeout*1000){

        }
        this.stopAll();
    }
    public void strafeLeft(double power, double inches, double timeout){
        timer.reset();
        strafeLeft(power);
        int startPosition = rF.getCurrentPosition();
        double endPosition = startPosition - inches*TICKS_PER_INCH_STRAFE;
        while (opMode.opModeIsActive() && rF.getCurrentPosition()< endPosition && timer.time() < timeout*1000){
        }
        this.stopAll();
    }

    public static void resetEncoders() {
        lF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void moveFwd(double speed, double inches, double timeoutS) {
        resetEncoders();
        lB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        int newLBTarget;
        int newRBTarget;
        int newRFTarget;
        int newLFTarget;

        // Ensure that the opmode is still active

            // Determine new target position, and pass to motor controller
            newLBTarget = lB.getCurrentPosition() + (int)(inches * TICKS_PER_INCH_FORWARD);
            newRBTarget = rB.getCurrentPosition() + (int)(inches * TICKS_PER_INCH_FORWARD);
            newLFTarget = lF.getCurrentPosition() + (int)(inches * TICKS_PER_INCH_FORWARD);
            newRFTarget = rF.getCurrentPosition() + (int)(inches * TICKS_PER_INCH_FORWARD);
            lF.setTargetPosition(newLFTarget);
            rF.setTargetPosition(newRFTarget);
            lB.setTargetPosition(newLBTarget);
            rB.setTargetPosition(newRBTarget);

            // Turn On RUN_TO_POSITION
            lF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            lB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rB.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
        timer.reset();
            lF.setPower(Math.abs(speed));
            rF.setPower(Math.abs(speed));
            lB.setPower(Math.abs(speed));
            rB.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opMode.opModeIsActive() &&
                    (timer.time()< timeoutS*1000) &&
                    (lF.isBusy() && rF.isBusy() && rB.isBusy() && lB.isBusy())) {
            }

            // Stop all motion
            this.stopAll();

            // Turn off RUN_TO_POSITION
            lF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            lB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            // resetEncoders();
            //  sleep(250);   // optional pause after each move
        }

    /*
     * Move Forward/Backward and lean towards right against wall
     * @param speed - moving power - > 0 Forward; <0 backward
     * @param rightSpeed - power to lean towards right, should be smaller than speed need to be >0
     * @param inches
     */
    public void moveFwdRight(double speed, double rightSpeed, double inches, double timeoutS) {

        lF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        lB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        int newLBTarget;
        int newRBTarget;
        int newRFTarget;
        int newLFTarget;


        newLBTarget = lB.getCurrentPosition() + (int) (inches * TICKS_PER_INCH_FORWARD);
        newRFTarget = rF.getCurrentPosition() + (int) (inches * TICKS_PER_INCH_FORWARD);
        newRBTarget = rB.getCurrentPosition() + (int)(inches * TICKS_PER_INCH_FORWARD);
        newLFTarget = lF.getCurrentPosition() + (int)(inches * TICKS_PER_INCH_FORWARD);

        // reset the timeout time and start motion.
        timer.reset();

        if ( speed > 0 ) { // Move forward
            lF.setPower(speed - rightSpeed);
            rF.setPower(speed ); //
            lB.setPower(speed );
            rB.setPower(speed - rightSpeed);
        } else {    // Move back
            lF.setPower(speed );
            rF.setPower(speed + rightSpeed);
            lB.setPower(speed + rightSpeed);
            rB.setPower(speed );

        }

        // keep looping while we are still active, and there is time left, and both motors are running.
        while (opMode.opModeIsActive() && (timer.time()< timeoutS*1000) ) {
            int encoderLF = Math.abs(lF.getCurrentPosition());
            int encoderRF = Math.abs(rF.getCurrentPosition());
            int encoderLB = Math.abs(lB.getCurrentPosition());
            int encoderRB = Math.abs(rB.getCurrentPosition());

            // stop if any wheel reach the encoder position
            if ( encoderLF > newLFTarget) break;
            if ( encoderLB > newLBTarget) break;
            if ( encoderRF > newLFTarget) break;
            if ( encoderRB > newRBTarget) break;
        }
        // Stop all motion
        this.stopAll();
    }

    public void moveBkwRight(double speed, double rightSpeed, double inches, double timeoutS) {
        resetEncoders();
        lB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        int newLBTarget;
        int newRBTarget;
        int newRFTarget;
        int newLFTarget;

        // Ensure that the opmode is still active

        // Determine new target position, and pass to motor controller
        if ( rightSpeed == 0 ) {
            newLFTarget = lF.getCurrentPosition() - (int) (inches * TICKS_PER_INCH_FORWARD);
            lF.setTargetPosition(newLFTarget);
            newRBTarget = rB.getCurrentPosition() - (int) (inches * TICKS_PER_INCH_FORWARD);
            rB.setTargetPosition(newRBTarget);

        }

        newLBTarget = lB.getCurrentPosition() - (int)(inches * TICKS_PER_INCH_FORWARD);
        lB.setTargetPosition(newLBTarget);
        newRFTarget = rF.getCurrentPosition() - (int)(inches * TICKS_PER_INCH_FORWARD);
        rF.setTargetPosition(newRFTarget);

        // Turn On RUN_TO_POSITION
        if ( rightSpeed == 0) { // turn off run to position mode, just run with PID mode
            lF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        rF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lB.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
        timer.reset();
        lF.setPower(Math.abs(-speed));
        rF.setPower(-(speed - rightSpeed));
        lB.setPower(Math.abs(-speed));
        rB.setPower(-(speed - rightSpeed));

        // keep looping while we are still active, and there is time left, and both motors are running.
        while ( rF.isBusy() && lB.isBusy() &&
                opMode.opModeIsActive() &&
                (timer.time() < timeoutS * 1000)) {

        }

        // Stop all motion
        this.stopAll();

        // Turn off RUN_TO_POSITION
        lF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // resetEncoders();
        //  sleep(250);   // optional pause after each move
    }


    public void moveBkwd(double speed, double inches, double timeoutS) {
        resetEncoders();
        lB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        int newLBTarget;
        int newRBTarget;
        int newRFTarget;
        int newLFTarget;

        // Ensure that the opmode is still active

        // Determine new target position, and pass to motor controller
        newLBTarget = lB.getCurrentPosition() - (int)(inches * TICKS_PER_INCH_FORWARD);
        newRBTarget = rB.getCurrentPosition() - (int)(inches * TICKS_PER_INCH_FORWARD);
        newLFTarget = lF.getCurrentPosition() - (int)(inches * TICKS_PER_INCH_FORWARD);
        newRFTarget = rF.getCurrentPosition() - (int)(inches * TICKS_PER_INCH_FORWARD);
        lF.setTargetPosition(newLFTarget);
        rF.setTargetPosition(newRFTarget);
        lB.setTargetPosition(newLBTarget);
        rB.setTargetPosition(newRBTarget);

        // Turn On RUN_TO_POSITION
        lF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rB.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
        timer.reset();
        lF.setPower(Math.abs(-speed));
        rF.setPower(Math.abs(-speed));
        lB.setPower(Math.abs(-speed));
        rB.setPower(Math.abs(-speed));

        // keep looping while we are still active, and there is time left, and both motors are running.
        while (opMode.opModeIsActive() &&
                (timer.time()< timeoutS*1000) &&
                (lF.isBusy() && rF.isBusy() && rB.isBusy() && lB.isBusy())) {
        }

        // Stop all motion
        this.stopAll();

        // Turn off RUN_TO_POSITION
        lF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // resetEncoders();
        //  sleep(250);   // optional pause after each move
    }

    public void spinClockwiseGyroNoCorrection(int degrees, double power, GyroSensor gyro, long
            timeoutMill) {
        long endTime = System.currentTimeMillis() + timeoutMill;
        int endHeading = gyro.getHeading() - degrees;
        boolean fineAdjustment = false;
        resetEncoders(); // reset encoder and turn on run_to_position mode
        lF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rotateCW(power);
        while (endTime > System.currentTimeMillis()) {
            int currentHeading = gyro.getHeading();
            if (!fineAdjustment) { // initial turn
                if (Math.abs(endHeading - currentHeading) <= 5) {
                    // stop turning
                    stopAll();
                    fineAdjustment = true; // enter fine tuning state
                }
            } else { // fine tuning
                if (Math.abs(endHeading - currentHeading) <= 1) {
                    // stop if within 1 degree
                    break;
                }
                if (endHeading < gyro.getHeading()) {
                    rotateCW(Functions.adjustmentSpeed);
                } else {
                    rotateCCW(Functions.adjustmentSpeed);
                }
            }
        }
        stopAll();
    }

    public void stopAll() {
        lF.setPower(0);
        lB.setPower(0);
        rF.setPower(0);
        rB.setPower(0);
    }

    public void setDriveMotorMode ( DcMotor.RunMode mode) {
        lB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void moveLeft(double speed, double inches, double timeoutS) {
        resetEncoders();
        lB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        int newLBTarget;
        int newRBTarget;
        int newRFTarget;
        int newLFTarget;

        // Ensure that the opmode is still active

        // Determine new target position, and pass to motor controller
        newLBTarget = lB.getCurrentPosition() - (int)(inches * TICKS_PER_INCH_FORWARD);
        newRBTarget = rB.getCurrentPosition() + (int)(inches * TICKS_PER_INCH_FORWARD);
        newLFTarget = lF.getCurrentPosition() + (int)(inches * TICKS_PER_INCH_FORWARD);
        newRFTarget = rF.getCurrentPosition() - (int)(inches * TICKS_PER_INCH_FORWARD);
        lF.setTargetPosition(newLFTarget);
        rF.setTargetPosition(newRFTarget);
        lB.setTargetPosition(newLBTarget);
        rB.setTargetPosition(newRBTarget);

        // Turn On RUN_TO_POSITION
        lF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rB.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
        timer.reset();
        lF.setPower(Math.abs(speed));
        rF.setPower(Math.abs(speed));
        lB.setPower(Math.abs(speed));
        rB.setPower(Math.abs(speed));

        // keep looping while we are still active, and there is time left, and both motors are running.
        while (opMode.opModeIsActive() &&
                (timer.time()< timeoutS*1000) &&
                (lF.isBusy() && rF.isBusy() && rB.isBusy() && lB.isBusy())) {
        }

        // Stop all motion
        this.stopAll();

        // Turn off RUN_TO_POSITION
        lF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //  sleep(250);   // optional pause after each move
    }

    public void moveRight(double speed, double inches, double timeoutS) {
        resetEncoders();
        lB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        int newLBTarget;
        int newRBTarget;
        int newRFTarget;
        int newLFTarget;

        // Ensure that the opmode is still active

        // Determine new target position, and pass to motor controller
        newLBTarget = lB.getCurrentPosition() + (int)(inches * TICKS_PER_INCH_FORWARD);
        newRBTarget = rB.getCurrentPosition() - (int)(inches * TICKS_PER_INCH_FORWARD);
        newLFTarget = lF.getCurrentPosition() - (int)(inches * TICKS_PER_INCH_FORWARD);
        newRFTarget = rF.getCurrentPosition() + (int)(inches * TICKS_PER_INCH_FORWARD);
        lF.setTargetPosition(newLFTarget);
        rF.setTargetPosition(newRFTarget);
        lB.setTargetPosition(newLBTarget);
        rB.setTargetPosition(newRBTarget);

        // Turn On RUN_TO_POSITION
        lF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rB.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
        timer.reset();
        lF.setPower(Math.abs(speed));
        rF.setPower(Math.abs(speed));
        lB.setPower(Math.abs(speed));
        rB.setPower(Math.abs(speed));

        // keep looping while we are still active, and there is time left, and both motors are running.
        while (opMode.opModeIsActive() &&
                (timer.time()< timeoutS*1000) &&
                (lF.isBusy() && rF.isBusy() && rB.isBusy() && lB.isBusy())) {
        }

        // Stop all motion
        this.stopAll();

        // Turn off RUN_TO_POSITION
        lF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //  sleep(250);   // optional pause after each move
    }

}
