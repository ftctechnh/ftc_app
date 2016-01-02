package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


public class BasketController {
    RunToPositionController mBasketController;
    Servo mDoor;
    boolean first=true;
    boolean mShaking=false;
    int mShakeCount=0;
    int mHomePos,mOutPos,mShakePos;

    public BasketController(DcMotor basket,Servo door,int homePos,int outPos) {
        mHomePos=homePos;
        mOutPos=outPos;
        mDoor=door;
        mBasketController=new RunToPositionController(basket);
        close();
    }

    public void open() {
        mDoor.setPosition(0);
    }
    public void close() {
        mDoor.setPosition(1);
    }
    public void swing(int pos, double power) {
        mBasketController.goTo(pos, power);
    }
    public void dump(double shakePercent) {
        mShakePos=(int)(mOutPos-((mOutPos-mHomePos)*shakePercent));
        mShaking=true;
        mShakeCount=0;
        close();
        mBasketController.goTo(mOutPos, 1);
    }
    public boolean isBusy() {
        return mBasketController.isBusy();
    }
    public void check()
    {
        mBasketController.check();
        if(mShaking && !mBasketController.isBusy()) {
            if(mShakeCount==0) {
                open();
            }
            if(mShakeCount%2==0) {
                mBasketController.goTo(mShakePos,1);
            }
            else {
                mBasketController.goTo(mOutPos, 1);
            }
            if(mShakeCount==9) {
                mShaking=false;
                close();
                mBasketController.goTo(mHomePos,0.4);
            }
            mShakeCount++;
        }
    }
}

