package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="PhatSwipe; DescendMarkerCrater", group="BPhatSwipe")
public class PhatSwipeDescendMarkerCrater extends DescendMarkerCrater {
    public PhatSwipeDescendMarkerCrater() {
        super(ChassisConfig.forPhatSwipe());
    }
}