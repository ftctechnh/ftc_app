package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.util.ElapsedTime;

public class BlueDelayBeacon extends PacmanBotHardwareBase {
    final static VersionNumber version = new VersionNumber(1,0,0);
    boolean set=false;
    boolean moveameoba=true;
    ElapsedTime timer=new ElapsedTime();
    boolean checkRed=false;
    boolean drive=false;
    int red=0;
    int blue=0;
    int iterations=10000;

    int red1 = 999, red2 = 999;
    int blue1 = 999, blue2 = 999;

    @Override
    public void init() {
        telemetry.addData("Program", "Pacman Auto");
        telemetry.addData("Version", version.string());
        telemetry.addData("Hardware Base Version", hwbVersion.string());
        setupHardware();
        timer.reset();
    }

    @Override
    public void loop(){
        if(moveameoba) {

            brush.setPower(-1);
        }
        else {

            brush.setPower(0);
        }
        if(!set){
            set=true;
            timer.reset();
        }
        if (timer.time() > 10 && timer.time() < 11.15) {
            drive(1,0); //1.15
        }
        else if (timer.time() < 11.55) {
            drive(0,1); //1.4
        }
        else if (timer.time() < 14.9) {
            drive(1,0); //3.75, 3.5
        }
        else if (timer.time() < 15.3) {
            drive(0,1); //4.0, 3.75
        }
        else if (timer.time() < 16.3) {//5.4
            drive(0.5,0); //5.2, 5.4
        }
        else {
            drive(0,0);
            iterations=-1;
            moveameoba=false;
        }
        if (iterations < 20) {
            arm.setPosition(0.5);
            red += eye.red();
            blue += eye.blue();
        }
        else if (iterations == 20) {
            red1 = red;
            blue1 = blue;
            if (red < blue) {
                arm.setPosition(0.53);
                drive = true;
                timer.reset();
                timer.startTime();
            }
            else {
                checkRed = true;
                red = 0;
                blue = 0;
            }
        }
        else if (iterations < 121 && checkRed) {
            arm.setPosition(0.4);
            red += eye.red();
            blue += eye.blue();
        }
        else if (iterations == 121 && checkRed) {
            red2 = red;
            blue2 = blue;
            if (red > blue) {
                drive = true;
                timer.reset();
                timer.startTime();
            }
        }
        else if (timer.time() > 2 && timer.time()<2.5 && drive && iterations!=10000) {
            drive(0.3, 0);
        }
        else if(timer.time() > 2.5 && timer.time() < 6.2 && drive && iterations!=10000) {
            drive(0, 0);
            arm.setPosition(0.6);
            thrower.setPosition(0);
        }
        if(iterations<10000) {
            iterations++;
        }
    }
}
