package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="PhatSwipe; DescendDepot", group="BPhatSwipe")
public class PhatSwipeDescend1 extends DescendFromLander {
    public PhatSwipeDescend1() {
        super(ChassisConfig.forPhatSwipe());
    }
}