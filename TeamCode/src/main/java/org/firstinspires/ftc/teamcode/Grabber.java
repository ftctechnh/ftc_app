package org.firstinspires.ftc.teamcode;

/**
 * Created by DiegoGutiDev on 11/12/17.
 */

import com.qualcomm.robotcore.hardware.Servo;

public class Grabber {

    private Servo leftServo, rightServo;

    public boolean closed = false;
    public boolean isPressed = false;

    private final double LEFT_CLOSED_POSITION = 0.5 - (6.0/180);
    private final double LEFT_OPEN_POSITION = 1 - (6.0/180);
    private final double RIGHT_OPEN_POSITION = (28.0/180);
    private final double RIGHT_CLOSED_POSITION = RIGHT_OPEN_POSITION + 0.5;

    public Grabber(Servo left, Servo right) {

        this.leftServo = left;
        this.rightServo = right;

        leftServo.setPosition(LEFT_OPEN_POSITION);
        rightServo.setPosition(RIGHT_OPEN_POSITION);

    }

    public void Grab(boolean change) {

        if (!isPressed) {

            if (change) {
                if (closed) {
                    leftServo.setPosition(LEFT_OPEN_POSITION);
                    rightServo.setPosition(RIGHT_OPEN_POSITION);

                    closed = false;
                } else {
                    leftServo.setPosition(LEFT_CLOSED_POSITION);
                    rightServo.setPosition(RIGHT_CLOSED_POSITION);

                    closed = true;
                }

                isPressed = true;
            }

        }
        if (!change) {
            isPressed = false;
        }
    }

}
