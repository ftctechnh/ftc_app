package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="PhatSwipe; DescendMarkerDepot", group="BPhatSwipe")
public class PhatSwipeDescendMarkerDepot extends DescendMarkerDepot {
    public PhatSwipeDescendMarkerDepot() {
        super(ChassisConfig.forPhatSwipe());
    }
}