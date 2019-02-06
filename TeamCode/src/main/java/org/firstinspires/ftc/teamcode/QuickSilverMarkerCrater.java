package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Quick; CraterMarker", group="GQuickSilver")
public class QuickSilverMarkerCrater extends TeamMarkerCrater {
    public QuickSilverMarkerCrater() {
        super(ChassisConfig.forQuickSilver());
    }
}
