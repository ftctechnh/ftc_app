package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.util.ElapsedTime;

public class ColorSensorTest extends PacmanBotHardwareBase {
    final static VersionNumber version = new VersionNumber(1,0,0);
    ElapsedTime timer = new ElapsedTime();
    boolean checkRed = false;
    boolean drive = false;
    int red = 0;
    int blue = 0;
    int iterations = 0;

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
    public void loop() {
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
                if (red < blue) {
                    drive = true;
                    timer.reset();
                    timer.startTime();
                }
            }
            else if (timer.time() > 2 && timer.time()<3.5 && drive) {
                drive(0.3, 0);
            }
            else if(timer.time()>3.5 && drive) {
                drive(0, 0);
                arm.setPosition(0.6);
                thrower.setPosition(0);
            }
        telemetry.addData("=== iteration ", iterations);
        telemetry.addData("=== timer     ", timer.time());
        telemetry.addData("=== Red  ", red);
        telemetry.addData("=== Blue ", blue);
        telemetry.addData("=== Red1  ", red1);
        telemetry.addData("=== Blue1 ", blue1);
        telemetry.addData("=== Red2  ", red2);
        telemetry.addData("=== Blue2 ", blue2);

            iterations++;
    }
}

