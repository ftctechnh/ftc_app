package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.util.ElapsedTime;

public class ColorSensorTest extends PacmanBotHardwareBase {
    final static VersionNumber version = new VersionNumber(1,0,0);
    ElapsedTime timer = new ElapsedTime();
    boolean checkRed = false;
    boolean drive = false;
//    boolean positioned = false;
//    boolean EMERGENCY = false;
//    double middle;
    int red = 0;
    int blue = 0;
//    int color = 0;
    int iterations = 0;

    int red1 = 999, red2 = 999;
    int blue1 = 999, blue2 = 999;
    

//    public double binSearchIt(double angle, double modifier) {
//        color = 0;
//        sweeper.setPosition(angle);
//        color += eye.red()+eye.blue();
//        if(color > 0) {
//            angle += modifier;
//        }
//        else {
//            angle -= modifier;
//        }
//        return angle;
//    }
//
//    public double  middleValue() {
//        double rightEdge = 0.4;
//        sweeper.setPosition(rightEdge);//range=0.2 through 0.6
//        color += eye.red() + eye.blue();
//        if(color > 0) {//1st calibration
//            rightEdge += 0.2;
//        }
//        else {
//            rightEdge = 0.5;
//            sweeper.setPosition(rightEdge);
//            color += eye.red() + eye.blue();
//            if(color > 0) {//1st calibration
//                rightEdge += 0.2;
//            }
//            else {
//                rightEdge = 0.3;
//                sweeper.setPosition(rightEdge);//range=0.2 through 0.6
//                color += eye.red() + eye.blue();
//                if(color > 0) {//1st calibration
//                    rightEdge += 0.2;
//                }
//                else {
//                    return 1;
//                }
//            }
//        }
//        double leftEdge = rightEdge;//already checked, so copy value
//
//        rightEdge = binSearchIt(rightEdge,0.1);
//        rightEdge = binSearchIt(rightEdge,0.05);
//        rightEdge = binSearchIt(rightEdge,0.025);
//        rightEdge = binSearchIt(rightEdge,0.012);
//        leftEdge = binSearchIt(leftEdge,-0.1);
//        leftEdge = binSearchIt(leftEdge,-0.05);
//        leftEdge = binSearchIt(leftEdge,-0.025);
//        leftEdge = binSearchIt(leftEdge,-0.012);
//        return (rightEdge+leftEdge)/2;
//    }

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
//        if(!EMERGENCY) {//Emergency break
//            while(!positioned) {
//                middle = middleValue();
//                if(middle > 0.4 && middle < 0.5) {
//                    positioned = true;
//                }
//                else if(middle == 1) {
//                    EMERGENCY = true;
//                }
//                else {
//                    timer.reset();
//                    timer.startTime();
//                    while(timer.time()<1) {
//                        drive(-0.3,0);
//                    }
//                    while(timer.time()<1.1) {
//                        drive(0,0.3);
//                    }
//                    while(timer.time()<2.1) {
//                        drive(0.3,0);
//                    }
//                    drive(0,0);
//                }
//            }
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
            else if (iterations < 41 && checkRed) {
                arm.setPosition(0.4);
                red += eye.red();
                blue += eye.blue();
//            blue += eye.blue();
//            String sr = String.format("%d", red);
//            String sb = String.format("%d", blue);
//            telemetry.addData("Red", sr);
//            telemetry.addData("Blue", sb);
            }
            else if (iterations == 41 && checkRed) {
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
        //}
    }
}

