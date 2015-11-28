package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.util.ElapsedTime;

public class ColorSensorTest extends PacmanBotHardwareBase {
    final static VersionNumber version = new VersionNumber(1,0,0);
    ElapsedTime timer=new ElapsedTime();
    boolean checkRed=false;
    boolean drive=false;
    int red=0;
    int blue=0;
    int iterations=0;

    @Override
    public void init() {
        telemetry.addData("Program", "Pacman Auto");
        telemetry.addData("Version", version.string());
        telemetry.addData("Hardware Base Version", hwbVersion.string());
        setupHardware();
    }
    @Override
    public void loop() {
        if(iterations<5) {
            sweeper.setPosition(0.5);
            red += eye.red();
            blue += eye.blue();
            String sr = String.format("%d", red);
            String sb = String.format("%d", blue);
            telemetry.addData("Red", sr);
            telemetry.addData("Blue", sb);
        }
        else if(iterations==5) {
            if(red<blue) {
                sweeper.setPosition(0.53);

                timer.startTime();
                drive=true;
            }
            else {
                checkRed=true;
            }
        }
        else if(iterations<11 && checkRed) {
            sweeper.setPosition(0.4);
            red += eye.red();
            blue += eye.blue();
            String sr = String.format("%d", red);
            String sb = String.format("%d", blue);
            telemetry.addData("Red", sr);
            telemetry.addData("Blue", sb);
        }
        else if(iterations==11 && checkRed) {
            if(red<blue) {
                drive=true;
                timer.startTime();
            }
        }
        else if(timer.time()>2 && drive) {
            drive(0.3,0);
        }
        else if(timer.time()>3.5 && timer.time()<4 && drive){
            drive(0,0);
        }
        else {
            sweeper.setPosition(0.6);
            thrower.setPosition(0);
        }
        iterations++;
    }
}

