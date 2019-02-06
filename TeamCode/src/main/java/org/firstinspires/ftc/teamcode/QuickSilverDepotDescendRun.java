package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Quick; DepotDescendRun", group="CQuickSilver")
public class QuickSilverDepotDescendRun extends DepotDescendRun {
    public QuickSilverDepotDescendRun() {
        super(ChassisConfig.forQuickSilver());
    }
}