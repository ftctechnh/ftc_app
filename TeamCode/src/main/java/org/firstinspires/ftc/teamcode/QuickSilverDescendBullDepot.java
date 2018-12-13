package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Quick; DescendBullDepot", group="AQuickSilver")
public class QuickSilverDescendBullDepot extends DescendBullDepot {
    public QuickSilverDescendBullDepot() {
        super(ChassisConfig.forQuickSilver());
    }
}