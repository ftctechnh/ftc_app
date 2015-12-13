package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Peter on 11/12/2015.
 */
public class TestBot implements DriverInterface
{

    static DcMotor leftMotor;
    static DcMotor rightMotor;
    private static final String leftMotorName = "leftMotor";
    private static final String rightMotorName = "rightMotor";

    public TestBot(HardwareMap hardwareMap)
    {
        leftMotor = hardwareMap.dcMotor.get(leftMotorName);
        rightMotor = hardwareMap.dcMotor.get(rightMotorName);
        rightMotor.setDirection(DcMotor.Direction.REVERSE);
    }


    @Override
    public void moveStraightEncoders(float inches, float speed)
    {
        leftMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        rightMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        //makes sure the encoders reset
        while(leftMotor.getCurrentPosition() != 0){}
        while(rightMotor.getCurrentPosition() != 0){}
        /*leftMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        leftMotor.setTargetPosition((int)(131.65432 * inches));
        rightMotor.setTargetPosition((int)(131.65432 * inches));*///previous code to run to set positions with setTargetPosition function
        leftMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        if(speed < 0)
        {
            speed*=-1.0;
            inches*=-1.0;
        }
        int encoderTarget = ((int)(131.65432 * inches));
        if(encoderTarget > 0)
        {
            leftMotor.setPower(speed);
            rightMotor.setPower(speed);
            while (leftMotor.getCurrentPosition() < encoderTarget) {}
            leftMotor.setPower(0.0f);
            rightMotor.setPower(0.0f);
        }
        else
        {
            leftMotor.setPower(-speed);
            rightMotor.setPower(-speed);
            while (leftMotor.getCurrentPosition() > encoderTarget) {}
            leftMotor.setPower(0.0f);
            rightMotor.setPower(0.0f);
        }
    }

    public void pivotTurn(float degrees, float speed, boolean useGyro) {

    }

    public void spinOnCenter(float degrees, float speed) {

    }


    public void pivotTurn(float degrees, float speed)
    {
        leftMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        rightMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        //makes sure the encoders reset
        while(leftMotor.getCurrentPosition() != 0){}
        while(rightMotor.getCurrentPosition() != 0){}
        /*leftMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        leftMotor.setTargetPosition((int)(131.65432 * inches));
        rightMotor.setTargetPosition((int)(131.65432 * inches));*///previous code to run to set positions with setTargetPosition function
        leftMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        int encoderPivotTarget = ((int)(131.65432 * 94.24778 * degrees/360));
        if (encoderPivotTarget > 0 && speed > 0)
        {
            rightMotor.setPower(speed);
            while(rightMotor.getCurrentPosition() < encoderPivotTarget){}
            rightMotor.setPower(0.0f);
        }
        else if(encoderPivotTarget < 0 && speed > 0)
        {
            leftMotor.setPower(speed);
            while(leftMotor.getCurrentPosition() < (encoderPivotTarget * -1)){}
            leftMotor.setPower(0.0f);
        }
        else if(encoderPivotTarget > 0 && speed < 0)
        {
            leftMotor.setPower(speed);
            while (leftMotor.getCurrentPosition() > encoderPivotTarget * -1){}
            leftMotor.setPower(0.0f);
        }
        else if(encoderPivotTarget < 0 && speed < 0)
        {
            rightMotor.setPower(speed);
            while (rightMotor.getCurrentPosition() > (encoderPivotTarget)){}
            rightMotor.setPower(0.0f);
        }
    }


    public void spinOnCenter(float degrees, float speed)
    {
        leftMotor.setPower(0.0f);
        rightMotor.setPower(0.0f);
        leftMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        rightMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        //makes sure the encoders reset
        while(leftMotor.getCurrentPosition() != 0){}
        while(rightMotor.getCurrentPosition() != 0){}
        leftMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        int encoderSpinTarget = ((int)(131.65432 * 47.12389 * degrees/360));
        if(encoderSpinTarget > 0 && speed > 0)
        {
            leftMotor.setPower(speed);
            rightMotor.setPower(-speed);
            while(leftMotor.getCurrentPosition() < encoderSpinTarget){}
            leftMotor.setPower(0.0f);
            rightMotor.setPower(0.0f);
        }
        else if(encoderSpinTarget > 0 && speed < 0)
        {
            leftMotor.setPower(speed);
            rightMotor.setPower(-speed);
            while(rightMotor.getCurrentPosition() < encoderSpinTarget){}
            leftMotor.setPower(0.0f);
            rightMotor.setPower(0.0f);
        }
        else if(encoderSpinTarget < 0 && speed > 0)
        {
            leftMotor.setPower(-speed);
            rightMotor.setPower(speed);
            while(rightMotor.getCurrentPosition() < -encoderSpinTarget){}
            leftMotor.setPower(0.0f);
            rightMotor.setPower(0.0f);
        }
        else if(encoderSpinTarget < 0 && speed < 0)
        {
            leftMotor.setPower(-speed);
            rightMotor.setPower(speed);
            while(leftMotor.getCurrentPosition() < -encoderSpinTarget){}
            leftMotor.setPower(0.0f);
            rightMotor.setPower(0.0f);
        }
    }

    public float leftTurn(float degrees)
    {
        return degrees;
    }
    public float rightTurn(float degrees)
    {
        return degrees;
    }
    @Override
    public void stop()
    {
        leftMotor.setPower(0.0f);
        rightMotor.setPower(0.0f);
    }

    @Override
    public void pivotTurn(float degrees, float speed, boolean useGyro) {

    }

    @Override
    public void spinOnCenter(float degrees, float speed, boolean useGyro) {

    }

    @Override
    public void pitchFrontTracks(float time, float speed)
    {

    }

    @Override
    public void pitchBackTracks(float time, float speed)
    {

    }

}
