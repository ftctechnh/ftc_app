package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="PhatSwipeMarkerRun", group="PhatSwipe")
public class PhatSwipeMarkerRun extends TeamMarkerRun {
    public PhatSwipeMarkerRun() {
        super(ChassisConfig.forPhatSwipe());
    }
}