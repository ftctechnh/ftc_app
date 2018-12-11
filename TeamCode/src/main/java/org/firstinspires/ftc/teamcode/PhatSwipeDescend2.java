package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="PhatSwipe; DescendCrater", group="BPhatSwipe")
public class PhatSwipeDescend2 extends DescendFromLander2 {
    public PhatSwipeDescend2() {
        super(ChassisConfig.forPhatSwipe());
    }
}