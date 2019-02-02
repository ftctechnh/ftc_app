package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Quick; DescendMarkerDepot", group="AQuickSilver")
public class QuickSilverShaggyDescendMarkerDepot extends ShaggyDescendMarkerDepot {
    public QuickSilverShaggyDescendMarkerDepot() {
        super(ChassisConfig.forQuickSilver());
    }
}