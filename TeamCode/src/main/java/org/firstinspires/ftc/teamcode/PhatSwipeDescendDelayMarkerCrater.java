package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Phat; DescendDelayMarkerCrater", group="BPhatSwipe")
public class PhatSwipeDescendDelayMarkerCrater extends DescendDelayMarkerCrater {
    public PhatSwipeDescendDelayMarkerCrater() {
        super(ChassisConfig.forPhatSwipe());
    }
}