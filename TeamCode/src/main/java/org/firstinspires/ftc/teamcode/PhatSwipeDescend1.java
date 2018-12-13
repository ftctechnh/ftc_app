package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="PhatSwipe; DescendDepot", group="CPhatSwipe")
public class PhatSwipeDescend1 extends DescendMarkerDepot {
    public PhatSwipeDescend1() {
        super(ChassisConfig.forPhatSwipe());
    }
}