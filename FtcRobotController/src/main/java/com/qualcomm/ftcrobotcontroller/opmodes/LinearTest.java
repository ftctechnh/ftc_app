package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by wolfie on 1/16/16.
 */
public class LinearTest extends LinearOpMode {
    public DcMotor leftMotor;
    public DcMotor rightMotor;
    public static final int ROTATION = 1440;
    public void runOpMode(){
        leftMotor = hardwareMap.dcMotor.get("motorLeft");
        rightMotor = hardwareMap.dcMotor.get("motorRight");
        leftMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

        int currentLeft = leftMotor.getCurrentPosition();
        int currentRight = rightMotor.getCurrentPosition();

        leftMotor.setTargetPosition(currentLeft);
        rightMotor.setTargetPosition(currentRight);
        try{
            waitForStart();
        }
        catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
        leftMotor.setPower(1);
        rightMotor.setPower(1);

        int leftTarget = currentLeft + 5*ROTATION;
        int rightTarget = currentRight + 5*ROTATION;

        leftMotor.setTargetPosition(leftTarget);
        rightMotor.setTargetPosition(rightTarget);

        while(!isInRange(leftTarget,leftMotor.getCurrentPosition(),100)&&
                !isInRange(rightTarget, rightMotor.getCurrentPosition(),100)){}

        leftMotor.setPower(0);
        rightMotor.setPower(0);

        currentLeft = leftMotor.getCurrentPosition();
        currentRight = rightMotor.getCurrentPosition();
    }
    public boolean isInRange(int target,int val, int range){
        return (val>target-range)&&(val<target+range);
    }
}
