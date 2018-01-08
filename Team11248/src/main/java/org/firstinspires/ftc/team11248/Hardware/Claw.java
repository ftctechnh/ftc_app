package org.firstinspires.ftc.team11248.Hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Tony_Air on 11/7/17.
 */

public class Claw {

    public enum Position {
        OPEN,
        RELEASE,
        GRAB,
        CLOSE,
    }

    public Position state;

    private double[] open, grab, close, release;

    private Servo tl, tr, bl, br;


    /**
     * @param hardwareMap robot's hardwaremap
     * @param servoNames String array of 4 Servonames of one claw in order of {top_left, top_right, bottom_left, bottom_right}
     * @param open Double array of 4 open values of the servos {top_left, top_right, bottom_left, bottom_right}
     * @param grab Double array of 4 grabbing values of the servos {top_left, top_right, bottom_left, bottom_right}
     * @param close Double array of 4 closed values of the servos {top_left, top_right, bottom_left, bottom_right}
     */
    public Claw(HardwareMap hardwareMap, String[] servoNames, double[] open, double[] release, double[] grab, double[] close ){

        this.open = open;
        this.release = release;
        this.grab = grab;
        this.close = close;

        this.tl = hardwareMap.servo.get(servoNames[0]);
        this.tr = hardwareMap.servo.get(servoNames[1]);
        this.bl = hardwareMap.servo.get(servoNames[2]);
        this.br = hardwareMap.servo.get(servoNames[3]);

    }

    public void open(){
        tl.setPosition(open[0]);
        tr.setPosition(open[1]);
        bl.setPosition(open[2]);
        br.setPosition(open[3]);

        state = Position.OPEN;
    }

    public void release(){

        tl.setPosition(release[0]);
        tr.setPosition(release[1]);
        bl.setPosition(release[2]);
        br.setPosition(release[3]);

        state = Position.RELEASE;


    }

    public void grab(){
        tl.setPosition(grab[0]);
        tr.setPosition(grab[1]);
        bl.setPosition(grab[2]);
        br.setPosition(grab[3]);

        state = Position.GRAB;
    }

    public void close() {

        tr.setPosition(close[1]);
        br.setPosition(close[3]);

        tl.setPosition(close[0]);
        bl.setPosition(close[2]);

        state = Position.CLOSE;
    }

}
