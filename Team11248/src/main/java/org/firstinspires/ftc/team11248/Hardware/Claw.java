package org.firstinspires.ftc.team11248.Hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Tony_Air on 11/7/17.
 */

public class Claw {


    private double[] open, release, close;

    private Servo tl, tr, bl, br;


    /**
     * @param hardwareMap robot's hardwaremap
     * @param servoNames String array of 4 Servonames of one claw in order of {top_left, top_right, bottom_left, bottom_right}
     * @param open Double array of 4 open values of the servos {top_left, top_right, bottom_left, bottom_right}
     * @param release Double array of 4 release values of the servos {top_left, top_right, bottom_left, bottom_right}
     * @param close Double array of 4 closed values of the servos {top_left, top_right, bottom_left, bottom_right}
     */
    public Claw(HardwareMap hardwareMap, String[] servoNames, double[] open, double[] release, double[] close){

        this.open = open;
        this.release = release;
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
    }

    public void release(){
        tl.setPosition(release[0]);
        tr.setPosition(release[1]);
        bl.setPosition(release[2]);
        br.setPosition(release[3]);
    }

    public void close(){
        tl.setPosition(close[0]);
        tr.setPosition(close[1]);
        bl.setPosition(close[2]);
        br.setPosition(close[3]);
    }

}
