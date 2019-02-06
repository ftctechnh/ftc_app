package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Quick; CraterDescendMarker", group="GQuickSilver")
public class QuickSilverDescendMarkerCrater extends DescendMarkerCrater {
    public QuickSilverDescendMarkerCrater() {
        super(ChassisConfig.forQuickSilver());
    }
}