package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Phat; DescendChickenSquat", group="BPhatSwipe")
public class PhatSwipeChickenSquat extends ChickenSquat {
    public PhatSwipeChickenSquat() {
        super(ChassisConfig.forPhatSwipe());
    }
}