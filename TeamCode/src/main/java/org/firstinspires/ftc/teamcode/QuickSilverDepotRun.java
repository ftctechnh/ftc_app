package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Quick; DepotRun", group="CQuickSilver")
public class QuickSilverDepotRun extends DepotRun {
    public QuickSilverDepotRun() {
        super(ChassisConfig.forQuickSilver());
    }
}