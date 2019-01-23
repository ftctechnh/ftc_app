package org.firstinspires.ftc.teamcode.RoverRuckus.Mappings;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Gamepad;
@Config
public class TandemMapping extends ControlMapping {
    public static double INTAKE_SPEED = 0.85;

    public static double FLIP_LEFT_FACTOR = 0.65;
    public static double FLIP_RIGHT_FACTOR = 0.6;

    public static double MAX_TURN_SPEED_F = 1;
    public static double MIN_TURN_SPEED_F = 0.2;
    public static double MIN_MOVE_SPEED = 0.6;

    public static double MAX_GP2_TURN_SPEED = 0.3;

    public static double EXPONENT = 1.5;
    public static double TURN_SPEED_FACTOR = 0.8;

    public int spinDir;
    private boolean g1x_down, g1b_down, g2x_down, g2b_down;
    private boolean up_down, down_down;

    public TandemMapping(Gamepad gamepad1, Gamepad gamepad2) {
        super(gamepad1, gamepad2);
    }

    private double exp(double d) {
        return Math.copySign(Math.pow(Math.abs(d), EXPONENT), d);
    }
    @Override
    public double driveStickX() {
        return exp(gamepad1.left_stick_x);
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
    public double translateSpeedScale() {
        return scaleControl(1 - gamepad1.left_trigger, MIN_MOVE_SPEED, 1);
    }

    @Override
    public double turnSpeedScale() {
        return scaleControl(1 - gamepad1.left_trigger, MIN_TURN_SPEED_F, MAX_TURN_SPEED_F);
    }

    @Override
    public boolean lockTo45() {
        return gamepad1.a || gamepad2.a;
    }

    public boolean lockTo225() {
        return gamepad1.y;
    }

    @Override
    public double armSpeed() {
        return removeLowVals(gamepad2.left_trigger * FLIP_LEFT_FACTOR
                - gamepad2.right_trigger * FLIP_RIGHT_FACTOR, 0.05);
    }

    @Override
    public boolean collectWithArm() {
        if (!down_down && gamepad2.dpad_down) {
            down_down = true;
            return true;
        }
        if (!gamepad2.dpad_down && down_down) {
            down_down = false;
        }
        return false;
    }

    @Override
    public boolean depositWithArm() {
        if (!up_down && gamepad2.dpad_up) {
            up_down = true;
            return true;
        }
        if (!gamepad2.dpad_up && up_down) {
            up_down = false;
        }
        return false;
    }

    public double getExtendSpeed() {
        return -clamp(gamepad2.left_stick_y);
    }

    public double getSlewSpeed() {
        return -gamepad2.left_stick_x;
    }

    public double getGP2TurnSpeed() {
        return gamepad2.right_stick_x * MAX_GP2_TURN_SPEED;
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
    public boolean shakeCamera() {
        return gamepad1.back ^ gamepad2.a;
    }

    @Override
    public double getSpinSpeed() {
        if ((gamepad2.x && !g2x_down) || (gamepad1.x && !g1x_down)) {

            spinDir = (spinDir == -1) ? 0 : -1;
            if (gamepad2.x && !g2x_down) {
                g2x_down = true;
            } else {
                g1x_down = true;
            }
        }

        if (!gamepad2.x && g2x_down) {
            g2x_down = false;
        }
        if (!gamepad1.x && g1x_down) {
            g1x_down = false;
        }

        if ((gamepad2.b && !g2b_down) || (gamepad1.b && !g1b_down)) {

            // X was just pressed
            spinDir = (spinDir == 1) ? 0 : 1;
            if (gamepad2.b && !g2b_down) {
                g2b_down = true;
            } else {
                g1b_down = true;
            }
        }

        if (!gamepad2.b && g2b_down) {
            g2b_down = false;
        }
        if (!gamepad1.b && g1b_down) {
            g1b_down = false;
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
