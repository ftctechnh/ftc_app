package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.util.ElapsedTime;


public class EncoderTest extends PacmanBotHWB2 {
    boolean first=true,out,moveBasket;
    ElapsedTime timer = new ElapsedTime();
    ElapsedTime timer2 = new ElapsedTime();

    final static VersionNumber version = new VersionNumber(1,0,0);
    @Override
    public void init() {
        telemetry.addData("Program", "Pacman Auto");
        telemetry.addData("Version", version.string());
        telemetry.addData("Hardware Base Version", hwbVersion.string());
        setupHardware();
    }

    @Override
    public void loop() {
        if(first) {
            first=false;
            update();
            basket.setPower(0.35);
            basket.setTargetPosition(150);
            timer.reset();
        }
        else if(!basket.isBusy() && timer.time()>0.2 && !moveBasket) {
            basket.setPower(0);
            timer.reset();
            moveBasket=true;
        }
        else if(timer.time()>1 && timer.time()<10 && moveBasket) {
            basket.setPower(0.8);
            if(out && !basket.isBusy() && timer2.time()>0.2) {
                basket.setTargetPosition(170);
                timer2.reset();
                out=!out;
            }
            else if(!basket.isBusy() && timer2.time()>0.2){
                basket.setTargetPosition(130);
                timer2.reset();
                out=!out;
            }
        }
        else {
            basket.setPower(0);
        }
        telemetry.addData("Encoder", basket.getCurrentPosition());
        telemetry.addData("Busy", basket.isBusy());
    }
}

