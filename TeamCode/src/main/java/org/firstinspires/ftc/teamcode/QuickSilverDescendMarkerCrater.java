package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Quick; DescendMarkerCrater", group="AQuickSilver")
public class QuickSilverDescendMarkerCrater extends DescendMarkerCrater {
    public QuickSilverDescendMarkerCrater() {
        super(ChassisConfig.forQuickSilver());
    }
}