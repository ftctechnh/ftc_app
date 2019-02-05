package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Phat; DescendDelayMarkerDepot", group="BPhatSwipe")
public class PhatSwipeDescendDelayMarkerDepot extends DescendDelayMarkerDepot {
    public PhatSwipeDescendDelayMarkerDepot() {
        super(ChassisConfig.forPhatSwipe());
    }
}