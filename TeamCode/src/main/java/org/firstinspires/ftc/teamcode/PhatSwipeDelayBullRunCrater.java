package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Phat; DescendDelayBullCrater", group="BPhatSwipe")
public class PhatSwipeDelayBullRunCrater extends DescendDelayBullCrater {
    public PhatSwipeDelayBullRunCrater() {
        super(ChassisConfig.forPhatSwipe());
    }
}