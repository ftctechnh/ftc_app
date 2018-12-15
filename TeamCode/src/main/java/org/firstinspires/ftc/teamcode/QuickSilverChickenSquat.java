package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Quick; ADescendChickenSquat", group="AQuickSilver")
public class QuickSilverChickenSquat extends ChickenSquat {
    public QuickSilverChickenSquat() {
        super(ChassisConfig.forQuickSilver());
    }
}