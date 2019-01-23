package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Phat; BullRunCrater", group="BPhatSwipe")
public class PhatSwipeBullRunCrater extends BullRunCrater {
    public PhatSwipeBullRunCrater() {
        super(ChassisConfig.forPhatSwipe());
    }
}