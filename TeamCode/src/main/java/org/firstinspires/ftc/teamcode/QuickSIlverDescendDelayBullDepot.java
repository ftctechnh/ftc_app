package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Quick; DescendBullDepot(delay)", group="AQuickSilver")
public class QuickSilverDescendDelayBullDepot extends DescendDelayBullDepot {
    public QuickSilverDescendDelayBullDepot() {
        super(ChassisConfig.forQuickSilver());
    }
}