package org.firstinspires.ftc.teamcode.Components.JewelRejector;

import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by spmce on 11/18/2017.
 */
public class JewelRejector {
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

    public JewelRejector() {
         ballPusherPosition = BALL_PUSHER_UP;
         ballRotatorPosition = BALL_ROTATER_CENTER;
    }

    public void runJewel() {

    }

}
