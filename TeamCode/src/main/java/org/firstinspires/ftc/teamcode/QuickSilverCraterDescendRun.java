package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Quick; CraterDescendRun", group="CQuickSilver")
public class QuickSilverCraterDescendRun extends CraterDescendRun {
    public QuickSilverCraterDescendRun() {
        super(ChassisConfig.forQuickSilver());
    }
}