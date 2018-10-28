package org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.Configs;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.TeleOpMecanum;

/**
 * Created by guberti on 11/2/2017.
 */
@TeleOp(name="Mecanum - Field Centric", group="_Mecanum")
@Disabled
public class FieldCentricMecanum extends TeleOpMecanum {
    @Override
    public void runOpMode() {
        nonrelativeDriveModeEnabled = true;
        accelTime = 250;
        super.runOpMode();
    }
}