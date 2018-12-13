package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Quick; DescendBullCrater(delay)", group="AQuickSilver")
public class QuickSilverDescendDelayBullCrater extends DescendDelayBullCrater {
    public QuickSilverDescendDelayBullCrater() {
        super(ChassisConfig.forQuickSilver());
    }
}