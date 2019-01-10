package org.firstinspires.ftc.teamcode.RoverRuckus.Mappings;

import com.qualcomm.robotcore.hardware.Gamepad;
public class TandemMapping extends SoloMapping {
    public TandemMapping(Gamepad gamepad1, Gamepad gamepad2) {
        super(gamepad1, gamepad2);
    }

    private boolean invert() {return gamepad1.right_trigger > 0.2;}
    private int invFact() {return invert() ? -1 : 1; }

    @Override
    public double driveStickX() {
        return gamepad1.left_stick_x * invFact();
    }

    @Override
    public double driveStickY() {
        return gamepad1.left_stick_y;
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
