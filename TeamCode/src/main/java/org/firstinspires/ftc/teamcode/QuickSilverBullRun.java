package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="QuickSilverBullRun", group="QuickSilver")
public class QuickSilverBullRun extends BullRun {
    public QuickSilverBullRun() {
        super(ChassisConfig.forQuickSilver());
    }
}