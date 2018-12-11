package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Quick; BullRunCrater", group="AQuickSilver")
public class QuickSilverBullRun extends BullRun {
    public QuickSilverBullRun() {
        super(ChassisConfig.forQuickSilver());
    }
}