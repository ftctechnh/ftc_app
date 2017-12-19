package org.firstinspires.ftc.teamcode.Qualifier;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by ftc8045 on 10/22/2017.
 */

public class RelicArm {
    public Servo relicArmServo;
    public Servo relicClawServo;


    public void init(HardwareMap hardwareMap) {
        relicClawServo = hardwareMap.servo.get("relic_claw");

        //jawOpen();
        //jawClosed();
    }

    /*public void jawOpen() {
        relicServo.setPosition(0.2);
    }

    public void jawClosed() {
        relicServo.setPosition(0.8);
    }*/



}
