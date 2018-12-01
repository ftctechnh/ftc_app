package org.firstinspires.ftc.teamcode.RoverRuckus.Mappings;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Gamepad;

@Config
public class ComputerMapping extends ControlMapping {
    public static double driveStickX;
    public static double driveStickY;
    public static double turnSpeed;
    public static double moveSpeedScale;
    public static double armSpeed;
    public static boolean flipOut;
    public static boolean flipBack;
    public static double extendSpeed;
    public static int hangDir;
    public static boolean shakeCamera;
    public static double spinSpeed;
    public static boolean override;

    // All these are initially 0 and false

    public ComputerMapping(Gamepad gamepad1, Gamepad gamepad2) {
        super(gamepad1, gamepad2);
    }


    @Override
    public double driveStickX() {
        return driveStickX;
    }

    @Override
    public double driveStickY() {
        return driveStickY;
    }

    @Override
    public double turnSpeed() {
        return turnSpeed;
    }

    @Override
    public double moveSpeedScale() {
        return moveSpeedScale;
    }

    @Override
    public double armSpeed() {
        return armSpeed;
    }

    @Override
    public boolean flipOut() {
        return flipOut;
    }

    @Override
    public boolean flipBack() {
        return flipBack;
    }

    @Override
    public double getExtendSpeed() {
        return extendSpeed;
    }

    @Override
    public int getHangDir() {
        return hangDir;
    }

    @Override
    public boolean shakeCamera() {
        return shakeCamera;
    }

    @Override
    public double getSpinSpeed() {
        return spinSpeed;
    }

    @Override
    public boolean override() {
        return override;
    }
}
