package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Quick; LandBullDepot", group="BQuickSilver")
public class QuickSilverBullDepot extends BullRunDepot {
    public QuickSilverBullDepot() {
        super(ChassisConfig.forQuickSilver());
    }
}