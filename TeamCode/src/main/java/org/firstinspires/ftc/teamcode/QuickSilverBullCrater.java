package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Quick; LandBullCrater", group="BQuickSilver")
public class QuickSilverBullCrater extends BullRunCrater {
    public QuickSilverBullCrater() {
        super(ChassisConfig.forQuickSilver());
    }
}