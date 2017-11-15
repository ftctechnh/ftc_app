package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by pramo on 11/15/2017.
 */

public class VerticalLift {

    private Servo liftServo;

    public VerticalLift(Servo servo) {

        this.liftServo = servo;

    }

    public void Lift(boolean gp1LeftBumper, boolean gp2LeftBumper
            , double gp1Trigger, double gp2Trigger) {

        if (gp1LeftBumper|| gp2LeftBumper) {
            liftServo.setPosition(0);
        } else if (gp1Trigger != 0 || gp2Trigger != 0) {
            liftServo.setPosition(1);
        } else {
            liftServo.setPosition(.5);
        }
    }

}
