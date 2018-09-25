package org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.Configs;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.TeleOpMecanum;

/**
 * Created by guberti on 11/2/2017.
 */
@TeleOp(name="Mecanum - Robot Centric", group="_Mecanum")
public class RobotCentricMecanum extends TeleOpMecanum {
    @Override
    public void runOpMode() {
        nonrelativeDriveModeEnabled = false;
        accelTime = 250;
        super.runOpMode();
    }
}