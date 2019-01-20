package org.firstinspires.ftc.teamcode.RoverRuckus.Mappings;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Gamepad;
@Config
public class TandemMapping extends ControlMapping {
    public static double INTAKE_SPEED = 0.85;
    public static double INTAKE_SLOW_SPEED = 0.2;

    public static double FLIP_LEFT_FACTOR = 0.65;
    public static double FLIP_RIGHT_FACTOR = 0.6;

    public static double MIN_SLOW_MOVE_SPEED = 0.6;

    public static double EXPONENT = 1.5;
    public static double TURN_SPEED_FACTOR = 0.8;

    public int spinDir;
    private boolean x_down, b_down;

    public TandemMapping(Gamepad gamepad1, Gamepad gamepad2) {
        super(gamepad1, gamepad2);
    }

    private boolean invert() {return gamepad1.right_trigger > 0.2;}
    private int invFact() {return invert() ? -1 : 1; }

    private double exp(double d) {
        return Math.copySign(Math.pow(Math.abs(d), EXPONENT), d);
    }
    @Override
    public double driveStickX() {
        return invFact() * exp(gamepad1.left_stick_x);
    }

    @Override
    public double driveStickY() {
        return exp(gamepad1.left_stick_y);
    }

    @Override
    public double turnSpeed() {
        return removeLowVals(exp(gamepad1.right_stick_x), 0.1) * TURN_SPEED_FACTOR;
    }

    @Override
    public double armSpeed() {
        return removeLowVals(gamepad2.left_trigger * FLIP_LEFT_FACTOR
                - gamepad2.right_trigger * FLIP_RIGHT_FACTOR, 0.05);
    }

    public double getExtendSpeed() {
        return -clamp(gamepad2.left_stick_y + gamepad2.right_stick_y);
    }

    @Override
    public boolean flipOut() {
        return gamepad2.dpad_left;
    }

    @Override
    public boolean flipBack() {
        if (gamepad2.dpad_right) {
            spinDir = -1;
        }
        return gamepad2.dpad_right;

    }

    @Override
    public double moveSpeedScale() {
        return scaleControl(1 - gamepad1.left_trigger, MIN_SLOW_MOVE_SPEED, 1);
    }

    @Override
    public boolean shakeCamera() {
        return gamepad1.a ^ gamepad2.a;
    }

    @Override
    public double getSpinSpeed() {
        if (gamepad1.x && !x_down) {

            // X was just pressed
            spinDir = (spinDir == -1) ? 0 : -1;
            x_down = true;
        } else if (!gamepad1.x && x_down) {
            x_down = false;
        }

        if (gamepad1.b && !b_down) {
            // B was just pressed
            spinDir = (spinDir == 1) ? 0 : 1;
            b_down = true;
        } else if (!gamepad1.b && b_down) {
            b_down = false;
        }
        return spinDir * INTAKE_SPEED;
    }


    @Override
    public boolean override() {
        return gamepad1.start;
    }

    @Override
    public int getHangDir() {
        int dir;
        if (gamepad2.left_bumper || gamepad2.right_bumper) {
            dir = boolsToDir(gamepad2.left_bumper, gamepad2.right_bumper);
        } else {
            dir = boolsToDir(gamepad1.left_bumper, gamepad1.right_bumper);
        }
        if (dir != 0) {
            spinDir = 0;
        }
        return dir;
    }
}
