package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="PhatSwipe; MarkerCrater", group="BPhatSwipe")
public class PhatSwipeMarkerCrater extends TeamMarkerCrater {
    public PhatSwipeMarkerCrater() {
        super(ChassisConfig.forPhatSwipe());
    }
}


