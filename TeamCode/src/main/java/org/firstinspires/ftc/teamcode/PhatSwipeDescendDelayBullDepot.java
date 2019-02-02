package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Phat; DescendDelayBullDepot", group="BPhatSwipe")
public class PhatSwipeDescendDelayBullDepot extends DescendDelayBullDepot {
    public PhatSwipeDescendDelayBullDepot() {
        super(ChassisConfig.forPhatSwipe());
    }
}