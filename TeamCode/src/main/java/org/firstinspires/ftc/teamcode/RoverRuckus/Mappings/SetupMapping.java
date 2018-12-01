package org.firstinspires.ftc.teamcode.RoverRuckus.Mappings;

import com.qualcomm.robotcore.hardware.Gamepad;

public class SetupMapping extends TandemMapping {
    public SetupMapping(Gamepad gamepad1, Gamepad gamepad2) {
        super(gamepad1, gamepad2);
    }

    @Override
    public double driveStickX() {
        return 0;
    }

    @Override
    public double driveStickY() {
        return 0;
    }

    @Override
    public double turnSpeed() {
        return 0;
    }

    @Override
    public boolean override() {
        return true;
    }
}
