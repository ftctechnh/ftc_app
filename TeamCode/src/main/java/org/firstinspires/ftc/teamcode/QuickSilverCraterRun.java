package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Quick; CraterRun", group="CQuickSilver")
public class QuickSilverCraterRun extends CraterRun {
    public QuickSilverCraterRun() {
        super(ChassisConfig.forQuickSilver());
    }
}