package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="PhatSwipe; BullRunCrater", group="BPhatSwipe")
public class PhatSwipeBullRun extends BullRun {
    public PhatSwipeBullRun() {
        super(ChassisConfig.forPhatSwipe());
    }
}