package org.firstinspires.ftc.teamcode.RoverRuckus.Mappings;

import com.qualcomm.robotcore.hardware.Gamepad;
public class TandemMapping extends SoloMapping {
    public TandemMapping(Gamepad gamepad1, Gamepad gamepad2) {
        super(gamepad1, gamepad2);
    }

    @Override
    public double armSpeed() {
        return removeLowVals(gamepad2.left_trigger - gamepad2.right_trigger, 0.05);
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
        return gamepad2.dpad_right;
    }

    @Override
    public double moveSpeedScale() {
        return scaleControl(gamepad1.left_trigger, 0.3, 1);
    }

    @Override
    public boolean shakeCamera() {
        return gamepad1.a ^ gamepad2.a;
    }

    @Override
    public int getHangDir() {
        if (gamepad2.left_bumper || gamepad2.right_bumper) {
            return boolsToDir(gamepad2.left_bumper, gamepad2.right_bumper);
        } else {
            return boolsToDir(gamepad1.left_bumper, gamepad1.right_bumper);
        }
    }
}
