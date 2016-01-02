package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by bennettliu on 1/1/16.
 */
public class RunToPositionController {
    DcMotor mEncoderMotor;
    boolean running=false;
    boolean mAutoReset;

    //creates mEncoderMotor for the given motor encoderMotor
    //saves motor encoderMotor and sets to correct mode
    public RunToPositionController(DcMotor encoderMotor, boolean autoReset) {
        mAutoReset=autoReset;
        mEncoderMotor=encoderMotor;
        mEncoderMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        mEncoderMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
    }
    //moves motor to desired position at desired speed
    public void goTo (int pos, double power) {
        if(mAutoReset) {
            pos+=mEncoderMotor.getCurrentPosition();
        }
        mEncoderMotor.setTargetPosition(pos);
        mEncoderMotor.setPower(power);
        running=true;
    }
    //checks if motor is moving to a position
    public boolean isBusy() {
        return mEncoderMotor.isBusy() ||
                (Math.abs(mEncoderMotor.getCurrentPosition()-mEncoderMotor.getTargetPosition())>4 && mEncoderMotor.getPower()!=0);//checks if busy
    }
    //call within loop always to silence non-moving motor
    public void check() {
        if(running && !isBusy()) {
            mEncoderMotor.setPower(0);//stops noise
            running=false;
        }
    }
}
