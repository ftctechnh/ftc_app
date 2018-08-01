package org.firstinspires.ftc.teamcode.relicrecoveryv2;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

/**
 * Created by Eric on 2/3/2018.
 */

@Autonomous (name = "SauthBlue", group = "Autos")
@Disabled
public class SouthBlue extends RelicAutoMode{
    @Override
    public void runOpMode() {
        autoSouth(-1);
    }
}