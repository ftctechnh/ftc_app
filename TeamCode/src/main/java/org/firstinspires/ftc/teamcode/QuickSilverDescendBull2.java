package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Quick; DescendBullDepot", group="AQuickSilver")
public class QuickSilverDescendBull2 extends DescendBull2 {
    public QuickSilverDescendBull2() {
        super(ChassisConfig.forQuickSilver());
    }
}