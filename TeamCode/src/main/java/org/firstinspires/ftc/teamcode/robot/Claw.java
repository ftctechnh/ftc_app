package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Derek on 10/24/2017.
 */

public class Claw {    private Servo right,left;
    public static final double CLAW_INCREMENT = 0.005;
    private double clawPos = 0;

    public Claw(Servo left, Servo right) {
        this.left = left;
        this.right = right;
    }

    public void setPosition(ClawPosition p) {
        setPosition(p.position);
    }

    public void setPosition(double d) {
        clawPos = (d >= 0 & d <= 1) ? d : clawPos;
        right.setPosition(clawPos);
        left.setPosition(clawPos);
    }

    public enum ClawPosition {
        OPEN(0.9),CLOSED(0),CENTER(0.5);
        private double position;

        ClawPosition(double value) {
            this.position = value;
        }
    }

    public double getPosition() {
        return clawPos;
    }
}
