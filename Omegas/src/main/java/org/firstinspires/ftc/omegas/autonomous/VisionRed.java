package org.firstinspires.ftc.omegas.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.omegas.OmegasAlliance;

/**
 * Created by ethertyper on 12/5/16.
 */

@SuppressWarnings("unused")
@Autonomous(name = "Omegas: Red Alliance Vision Control", group = "Linear Opmode")
public class VisionRed extends OmegasVision {
    public OmegasAlliance getColor() {
        return OmegasAlliance.RED;
    }
}
