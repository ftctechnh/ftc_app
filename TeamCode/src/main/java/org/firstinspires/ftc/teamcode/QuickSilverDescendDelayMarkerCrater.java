package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Quick; DescendMarkerCrater(delay)", group="AQuickSilver")
public class QuickSilverDescendDelayMarkerCrater extends DescendDelayMarkerCrater {
    public QuickSilverDescendDelayMarkerCrater() {
        super(ChassisConfig.forQuickSilver());
    }
}