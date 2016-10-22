package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.OmegasAlliance;

/**
 * Created by ethertyper on 10/14/16.
 */

@Autonomous(name = "Red Alliance: Beacon Pusher", group = "Linear Opmode")
public class AutonomousRed extends OmegasAutonomous {
    public OmegasAlliance getColor() { return OmegasAlliance.RED; }
}
