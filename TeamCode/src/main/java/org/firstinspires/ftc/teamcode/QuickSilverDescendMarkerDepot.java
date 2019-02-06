package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Quick; DepotDescendMarker", group="GQuickSilver")
public class QuickSilverDescendMarkerDepot extends DescendMarkerDepot {
    public QuickSilverDescendMarkerDepot() {
        super(ChassisConfig.forQuickSilver());
    }
}