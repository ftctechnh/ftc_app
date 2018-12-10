package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Quick; DescendMarkerCrater", group="AQuickSilver")
public class QuickSilverDescend2 extends DescendFromLander2 {
    public QuickSilverDescend2() {
        super(ChassisConfig.forQuickSilver());
    }
}