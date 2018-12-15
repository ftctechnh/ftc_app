package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="PhatSwipe; DescendDelayBullDepot", group="BPhatSwipe")
public class PhatSwipeDescendDelayBullDepot extends DescendDelayBullDepot {
    public PhatSwipeDescendDelayBullDepot() {
        super(ChassisConfig.forPhatSwipe());
    }
}