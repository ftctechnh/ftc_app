package org.firstinspires.ftc.teamcode.robotutil;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.robotcore.external.Telemetry;

//import com.qualcomm.hardware.adafruit.JustLoggingAccelerationIntegrator;

public class DriveTrainNew {
    private ElapsedTime timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    private DcMotor lfDrive,lbDrive,rfDrive,rbDrive;
    private BNO055IMU adaImu;
    private IMU imu;
    private LinearOpMode opMode;
    private MotorGroup driveMotors;

    public DriveTrainNew(LinearOpMode opMode) {
        this.opMode = opMode;
        lfDrive = opMode.hardwareMap.dcMotor.get("lfDrive");
        lbDrive = opMode.hardwareMap.dcMotor.get("lbDrive");
        rfDrive = opMode.hardwareMap.dcMotor.get("rfDrive");
        rbDrive = opMode.hardwareMap.dcMotor.get("rbDrive");

        lfDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        lbDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rbDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        rfDrive.setDirection(DcMotorSimple.Direction.FORWARD);

        adaImu = opMode.hardwareMap.get(BNO055IMU.class, "imu");
        imu = new IMU(adaImu);
        DcMotor[] driveMotorArray = new DcMotor[]{lfDrive,rfDrive,lbDrive,rbDrive};
        this.driveMotors = new MotorGroup(driveMotorArray);
        initMotors();
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

    public void stopAll(){
        setPowers(0, 0, 0, 0);
    }

    public void move(Direction direction,double power){
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
                setPowers(power,-power,
                        -power,power);
                break;
            case RIGHT:
                setPowers(-power,power,
                        power,-power);
                break;
            case CW:
                setPowers(power,-power,
                        power,-power);
                break;
            case CCW:
                setPowers(-power,power,
                        -power,power);
                break;
            default:
                setPowers(0, 0, 0, 0);
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
                rfTarget = rfDrive.getCurrentPosition() + (int) (inches * Values.TICKS_PER_INCH_FORWARD);
                lfTarget = lfDrive.getCurrentPosition() + (int) (inches * Values.TICKS_PER_INCH_FORWARD);
                lbTarget = lbDrive.getCurrentPosition() + (int) (inches * Values.TICKS_PER_INCH_FORWARD);
                rbTarget = rbDrive.getCurrentPosition() + (int) (inches * Values.TICKS_PER_INCH_FORWARD);
                break;
            case BACK:
                rfTarget = rfDrive.getCurrentPosition() - (int) (inches * Values.TICKS_PER_INCH_FORWARD);
                lfTarget = lfDrive.getCurrentPosition() - (int) (inches * Values.TICKS_PER_INCH_FORWARD);
                lbTarget = lbDrive.getCurrentPosition() - (int) (inches * Values.TICKS_PER_INCH_FORWARD);
                rbTarget = rbDrive.getCurrentPosition() - (int) (inches * Values.TICKS_PER_INCH_FORWARD);
                break;
            case RIGHT:
                rfTarget = rfDrive.getCurrentPosition() + (int) (inches * Values.TICKS_PER_INCH_FORWARD);
                lfTarget = lfDrive.getCurrentPosition() + (int) (inches * Values.TICKS_PER_INCH_FORWARD);
                lbTarget = lbDrive.getCurrentPosition() - (int) (inches * Values.TICKS_PER_INCH_FORWARD);
                rbTarget = rbDrive.getCurrentPosition() - (int) (inches * Values.TICKS_PER_INCH_FORWARD);
                break;
            case LEFT:
                rfTarget = rfDrive.getCurrentPosition() - (int) (inches * Values.TICKS_PER_INCH_FORWARD);
                lfTarget = lfDrive.getCurrentPosition() - (int) (inches * Values.TICKS_PER_INCH_FORWARD);
                lbTarget = lbDrive.getCurrentPosition() + (int) (inches * Values.TICKS_PER_INCH_FORWARD);
                rbTarget = rbDrive.getCurrentPosition() + (int) (inches * Values.TICKS_PER_INCH_FORWARD);
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

    private double minAbs(double a, double b) {
        if (Math.abs(a) > Math.abs(b)) {
            return a;
        } else {
            return b;
        }
    }

    public void moveP(Direction direction,double power,double inches,double timeoutS) {
        final double minError = Values.TICKS_PER_INCH_FORWARD * 0.5;
        final double kp = 1 / (6 * Values.TICKS_PER_INCH_FORWARD);

        driveMotors.resetEncoders();
        driveMotors.useEncoders();
        int rfTarget,lfTarget,lbTarget,rbTarget;
        switch (direction) {
            case FORWARD:
                rfTarget = rfDrive.getCurrentPosition() + (int) (inches * Values.TICKS_PER_INCH_FORWARD);
                lfTarget = lfDrive.getCurrentPosition() + (int) (inches * Values.TICKS_PER_INCH_FORWARD);
                lbTarget = lbDrive.getCurrentPosition() + (int) (inches * Values.TICKS_PER_INCH_FORWARD);
                rbTarget = rbDrive.getCurrentPosition() + (int) (inches * Values.TICKS_PER_INCH_FORWARD);
                break;
            case BACK:
                rfTarget = rfDrive.getCurrentPosition() - (int) (inches * Values.TICKS_PER_INCH_FORWARD);
                lfTarget = lfDrive.getCurrentPosition() - (int) (inches * Values.TICKS_PER_INCH_FORWARD);
                lbTarget = lbDrive.getCurrentPosition() - (int) (inches * Values.TICKS_PER_INCH_FORWARD);
                rbTarget = rbDrive.getCurrentPosition() - (int) (inches * Values.TICKS_PER_INCH_FORWARD);
                break;
            case RIGHT:
                rfTarget = rfDrive.getCurrentPosition() + (int) (inches * Values.TICKS_PER_INCH_FORWARD);
                lfTarget = lfDrive.getCurrentPosition() + (int) (inches * Values.TICKS_PER_INCH_FORWARD);
                lbTarget = lbDrive.getCurrentPosition() - (int) (inches * Values.TICKS_PER_INCH_FORWARD);
                rbTarget = rbDrive.getCurrentPosition() - (int) (inches * Values.TICKS_PER_INCH_FORWARD);
                break;
            case LEFT:
                rfTarget = rfDrive.getCurrentPosition() - (int) (inches * Values.TICKS_PER_INCH_FORWARD);
                lfTarget = lfDrive.getCurrentPosition() - (int) (inches * Values.TICKS_PER_INCH_FORWARD);
                lbTarget = lbDrive.getCurrentPosition() + (int) (inches * Values.TICKS_PER_INCH_FORWARD);
                rbTarget = rbDrive.getCurrentPosition() + (int) (inches * Values.TICKS_PER_INCH_FORWARD);
                break;

            default:
                rfTarget = 0;
                lfTarget = 0;
                lbTarget = 0;
                rbTarget = 0;

        }

        double rfError = rfTarget - rfDrive.getCurrentPosition();
        double lfError = rfTarget - lfDrive.getCurrentPosition();
        double rbError = rfTarget - rbDrive.getCurrentPosition();
        double lbError = rfTarget - lbDrive.getCurrentPosition();

        double startTime = System.currentTimeMillis();

        while (Math.abs(rfError) > minError && Math.abs(lfError) > minError &&
                Math.abs(rbError) > minError && Math.abs(lbError) > minError &&
                (System.currentTimeMillis() - startTime) / 1000 < timeoutS) {

            double rfPower = minAbs(power, kp * rfError * power);
            double lfPower = minAbs(power, kp * lfError * power);
            double rbPower = minAbs(power, kp * rbError * power);
            double lbPower = minAbs(power, kp * lbError * power);

            setPowers(lfPower, rfPower,
                    lbPower, rbPower);

            rfError = rfTarget - rfDrive.getCurrentPosition();
            lfError = lfTarget - lfDrive.getCurrentPosition();
            rbError = rbTarget - rbDrive.getCurrentPosition();
            lbError = lbTarget - lbDrive.getCurrentPosition();
        }

        stopAll();

    }

    // Untested proportional IMU rotation
    public void rotateIMU(Direction direction, double angle, double power, double timeoutS) {
        final double kp = 1.0 / 45.0;
        final double minError = 2.0;

        double currentHeading = imu.getAngle();
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

        double error = fixAngle(targetHeading - currentHeading);
        double startTime = System.currentTimeMillis();

        Telemetry.Item telPower = opMode.telemetry.addData("power", 0);
        Telemetry.Item telCurrAngle = opMode.telemetry.addData("angle",currentHeading);
        Telemetry.Item telError = opMode.telemetry.addData("error",error);
        Telemetry.Item powerStat = opMode.telemetry.addData("kp,power,error",String.format("%.3f || %.3f || %.3f",kp,power,error));

        Telemetry.Item timeLeft = opMode.telemetry.addData("time left",(System.currentTimeMillis() - startTime));
        while (Math.abs(error) > minError
//                && (System.currentTimeMillis() - startTime) / 1000 < timeoutS
                ) {
            double proportionalPower = minAbs(power, Math.abs(kp * power * error));

            move(Direction.CW, proportionalPower);

            telPower.setValue(proportionalPower);
            telCurrAngle.setValue(currentHeading);
            powerStat.setValue(String.format("%.3f || %.3f || %.3f",kp,power,error));
            telError.setValue(error);
            timeLeft.setValue((System.currentTimeMillis() - startTime));
            this.opMode.telemetry.update();

            currentHeading = imu.getAngle();
            error = fixAngle(targetHeading - currentHeading);
        }

        driveMotors.stopAll();
    }

    public void rotateToHeading(double targetHeading, double power, double timeoutS) {
        Direction direction;
        double angle = targetHeading - imu.getAngle();
        if (angle < 0) {
            angle = Math.abs(angle);
            direction = Direction.CCW;
        } else {
            direction = Direction.CW;
        }
        rotateIMU(direction, angle, power, timeoutS);
    }

    // Get angle between -180 and 180
    private double fixAngle(double angle) {
        while (angle > 180 || angle < -180) {
            if (angle > 180) {
                angle -= 360;
            } else {
                angle += 360;
            }
        }
        return angle;
    }

}
