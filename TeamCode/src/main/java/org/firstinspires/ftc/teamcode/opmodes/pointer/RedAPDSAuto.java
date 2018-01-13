package org.firstinspires.ftc.teamcode.opmodes.pointer;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.ADPSAuto;

/**
 * Created by Noah on 12/31/2017.
 */

@Autonomous(name="Red APDS Auto", group="test")
public class RedAPDSAuto extends ADPSAuto {
    @Override
    public void init() {
        red = true;
        super.init();
    }
}
