package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

/**
 * This OpMode tests the Claw.
 */
@TeleOp(name = "Monsieur Mallah Claw", group = "Concept")
public class ClawTest extends OpMode {

    private static final double LEFT_CLAWS_OPEN = 0.40;
    private static final double LEFT_CLAWS_CLOSE = 0.55;
    private static final double RIGHT_CLAWS_OPEN = 0.15;
    private static final double RIGHT_CLAWS_CLOSE = 0.0;

    // Define class members
    private Servo servoLeft;
    private Servo servoRight;
    private boolean lastButtonLeftState = false;
    private boolean lastButtonRightState = false;
    private boolean lastButtonReleaseState = false;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        servoLeft = hardwareMap.get(Servo.class, "servo0");
        servoRight = hardwareMap.get(Servo.class, "servo1");
        lastButtonLeftState = false;
        lastButtonRightState = false;
        lastButtonReleaseState = false;
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        // Start with claws open.
        servoLeft.setPosition(LEFT_CLAWS_OPEN);
        servoRight.setPosition(RIGHT_CLAWS_OPEN);
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        if (gamepad1.x) {
            lastButtonLeftState = true;
        }

        if (gamepad1.b) {
            lastButtonRightState = true;
        }

        if (gamepad1.a) {
            lastButtonLeftState = false;
            lastButtonRightState = false;
        }

        servoLeft.setPosition(lastButtonLeftState ? LEFT_CLAWS_CLOSE : LEFT_CLAWS_OPEN);
        servoRight.setPosition(lastButtonRightState ? RIGHT_CLAWS_CLOSE : RIGHT_CLAWS_OPEN);
    }
}



