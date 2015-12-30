package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by sathk_000 on 12/17/2015.
 */
public class ServoManager8737 {
    private Servo mServo;
    private ElapsedTime mTimer = new ElapsedTime();
    private double mFinalTime;
    private double mFinalPosition;
    private double mOldPosition;

    ServoManager8737(Servo servo){
        mServo = servo;
        mFinalPosition = mServo.getPosition();
    }

    public void setPosition(double position, double time) {
        mFinalPosition = position;
        mFinalTime = time;
        mOldPosition = mServo.getPosition();
        mTimer.reset();
        if (time == 0) { //if there is no time, go immediately
            mServo.setPosition(position);
        }
    }

    public void check(){
        double currentPosition = mServo.getPosition();
        if (currentPosition != mFinalPosition){
            double currentTime = mTimer.time();
            if (currentTime < mFinalTime) {
                double newPosition = mOldPosition + (currentTime / mFinalTime) * (mFinalPosition - mOldPosition);
                mServo.setPosition(newPosition);
            }
            else{
                mServo.setPosition(mFinalPosition);
            }
        }

    }

    public boolean isBusy() {
        return (mServo.getPosition() != mFinalPosition);
    }

}
