package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Quick; DescendMarkerDepot(delay)", group="AQuickSilver")
public class QuickSilverDescendDelayMarkerDepot extends DescendDelayMarkerDepot {
    public QuickSilverDescendDelayMarkerDepot() {
        super(ChassisConfig.forQuickSilver());
    }
}