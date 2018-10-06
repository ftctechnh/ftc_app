package org.firstinspires.ftc.teamcode.DriveSystems.Swerve.Configs;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.DriveSystems.Swerve.TeleOpSwerve;

/**
 * Created by guberti on 11/2/2017.
 */
@TeleOp(name="Swerve - Robot Centric", group="_Swerve")
public class RobotCentricSwerve extends TeleOpSwerve {
    @Override
    public void runOpMode() {
        nonrelativeDriveMode = false;
        super.runOpMode();
    }
}