package org.firstinspires.ftc.omegas.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.omegas.OmegasAlliance;

/**
 * Created by ethertyper on 10/14/16.
 */

@SuppressWarnings("unused")
@Autonomous(name = "Omegas: Red Alliance Autonomous Controls", group = "Linear Opmode")
public class AutonomousRed extends OmegasAutonomous {
    public OmegasAlliance getColor() {
        return OmegasAlliance.RED;
    }
}
