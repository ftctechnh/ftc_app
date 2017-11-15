package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by pramo on 11/15/2017.
 */

public class VerticalLift {

    private Servo liftServo;
    private int increment = 0;

    public VerticalLift(Servo servo) {

        this.liftServo = servo;

    }

    public void Lift(boolean rightButton, double gp1Trigger, double gp2Trigger) {
        if ((gp1Trigger != 0 || gp2Trigger != 0)
                && !rightButton) {
            liftServo.setPosition(0);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            liftServo.setPosition(0.5);
            increment--;
        }
    }

}
