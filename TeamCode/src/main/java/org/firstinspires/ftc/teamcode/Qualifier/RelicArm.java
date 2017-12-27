package org.firstinspires.ftc.teamcode.Qualifier;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by ftc8045 on 10/22/2017.
 */

public class RelicArm {
    public Servo relicElbowServo;
    public Servo relicClawServo;


    public void init(HardwareMap hardwareMap) {
        relicClawServo = hardwareMap.servo.get("relic_claw");
        relicElbowServo = hardwareMap.servo.get("relic_elbow");
        //jawOpen();
        //jawClosed();

        relicClawServo.setPosition(0.4);
        relicElbowServo.setPosition(0.5);
    }

    /*public void jawOpen() {
        relicServo.setPosition(0.2);
    }

    public void jawClosed() {
        relicServo.setPosition(0.8);
    }*/



}
