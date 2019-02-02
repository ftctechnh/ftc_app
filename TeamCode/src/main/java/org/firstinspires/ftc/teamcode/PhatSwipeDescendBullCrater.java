package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Phat; DescendBullCrater", group="BPhatSwipe")
public class PhatSwipeDescendBullCrater extends DescendBullCrater {
    public PhatSwipeDescendBullCrater() {
        super(ChassisConfig.forPhatSwipe());
    }
}