package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="PhatSwipeBullRun", group="PhatSwipe")
public class PhatSwipeBullRun extends BullRun4 {
    public PhatSwipeBullRun() {
        super(ChassisConfig.forPhatSwipe());
    }
}