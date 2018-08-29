package org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.Demos;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.TeleOpMecanum;

/**
 * Created by guberti on 11/2/2017.
 */
@TeleOp(name="MecanumDemo - Low Friction Sim", group="MecanumDemo")
public class LowFrictionMecanum extends TeleOpMecanum {
    @Override
    public void runOpMode() {
        nonrelativeDriveModeEnabled = false;
        accelTime = 2000;
        super.runOpMode();
    }
}