package org.firstinspires.ftc.teamcode.Components.Jewel;

import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by spmce on 11/18/2017.
 */
public class Jewel {
    // ------------------------- Constants --------------------------
    private static final double BALL_PUSHER_UP = .5;
    private static final double BALL_PUSHER_DOWN = 1;
    private static final double BALL_ROTATER_CENTER = .5;
    private static final double BALL_ROTATOR_RIGHT = .7;
    private static final double BALL_ROTATOR_LEFT = .3;
    // ---------------------- Hardware Devices ----------------------
    // ------------ Standard Servos -------------
    Servo ssBallPusher;
    Servo ssBallRotator;

    private double ballPusherPosition;
    private double ballRotatorPosition;

    public Jewel() {
         ballPusherPosition = BALL_PUSHER_UP;
         ballRotatorPosition = BALL_ROTATER_CENTER;
    }

    public void runJewel() {

    }

}
