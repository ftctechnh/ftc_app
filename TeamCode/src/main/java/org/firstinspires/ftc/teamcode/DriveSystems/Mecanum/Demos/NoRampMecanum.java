package org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.Demos;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.TeleOpMecanum;

/**
 * Created by guberti on 11/2/2017.
 */
@TeleOp(name="MecanumDemo - No Power Ramp", group="MecanumDemo")
public class NoRampMecanum extends TeleOpMecanum {
    @Override
    public void runOpMode() {
        nonrelativeDriveModeEnabled = false;
        accelTime = 1;
        super.runOpMode();
    }
}