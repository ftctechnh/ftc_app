package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.technicbots.MainRobot;
/**
 * Created by brandon on 9/26/2015.
 */

public class ResQTeleop extends OpMode {
    //Samuel's Comment123
    private ElapsedTime mStateTime = new ElapsedTime();
    //experimental
    final static double BOXSERVO_MIN_RANGE  = 0.01;
    final static double BOXSERVO_MAX_RANGE  = 1;

    final static double CLAMP_MIN_RANGE = 0.01;
    final static double CLAMP_MAX_RANGE = 0.70;

    double boxservoPosition;
    double boxservoPosition2;
    double clampPosition;
    double lastTime;

    double boxServoDelta = 0.74;
    double boxServoDelta2 = 0.19;
    double clampDelta = 0.69;

    private ElapsedTime runtime = new ElapsedTime();

    DcMotor LinearSlide1;
    DcMotor LinearSlide2;
    DcMotor rightMotor;
    DcMotor leftMotor;
    DcMotor harvester;

    Servo boxservo;
    Servo clamp;

    MainRobot mainRobot;
    public ResQTeleop() {

    }

    /*
    * Code to run when the op mode is first enabled goes here
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
    */
    @Override
    public void init() {

        mainRobot = new MainRobot(leftMotor, rightMotor, null, null, null, null, null);
    }

    /*
    * This method will be called repeatedly in a loop
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
    */
    @Override
    public void loop() {

    }
    public void stop() {

    }

}
