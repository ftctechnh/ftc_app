package org.firstinspires.ftc.teamcode.robotutil;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by antonlin on 4/24/18.
 */

public class Robot{


    private DcMotor rF, rB, lF, lB;
    private IMU imu;
    DcMotor[] driveMotors = {lF,lB,rF,rB};
    DcMotor[] allMotors = {lF,lB,rF,rB};
    OpMode opMode;

    Robot(OpMode opMode){
        this.opMode = opMode;


        lF = this.opMode.hardwareMap.dcMotor.get("lF");
        rF = this.opMode.hardwareMap.dcMotor.get("rF");
        lB = this.opMode.hardwareMap.dcMotor.get("lB");
        rB = this.opMode.hardwareMap.dcMotor.get("rB");
        BNO055IMU adaImu = this.opMode.hardwareMap.get(BNO055IMU.class, "imu");
        imu = new IMU(adaImu);

        useEncoders(allMotors);
        setBrake(allMotors);

        rF.setDirection(DcMotorSimple.Direction.REVERSE);
        rB.setDirection(DcMotorSimple.Direction.REVERSE);
        lB.setDirection(DcMotorSimple.Direction.FORWARD);
        lF.setDirection(DcMotorSimple.Direction.FORWARD);

    }

    public void useEncoders(DcMotor[] motorArray){

        for(DcMotor motor : motorArray){
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    public void dontUseEncoders(DcMotor[] motorArray){
        for(DcMotor motor : motorArray){
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }
    public void setBrake(DcMotor[] motorArray){
        for(DcMotor motor : motorArray){
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
    }

    public void setCoast(DcMotor[] motorArray){
        for(DcMotor motor : motorArray){
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
    }

    public void resetEncoders(DcMotor[] motorArray){
        for(DcMotor motor: motorArray) {
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }
    public double getHeading(){
        return (imu.getAngle());

    }
    public void rotate(Values.RotationMode rotationMode, double angle, double speed, double timeoutS){

    }

    public void driveDist(double power, double distance,double movementAngle, boolean ramp, boolean imuGuidance, long timeoutS) {
        resetEncoders(driveMotors);


        int lBstart = lB.getCurrentPosition();
        int rBstart = rB.getCurrentPosition();
        int rFstart = rF.getCurrentPosition();
        int lFstart = lF.getCurrentPosition();

        // Ensure that the opmode is still active

        // Determine new target position, and pass to motor controller

        double ticksToMove = (distance * Values.TICKS_PER_INCH_FORWARD);
        double newLBTarget = lB.getCurrentPosition() + ticksToMove;
        double newRBTarget = rB.getCurrentPosition() - ticksToMove;
        double newLFTarget = lF.getCurrentPosition() - ticksToMove;
        double newRFTarget = rF.getCurrentPosition() + ticksToMove;

        double avg = 0;

        long endTime = System.currentTimeMillis() + 1000 * timeoutS;
        while (System.currentTimeMillis() <= endTime && avg < ticksToMove) {
            int currentLb = lB.getCurrentPosition();
            int currentLf = lF.getCurrentPosition();
            int currentRb = rB.getCurrentPosition();
            int currentRf = rF.getCurrentPosition();
            avg = (currentLb - lBstart + Math.abs(currentLf - lFstart) + Math.abs(currentRb - rBstart) + Math.abs(currentRf - rFstart)) / 4;

        }
    }



    public void driveToPoint(double power, double x, double y, boolean ramp,boolean imuGuidance, boolean holdExistingAngle, double timeoutS){

    }



}
