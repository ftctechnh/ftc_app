package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="QuickSilverDescend", group="QuickSilver")
public class QuickSilverDescendFromLander extends DescendFromLander {
    public QuickSilverDescendFromLander() {
        super(ChassisConfig.forQuickSilver());
    }
}