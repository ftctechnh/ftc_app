package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Quick; ChickenSquat", group="GQuickSilver")
public class QuickSilverChickenSquat extends ChickenSquat {
    public QuickSilverChickenSquat() {
        super(ChassisConfig.forQuickSilver());
    }
}