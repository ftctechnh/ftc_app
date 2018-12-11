package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Quick; DescendMarkerDepot", group="AQuickSilver")
public class QuickSilverDescend1 extends DescendFromLander {
    public QuickSilverDescend1() {
        super(ChassisConfig.forQuickSilver());
    }
}