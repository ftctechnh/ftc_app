package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Quick; DescendBullCrater", group="AQuickSilver")
public class QuickSilverDescendBullCrater extends DescendBullCrater {
    public QuickSilverDescendBullCrater() {
        super(ChassisConfig.forQuickSilver());
    }
}