package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.util.ElapsedTime;


public class EncoderTest extends PacmanBotHWB2 {
    boolean first=true,out,moveBasket;
    BasketController basketController;
    ElapsedTime timer = new ElapsedTime();
    boolean isOpen=false;
    boolean startBusy = false;
    int b=0;
    int maxDumpRange=185;

    @Override
    public void init() {
        setupHardware();
        basketController=new BasketController(basket,door,0,maxDumpRange);
    }

    @Override
    public void loop() {
        basketController.check();
        if(first) {
            first=false;
        }
        if((gamepad1.x) && b!=1) {
            b=1;
            basketController.swing(maxDumpRange, 0.4);
            startBusy = basketController.isBusy();
        }
        if((gamepad1.b) && b!=2) {
            b=2;
            basketController.swing(0, 0.4);
            startBusy = basketController.isBusy();
        }
        if((gamepad1.y) && b!=3) {
            b=3;
            basketController.dump(.4);
            startBusy = basketController.isBusy();
        }
        if(gamepad1.a) {
            b=4;
            if(isOpen) {
                basketController.close();
            }
            else {
                basketController.open();
            }
            isOpen=!isOpen;
        }
        telemetry.addData("b",b);
        telemetry.addData("isBusy", basketController.isBusy());
        telemetry.addData("StartBusy", startBusy);
    }
}

