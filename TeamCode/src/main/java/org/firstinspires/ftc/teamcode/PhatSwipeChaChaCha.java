package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="PhatSwipe; BullRunDepot", group="BPhatSwipe")
public class PhatSwipeChaChaCha extends BullRunChaChaCha {
    public PhatSwipeChaChaCha() {
        super(ChassisConfig.forPhatSwipe());
    }
}