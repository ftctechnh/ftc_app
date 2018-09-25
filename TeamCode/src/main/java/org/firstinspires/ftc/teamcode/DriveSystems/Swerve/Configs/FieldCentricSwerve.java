package org.firstinspires.ftc.teamcode.DriveSystems.Swerve.Configs;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.DriveSystems.Swerve.TeleOpSwerve;

/**
 * Created by guberti on 11/2/2017.
 */
@TeleOp(name="Swerve - Field Centric", group="_Swerve")
public class FieldCentricSwerve extends TeleOpSwerve {
    @Override
    public void runOpMode() {
        nonrelativeDriveMode = true;
        super.runOpMode();
    }
}