package org.firstinspires.ftc.teamcode.relicrecoveryv2;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

/**
 * Created by Eric on 2/3/2018.
 */

@Autonomous (name = "NarthBlue", group = "Autos")
@Disabled
public class NorthBlue extends RelicAutoMode{
    @Override
    public void runOpMode() {
        autoNorth(-1);
    }
}