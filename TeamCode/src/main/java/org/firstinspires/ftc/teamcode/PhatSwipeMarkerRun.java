package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="PhatSwipe; MarkerDepot", group="BPhatSwipe")
public class PhatSwipeMarkerRun extends TeamMarkerRun {
    public PhatSwipeMarkerRun() {
        super(ChassisConfig.forPhatSwipe());
    }
}