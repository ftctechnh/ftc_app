package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="PhatSwipe; MarkerDepot", group="BPhatSwipe")
public class PhatSwipeMarkerDepot extends TeamMarkerDepot {
    public PhatSwipeMarkerDepot() {
        super(ChassisConfig.forPhatSwipe());
    }
}