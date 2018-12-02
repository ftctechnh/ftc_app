package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="PhatSwipeCha", group="PhatSwipe")
public class PhatSwipeChaChaCha extends BullRunChaChaCha {
    public PhatSwipeChaChaCha() {
        super(ChassisConfig.forPhatSwipe());
    }
}