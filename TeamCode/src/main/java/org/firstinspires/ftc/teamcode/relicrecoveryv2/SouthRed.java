package org.firstinspires.ftc.teamcode.relicrecoveryv2;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Eric on 2/3/2018.
 */

@Autonomous (name = "SauthRed", group = "Autos")
public class SouthRed extends RelicAutoMode{
    @Override
    public void runOpMode() {
        autoSouth(1);
    }
}