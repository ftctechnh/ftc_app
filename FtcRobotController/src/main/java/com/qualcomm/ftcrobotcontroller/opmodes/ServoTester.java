package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by tdoylend on 2015-12-01.
 */
public class ServoTester extends PacmanBotManual3000 {
    ElapsedTime timer = new ElapsedTime();
    Servo test;

    public void init() {
        timer.reset();
        test = hardwareMap.servo.get("test");
        setupHardware();
    }

    public void loop() {
        test.setPosition(Math.sin(timer.time()*4)/2 + .5);
    }
}
