package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Quick; DescendBullCrater", group="AQuickSilver")
public class QuickSilverDescendBull extends DescendBull {
    public QuickSilverDescendBull() {
        super(ChassisConfig.forQuickSilver());
    }
}